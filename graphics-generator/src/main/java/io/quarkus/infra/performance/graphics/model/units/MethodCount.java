package io.quarkus.infra.performance.graphics.model.units;

public class MethodCount extends DimensionalNumber {
    public MethodCount(String count) {
        super(Integer.parseInt(count.replace(",", "")));
    }

    @Override
    public String getUnits() {
        return "methods";
    }
}
