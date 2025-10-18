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
        return true;    // The base of the ground
    }

    @Override
    public boolean isFalling() {
        return false;
    }

    @Override
    public boolean canHit(IslandObject other) {
        return false;   // shouldn't hit Beach
    }

    /**
     * Defines the Y where this Object can be Hit.
     * Changes per Hittable Object. Coconut Bottom vs Laser top.
     *
     * @return int of Hittable Y value.
     */
    @Override
    protected int hittable_height() {
        return super.y;
    }

    @Override
    public void step() { /* do nothing */ }
}
