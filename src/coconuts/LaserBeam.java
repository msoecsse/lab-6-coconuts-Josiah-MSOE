package coconuts;

import javafx.scene.image.Image;

// Represents the beam of light moving from the crab to a coconut; can hit only falling objects
// This is a domain class; do not introduce JavaFX or other GUI components here
public class LaserBeam extends IslandObject {
    private static final int WIDTH = 16; // updated with image
    private static final Image laserImage = new Image("file:images/laser-1.png");

    public LaserBeam(OhCoconutsGameManager game, int eyeHeight, int crabCenterX) {
        super(game, crabCenterX, eyeHeight, WIDTH, laserImage);
    }

    @Override
    public boolean isHittable() {
        return false;  // not a hittable object?
    }

    @Override
    public boolean isGroundObject() {
        return false;
    }

    @Override
    public boolean isFalling() {
        return false;
    }

    @Override
    public boolean canHit(IslandObject other) {
        return other.isHittable() && !other.isGroundObject();
    }

    public int hittable_height() {
        return super.y; // top of image?
    }

    @Override
    public void step() {
        y -= 3;
    }
}
