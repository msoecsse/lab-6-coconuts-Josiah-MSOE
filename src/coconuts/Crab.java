package coconuts;

import javafx.scene.image.Image;

public class Crab extends HittableIslandObject {
    private static final int WIDTH = 50;
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

    @Override
    protected int hittable_height() {
        return y;
    }

    @Override
    public void step() {
    }

    public void crawl(int offset) {
        x += offset;
        int maxX = containingGame.getWidth() - this.width;
        if (x < 0) x = 0;
        if (x > maxX) x = maxX;
        display();
    }
}
