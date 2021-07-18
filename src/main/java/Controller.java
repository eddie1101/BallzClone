import processing.core.PApplet;
import processing.core.PVector;

public class Controller extends PApplet {

    private static String path = "";

    GameStateMachine gsm;
    HighScoreHandler handler = new HighScoreHandler(path);

    RayCaster rayCaster;

    boolean cheatRay = false;

    public void settings() {
        size(392, 800);
    }

    public void setup() {
        frameRate(60);
        gsm = new GameStateMachine(this, handler);
        rayCaster = new RayCaster(this, gsm.grid());
    }

    public void draw() {
        gsm.loop();
        if(cheatRay) {
            PVector home = new PVector(gsm.runner().home(), BallRunner.Y);
            rayCaster.rayCast(home, new PVector(mouseX, mouseY), -1f);
        }
    }

    public void keyPressed() {
        if(key == '\n') {
            gsm.initialize();
            rayCaster = new RayCaster(this, gsm.grid());
        } else if(key == ' ') {
            cheatRay = !cheatRay;
        }
    }

    public void mouseClicked() {
        if(mouseButton == LEFT)
            gsm.click();
    }

    public static void main(String[] args) {

        String jar = Controller.class.getResource("Controller.class").toString().split(":")[0];

        if(jar.equals("jar")) {
            path = "high_score.txt";
        } else if(jar.equals("file")) {
            path = "src/main/resources/high_score.txt";
        }

        PApplet.main("Controller");
    }

}
