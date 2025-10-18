package coconuts;

public class ObjectRemover implements HitObserver {
    private final OhCoconutsGameManager gameManager;

    public ObjectRemover(OhCoconutsGameManager theGame) {
        this.gameManager = theGame;
    }

    @Override
    public void update(HitEvent e) {
        IslandObject a = e.islandObject1;
        IslandObject b = e.islandObject2;

        IslandObject coconut =
                (a.isHittable() && a.isFalling()) ? a :
                        (b.isHittable() && b.isFalling()) ? b : null;
        if (coconut == null) return;

        IslandObject other = (coconut == a) ? b : a;

        boolean isLaser = !other.isHittable() && !other.isGroundObject() && !other.isFalling();
        boolean isBeach = other.isGroundObject() && !other.isFalling();   // your Beach has no imageView, but that’s ok
        boolean isCrab  = other.isGroundObject() && other.isHittable();   // crab is ground + hittable

        if (isLaser) {
            double p = 0.75;
            if (Math.random() <= p) {
                gameManager.scheduleForDeletion(coconut);
                gameManager.coconutDestroyed();
            }
            gameManager.scheduleForDeletion(other);
        } else if (isBeach) {
            gameManager.scheduleForDeletion(coconut);
            gameManager.coconutDestroyed();
        } else if (isCrab) {
            gameManager.scheduleForDeletion(coconut);
            gameManager.coconutDestroyed();
        }
    }
}