// Author: Josiah Mathews
package coconuts;

public class ObjectRemover implements HitObserver {
    private final OhCoconutsGameManager gameManager;

    public ObjectRemover(OhCoconutsGameManager theGame) {
        this.gameManager = theGame;
    }

    /**
     * Properly Removes the HitEvent objects through Game Manager once notified.
     * @param hitEvent HitEvent from collision in Game
     */
    @Override
    public void update(HitEvent hitEvent) {
        gameManager.scheduleForDeletion(hitEvent.islandObject1);
        gameManager.scheduleForDeletion(hitEvent.islandObject2);
    }
}
