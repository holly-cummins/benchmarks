package io.quarkus.infra.performance.graphics.model.units;

public class ClassCount extends DimensionalNumber {
    public ClassCount(String count) {
        super(Integer.parseInt(count.replace(",", "")));
    }

    @Override
    public String getUnits() {
        return "classes";
    }
}
