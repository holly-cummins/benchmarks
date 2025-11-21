package io.quarkus.infra.performance.graphics.charts;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.quarkus.infra.performance.graphics.model.Config;

public interface Chart {

    void draw(String title, List<Datapoint> datasets, Config metadata);

    // SVGs after to be done *after* the main drawing, because getting the document root before drawing causes all subsequent draws to be dropped.
    // This seems to be a characteristic of the Batik streaming model.
    default Collection<InlinedSVG> getInlinedSVGs() {
        return Collections.emptyList();
    }
}
