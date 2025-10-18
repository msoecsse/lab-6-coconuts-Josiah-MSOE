package coconuts;

public class GameStateManager implements HitObserver {
    private final OhCoconutsGameManager game;

    public GameStateManager(OhCoconutsGameManager theGame) {
        this.game = theGame;
    }

    @Override
    public void update(HitEvent e) {
        IslandObject a = e.islandObject1;
        IslandObject b = e.islandObject2;

        IslandObject coconut = (a.isHittable() && a.isFalling()) ? a
                : (b.isHittable() && b.isFalling()) ? b : null;
        if (coconut == null) return;

        IslandObject other = (coconut == a) ? b : a;


        boolean isCrab = other.isGroundObject() && other.getImageView() != null;
        if (isCrab) {
            game.killCrab();
            game.scheduleForDeletion(other);
        }
    }

    private boolean isCoconut(IslandObject o) {
        return o.isHittable() && o.isFalling();
    }
    private boolean isCrab(IslandObject o) {
        return o.isGroundObject() && o.isHittable();
    }
}