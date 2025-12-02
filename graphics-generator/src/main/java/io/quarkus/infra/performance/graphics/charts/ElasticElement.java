package io.quarkus.infra.performance.graphics.charts;

import io.quarkus.infra.performance.graphics.Theme;

public interface ElasticElement {
    int getMaximumVerticalSize();

    int getMaximumHorizontalSize();

    int getMinimumVerticalSize();

    int getMinimumHorizontalSize();

    void draw(Subcanvas g, Theme theme);
}
