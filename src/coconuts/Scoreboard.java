/**
 * Author: Shritej
 * Description:
 *  This observer updates the scoreboard whenever a laser
 *  destroys a coconut or when a coconut hits the beach.
 *  It listens for hit events and updates the label counts
 *  on the JavaFX interface.
 */

package coconuts;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Scoreboard implements HitObserver {
    private final Label coconutsDestroyedLabel;
    private final Label hitBeachLabel;
    private int destroyed = 0;
    private int hitBeach = 0;

    /**
     * Creates a scoreboard observer that displays the counts
     * of destroyed coconuts and those that hit the beach.
     *
     * @param destroyedCoconutsLabel label showing destroyed coconuts
     * @param coconutsHitBeachLabel label showing coconuts that reached the beach
     */
    public Scoreboard(Label destroyedCoconutsLabel, Label coconutsHitBeachLabel) {
        this.coconutsDestroyedLabel = destroyedCoconutsLabel;
        this.hitBeachLabel = coconutsHitBeachLabel;

        Platform.runLater(() -> {
            coconutsDestroyedLabel.setText("0");
            hitBeachLabel.setText("0");
        });
    }

    /**
     * Called whenever two game objects collide.
     * Checks if a laser hit a coconut or a coconut hit the beach,
     * and updates the proper counter label accordingly.
     *
     * @param e the collision (hit) event between two island objects
     */
    @Override
    public void update(HitEvent e) {
        IslandObject a = e.islandObject1;
        IslandObject b = e.islandObject2;

        boolean laserHitsCoconut =
                (isLaser(a) && isCoconut(b)) || (isLaser(b) && isCoconut(a));

        boolean coconutHitsBeach =
                (isCoconut(a) && isBeach(b)) || (isCoconut(b) && isBeach(a));

        if (laserHitsCoconut) {
            destroyed++;
            Platform.runLater(() ->
                    coconutsDestroyedLabel.setText(Integer.toString(destroyed))
            );
        } else if (coconutHitsBeach) {
            hitBeach++;
            Platform.runLater(() ->
                    hitBeachLabel.setText(Integer.toString(hitBeach))
            );
        }
    }

    /**
     * Checks if the given object is a laser beam.
     * @param o island object
     * @return true if the object is a laser
     */
    private boolean isLaser(IslandObject o) {
        return !o.isHittable() && !o.isGroundObject() && !o.isFalling();
    }

    /**
     * Checks if the given object is a coconut.
     * @param o island object
     * @return true if the object is falling and hittable
     */
    private boolean isCoconut(IslandObject o) {
        return o.isHittable() && o.isFalling();
    }

    /**
     * Checks if the given object is the beach.
     * @param o island object
     * @return true if it's the beach (ground with no image)
     */
    private boolean isBeach(IslandObject o) {
        return o.isGroundObject() && o.getImageView() == null;
    }
}