package coconuts;

import javafx.scene.control.Label;

public class Scoreboard implements HitObserver {
    private Label coconutsDestroyed;
    private Label hitBeach;

    public Scoreboard(Label destroyedCoconutsLabel, Label coconutsHitBeachLabel) {
        this.coconutsDestroyed = destroyedCoconutsLabel;
        this.hitBeach = coconutsHitBeachLabel;
    }

    @Override
    public void update(HitEvent hitEvent) {

    }
}
