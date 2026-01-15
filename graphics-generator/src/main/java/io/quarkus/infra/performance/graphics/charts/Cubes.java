package io.quarkus.infra.performance.graphics.charts;

import java.awt.Font;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.VAlignment;

import static io.quarkus.infra.performance.graphics.charts.Bar.BAR_THICKNESS;

public class Cubes implements ElasticElement {
    private static final int LABEL_HEIGHT = BAR_THICKNESS;
    public static final double DECREMENT_INCREMENT = 0.97;
    private final Datapoint d;
    private static int numCubesPerColumn = 16;
    private static final int CUBE_PADDING = 1;
    private final int unitsPerCube = 1; // For now, assume 1mb per square
    private int totalCubeSize;
    private final Label valueLabel;
    private final Label frameworkLabel;
    private static final int MINIMUM_CUBE_SIZE = 2;
    private static int maximumCubeSize = 20;

    public Cubes(Datapoint d) {
        this.d = d;
        double val = d.value().getValue();

        frameworkLabel = new Label(d.framework().getExpandedName())
                .setHorizontalAlignment(Alignment.CENTER)
                .setVerticalAlignment(VAlignment.TOP)
                .setTargetHeight(LABEL_HEIGHT);
        valueLabel = new Label(String.format("%d %s", Math.round(val), d.value().getUnits()))
                .setHorizontalAlignment(Alignment.CENTER)
                .setVerticalAlignment(VAlignment.TOP)
                .setStyle(Font.BOLD).setTargetHeight(LABEL_HEIGHT * 2 / 3);
    }

    public static void setNumCubesPerColumn(int num) {
        numCubesPerColumn = num;
    }

    public static void setMaxCubeSize(int m) {
        maximumCubeSize = m;
    }

    @Override
    public int getMaximumVerticalSize() {
        return (int) Math.min(numCubesPerColumn, d.value().getValue()) * maximumCubeSize + valueLabel.getTargetHeight()
                + frameworkLabel.getTargetHeight();
    }

    @Override
    public int getMaximumHorizontalSize() {
        return (int) Math.ceil(d.value().getValue() / numCubesPerColumn * maximumCubeSize);
    }

    @Override
    public int getMinimumVerticalSize() {
        return (int) Math.min(numCubesPerColumn, d.value().getValue()) * MINIMUM_CUBE_SIZE + valueLabel.getTargetHeight()
                + frameworkLabel.getTargetHeight();
    }

    @Override
    public int getMinimumHorizontalSize() {
        // Assume we can shrink columns smaller than the label
        int widestLabel = Math.max(valueLabel.calculateWidth(), frameworkLabel.calculateWidth());
        int cubesWidth = (int) Math.ceil(d.value().getValue() / numCubesPerColumn * MINIMUM_CUBE_SIZE);
        return Math.max(widestLabel, cubesWidth);
    }

    public int getActualHorizontalSize() {
        // Assume we can shrink columns smaller than the label
        int widestLabel = Math.max(valueLabel.calculateWidth(), frameworkLabel.calculateWidth());
        int cubesWidth = (int) Math.ceil(d.value().getValue() / numCubesPerColumn * totalCubeSize);
        return Math.max(widestLabel, cubesWidth);
    }

    @Override
    public void draw(Subcanvas dataArea, Theme theme) {
        double val = d.value().getValue();
        // If this framework isn't found, it will just be the text colour, which is fine
        dataArea.setPaint(theme.chartElements().get(d.framework()));

        int cubeSize = totalCubeSize - CUBE_PADDING;

        Subcanvas cubeArea = new Subcanvas(dataArea, dataArea.getWidth(), numCubesPerColumn * totalCubeSize, 0, 0);

        Subcanvas labelArea = new Subcanvas(dataArea, dataArea.getWidth(), dataArea.getHeight() - cubeArea.getHeight(), 0,
                cubeArea.getHeight());

        int startingY = cubeArea.getHeight() - totalCubeSize;

        // Center the cubes
        int cx = (cubeArea.getWidth() - (getColumnCount() * totalCubeSize)) / 2;
        int cy = startingY;
        int numCubes = (int) Math.round(val / unitsPerCube);

        for (int i = 0; i < numCubes; i++) {
            cubeArea.fillRect(cx, cy, cubeSize, cubeSize);
            cy -= totalCubeSize;
            if ((i + 1) % numCubesPerColumn==0) {
                cx += totalCubeSize;
                cy = startingY;
            }

        }

        labelArea.setPaint(theme.text());
        int labelY = 0;
        frameworkLabel.draw(labelArea, labelArea.getWidth() / 2, 0);
        valueLabel.draw(labelArea, labelArea.getWidth() / 2, labelY + LABEL_HEIGHT);
    }

    // Includes the padding
    public void setCubeSize(int totalCubeSize) {
        this.totalCubeSize = totalCubeSize;
    }

    public int getColumnCount() {
        return (int) Math.ceil(d.value().getValue() / (numCubesPerColumn * unitsPerCube));
    }

    public void decrementFonts() {
        frameworkLabel.setTargetHeight((int) (frameworkLabel.getTargetHeight() * DECREMENT_INCREMENT));
        valueLabel.setTargetHeight((int) (valueLabel.getTargetHeight() * DECREMENT_INCREMENT));
    }
}
