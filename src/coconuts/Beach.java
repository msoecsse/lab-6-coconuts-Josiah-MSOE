package coconuts;

public class Beach extends HittableIslandObject {

    public Beach(OhCoconutsGameManager game, int skyHeight, int islandWidth) {
        super(game, 0, skyHeight, islandWidth, null);
    }

    @Override
    public boolean isHittable() {
        return true;
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
        return false;
    }

    @Override
    protected int hittable_height() {
        return super.y;
    }

    @Override
    public void step() { /* do nothing */ }
}
