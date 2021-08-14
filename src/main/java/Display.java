import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.Random;

import static processing.core.PConstants.CENTER;

public class Display {

    private static final int COUNTER_MAX = 30;

    private static final String[] INSULTS = {
            "Man, you really\nsuck at this",
            "kys nerd",
            "Git Gud scrub",
            "This is\nembarrassing, I'm\nembarrassed for you",
//            "Baka!",
//            "全体的に死ぬ、ばか",
            "Just bounce the\nfuckin' balls, it's not\nthat fucking hard",
            "Bella is a ho",
            "You are a useless\ncrayon",
            "You are infertile",
            "You mayo cricket",
            "Your mom lost the\n50/50 when she\nhad you",
            "You have holes\nin your brain",
            "You were a one\nnight stand",
            "You peaked in\nmiddle school",
            "You're a fucking\nloser, how can\nyou suck so much\nat this easy ass\ngame you dumb\nfucking bitch",
            "Hillbilly",
            "8====D",
            "You must be a\ndiscord moderator",
            "You look like\nSquidward's left\ntentacle",
            "You are as use-\nless as a fart\nin the wind"
    };

    private String insult = null;

    private PApplet pApplet;
    private Grid grid;

    private int targetingCounter = 0;
    private ArrayList<Ball> targetingBalls = new ArrayList<Ball>();
    private Ball expiredBall = null;

    public Display(PApplet pApplet, Grid grid) {
        this.pApplet = pApplet;
        this.grid = grid;
    }

    public void resetInsult() {
        insult = null;
    }

    public void background() {
        pApplet.background(0);
    }

    public void drawGrid() {
        grid.draw();
    }

    public void drawBalls(Ball[] balls) {
        for(Ball ball: balls) {
            ball.draw();
        }
    }

    private void createTargetingBall(PVector origin, PVector mouse, BallRunner runner) {
        Ball ball = new TargetingBall(pApplet, runner);
        ball.setPos(origin.x, origin.y);

        ball.expireIn(90);
        targetingBalls.add(ball);
    }

    public void handleTargeting(BallRunner runner, boolean draw) {

        targetingCounter--;

        PVector origin = new PVector(runner.home(), BallRunner.Y);
        PVector mouse = new PVector(pApplet.mouseX, pApplet.mouseY);

        if(targetingCounter <= 0) {
            createTargetingBall(origin, mouse, runner);
            targetingCounter = COUNTER_MAX;
        }

        for(Ball ball: targetingBalls) {
            ball.update();
            if(ball.getExpireTime() <= 0) {
                expiredBall = ball;
            }
        }

        if(expiredBall != null) {
            targetingBalls.remove(expiredBall);
            expiredBall = null;
        }

        Ball[] balls = new Ball[targetingBalls.size()];

        if(draw)
            drawBalls(targetingBalls.toArray(balls));

    }

    public void drawBoundaries() {
        pApplet.stroke(255);
        pApplet.line(0, BallRunner.Y, pApplet.width, BallRunner.Y);
        pApplet.stroke(255, 0, 0);

        float gridBoundary = (Grid.CELL_WIDTH * Grid.ROWS) + (Grid.ROWS * 2) + 1;

        pApplet.line(0, gridBoundary, pApplet.width, gridBoundary);
    }

    public void drawInfo(int level, int numBalls) {
        pApplet.stroke(255);
        pApplet.fill(255);
        pApplet.textAlign(PConstants.LEFT);
        pApplet.textSize(12);
        pApplet.text("Level: " + level, 12, pApplet.height - 16);
        pApplet.text("Balls: " + numBalls, 100, pApplet.height - 16);
        pApplet.text("(Enter to restart)", 250, pApplet.height - 16);
    }

    public void drawInsult() {

        if(insult == null) {
            Random random = new Random();
            int rand = random.nextInt(INSULTS.length);
            insult = INSULTS[rand];
        }

        pApplet.textAlign(CENTER, CENTER);
        pApplet.textLeading(20);
        pApplet.textSize(40);

        pApplet.fill(255);
        pApplet.stroke(255);

        if(insult.contains("discord moderator")) {
            String[] contents = insult.split("\n");
            pApplet.text(contents[0], pApplet.width / 2, (pApplet.height / 2) - 60);
            pApplet.stroke(255, 0 , 0);
            pApplet.fill(255, 0 ,0);
            pApplet.text(contents[1], pApplet.width / 2, (pApplet.height / 2));
        } else {

            pApplet.text(insult, pApplet.width / 2, (pApplet.height / 2) - 60);

            pApplet.textSize(12);
            pApplet.text("(Click to play again)", pApplet.width / 2, pApplet.height - 16);
        }

    }

    public void drawScores(int score, int highscore, boolean newHighScore) {

        pApplet.fill(255);
        pApplet.stroke(255);

        pApplet.textSize(20);
        pApplet.textAlign(CENTER);
        pApplet.textLeading(10);
        pApplet.text("Score: " + score, pApplet.width / 2, 100);

        StringBuilder highscoreBuilder = new StringBuilder();
        highscoreBuilder.append("Highscore: ").append(highscore);
        if(newHighScore) highscoreBuilder.append(" (new!)");
        String sHighscore = highscoreBuilder.toString();

        pApplet.text(sHighscore, pApplet.width / 2, 130);

    }



    //TODO
    public void transition() {}

    public void drawDebugBall(Ball ball) {
        pApplet.stroke(255, 0, 0);

        PVector pos = ball.getPos();

        pApplet.line(pos.x, 0, pos.x, pApplet.height);
        pApplet.line(0, pos.y, pApplet.width, pos.y);
        ball.draw();
    }

}
