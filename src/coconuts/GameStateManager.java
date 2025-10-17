package coconuts;

// Tracks for if Game ends by a Coconut hit directly to the CRAB
public class GameStateManager implements HitObserver {
    private OhCoconutsGameManager gameManager;

    public GameStateManager(OhCoconutsGameManager theGame) {
        this.gameManager = theGame;
    }

    @Override
    public void update(HitEvent hitEvent) {

    }
}
