package smartfactory.motion;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;

/** Lightweight canvas compass so the lesson needs no third-party chart control. */
public final class CompassView extends Region {
    private static final Color BRAND_BLUE = Color.web("#155eef");
    private static final Color BRAND_RED = Color.web("#e3222d");
    private static final Color MUTED = Color.web("#708095");
    private static final Color TEXT = Color.web("#d7dee8");
    private final Canvas canvas = new Canvas();
    private final DoubleProperty heading = new SimpleDoubleProperty(this, "heading", 0.0);

    public CompassView() {
        getChildren().add(canvas);
        setMinSize(260, 260);
        setPrefSize(340, 340);
        widthProperty().addListener(observable -> draw());
        heightProperty().addListener(observable -> draw());
        heading.addListener(observable -> draw());
    }

    public void setHeading(double degrees) {
        heading.set(((degrees % 360.0) + 360.0) % 360.0);
    }

    public double getHeading() {
        return heading.get();
    }

    @Override
    protected void layoutChildren() {
        double size = Math.min(getWidth(), getHeight());
        canvas.setWidth(size);
        canvas.setHeight(size);
        canvas.relocate((getWidth() - size) / 2.0, (getHeight() - size) / 2.0);
        draw();
    }

    private void draw() {
        double size = Math.min(canvas.getWidth(), canvas.getHeight());
        if (size <= 0) {
            return;
        }
        GraphicsContext graphics = canvas.getGraphicsContext2D();
        graphics.clearRect(0, 0, size, size);
        double center = size / 2.0;
        double radius = size * 0.43;

        graphics.setFill(Color.web("#0d131b"));
        graphics.fillOval(center - radius, center - radius, radius * 2, radius * 2);
        graphics.setStroke(Color.web("#344052"));
        graphics.setLineWidth(2.0);
        graphics.strokeOval(center - radius, center - radius, radius * 2, radius * 2);
        graphics.setStroke(Color.web("#202a38"));
        graphics.setLineWidth(1.0);
        graphics.strokeOval(center - radius * 0.74, center - radius * 0.74,
                radius * 1.48, radius * 1.48);

        for (int degree = 0; degree < 360; degree += 5) {
            boolean major = degree % 30 == 0;
            boolean medium = degree % 10 == 0;
            double outer = radius - 8;
            double inner = outer - (major ? 16 : medium ? 10 : 5);
            double radians = Math.toRadians(degree - 90);
            graphics.setStroke(major ? TEXT : MUTED);
            graphics.setLineWidth(major ? 2.0 : 1.0);
            graphics.strokeLine(
                    center + Math.cos(radians) * inner,
                    center + Math.sin(radians) * inner,
                    center + Math.cos(radians) * outer,
                    center + Math.sin(radians) * outer
            );
        }

        drawCardinal(graphics, center, radius * 0.68, "N", 0, BRAND_RED);
        drawCardinal(graphics, center, radius * 0.68, "E", 90, TEXT);
        drawCardinal(graphics, center, radius * 0.68, "S", 180, TEXT);
        drawCardinal(graphics, center, radius * 0.68, "W", 270, TEXT);

        double angle = Math.toRadians(getHeading() - 90.0);
        double tipX = center + Math.cos(angle) * radius * 0.61;
        double tipY = center + Math.sin(angle) * radius * 0.61;
        double tailX = center - Math.cos(angle) * radius * 0.35;
        double tailY = center - Math.sin(angle) * radius * 0.35;

        graphics.setStroke(BRAND_BLUE);
        graphics.setLineWidth(6.0);
        graphics.strokeLine(tailX, tailY, center, center);
        graphics.setStroke(BRAND_RED);
        graphics.strokeLine(center, center, tipX, tipY);

        double leftAngle = angle + Math.toRadians(155);
        double rightAngle = angle - Math.toRadians(155);
        graphics.setFill(BRAND_RED);
        graphics.fillPolygon(
                new double[]{tipX, tipX + Math.cos(leftAngle) * 22, tipX + Math.cos(rightAngle) * 22},
                new double[]{tipY, tipY + Math.sin(leftAngle) * 22, tipY + Math.sin(rightAngle) * 22},
                3
        );
        graphics.setFill(Color.web("#dce3eb"));
        graphics.fillOval(center - 7, center - 7, 14, 14);
        graphics.setFill(Color.web("#0d131b"));
        graphics.fillOval(center - 3, center - 3, 6, 6);
    }

    private void drawCardinal(
            GraphicsContext graphics,
            double center,
            double distance,
            String text,
            double degree,
            Color color
    ) {
        double radians = Math.toRadians(degree - 90);
        graphics.setFill(color);
        graphics.setFont(Font.font("System", FontWeight.BOLD, 17));
        graphics.setTextAlign(TextAlignment.CENTER);
        graphics.setTextBaseline(VPos.CENTER);
        graphics.fillText(
                text,
                center + Math.cos(radians) * distance,
                center + Math.sin(radians) * distance
        );
    }
}
