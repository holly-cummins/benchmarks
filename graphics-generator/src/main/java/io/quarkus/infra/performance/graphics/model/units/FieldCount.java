package io.quarkus.infra.performance.graphics.model.units;

public class FieldCount extends DimensionalNumber {
    public FieldCount(String count) {
        super(Integer.parseInt(count.replace(",", "")));
    }

    public FieldCount(int count) {
        super(count);
    }

    @Override
    public String getUnits() {
        return "fields";
    }
}
