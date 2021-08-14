import processing.core.PApplet;
import processing.core.PVector;

public class GameStateMachine {

    private enum GameState {
        IDLE,
        AIMING,
        RUNNING,
        TRANSITION,
        END
    }

    private boolean newHighScore = false;
    private boolean click = false;
    private int level;
    private int highscore;

    private PApplet pApplet;
    private GameState state;
    private GameState nextState;

    private Grid grid;
    private Display display;
    private BallRunner runner;

    private HighScoreHandler hsh;

    public GameStateMachine(PApplet pApplet, HighScoreHandler hsh) {

        this.pApplet = pApplet;
        this.hsh = hsh;

        grid = new Grid(this.pApplet);
        display = new Display(this.pApplet, this.grid);
        runner = new BallRunner(this.pApplet);

    }

    public void initialize() {
        grid = new Grid(this.pApplet);
        display = new Display(this.pApplet, this.grid);
        runner = new BallRunner(this.pApplet);

        Ball startingBall = new Ball(pApplet);
        startingBall.setPos(runner.home(), BallRunner.Y);

        runner.addBall(startingBall);
        runner.flushBallBuffer();

        level = 1;

        grid.rowShift(level);

        state = GameState.AIMING;
        nextState = state;

        newHighScore = false;
        hsh.ensureHighScoreExists();
        highscore = hsh.getHighScore();
    }

    public BallRunner runner() {
        return runner;
    }

    public Grid grid() {
        return grid;
    }

    public Display display() {
        return display;
    }

    public PApplet PApplet() {
        return this.pApplet;
    }

    public void click() {
        if(!click) click = true;
    }

    public void loop() {

        display.background();

        if(state != GameState.END) {
            display.drawGrid();
            display.drawBoundaries();
            display.drawInfo(level, runner.numBalls());
            pApplet.noCursor();
        }

        switch (state) {
            case IDLE:
                display.resetInsult();
                nextState = GameState.AIMING;
                break;
            case AIMING:
                PVector firingVelocity = new PVector(pApplet.mouseX - runner.home(), pApplet.mouseY - BallRunner.Y);
                float m = Math.abs(firingVelocity.y / firingVelocity.x);
                if(m >= 0.25 && pApplet.mouseY < BallRunner.Y) {
                    display.handleTargeting(runner, true);
                } else {
                    display.handleTargeting(runner, false);
                }
                display.drawBalls(runner.getBalls());

                if (click) {
                    if(runner.initialize()) {
                        nextState = GameState.RUNNING;
                    }
                }

                break;
            case RUNNING:

                runner.update(grid.getPhysicalCells());
                display.drawBalls(runner.getBalls());

                if(runner.isIdle()) {
                    nextState = GameState.TRANSITION;
                }

                break;
            case TRANSITION:
                display.transition();
                if(grid.rowShift(level++)) {
                    nextState = GameState.END;
                } else {
                    nextState = GameState.AIMING;
                }

                break;
            case END:

                if(highscore < level) {
                    hsh.putHighScore(level);
                    highscore = hsh.getHighScore();
                    newHighScore = true;
                }

                display.drawInsult();
                display.drawScores(level, highscore, newHighScore);
                if(click) {
                    initialize();                }
                break;
            default:
                break;
        }

        if(state != nextState) {
            state = nextState;
        }

        click = false;

    }

}
