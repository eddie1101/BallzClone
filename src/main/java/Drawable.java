import processing.core.PApplet;

public abstract class Drawable {

    protected PApplet pApplet;

    public Drawable(PApplet pApplet) {
        this.pApplet = pApplet;
    }

    public abstract void draw();

}
