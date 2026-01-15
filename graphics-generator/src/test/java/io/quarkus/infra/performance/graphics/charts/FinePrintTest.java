package io.quarkus.infra.performance.graphics.charts;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

import io.quarkus.infra.performance.graphics.model.Config;
import io.quarkus.infra.performance.graphics.model.FrameworkBuild;
import io.quarkus.infra.performance.graphics.model.Repo;

class FinePrintTest extends ElasticElementTest {

    @Test
    public void quarkusVersionIsPresent() {
        Config config = mock(Config.class);
        when(config.repo()).thenReturn(new Repo("main", "somerepo", "ootb"));
        when(config.quarkus()).thenReturn(new FrameworkBuild("", "3.28.3"));

        FinePrint p = new FinePrint(config);
        String s = drawSvg(p);
        assertTrue(s.contains("3.28.3"), s);
    }

    @Test
    public void quarkusVersionIsLabelled() {
        Config config = mock(Config.class);
        when(config.repo()).thenReturn(new Repo("main", "somerepo", "ootb"));
        when(config.quarkus()).thenReturn(new FrameworkBuild("", "3.28.3"));

        FinePrint p = new FinePrint(config);
        String s = drawSvg(p);
        assertTrue(s.contains("Quarkus: 3.28.3"), s);
    }

    @Test
    public void unqualifiedSpringVersionIsLabelled() {
        Config config = mock(Config.class);
        when(config.repo()).thenReturn(new Repo("main", "somerepo", "ootb"));
        when(config.springboot()).thenReturn(new FrameworkBuild("", "3.10.3"));

        FinePrint p = new FinePrint(config);
        String s = drawSvg(p);
        assertTrue(s.contains("Spring: 3.10.3"), s);
    }

    @Test
    public void qualifiedSpringVersionIsLabelledForSpring3() {
        Config config = mock(Config.class);
        when(config.repo()).thenReturn(new Repo("main", "somerepo", "ootb"));
        when(config.springboot3()).thenReturn(new FrameworkBuild("", "3.10.3"));
        FinePrint p = new FinePrint(config);
        String s = drawSvg(p);
        assertTrue(s.contains("Spring: 3.10.3"), s);

    }

    @Test
    public void qualifiedSpringVersionIsLabelledForSpring4() {
        Config config = mock(Config.class);
        when(config.repo()).thenReturn(new Repo("main", "somerepo", "ootb"));
        when(config.springboot4()).thenReturn(new FrameworkBuild("", "4.10.3"));
        FinePrint p = new FinePrint(config);
        String s = drawSvg(p);
        assertTrue(s.contains("Spring: 4.10.3"), s);
    }

    @Test
    public void springVersionsHaveDistinctLabelsWhenMultipleArePresent() {
        Config config = mock(Config.class);
        when(config.repo()).thenReturn(new Repo("main", "somerepo", "ootb"));
        when(config.springboot3()).thenReturn(new FrameworkBuild("", "3.10.3"));
        when(config.springboot4()).thenReturn(new FrameworkBuild("", "4.10.3"));
        FinePrint p = new FinePrint(config);
        String s = drawSvg(p);
        assertTrue(s.contains("Spring 3: 3.10.3"), s);
        assertTrue(s.contains("Spring 4: 4.10.3"), s);

    }

    @Test
    void scenarioIncluded() {
      var config = mock(Config.class);
      when(config.repo()).thenReturn(new Repo("main", "somerepo", "ootb"));

      var finePrint = new FinePrint(config);
      var s = drawSvg(finePrint);

      assertTrue(s.contains("Scenario: ootb"));
    }
}