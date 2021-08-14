import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class DebugController extends PApplet {

    private static String path = "";

    GameStateMachine gsm;
    HighScoreHandler handler = new HighScoreHandler(path);

    RayCaster rayCaster;

    boolean debugRay = false;
    boolean debug = false;

    ArrayList<Ball> debugBalls;
    Box debugBox;
    PVector debugTarget;

    public void settings() {
        size(392, 800);
    }

    public void setup() {
        frameRate(60);
        gsm = new GameStateMachine(this, handler);
        rayCaster = new RayCaster(this, gsm.grid());

        for(int i = 0; i < 4; i++) {
            gsm.runner().addBall();
        }

        debugBox = new Box(this, gsm.grid(), 9999);

        Griddable[] debugRow = new Griddable[] {
                new Box(this, gsm.grid(), 0),
                new Box(this, gsm.grid(), 0),
                new Box(this, gsm.grid(), 0),
                debugBox,
                new Box(this, gsm.grid(), 0),
                new Box(this, gsm.grid(), 0),
                new Box(this, gsm.grid(), 0)
        };

        debugBalls = new ArrayList<Ball>();
        gsm.grid().insertRow(debugRow, 5);
        debugTarget = new PVector(debugBox.pos.x, debugBox.pos.y);

    }

    public void draw() {
//        gsm.loop();
//        if(debugRay) {
//            PVector home = new PVector(gsm.runner().home(), BallRunner.Y);
//            rayCaster.rayCast(home, new PVector(mouseX, mouseY), -1f);
//        }

        background(0);
        gsm.display().drawGrid();

        stroke(0, 0, 255);
        line(mouseX, mouseY, debugTarget.x, debugTarget.y);


        for(Ball ball: debugBalls) {
            gsm.display().drawDebugBall(ball);
            ball.update();
            for(Griddable griddable: GameHelper.getPhysicalsInCollisionRange(ball, gsm.grid().getPhysicalCells(), (int) Ball.R)) {
                if(griddable instanceof Box) {
                    Box box = (Box) griddable;
                    boolean collision = GameHelper.boxCollision(ball, box);

                    if(collision && debug) {
                        System.out.println();
                        GameHelper.boxCollision(ball, box);
                    }

                    if (collision && !ball.isCollidingWith(box)) {
                        ball.addColliding(box);
                        box.decrement();
                        bounce(ball, box);
                    } else if (!collision && ball.isCollidingWith(box)) {
                        ball.removeColliding(box);
                    }
                }
            }
        }

        trimBalls();

    }

    public void keyPressed() {
        if(key == '\n') {
            gsm.initialize();
            rayCaster = new RayCaster(this, gsm.grid());
        } else if(key == ' ') {
            debugRay = !debugRay;
        } else if(key == CODED && keyCode == BACKSPACE) {
            gsm.grid().rowShift(10);
        } else if(key == '1') {
            gsm.grid().rowShift(100);
        } else if(key == 's') {
            debugTarget.y++;
        } else if(key == 'w') {
            debugTarget.y--;
        } else if(key == 'a') {
            debugTarget.x--;
        } else if(key == 'd') {
            debugTarget.x++;
        } else if(key == '2') {
            debug = !debug;
        }
    }

    public void mouseClicked() {
        if(mouseButton == LEFT) {
            gsm.click();
        } else if(mouseButton == RIGHT) {
            Ball ball = new Ball(this).setPos(mouseX, mouseY);
            PVector vel = calculateFiringVelocity(ball);
            debugBalls.add(ball.setVel(vel.x, vel.y));
        }
    }

    void bounce(Ball ball, Box box) {

        PVector vel = ball.getVel();

        int side = GameHelper.findBoxCollisionSide(ball, box);

        if(side == 0 || side == 2) {
            ball.setVel(vel.x, -vel.y);
        } else if(side == 1 || side == 3) {
            ball.setVel(-vel.x, vel.y);
        }

    }

    PVector calculateFiringVelocity(Ball ball) {
        PVector firingVelocity = new PVector(debugTarget.x - ball.pos.x, debugTarget.y - ball.pos.y);
        return firingVelocity.normalize().mult(4);
    }

    void trimBalls() {
        for(int i = debugBalls.size() - 1; i >= 0; i--) {
            Ball ball = debugBalls.get(i);
            if(ball.pos.x > 392 || ball.pos.x < 0 || ball.pos.y > 800 || ball.pos.y < 0) {
                debugBalls.remove(i);
            }
        }
    }

    public static void main(String[] args) {
        String jar = Controller.class.getResource("Controller.class").toString().split(":")[0];

        if(jar.equals("jar")) {
            System.err.println("The debug class can only be run in a development environment!");
            System.exit(0);
        }

        path = "src/main/resources/high_score.txt";

        PApplet.main("DebugController");
    }

}
