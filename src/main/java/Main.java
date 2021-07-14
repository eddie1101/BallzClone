import processing.core.PApplet;

public class Main extends PApplet {

    public static final PApplet controller = new Controller();

    public static void main(String[] args) {
        PApplet.runSketch(new String[] {"Controller"}, controller);
    }

}
