/**
 * Author: Shritej
 * Description:
 *  This class controls the main game logic for the OhCoconuts game.
 *  It handles user input, connects JavaFX UI elements with the game engine,
 *  updates scores, manages the animation timeline, and displays the game-over message.
 */

package coconuts;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class GameController {

    private static final double MILLISECONDS_PER_STEP = 1000.0 / 30;
    private Timeline coconutTimeline;
    private boolean started = false;

    @FXML private Label coconutsHitBeachLabel;
    @FXML private Label destroyedCoconutsLabel;
    @FXML private Pane gamePane;
    @FXML private Pane theBeach;
    @FXML public Label gameOverLabel;

    private OhCoconutsGameManager theGame;

    /**
     * Initializes the game when the FXML file loads.
     * Sets up the game manager, observers, timeline, and UI labels.
     * Also ensures the game starts in a paused state until SPACE is pressed.
     */
    @FXML
    public void initialize() {
        gameOverLabel.setVisible(false);
        theGame = new OhCoconutsGameManager((int) (gamePane.getPrefHeight() - theBeach.getPrefHeight()),
                (int) (gamePane.getPrefWidth()), gamePane);

        gamePane.setFocusTraversable(true);

        destroyedCoconutsLabel.setText("0");
        coconutsHitBeachLabel.setText("0");

        HitObserver scoreboard = new Scoreboard(destroyedCoconutsLabel, coconutsHitBeachLabel);
        HitObserver gameStateManager = new GameStateManager(theGame, gameOverLabel);
        HitObserver objectRemover = new ObjectRemover(theGame);
        HitObserver effectManager = new CollisionEffectManager(gamePane);
        theGame.attach(effectManager);
        theGame.attach(scoreboard);
        theGame.attach(gameStateManager);
        theGame.attach(objectRemover);

        coconutTimeline = new Timeline(new KeyFrame(Duration.millis(MILLISECONDS_PER_STEP), (e) -> {
            theGame.tryDropCoconut();
            theGame.advanceOneTick();

            if (theGame.done()) {
                coconutTimeline.stop();
                started = false;
                gameOverLabel.setVisible(true);
            }
        }));
        coconutTimeline.setCycleCount(Timeline.INDEFINITE);
    }

    /**
     * Handles all key presses during gameplay.
     * - LEFT/RIGHT moves the crab.
     * - SPACE pauses or resumes the game.
     * - UP makes the crab shoot a laser.
     */
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
        } else if (keyEvent.getCode() == KeyCode.UP) {
            theGame.shootLaser();
        }
    }

    /**
     * Updates the score labels when certain hit events occur.
     * Called when a laser destroys a coconut or when a coconut hits the beach.
     */
    public void update(HitEvent e) {
        IslandObject a = e.islandObject1;
        IslandObject b = e.islandObject2;

        IslandObject coconut = (a.isHittable() && a.isFalling()) ? a
                : (b.isHittable() && b.isFalling()) ? b : null;
        if (coconut == null) return;

        IslandObject other = (coconut == a) ? b : a;

        boolean laserVsCoconut = !other.isHittable() && !other.isGroundObject();
        if (laserVsCoconut) {
            javafx.application.Platform.runLater(() -> {
                int cur = Integer.parseInt(destroyedCoconutsLabel.getText());
                destroyedCoconutsLabel.setText(String.valueOf(cur + 1));
            });
            try { coconut.getClass(); /* nop */ } finally { /* safe call */ }
            return;
        }

        boolean isBeach = other.isGroundObject() && other.getImageView() == null;
        if (isBeach) {
            javafx.application.Platform.runLater(() -> {
                int cur = Integer.parseInt(coconutsHitBeachLabel.getText());
                coconutsHitBeachLabel.setText(String.valueOf(cur + 1));
            });
        }
    }
}