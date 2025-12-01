package io.quarkus.infra.performance.graphics.charts;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.quarkus.infra.performance.graphics.Theme;
import io.quarkus.infra.performance.graphics.VAlignment;
import io.quarkus.infra.performance.graphics.model.Config;

public class FinePrint {

    private static final int padding = 60;

    private final Subcanvas g;
    private final int canvasHeight;
    private final int canvasWidth;
    private final Theme theme;
    private InlinedSVG svg;

    public FinePrint(Subcanvas g, Theme theme) {
        this.g = g;
        this.theme = theme;
        this.canvasHeight = g.getHeight();
        this.canvasWidth = g.getWidth();

    }

    public void draw(Config metadata) {

        g.setPaint(theme.background());

        g.setPaint(theme.text());

        List<String> leftColumn = new ArrayList<>();
        List<String> rightColumn = new ArrayList();

        if (metadata.quarkus() != null) {
            leftColumn.add("Quarkus: "
                    + metadata.quarkus().version());
        }
        if (metadata.springboot() != null) {
            leftColumn.add("Spring: "
                    + metadata.springboot().version());
        }
        if (metadata.jvm() != null) {

            leftColumn.add("JVM: "
                    + metadata.jvm().version());

            if (metadata.jvm().graalVM() != null) {
                leftColumn.add("GraalVM: "
                        + metadata.jvm().graalVM().version());
            }
            if (metadata.jvm().memory() != null) {
                rightColumn.add("Memory: "
                        + metadata.jvm().memory());
            }

            if (metadata.jvm().args() != null && metadata.jvm().args().contains("-XX:ActiveProcessorCount=")) {
                // TODO risky calculation, what if the core is a cgroup or something else and not an arg?
                rightColumn.add("CPUs: "
                        + metadata.jvm().args().replace("-XX:ActiveProcessorCount=", ""));
            }
        }

        if (metadata.repo() != null) {
            rightColumn.add("Source code: "
                    + metadata.repo().url().replace("https://github.com/", "     ").replaceAll(".git$", ""));
            // Use a few spaces to leave room for a logo
        }

        if (!"main".equals(metadata.repo().branch())) {
            rightColumn.add("Branch: " + metadata.repo().branch());

        }

        // Make sure font sizes are the same
        // TODO this can go away when we have label groups
        while (rightColumn.size() < leftColumn.size()) {
            // Put the padding before the last source control line
            rightColumn.add(Math.max(0, rightColumn.size() - 1), " ");
        }

        Label leftLabel = new Label(leftColumn.toArray(String[]::new), 0, 0)
                .setHorizontalAlignment(Alignment.LEFT)
                .setVerticalAlignment(VAlignment.TOP)
                .setTargetHeight(g.getHeight());
        leftLabel.draw(g);

        int leftLabelWidth = leftLabel
                .calculateWidth(leftColumn.stream().max(Comparator.comparingInt(String::length)).orElse(""));
        int rightLabelX = leftLabelWidth
                + padding;
        Label rightLabel = new Label(rightColumn.toArray(String[]::new), rightLabelX, 0)
                .setHorizontalAlignment(Alignment.LEFT)
                .setVerticalAlignment(VAlignment.TOP)
                .setTargetHeight(g.getHeight());
        rightLabel.draw(g);
        int sw = rightLabel.calculateWidth("Source code:");

        if (metadata.repo() != null) {
            int logoSize = rightLabel.getAscent();
            int logoX = g.getXOffset() + rightLabelX + sw + 2;
            int logoY = g.getYOffset() + (rightColumn.size()) * rightLabel.getLineHeight() - rightLabel.getAscent()
                    + rightLabel.getDescent() / 2;
            this.svg = new InlinedSVG(getPath(theme), logoSize,
                    logoX,
                    logoY);
        }
    }

    private static String getPath(Theme theme) {
        if (theme == Theme.DARK) {
            return "/github-mark-dark.svg";
        } else {
            return "/github-mark-light.svg";
        }
    }

    public Collection<InlinedSVG> getInlinedSVGs() {
        if (svg == null) {
            return Collections.emptyList();
        }
        return List.of(svg);
    }
}
