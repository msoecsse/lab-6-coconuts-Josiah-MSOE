/**
 * Author: Shritej
 * Description:
 *  This observer checks when important game events happen,
 *  such as when a falling coconut hits the crab.
 *  If the crab is hit, it ends the game and shows the Game Over label.
 */

package coconuts;

import javafx.scene.control.Label;

public class GameStateManager implements HitObserver {
    private final OhCoconutsGameManager game;
    public final Label gameOverLabel;

    /**
     * Creates a GameStateManager observer.
     * It connects to the main game manager and the Game Over label.
     *
     * @param theGame the main game manager
     * @param gameOverLabel the label that shows "Game Over" when the crab dies
     */
    public GameStateManager(OhCoconutsGameManager theGame, Label gameOverLabel) {
        this.game = theGame;
        this.gameOverLabel = gameOverLabel;
    }

    /**
     * Called when any two objects collide in the game.
     * If a coconut hits the crab, the crab is removed,
     * the coconut is deleted, and the Game Over label appears.
     *
     * @param e the collision event between two island objects
     */
    @Override
    public void update(HitEvent e) {
        IslandObject a = e.islandObject1;
        IslandObject b = e.islandObject2;

        IslandObject coconut = (a.isHittable() && a.isFalling()) ? a
                : (b.isHittable() && b.isFalling()) ? b : null;
        if (coconut == null) return;

        IslandObject other = (coconut == a) ? b : a;

        boolean isCrab = other.isGroundObject() && other.getImageView() != null;
        if (isCrab) {
            game.killCrab();
            game.scheduleForDeletion(other);
            game.scheduleForDeletion(coconut);
            gameOverLabel.setVisible(true);
        }
    }

    /**
     * Checks if the given object is a coconut.
     * @param o island object
     * @return true if it's a falling, hittable coconut
     */
    private boolean isCoconut(IslandObject o) {
        return o.isHittable() && o.isFalling();
    }

    /**
     * Checks if the given object is a crab.
     * @param o island object
     * @return true if it's a ground object that can be hit
     */
    private boolean isCrab(IslandObject o) {
        return o.isGroundObject() && o.isHittable();
    }
}