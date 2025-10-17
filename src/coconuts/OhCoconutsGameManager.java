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
        LaserBeam laserBeam = new LaserBeam(this, height - 10, theCrab.x);
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
        // see if objects hit; the hit itself is something you will add
        // you can't change the lists while processing them, so collect
        //   items to be removed in the first pass and remove them later
        scheduledForRemoval.clear();
        for (IslandObject thisObj : allObjects) {
            for (HittableIslandObject hittableObject : hittableIslandSubjects) {
                if (thisObj.canHit(hittableObject) && thisObj.isTouching(hittableObject)) {
                    // process the HIT EVENT for notifyAll()
                    notifyAll(new HitEvent(thisObj, hittableObject));
                    /* I moved this for you Shritej: This responsibility is moved to ObjectRemover Observer for both Laser and Coconut to be wiped
                    scheduledForRemoval.add(hittableObject);
                    gamePane.getChildren().remove(hittableObject.getImageView());

                    gamePane.getChildren().remove(hittableObject.getImageView());
                    gamePane.getChildren().remove(thisObj.getImageView());
                     */
                }
            }
        }

        // actually remove the objects as needed
        for (IslandObject thisObj : scheduledForRemoval) {
            allObjects.remove(thisObj);
            if (thisObj.isHittable()) {
                hittableIslandSubjects.remove((HittableIslandObject) thisObj);
            }
        }
        scheduledForRemoval.clear();
    }

    // Called from Object Remover Observer after notified of HitEvents!
    public void scheduleForDeletion(IslandObject islandObject) {
        gamePane.getChildren().remove(islandObject.getImageView()); // added here to "hide" each object
        scheduledForRemoval.add(islandObject);
    }

    public boolean done() {
        return coconutsInFlight == 0 && gameTick >= MAX_TIME;
    }


    // Subject classes below
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
