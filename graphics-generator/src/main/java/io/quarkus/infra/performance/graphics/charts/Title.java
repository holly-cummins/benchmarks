package io.quarkus.infra.performance.graphics.charts;

import java.awt.Font;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.VAlignment;

public class Title implements ElasticElement {
    protected static final int MINIMUM_TITLE_TEXT_SIZE = 18;
    protected static final int MAXIMUM_TITLE_TEXT_SIZE = 58;
    protected final String title;

    private final Label titleLabel;

    public Title(String title) {
        this.title = title;
        titleLabel = new Label(title).setStyle(Font.BOLD).setVerticalAlignment(VAlignment.MIDDLE);
    }

    @Override
    public int getMaximumVerticalSize() {
        return Sizer.calculateHeight(MAXIMUM_TITLE_TEXT_SIZE);
    }

    @Override
    public int getMaximumHorizontalSize() {
        return Sizer.calculateWidth(title, MAXIMUM_TITLE_TEXT_SIZE);
    }

    @Override
    public int getMinimumVerticalSize() {
        return Sizer.calculateHeight(MINIMUM_TITLE_TEXT_SIZE);
    }

    @Override
    public int getMinimumHorizontalSize() {
        return Sizer.calculateWidth(title, MINIMUM_TITLE_TEXT_SIZE);
    }

    @Override
    public void draw(Subcanvas g, Theme theme) {
        // Allow margins, so occupy half the area
        titleLabel.setTargetHeight(g.getHeight() / 2);
        titleLabel.draw(g, 0, g.getHeight() / 2);
    }
}
