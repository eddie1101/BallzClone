import processing.core.PApplet;
import processing.core.PVector;

public abstract class Griddable extends Drawable {

    protected PVector pos;
    protected int cell;

    protected Grid parent;

    public Griddable(PApplet pApplet, Grid grid) {
        super(pApplet);
        this.pos = new PVector();
        this.parent = grid;
    }

    public PVector getPos() {
        return this.pos;
    }

    public void snapToCellCenter(int cell) {
        PVector pos = Grid.getGridCoordinateFromCell(cell);

        this.pos = new PVector(
                1 + Grid.HALF_CELL_WIDTH + (Grid.HALF_CELL_WIDTH * 2 * pos.x) + (2 * pos.x),
                1 + Grid.HALF_CELL_WIDTH + (Grid.HALF_CELL_WIDTH * 2 * pos.y) + (2 * pos.y));

    }

}
