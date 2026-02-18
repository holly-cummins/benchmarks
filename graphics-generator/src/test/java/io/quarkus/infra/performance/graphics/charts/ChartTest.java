package io.quarkus.infra.performance.graphics.charts;

import java.awt.Dimension;
import java.util.Random;
import java.util.function.Function;

import io.quarkus.infra.performance.graphics.PlotDefinition;
import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.model.BenchmarkData;
import io.quarkus.infra.performance.graphics.model.Config;
import io.quarkus.infra.performance.graphics.model.Framework;
import io.quarkus.infra.performance.graphics.model.FrameworkBuild;
import io.quarkus.infra.performance.graphics.model.Group;
import io.quarkus.infra.performance.graphics.model.Load;
import io.quarkus.infra.performance.graphics.model.Repo;
import io.quarkus.infra.performance.graphics.model.Result;
import io.quarkus.infra.performance.graphics.model.Results;
import io.quarkus.infra.performance.graphics.model.units.DimensionalNumber;
import io.quarkus.infra.performance.graphics.model.units.TransactionsPerSecond;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.svggen.SVGGraphics2D;
import org.junit.jupiter.api.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;

import static org.apache.batik.util.SVGConstants.SVG_NAMESPACE_URI;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public abstract class ChartTest {

    @Test
    public void testCanDrawInMinimumDimensions() {
        BenchmarkData data = mockBenchmarkData();
        PlotDefinition plotDefinition = createPlotDefinition();

        Chart chart = createChart(plotDefinition, data);

        int minHeight = chart.getMinimumVerticalSize();
        int minWidth = chart.getMinimumHorizontalSize();

        SVGGraphics2D svgGenerator = getSvgGraphics2D(minWidth, minHeight);
        Theme theme = Theme.DARK;
        chart.draw(new Subcanvas(svgGenerator), theme);

    }

    private static PlotDefinition createPlotDefinition() {
        Function<Result, ? extends DimensionalNumber> fun = framework -> framework.load().avThroughput();
        PlotDefinition plotDefinition = new PlotDefinition("test plot", "some subtitle", fun);
        return plotDefinition;
    }

    private static BenchmarkData mockBenchmarkData() {
        BenchmarkData data = mock(BenchmarkData.class);
        Results results = new Results();
        when(data.results()).thenReturn(results);
        when(data.group()).thenReturn(Group.ALL);
        addDatapoint(data, Framework.QUARKUS3_JVM, (double) new Random().nextInt());
        addDatapoint(data, Framework.SPRING3_JVM, 267.87);
        addConfig(data);
        return data;
    }

    protected static void addDatapoint(BenchmarkData data, Framework framework, Double throughput) {
        Result result = mock(Result.class);
        data.results().addFramework(framework.getName(), result);
        Load load = mock(Load.class);
        when(result.load()).thenReturn(load);
        when(load.avThroughput()).thenReturn(new TransactionsPerSecond(throughput));
    }

    protected static void addConfig(BenchmarkData data) {
        Config config = mock(Config.class);
        when(data.config()).thenReturn(config);
        when(config.repo()).thenReturn(new Repo("main", "somerepo", "ootb", "1234"));
        when(config.quarkus()).thenReturn(new FrameworkBuild("", "3.28.3"));
    }

    protected static void draw(Chart chart) {
        SVGGraphics2D svgGenerator = getSvgGraphics2D(chart.getPreferredHorizontalSize(), chart.getPreferredVerticalSize());
        Theme theme = Theme.DARK;
        chart.draw(new Subcanvas(svgGenerator), theme);
    }

    protected static SVGGraphics2D getSvgGraphics2D(int width, int height) {
        DOMImplementation impl = SVGDOMImplementation.getDOMImplementation();
        Document doc = impl.createDocument(SVG_NAMESPACE_URI, "svg", null);

        SVGGraphics2D svgGenerator = new SVGGraphics2D(doc);
        svgGenerator.setSVGCanvasSize(new Dimension(width, height));
        return svgGenerator;
    }

    protected abstract Chart createChart(PlotDefinition plotDefinition, BenchmarkData data);
}
