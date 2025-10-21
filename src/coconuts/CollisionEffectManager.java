/**
 * Author: Shritej
 * Description:
 *  This observer creates colorful visual effects whenever two
 *  game objects collide (like a laser hitting a coconut or a
 *  coconut hitting the crab). The effects use fade and scale
 *  animations to create a quick glowing burst.
 */

package coconuts;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

public class CollisionEffectManager implements HitObserver {
    private final Pane gamePane;

    /**
     * Creates a CollisionEffectManager that will display effects on the given pane.
     * @param gamePane the pane where the visual effects should appear
     */
    public CollisionEffectManager(Pane gamePane) {
        this.gamePane = gamePane;
    }

    /**
     * Called whenever two objects collide in the game.
     * Draws a colorful circle that glows, expands, and fades away at the
     * point of impact to show the collision visually.
     *
     * @param e the collision (hit) event between two objects
     */
    @Override
    public void update(HitEvent e) {
        // Find the middle point between both objects
        IslandObject a = e.islandObject1;
        IslandObject b = e.islandObject2;

        int ax = a.x + (a.width / 2);
        int ay = a.hittable_height();
        int bx = b.x + (b.width / 2);
        int by = b.hittable_height();

        double cx = (ax + bx) / 2.0;
        double cy = (ay + by) / 2.0;

        Platform.runLater(() -> {

            Color[] colors = {
                    Color.CYAN, Color.LIME, Color.ORANGE,
                    Color.MAGENTA, Color.DEEPPINK, Color.YELLOW,
                    Color.AQUA, Color.RED, Color.GREEN
            };
            Color randomColor = colors[(int) (Math.random() * colors.length)];

            Circle ring = new Circle(cx, cy, 8);
            ring.setStroke(randomColor);
            ring.setFill(Color.TRANSPARENT);
            ring.setStrokeWidth(3.5);

            gamePane.getChildren().add(ring);

            ScaleTransition grow = new ScaleTransition(Duration.millis(200), ring);
            grow.setFromX(1.0);
            grow.setFromY(1.0);
            grow.setToX(3.5);
            grow.setToY(3.5);

            FadeTransition fade = new FadeTransition(Duration.millis(200), ring);
            fade.setFromValue(1.0);
            fade.setToValue(0.0);

            ParallelTransition burst = new ParallelTransition(grow, fade);
            burst.setOnFinished(ev -> gamePane.getChildren().remove(ring));
            burst.play();
        });
    }
}