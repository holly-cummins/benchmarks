package io.quarkus.infra.performance.graphics.model.units;

// There is a duration class already but we want to extend dimensional number
public class Seconds extends DimensionalNumber {
    public Seconds(double duration) {
        super(duration);
    }

    @Override
    public String getUnits() {
        return "s";
    }
}
