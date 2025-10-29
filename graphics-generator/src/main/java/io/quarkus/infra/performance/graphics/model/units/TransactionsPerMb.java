package io.quarkus.infra.performance.graphics.model.units;

public class TransactionsPerMb extends DimensionalNumber {

    public TransactionsPerMb(double throughput) {
        super(throughput);
    }

    @Override
    public String getUnits() {
        return "transactions/MB";
    }
}
