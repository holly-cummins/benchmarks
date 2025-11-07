package io.quarkus.infra.performance.graphics.model.units;

// There is a duration class already but we want to extend dimensional number
public class Milliseconds extends DimensionalNumber {
    public Milliseconds(double duration) {
        super(duration);
    }

    @Override
    public String getUnits() {
        return "ms";
    }
}
