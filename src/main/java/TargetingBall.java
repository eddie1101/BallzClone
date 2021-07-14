import processing.core.PApplet;
import processing.core.PVector;

public class TargetingBall extends Ball {

    public static final float SPEED = 1.4f;

    BallRunner runner;
    PVector rpos;

    public TargetingBall(PApplet pApplet, BallRunner runner) {
        super(pApplet);
        rpos = GameHelper.toPolar(pos);
        this.runner = runner;
    }

    public void lockPolarAxis() {
        PVector mouse = new PVector(pApplet.mouseX, pApplet.mouseY);
        PVector polarMouse = GameHelper.toPolar(runner.shiftOrigin(mouse));
        rpos.y = polarMouse.y;
        this.pos = runner.returnOrigin(GameHelper.fromPolar(rpos));
    }

    @Override
    public void update() {
        super.update();
        this.rpos.x += SPEED;
        lockPolarAxis();
    }

}
