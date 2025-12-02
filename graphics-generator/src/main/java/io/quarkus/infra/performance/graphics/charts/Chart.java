package io.quarkus.infra.performance.graphics.charts;

import java.awt.Font;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.model.Config;

public abstract class Chart implements ElasticElement {

    protected final Label titleLabel;
    protected final int titleTextSize = 48;

    protected final List<Datapoint> data;
    protected final String title;
    protected final Config metadata;
    // For size calculations, the assumption is that these are stacked vertically
    protected final Set<ElasticElement> children;

    protected final double maxValue;

    protected Chart(String title, List<Datapoint> datasets, Config metadata) {
        this.data = datasets;
        this.title = title;
        this.metadata = metadata;
        children = new HashSet<>();

        titleLabel = new Label(title).setStyle(Font.BOLD);

        // TODO title not included in vertical height
        maxValue = data.stream().map(d -> d.value().getValue()).max(Double::compare).orElse(1.0);
    }

    public void draw(Subcanvas g, Theme theme) {
        if (getMinimumHorizontalSize() > g.getWidth()) {
            throw new SizeException("Cannot fit " + getMinimumHorizontalSize() + "px of content into a " + g.getHeight()
                    + "px horizontal space.");
        }
        if (getMinimumVerticalSize() > g.getHeight()) {
            throw new SizeException("Cannot fit " + getMinimumHorizontalSize() + "px of content into a " + g.getHeight()
                    + "px vertical space.");
        }
        drawNoCheck(g, theme);
    }

    protected abstract void drawNoCheck(Subcanvas g, Theme theme);

    // SVGs after to be done *after* the main drawing, because getting the document root before drawing causes all subsequent draws to be dropped.
    // This seems to be a characteristic of the Batik streaming model.
    public Collection<InlinedSVG> getInlinedSVGs() {
        return Collections.emptyList();
    }

    @Override
    public int getMaximumVerticalSize() {
        return children.stream().mapToInt(ElasticElement::getMaximumVerticalSize).sum();
    }

    @Override
    public int getMaximumHorizontalSize() {
        return children.stream().mapToInt(ElasticElement::getMaximumHorizontalSize).max().orElse(0);
    }

    @Override
    public int getMinimumVerticalSize() {
        return children.stream().mapToInt(ElasticElement::getMinimumVerticalSize).sum();
    }

    @Override
    public int getMinimumHorizontalSize() {
        return children.stream().mapToInt(ElasticElement::getMinimumHorizontalSize).max().orElse(0);
    }
}
