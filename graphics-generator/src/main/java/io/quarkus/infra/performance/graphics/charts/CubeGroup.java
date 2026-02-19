package io.quarkus.infra.performance.graphics.charts;

public class CubeGroup {
    private int numCubesPerColumn = 16;
    private int unitsPerCube = 1; // For now, assume 1mb per square
    private int totalCubeSize;

    public void setNumCubesPerColumn(int n) {
        this.numCubesPerColumn = n;
    }

    public int getNumCubesPerColumn() {
        return numCubesPerColumn;
    }

    public double getUnitsPerCube() {
        return unitsPerCube;
    }

    public void setUnitsPerCube(int unitsPerCube) {
        this.unitsPerCube = unitsPerCube;
    }

    // The property including padding
    public int getTotalCubeSize() {
        return totalCubeSize;
    }

    public void setTotalCubeSize(int totalCubeSize) {
        this.totalCubeSize = totalCubeSize;
    }

    public void decrementTotalCubeSize() {
        this.totalCubeSize -= 1;
    }
}
