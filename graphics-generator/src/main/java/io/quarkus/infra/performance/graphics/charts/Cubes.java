package io.quarkus.infra.performance.graphics.charts;

import static io.quarkus.infra.performance.graphics.charts.Bar.BAR_THICKNESS;

import java.awt.Font;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.VAlignment;

public class Cubes implements ElasticElement {
    private final Datapoint d;
    private static int numCubesPerColumn = 16;
    private int cubePadding = 1;
    private int unitsPerCube = 1; // For now, assume 1mb per square
    private int totalCubeSize;
    private final Label valueLabel;
    private final Label frameworkLabel;

    public Cubes(Datapoint d) {
        this.d = d;
        double val = d.value().getValue();

        frameworkLabel = new Label(d.framework().getExpandedName())
                .setHorizontalAlignment(Alignment.CENTER)
                .setVerticalAlignment(VAlignment.TOP)
                .setTargetHeight(BAR_THICKNESS);
        valueLabel = new Label(String.format("%d %s", Math.round(val), d.value().getUnits()))
                .setHorizontalAlignment(Alignment.CENTER)
                .setVerticalAlignment(VAlignment.TOP)
                .setStyle(Font.BOLD).setTargetHeight(BAR_THICKNESS * 2 / 3);
    }

    public static void setNumCubesPerColumn(int num) {
        numCubesPerColumn = num;
    }

    @Override
    public int getMaximumVerticalSize() {
        return 0;
    }

    @Override
    public int getMaximumHorizontalSize() {
        // We can scale arbitrarily big
        return 1000;
    }

    @Override
    public int getMinimumVerticalSize() {
        return 0;
    }

    @Override
    public int getMinimumHorizontalSize() {
        // Assume we can shrink columns smaller than the label
        return Math.max(valueLabel.calculateWidth(), frameworkLabel.calculateWidth());
    }

    @Override
    public void draw(Subcanvas dataArea, Theme theme) {
        double val = d.value().getValue();
        // If this framework isn't found, it will just be the text colour, which is fine
        dataArea.setPaint(theme.chartElements().get(d.framework()));

        int cubeSize = totalCubeSize - cubePadding;

        Subcanvas cubeArea = new Subcanvas(dataArea, dataArea.getWidth(), numCubesPerColumn * totalCubeSize, 0, 0);

        Subcanvas labelArea = new Subcanvas(dataArea, dataArea.getWidth(), dataArea.getHeight() - cubeArea.getHeight(), 0,
                cubeArea.getHeight());

        int startingCy = cubeArea.getHeight() - totalCubeSize;
        int cx = 0, cy = startingCy;
        int numCubes = (int) Math.round(val / unitsPerCube);

        for (int i = 0; i < numCubes; i++) {
            cubeArea.fillRect(cx, cy, cubeSize, cubeSize);
            cy -= totalCubeSize;
            if ((i + 1) % numCubesPerColumn == 0) {
                cx += totalCubeSize;
                cy = startingCy;
            }

        }

        labelArea.setPaint(theme.text());
        int labelY = 0;
        frameworkLabel.draw(labelArea, labelArea.getWidth() / 2, 0);
        valueLabel.draw(labelArea, labelArea.getWidth() / 2, labelY + BAR_THICKNESS);
    }

    // Includes the padding
    public void setCubeSize(int totalCubeSize) {
        this.totalCubeSize = totalCubeSize;
    }

    public int getColumnCount() {
        return (int) Math.ceil(d.value().getValue() / (numCubesPerColumn * unitsPerCube));
    }
}
