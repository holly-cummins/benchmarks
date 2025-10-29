package io.quarkus.infra.performance.graphics.charts;

import java.util.List;

public interface Chart {

    void draw(List<Datapoint> datasets);
}
