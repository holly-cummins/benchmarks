package io.quarkus.infra.performance.graphics.model.units;

public class TransactionsPerSecond extends DimensionalNumber {

    public TransactionsPerSecond(double throughput) {
        super(throughput);
    }

    @Override
    public String getUnits() {
        return "tps";
    }
}
