// Editor: JOSIAH MATHEWS
package coconuts;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

// an object in the game, either something coming from the island or falling on it
// Each island object has a location and can determine if it hits another island object
// This is a domain class; do not introduce JavaFX or other GUI components here
public abstract class IslandObject {
    protected final int width;
    protected final OhCoconutsGameManager containingGame;
    protected int x, y;
    ImageView imageView = null;

    public IslandObject(OhCoconutsGameManager game, int x, int y, int width, Image image) {
        containingGame = game;
        if (image != null) {
            imageView = new ImageView(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(width);
        }
        this.x = x;
        this.y = y;
        this.width = width;
        display();
        //System.out.println(this + " left " + left() + " right " + right());
    }

    protected ImageView getImageView() {
        return imageView;
    }

    public void display() {
        if (imageView != null) {
            imageView.setLayoutX(x);
            imageView.setLayoutY(y);
        }
    }
/*
    public boolean isHittable() {
        return false;
    }

    protected int hittable_height() {
        return 0;
    }

    public boolean isGroundObject() {
        return false;
    }

    public boolean isFalling() {
        return false;
    }

    public boolean canHit(IslandObject other) {
        return false;
    }

    public boolean isTouching(IslandObject other) {
        return false;
    }

    public abstract void step();
*/

    // Changing to Abstract is more common sense here
    public abstract boolean isHittable();


    public abstract boolean isGroundObject();

    public abstract boolean isFalling();

    public abstract boolean canHit(IslandObject other);

    /**
     * Defines the Y where this Object can be Hit.
     * Changes per Hittable Object. Coconut Bottom vs Laser top.
     * @return int of Hittable Y value.
     */
    protected abstract int hittable_height();

    public boolean isTouching(IslandObject other) {
        boolean yTouching = Math.abs(this.hittable_height() - other.hittable_height()) < 5;

        if (yTouching) {
            // Check X centers
            int thisCenter = this.x + (this.width / 2);
            int otherCenter = other.x + (other.width / 2);

            int buffer = 10;
            boolean thisInBetweenOther = (thisCenter >= other.x + buffer && thisCenter <= other.x + other.width + buffer);
            boolean otherInBetweenThis = (otherCenter >= this.x + buffer && otherCenter <= this.x + this.width + buffer);

            return thisInBetweenOther || otherInBetweenThis;
        }
        return false;
    }

    public abstract void step();

}
