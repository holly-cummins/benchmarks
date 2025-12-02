package io.quarkus.infra.performance.graphics.charts;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.model.Config;

public class BarChart extends Chart {

    private final FinePrint fineprint;
    private final List<Bar> bars = new ArrayList<>();

    public BarChart(String titleText, List<Datapoint> data, Config metadata) {
        super(titleText, data, metadata);

        this.fineprint = new FinePrint(metadata);
        children.add(fineprint);

        for (Datapoint d : data) {
            Bar e = new Bar(d);
            bars.add(e);
            children.add(e);
        }

    }

    @Override
    protected void drawNoCheck(Subcanvas g, Theme theme) {
        int canvasHeight = g.getHeight();
        int canvasWidth = g.getWidth();

        g.setPaint(theme.background());
        g.fill(new Rectangle2D.Double(0, 0, canvasWidth, canvasWidth));

        int margins = 20;
        int ymargins = 20;

        int finePrintHeight = 80;

        Subcanvas canvasWithMargins = new Subcanvas(g, canvasWidth - 2 * margins, canvasHeight - 2 * ymargins, margins,
                ymargins);

        g.setPaint(theme.text());
        Subcanvas titleCanvas = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(), titleTextSize * 2, 0, 0);
        titleLabel.setTargetHeight(titleTextSize).draw(titleCanvas, 0, titleTextSize);

        Subcanvas barArea = new Subcanvas(canvasWithMargins, canvasWithMargins.getWidth(),
                canvasWithMargins.getHeight() - titleCanvas.getHeight() - finePrintHeight, 0, titleCanvas.getHeight());

        int leftLabelWidth = Sizer.calculateWidth(bars.stream().map(Bar::getLeftLabelText).collect(Collectors.toSet()),
                Bar.LEFT_LABEL_SIZE);

        // Set a common left label width before trying to calculate a scale
        for (Bar bar : bars) {
            bar.setLeftLabelWidth(leftLabelWidth);
        }

        double scale = bars.stream().mapToDouble(bar -> bar.getMaximumScale(barArea)).min().orElse(1);

        int y = 0;

        int finePrintPadding = 300; // TODO Arbitrary fudge padding, remove when scaling work is done
        Subcanvas finePrintArea = new Subcanvas(canvasWithMargins, barArea.getWidth() - 2 * finePrintPadding, finePrintHeight,
                finePrintPadding,
                barArea.getHeight());
        g.setPaint(theme.text());

        for (Bar bar : bars) {
            bar.setScale(scale);
            // TODO slight hack, assuming the min and max are always equal
            Subcanvas individualBarArea = new Subcanvas(barArea, barArea.getWidth(), bar.getMaximumVerticalSize(), 0, y);
            bar.draw(individualBarArea, theme);
            y += individualBarArea.getHeight();

        }

        fineprint.draw(finePrintArea, theme);
    }

    @Override
    public Collection<InlinedSVG> getInlinedSVGs() {
        return fineprint.getInlinedSVGs();
    }

}
