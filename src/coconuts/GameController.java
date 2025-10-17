package coconuts;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

// JavaFX Controller class for the game - generally, JavaFX elements (other than Image) should be here
public class GameController {

    /**
     * Time between calls to step() (ms)
     */
    private static final double MILLISECONDS_PER_STEP = 1000.0 / 30;
    private Timeline coconutTimeline;
    private boolean started = false;

    @FXML private Label coconutsHitBeachLabel;
    @FXML private Label destroyedCoconutsLabel;
    @FXML private Pane gamePane;
    @FXML private Pane theBeach;

    private OhCoconutsGameManager theGame;

    @FXML
    public void initialize() {
        theGame = new OhCoconutsGameManager((int) (gamePane.getPrefHeight() - theBeach.getPrefHeight()),
                (int) (gamePane.getPrefWidth()), gamePane);

        gamePane.setFocusTraversable(true);

        // initialize Observer objects are passed in GameManager instances (just like Island Objects) to call back for changes
        HitObserver scoreboard = new Scoreboard(destroyedCoconutsLabel, coconutsHitBeachLabel);
        HitObserver gameStateManager = new GameStateManager(theGame);
        HitObserver objectRemover = new ObjectRemover(theGame);

        // Subject attaches Observers!
        theGame.attach(scoreboard);
        theGame.attach(gameStateManager);
        theGame.attach(objectRemover);

        coconutTimeline = new Timeline(new KeyFrame(Duration.millis(MILLISECONDS_PER_STEP), (e) -> {
            theGame.tryDropCoconut();
            theGame.advanceOneTick();
            if (theGame.done())
                coconutTimeline.pause();
        }));
        coconutTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.RIGHT && !theGame.done()) {
            theGame.getCrab().crawl(10);
        } else if (keyEvent.getCode() == KeyCode.LEFT && !theGame.done()) {
            theGame.getCrab().crawl(-10);
        } else if (keyEvent.getCode() == KeyCode.SPACE) {
            if (!started) {
                coconutTimeline.play();
                started = true;
            } else {
                coconutTimeline.pause();
                started = false;
            }
        } else if (keyEvent.getCode() == KeyCode.UP) {  // Laser functionality
            theGame.shootLaser();
        }
    }
}
