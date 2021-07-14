import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import static processing.core.PConstants.CENTER;

public class Box extends Griddable{

    private int count;
    private int rgb[] = {255 - count, count, 255 - count};

    public Box(PApplet pApplet, Grid grid, int count) {
        super(pApplet, grid);
        this.count = count;
    }

    public int count() {
        return this.count;
    }

    public void setPos(float x, float y) {
        this.pos.x = x;
        this.pos.y = y;
    }

    public void decrement() {
        this.count--;
    }

    private void updateColor() {

        rgb[0] = 255 - (count * 25);
        rgb[1] = count * 25;
        rgb[2] = 128 - (count * 10);

    }

    public void draw() {

        if(count > 0) {

            updateColor();

            pApplet.stroke(0);
            pApplet.fill(rgb[0], rgb[1], rgb[2]);
            pApplet.rectMode(CENTER);
            pApplet.rect(pos.x, pos.y, Grid.CELL_WIDTH, Grid.CELL_WIDTH);

            pApplet.textAlign(CENTER);
            pApplet.textSize(20);
            pApplet.fill(0);
            pApplet.text(count, pos.x, pos.y + 5);
        }

    }

}
