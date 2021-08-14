import processing.core.PApplet;
import processing.core.PConstants;

public class BallUpgrade extends Griddable {

    public static final int INNER_R = 20;
    public static final int OUTER_R = 30;

    private boolean physical = true;

    public BallUpgrade(PApplet pApplet, Grid parent) {
        super(pApplet, parent);
    }

    public void disappear() {
        this.physical = false;
    }

    public boolean physical() {
        return this.physical;
    }

    public void draw() {

        if(this.physical) {
            pApplet.ellipseMode(PConstants.CENTER);

            pApplet.fill(0);
            pApplet.stroke(255);
            pApplet.ellipse(pos.x, pos.y, OUTER_R, OUTER_R);
            pApplet.fill(255);
            pApplet.ellipse(pos.x, pos.y, INNER_R, INNER_R);
        }

    }

}
