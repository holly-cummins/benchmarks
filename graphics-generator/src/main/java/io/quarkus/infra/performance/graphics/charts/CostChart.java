package io.quarkus.infra.performance.graphics.charts;

import io.quarkus.infra.performance.graphics.CostPlotDefinition;
import io.quarkus.infra.performance.graphics.PlotDefinition;
import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.charts.fonts.Alignment;
import io.quarkus.infra.performance.graphics.charts.fonts.VAlignment;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Ec2Pricing;

import java.util.Optional;

public class CostChart extends LoadDensityChart {

    private static final int RIGHT_AXIS_LABEL_WIDTH = 80;
    private static final int HOURS_PER_YEAR = 8760;

    private final Ec2Pricing pricing;

    public CostChart(PlotDefinition plotDefinition, BenchmarkData bmData) {
        this(plotDefinition, bmData, EmbedOptions.DEFAULT);
    }

    public CostChart(PlotDefinition plotDefinition, BenchmarkData bmData, EmbedOptions embedOptions) {
        super(plotDefinition, bmData, embedOptions);

        if (plotDefinition instanceof CostPlotDefinition cpDef) {
            this.pricing = cpDef.pricing();
        } else {
            throw new IllegalArgumentException(
                    "Cannot construct a " + this.getClass().getName()
                            + " with a " + plotDefinition.getClass());
        }

        if (!embedOptions.isEmbedded()) {
            if (fineprint.isPresent()) {
                children.remove(fineprint.get());
            }
            this.fineprint = Optional.of(new FinePrint(bmData,
                    "Assumptions: Throughput is the limiting factor; instances = ceil(load / max throughput)",
                    "EC2: " + pricing.instanceType() + " (" + pricing.vCPUs() + " vCPU, "
                            + pricing.memoryGiB() + " GiB), $" + formatPrice(pricing.onDemandPricePerHour())
                            + "/hr on-demand, " + pricing.region()));
            children.add(fineprint.get());
        }
    }

    @Override
    protected int getRightMargin() {
        return RIGHT_AXIS_LABEL_WIDTH;
    }

    @Override
    protected void drawAxes(Subcanvas g, Theme theme, int chartLeft, int chartTop, int chartBottom,
                             int chartRight, int chartWidth, int chartHeight) {
        super.drawAxes(g, theme, chartLeft, chartTop, chartBottom, chartRight, chartWidth, chartHeight);

        g.setPaint(theme.text());
        g.drawLine(chartRight, chartTop, chartRight, chartBottom, 2);

        int yStep = (int) Math.max(1, niceStep(maxInstances, 8));
        for (int i = yStep; i <= maxInstances; i += yStep) {
            int y = chartBottom - (int) ((double) i / maxInstances * chartHeight);
            g.drawLine(chartRight, y, chartRight + TICK_LENGTH, y);

            double cost = i * pricing.onDemandPricePerHour() * HOURS_PER_YEAR;
            String costText = "$" + formatAnnualCost(cost);
            int tickCount = maxInstances / yStep;
            Label tickLabel = new Label(costText)
                    .setTargetHeight(Math.min(AXIS_LABEL_FONT_SIZE, chartHeight / (tickCount + 1)))
                    .setHorizontalAlignment(Alignment.LEFT)
                    .setVerticalAlignment(VAlignment.MIDDLE);
            tickLabel.draw(g, chartRight + TICK_LENGTH + 4, y);
        }

        int labelHeight = Math.min(AXIS_LABEL_FONT_SIZE, RIGHT_AXIS_LABEL_WIDTH * 2 / 3);
        Label rightAxisLabel = new Label("Cost ($/year)")
                .setTargetHeight(labelHeight)
                .setHorizontalAlignment(Alignment.CENTER)
                .setVerticalAlignment(VAlignment.BOTTOM);
        int labelX = chartRight + RIGHT_AXIS_LABEL_WIDTH / 2;
        rightAxisLabel.draw(g, labelX, chartTop - 2);
    }

    private static String formatAnnualCost(double cost) {
        if (cost >= 1000) {
            double k = cost / 1000;
            if (k == Math.floor(k)) {
                return String.format("%.0fK", k);
            }
            return String.format("%.1fK", k);
        }
        return String.format("%.0f", cost);
    }

    private static String formatPrice(double price) {
        if (price >= 1.0) {
            return String.format("%.2f", price);
        }
        String s = String.format("%.3f", price);
        if (s.endsWith("0")) {
            return s.substring(0, s.length() - 1);
        }
        return s;
    }
}
