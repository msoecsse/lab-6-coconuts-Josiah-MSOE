package coconuts;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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


        destroyedCoconutsLabel.setText("0");
        coconutsHitBeachLabel.setText("0");

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
            if (theGame.done()) {
                coconutTimeline.stop();
                started = false;

                javafx.application.Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Game Over");
                    alert.setHeaderText(null);
                    alert.setContentText("🏝️ Game Over!\nYour crab has been crushed.");
                    alert.showAndWait();
                });
            }
        }));
        coconutTimeline.setCycleCount(Timeline.INDEFINITE);

    }

    @FXML
    public void onKeyPressed(KeyEvent keyEvent) {
        if (theGame.done()) return;
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


    public void update(HitEvent e) {
        IslandObject a = e.islandObject1;
        IslandObject b = e.islandObject2;

        // the coconut is the falling & hittable one
        IslandObject coconut = (a.isHittable() && a.isFalling()) ? a
                : (b.isHittable() && b.isFalling()) ? b : null;
        if (coconut == null) return;

        IslandObject other = (coconut == a) ? b : a;

        // Laser vs Coconut -> "Destroyed"
        boolean laserVsCoconut = !other.isHittable() && !other.isGroundObject();
        if (laserVsCoconut) {
            javafx.application.Platform.runLater(() -> {
                int cur = Integer.parseInt(destroyedCoconutsLabel.getText());
                destroyedCoconutsLabel.setText(String.valueOf(cur + 1));
            });
            // keep in-flight count correct
            try { coconut.getClass(); /* nop */ } finally { /* safe call */ }
            // call manager via label owner? teammate kept it simple:
            // if you prefer, add a setter in scoreboard; for now let GameStateManager handle counts on crab hit.
            return;
        }

        // Coconut vs Beach -> "Hit Beach"
        boolean isBeach = other.isGroundObject() && other.getImageView() == null;
        if (isBeach) {
            javafx.application.Platform.runLater(() -> {
                int cur = Integer.parseInt(coconutsHitBeachLabel.getText());
                coconutsHitBeachLabel.setText(String.valueOf(cur + 1));
            });

        }
    }
}
