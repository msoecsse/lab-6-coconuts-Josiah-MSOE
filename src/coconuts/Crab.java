package coconuts;

import javafx.scene.image.Image;

// Represents the object that shoots down coconuts but can be hit by coconuts. Killing the
//   crab ends the game
// This is a domain class; other than Image, do not introduce JavaFX or other GUI components here
public class Crab extends HittableIslandObject {
    private static final int WIDTH = 50; // assumption: height and width are the same
    private static final Image crabImage = new Image("file:images/crab-1.png");

    public Crab(OhCoconutsGameManager game, int skyHeight, int islandWidth) {
        super(game, islandWidth / 2, skyHeight, WIDTH, crabImage);
    }

    @Override
    public boolean isGroundObject() {
        return true;
    }

    @Override
    public boolean isFalling() {
        return false;
    }

    @Override
    public boolean canHit(IslandObject other) {
        return other.isHittable() && !other.isGroundObject();
    }

    /**
     * Defines the Y where this Object can be Hit.
     * Changes per Hittable Object. Coconut Bottom vs Laser top.
     *
     * @return int of Hittable Y value.
     */
    @Override
    protected int hittable_height() {
        return y;
    }

    @Override
    public void step() {
        // do nothing
    }

    public void crawl(int offset) {
        x += offset;
        int maxX = containingGame.getWidth() - this.width;
        if (x < 0) x = 0;
        if (x > maxX) x = maxX;
        display();
    }
}
