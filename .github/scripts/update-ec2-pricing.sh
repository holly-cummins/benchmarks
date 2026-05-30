#!/usr/bin/env bash
set -euo pipefail

INSTANCE_TYPE="${1:-c6i.xlarge}"
REGION="${2:-us-east-1}"
OUTPUT="graphics-generator/src/main/resources/ec2-pricing.json"

PRICE=$(aws pricing get-products \
  --service-code AmazonEC2 \
  --region us-east-1 \
  --filters \
    "Type=TERM_MATCH,Field=instanceType,Value=${INSTANCE_TYPE}" \
    "Type=TERM_MATCH,Field=location,Value=US East (N. Virginia)" \
    "Type=TERM_MATCH,Field=operatingSystem,Value=Linux" \
    "Type=TERM_MATCH,Field=tenancy,Value=Shared" \
    "Type=TERM_MATCH,Field=preInstalledSw,Value=NA" \
    "Type=TERM_MATCH,Field=capacitystatus,Value=Used" \
  --query 'PriceList[0]' \
  --output text | jq -r '
    .terms.OnDemand | to_entries[0].value.priceDimensions | to_entries[0].value.pricePerUnit.USD
  ')

if [ -z "$PRICE" ] || [ "$PRICE" = "null" ]; then
  echo "Failed to fetch price for ${INSTANCE_TYPE} in ${REGION}" >&2
  exit 1
fi

VCPUS=$(aws ec2 describe-instance-types \
  --instance-types "${INSTANCE_TYPE}" \
  --region "${REGION}" \
  --query 'InstanceTypes[0].VCpuInfo.DefaultVCpus' \
  --output text)

MEMORY_MIB=$(aws ec2 describe-instance-types \
  --instance-types "${INSTANCE_TYPE}" \
  --region "${REGION}" \
  --query 'InstanceTypes[0].MemoryInfo.SizeInMiB' \
  --output text)

MEMORY_GIB=$((MEMORY_MIB / 1024))
TODAY=$(date +%Y-%m-%d)

cat > "${OUTPUT}" <<EOF
{
  "instanceType": "${INSTANCE_TYPE}",
  "region": "${REGION}",
  "vCPUs": ${VCPUS},
  "memoryGiB": ${MEMORY_GIB},
  "onDemandPricePerHour": ${PRICE},
  "currency": "USD",
  "lastUpdated": "${TODAY}"
}
EOF

echo "Updated ${OUTPUT}: ${INSTANCE_TYPE} @ \$${PRICE}/hr (${VCPUS} vCPUs, ${MEMORY_GIB} GiB)"
