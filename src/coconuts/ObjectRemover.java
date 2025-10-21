/**
 * Author: Shritej
 * Description:
 *  This observer class listens for hit (collision) events and removes
 *  the involved objects from the game when necessary.
 *  It handles cases like lasers hitting coconuts, coconuts hitting the beach,
 *  and coconuts hitting the crab.
 */

package coconuts;

public class ObjectRemover implements HitObserver {
    private final OhCoconutsGameManager gameManager;

    /**
     * Creates a new ObjectRemover observer for the given game manager.
     * @param theGame reference to the main game manager
     */
    public ObjectRemover(OhCoconutsGameManager theGame) {
        this.gameManager = theGame;
    }

    /**
     * Called whenever a hit (collision) happens in the game.
     * Depending on what objects collided, it removes the correct items
     * from the screen and updates coconut counts through the game manager.
     *
     * @param e the hit event that contains both colliding objects
     */
    @Override
    public void update(HitEvent e) {
        IslandObject a = e.islandObject1;
        IslandObject b = e.islandObject2;

        IslandObject coconut =
                (a.isHittable() && a.isFalling()) ? a :
                        (b.isHittable() && b.isFalling()) ? b : null;
        if (coconut == null) return;

        IslandObject other = (coconut == a) ? b : a;

        boolean isLaser = !other.isHittable() && !other.isGroundObject() && !other.isFalling();
        boolean isBeach = other.isGroundObject() && !other.isFalling();   // beach (ground)
        boolean isCrab  = other.isGroundObject() && other.isHittable();   // crab (ground + hittable)

        if (isLaser) {
            double p = 0.75;
            if (Math.random() <= p) {
                gameManager.scheduleForDeletion(coconut);
                gameManager.coconutDestroyed();
            }
            gameManager.scheduleForDeletion(other);
        }

        else if (isBeach) {
            gameManager.scheduleForDeletion(coconut);
            gameManager.coconutDestroyed();
        }

        else if (isCrab) {
            gameManager.scheduleForDeletion(coconut);
            gameManager.coconutDestroyed();
        }
    }
}