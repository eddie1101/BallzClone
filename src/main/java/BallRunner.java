import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class BallRunner {

    private static final int COUNTER_MAX = 8;
    public static final int Y = 750;

    private PApplet pApplet;

    private int numBalls = 0;
    private int counter = 0;

    private int home;

    private ArrayList<Ball> balls = new ArrayList<Ball>();
    private ArrayList<Ball> ballBuffer = new ArrayList<Ball>();
    private ArrayList<Ball> idleBalls = new ArrayList<Ball>();

    private Stack<Ball> firingQueue = new Stack<Ball>();

    private PVector firingVelocity = new PVector();

    public BallRunner(PApplet pApplet) {
        this.pApplet = pApplet;
        home = this.pApplet.width / 2;
    }

    private boolean calculateFiringVelocity() {
        this.firingVelocity = new PVector(pApplet.mouseX - home, pApplet.mouseY - Y);
//        if(firingVelocity.y / firingVelocity.x < 0.25) {
//            return false;
//        }
        firingVelocity.normalize().mult(4);
        return true;
    }

    private void fireBall(Ball ball) {
        ball.setVel(firingVelocity.x, firingVelocity.y);
    }

    private void bounce(Ball ball, Box box) {

        PVector vel = ball.getVel();

        int side = GameHelper.findBoxCollisionSide(ball, box);

        if(side == 0 || side == 2) {
            ball.setVel(vel.x, -vel.y);
        } else if(side == 1 || side == 3) {
            ball.setVel(-vel.x, vel.y);
        }

    }

    public int home() {
        return this.home;
    }

    public int numBalls() {
        return this.numBalls;
    }

    public void setHome(int x) {
        this.home = x;
    }

    public Ball[] getBalls() {

        Ball[] retval = new Ball[balls.size()];

        return balls.toArray(retval);
    }

    public PVector getFiringVelocity() {
        return this.firingVelocity;
    }

    public void addBall(Ball ball) {
        ballBuffer.add(ball);
        this.numBalls++;
    }

    public void addBall() {

        Ball ball = new Ball(this.pApplet);

        ball.setPos(this.home, Y);
        ball.setVel(0, 0);

        ballBuffer.add(ball);
        this.numBalls++;

    }

    public void flushBallBuffer() {
        this.balls.addAll(this.ballBuffer);
        this.ballBuffer.clear();
    }

    public PVector shiftOrigin(PVector pos) {
        return new PVector(pos.x - this.home, pos.y - Y);
    }

    public PVector returnOrigin(PVector pos) {
        return new PVector(pos.x + this.home, pos.y + Y);
    }

    public boolean isIdle() {
        for(Ball ball: balls) {
            if(!idleBalls.contains(ball)) {
                return false;
            } else {
                if(ball.isTravelling()) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean initialize() {
        counter = COUNTER_MAX;
        flushBallBuffer();
        firingQueue.addAll(balls);
        idleBalls.clear();

        for(Ball ball: balls) {
            ball.setPos(home, Y);
        }

        return calculateFiringVelocity();
    }

    public void update(Griddable[] physicals) {

        counter++;

        if(counter >= COUNTER_MAX) {
            counter = 0;
            if(!firingQueue.empty())
                fireBall(firingQueue.pop());
        }

        for(Ball ball: balls) {
            ball.update();

            if(ball.getPos().y > Y + 10) {
                if(idleBalls.isEmpty()) {
                    home = (int) ball.getPos().x;
                }

                if(!ball.isTravelling())
                    ball.travelTo(new PVector(home, Y), 0.05f);
                if(!idleBalls.contains(ball))
                    idleBalls.add(ball);
            } else {

                Griddable[] testCollisions = GameHelper.getPhysicalsInCollisionRange(ball, physicals, 5);

                for (Griddable griddable : testCollisions) {

                    if (griddable instanceof Box) {

                        Box box = (Box) griddable;

                        if (GameHelper.boxCollision(ball, box)) {
                            box.decrement();
                            bounce(ball, box);
                        }

                    } else if (griddable instanceof BallUpgrade) {

                        BallUpgrade ballUpgrade = (BallUpgrade) griddable;

                        if (GameHelper.ballUpgradeCollision(ball, ballUpgrade) && ballUpgrade.physical()) {

                            Ball newBall = new Ball(pApplet);
                            newBall.setPos(home, Y);

                            addBall(newBall);
                            ballUpgrade.disappear();
                        }

                    }
                }

                GameHelper.wallCollision(ball);

            }
        }
    }
}
