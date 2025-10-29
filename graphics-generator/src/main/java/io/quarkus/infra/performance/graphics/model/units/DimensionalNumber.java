package io.quarkus.infra.performance.graphics.model.units;

public abstract class DimensionalNumber {
    // We could do a parameterised type of ? extends Number but doing math with Number is annoying, so stick to doubles and rounding
    private final double value;

    public DimensionalNumber(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public abstract String getUnits();

}
