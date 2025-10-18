// Editor: Josiah Mathews
package coconuts;

import javafx.scene.layout.Pane;

import java.util.Collection;
import java.util.LinkedList;

// This class manages the game, including tracking all island objects and detecting when they hit
// Becomes the Hit-Subject which all the Observers are following.
public class OhCoconutsGameManager implements HitSubject {
    private final Collection<IslandObject> allObjects = new LinkedList<>();
    private final Collection<HittableIslandObject> hittableIslandSubjects = new LinkedList<>();
    private final Collection<IslandObject> scheduledForRemoval = new LinkedList<>();
    private final Collection<HitObserver> observers = new LinkedList<>();
    private final int height, width;
    private final int DROP_INTERVAL = 40;
    private final int MAX_TIME = 100;

    // FXML Components
    private Pane gamePane;
    private Crab theCrab;
    private Beach theBeach;
    /* game play */
    private int coconutsInFlight = 0;
    private int gameTick = 0;

    public OhCoconutsGameManager(int height, int width, Pane gamePane) {
        this.height = height;
        this.width = width;
        this.gamePane = gamePane;

        this.theCrab = new Crab(this, height, width);
        registerObject(theCrab);
        gamePane.getChildren().add(theCrab.getImageView());

        this.theBeach = new Beach(this, height, width);
        registerObject(theBeach);
        if (theBeach.getImageView() != null)
            System.out.println("Unexpected image view for beach");
    }

    private void registerObject(IslandObject object) {
        allObjects.add(object);
        if (object.isHittable()) {
            HittableIslandObject asHittable = (HittableIslandObject) object;
            hittableIslandSubjects.add(asHittable);
        }
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public void coconutDestroyed() {
        coconutsInFlight -= 1;
    }

    public void tryDropCoconut() {
        if (gameTick % DROP_INTERVAL == 0 && theCrab != null) {
            coconutsInFlight += 1;
            final int buffer = 50;
            Coconut c = new Coconut(this, (int) (Math.random() * (width - buffer))); // added buffer so no more edge coconuts
            registerObject(c);
            gamePane.getChildren().add(c.getImageView());
        }
        gameTick++;
    }

    public Crab getCrab() {
        return theCrab;
    }

    public void shootLaser() {
        int crabCenterX = theCrab.x + (theCrab.width / 2);
        LaserBeam laserBeam = new LaserBeam(this, height - 10, crabCenterX);
        registerObject(laserBeam);
        gamePane.getChildren().add(laserBeam.getImageView());
    }

    public void killCrab() {
        theCrab = null;
    } // called from GameStateManager?



    public void advanceOneTick() {
        for (IslandObject o : allObjects) {
            o.step();
            o.display();
        }

        java.util.Set<String> handledPairs = new java.util.HashSet<>();
        scheduledForRemoval.clear();

        for (IslandObject actor : allObjects) {
            for (HittableIslandObject target : hittableIslandSubjects) {
                if (actor == target) continue;

                if (actor.canHit(target) && actor.isTouching(target)) {
                    int ha = System.identityHashCode(actor);
                    int hb = System.identityHashCode(target);
                    String key = (ha < hb) ? (ha + ":" + hb) : (hb + ":" + ha);

                    if (handledPairs.add(key)) {
                        notifyAll(new HitEvent(actor, target));
                    }
                }
            }
        }

        for (IslandObject obj : new java.util.ArrayList<>(scheduledForRemoval)) {
            allObjects.remove(obj);
            if (obj.isHittable()) {
                hittableIslandSubjects.remove((HittableIslandObject) obj);
            }
        }
        scheduledForRemoval.clear();
    }

    public void scheduleForDeletion(IslandObject obj) {
        if (obj.isGroundObject() && !obj.isHittable()) {
            return;
        }

        gamePane.getChildren().remove(obj.getImageView());

        if (obj.isHittable() && obj.isFalling()) {
            coconutsInFlight = Math.max(0, coconutsInFlight - 1);
        }

        scheduledForRemoval.add(obj);
    }

    public boolean done() {
        return (theCrab == null && coconutsInFlight == 0);

    }



    @Override
    public void attach(HitObserver hitObserver) {
        this.observers.add(hitObserver);
    }

    @Override
    public void detach(HitObserver hitObserver) {
        this.observers.remove(hitObserver);
    }

    @Override
    public void notifyAll(HitEvent hitEvent) {
        System.out.println("NOTIFYING!");
        for (HitObserver observer : observers) {
            observer.update(hitEvent);
        }
    }

}
