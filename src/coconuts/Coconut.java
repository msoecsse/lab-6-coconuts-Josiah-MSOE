package coconuts;

import javafx.scene.image.Image;

public class Coconut extends HittableIslandObject {
    private static final int WIDTH = 50;
    private static final Image coconutImage = new Image("file:images/coco-1.png");

    public Coconut(OhCoconutsGameManager game, int x) {
        super(game, x, 0, WIDTH, coconutImage);
    }

    @Override
    public boolean isGroundObject() {
        return false;
    }

    @Override
    public boolean isFalling() {
        return true;
    }

    @Override
    public boolean canHit(IslandObject other) {
        return other.isGroundObject(); // can hit Ground Objects like Beach or Crab
    }

    /**
     * Defines the Y where this Object can be Hit.
     * Changes per Hittable Object. Coconut Bottom vs Laser top.
     *
     * @return int of Hittable Y value.
     */
    @Override
    protected int hittable_height() {
        return super.y + width;
    }

    @Override
    public void step() {
        y += 5;
    }
}
