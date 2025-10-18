package coconuts;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Scoreboard implements HitObserver {
    private final Label coconutsDestroyedLabel;
    private final Label hitBeachLabel;
    private int destroyed = 0;
    private int hitBeach = 0;

    public Scoreboard(Label destroyedCoconutsLabel, Label coconutsHitBeachLabel) {
        this.coconutsDestroyedLabel = destroyedCoconutsLabel;
        this.hitBeachLabel = coconutsHitBeachLabel;

        Platform.runLater(() -> {
            coconutsDestroyedLabel.setText("0");
            hitBeachLabel.setText("0");
        });
    }

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
            Platform.runLater(() -> coconutsDestroyedLabel.setText(Integer.toString(destroyed)));
        } else if (coconutHitsBeach) {
            hitBeach++;
            Platform.runLater(() -> hitBeachLabel.setText(Integer.toString(hitBeach)));
        }
    }

    private boolean isLaser(IslandObject o) {
        return !o.isHittable() && !o.isGroundObject() && !o.isFalling();
    }
    private boolean isCoconut(IslandObject o) {
        return o.isHittable() && o.isFalling();
    }
    private boolean isBeach(IslandObject o) {
        return o.isGroundObject() && o.getImageView() == null;
    }

}