import processing.core.PApplet;
import processing.core.PVector;

public class Controller extends PApplet {

    private static String path = "";

    GameStateMachine gsm;
    HighScoreHandler handler = new HighScoreHandler(path);

    public void settings() {
        size(392, 800);
    }

    public void setup() {
        frameRate(60);
        gsm = new GameStateMachine(this, handler);
        gsm.initialize();
    }

    public void draw() {
        gsm.loop();
    }

    public void keyPressed() {
        if(key == '\n') {
            gsm.initialize();
        } else if(key == ' ' ) {
//            gsm.grid().rowShift(100);
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
