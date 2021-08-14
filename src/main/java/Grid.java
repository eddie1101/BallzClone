import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Grid {

    public static final int CIRCLE_R = 20;
    public static final int HALF_CELL_WIDTH = 27;
    public static final int CELL_WIDTH = 55;
    public static final int DIAGONAL_CELL_WIDTH = 78;

    public static final int ROWS = 10;
    public static final int COLS = 7;
    public static final int CELLS = 70;

    private HashMap<Integer, Griddable> grid;
    private PApplet pApplet;

    public Grid(PApplet pApplet) {
        this.pApplet = pApplet;

        grid = new HashMap<Integer, Griddable>();

        for(int i = 0; i < CELLS; i++) {
            safeInsert(new Box(pApplet, this, 0), i);
        }

    }

    public static PVector getGridCoordinateFromCell(int cell) {
        int col = cell % COLS;
        int row = cell / COLS;

        return new PVector(col, row);
    }

    public Griddable[] getPhysicalCells() {

        ArrayList<Griddable> griddables = new ArrayList<Griddable>();

        for(Griddable griddable: grid.values()) {
            if(griddable instanceof Box) {
                Box box = (Box) griddable;
                if(box.count() > 0)
                    griddables.add(griddable);
            } else if (griddable instanceof BallUpgrade) {
                BallUpgrade ballUpgrade = (BallUpgrade) griddable;
                if(ballUpgrade.physical())
                    griddables.add(griddable);
            }
        }

        Griddable[] retval = new Griddable[griddables.size()];
        return griddables.toArray(retval);

    }

    public Griddable getCell(int cell) {
        return grid.get(cell);
    }

    public boolean safeInsert(Griddable griddable, int cell) {
        if(grid.get(cell) instanceof Box) {
            Box box = (Box) grid.get(cell);
            if(box.count() > 0) {
                return false;
            }
        } else if (grid.get(cell) != null) {
            return false;
        }


        griddable.snapToCellCenter(cell);
        grid.put(cell, griddable);
        return true;
    }

    public void translate() {

        for(int i = CELLS - COLS - 1; i >= 0; i--) {
            Griddable griddable = grid.get(i);
            grid.put(i + 7, griddable);
            griddable.snapToCellCenter(i + 7);
        }

        insertRow(generateBlankRow(), 0);

    }

    public Griddable[] generateBlankRow() {

        Box[] row = new Box[7];

        for(int i = 0; i < COLS; i++) {
            row[i] = new Box(pApplet, this, 0);
        }

        return row;

    }

    public Griddable[] generateNewRow(int difficulty) {

        if(difficulty < 1) {
            difficulty = 1;
        }

        int offset = difficulty / 10 + 1;

        Random random = new Random();
        Griddable[] row = new Griddable[7];

        for(int i = 0; i < COLS; i++) {

            int rand = random.nextInt(100);

            if(rand < 30) {
                row[i] = new Box(this.pApplet, this, 0);
            } else if(rand < 40) {
                row[i] = new BallUpgrade(this.pApplet, this);
            } else {
                row[i] = new Box(this.pApplet, this, difficulty + random.nextInt(offset + 1) - offset);
            }
        }

        boolean emptyFlag = true;
        int upgradeCount = 0;
        for(Griddable griddable: row) {
            if(griddable instanceof Box) {
                Box box = (Box) griddable;
                if(box.count() > 0) {
                    emptyFlag = false;
                }
            } else if(griddable instanceof BallUpgrade){
                emptyFlag = false;
                upgradeCount++;
            }
        }

        if(emptyFlag) {
            int rand = random.nextInt(7);
            row[rand] = new Box(pApplet, this, difficulty);
        }

        if(upgradeCount > 1) {
            for(int i = 0; i < COLS; i++) {
                if(row[i] instanceof BallUpgrade) {
                    row[i] = new Box(this.pApplet, this, 0);
                    upgradeCount--;
                }
                if(upgradeCount <= 2) break;
            }
        }

        return row;

    }

    public void insertRow(Griddable[] row, int rowNum) {

        int rowCell = rowNum * COLS;

        if(row.length == 7) {
            for(int i = 0; i < COLS; i++) {
                insert(row[i], rowCell + i);
            }
        }
    }

    public void insert(Griddable griddable, int cell) {
        grid.put(cell, null);
        safeInsert(griddable, cell);
    }

    public Griddable[] getRow(int rowNum) {
        Griddable[] row = new Griddable[7];
        for(int i = rowNum * COLS; i < (rowNum * COLS) + COLS; i++) {
            row[i - (rowNum * COLS)] = getCell(i);
        }
        return row;
    }

    public boolean rowShift(int difficulty) {

        insertRow(generateNewRow(difficulty), 0);

        Griddable[] lastRow = getRow(ROWS - 1);
        for(Griddable griddable: lastRow) {
            if(griddable instanceof Box) {
                Box box = (Box) griddable;
                if(box.count() > 0) {
                    return true;
                }
            }
        }

        translate();
        return false;

    }

    public void draw() {
        for(Griddable griddable: grid.values()) {
            griddable.draw();
        }
    }

}
