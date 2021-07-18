import processing.core.PApplet;
import processing.core.PVector;

public class RayCaster {

    PApplet pApplet;

    private float step;
    private float x;
    private float y;
    private float dy;
    private boolean switx = false;
    private boolean swity = false;

    private int depth = 0;
    private int maxBounces = 1000;

    private PVector target;
    private PVector absoluteHome;
    private PVector home;

    private Grid grid;

    public RayCaster(PApplet pApplet, Grid grid) {
        this.pApplet = pApplet;
        this.grid = grid;
    }

    public void setMaxBounces(int max) {
        this.maxBounces = max;
    }

    public void initRayCast(PVector home, PVector target, float step) {
        this.target = target;
        this.home = home;
        this.absoluteHome = home;
        dy = (target.y - home.y) / (target.x - home.x);

        if(step == -1) {
            if(dy > 1) {
                this.step = 1 / dy;
            } else {
                this.step = 1;
            }
        } else {
            this.step = step;
        }

        switx = false;
        swity = false;

        y = home.y;
        x = home.x;
        depth = 0;
    }

    public void rayCast(PVector home, PVector target, float step) {
        initRayCast(home, target, step);
        if(Math.abs(dy) < 0.25 || this.step < 0.01) return;
        recursiveRayCast();
    }

    private void recursiveRayCast() {

        System.out.println(depth++);

        if(y >= pApplet.height || depth > maxBounces) {
            return;
        }

        while(true) {

            if(x - Math.sqrt(Ball.R) <= 0 || x + Math.sqrt(Ball.R) >= pApplet.width) {
                updateHome();
                switx = !switx;
                increment();
                recursiveRayCast();
                break;
            } else if(y - Math.sqrt(Ball.R) <= 0) {
                updateHome();
                swity = !swity;
                increment();
                recursiveRayCast();
                break;
            }

            int side = boxIntersect();
            if(side == 0 || side == 2) {
                updateHome();
                swity = !swity;
                increment();
                recursiveRayCast();
                break;
            } else if(side == 1 || side == 3) {
                updateHome();
                switx = !switx;
                increment();
                recursiveRayCast();
                break;
            }

            increment();

        }

    }

    private int boxIntersect() {
        Griddable[] griddables = grid.getPhysicalCells();
        Ball thisIsAHack = new Ball(this.pApplet);
        thisIsAHack.setPos(x, y);
        Griddable[] intersectionCandidates = GameHelper.getPhysicalsInCollisionRange(thisIsAHack, griddables, (int) (-Ball.R));
        for(Griddable ic: intersectionCandidates) {
            if(ic instanceof Box) {
                Box box = (Box) ic;


                float bx = box.getPos().x - Grid.HALF_CELL_WIDTH, by = box.getPos().y - Grid.HALF_CELL_WIDTH,
                        cx = x, cy = y,
                        testx = cx, testy = cy;

                if(cx + Math.sqrt(Ball.R) < bx) testx = bx;
                else if(cx - Math.sqrt(Ball.R)> bx + Grid.CELL_WIDTH) testx = bx + Grid.CELL_WIDTH;


                if(cy + Math.sqrt(Ball.R) < by) testy = by;
                else if(cy - Math.sqrt(Ball.R) > by + Grid.CELL_WIDTH) testy = by + Grid.CELL_WIDTH;

                float distx = cx - testx;
                float disty = cy - testy;

                float distance = (float) Math.sqrt((distx * distx) + (disty + disty));

                if (distance <= Math.sqrt(Ball.R)) {
                    bx = box.getPos().x;
                    by = box.getPos().y;

                    int side = -1;

                    double theta = Math.toDegrees(Math.atan2(cx - bx, cy - by));

                    if(theta >= -45 && theta <= 45) {
                        side = 2;
                    } else if(theta <= -45 && theta >= -135) {
                        side = 3;
                    } else if(theta <= 135 && theta >= 45) {
                        side = 1;
                    } else if( (theta >= -180 && theta <= -135) || (theta <= 180 && theta >= 135) ) {
                        side = 0;
                    }

                    return side;
                }
            }
        }
        return -1;
    }

    private void updateHome() {
        pApplet.stroke(255);
        pApplet.line(home.x, home.y, x, y);
        home = new PVector(x, y);
    }

    private void increment() {
        if(target.x >= absoluteHome.x) {
            inx();
            dey();
        }
        else {
            dex();
            iny();
        }
    }

    private void inx() {
        if(switx) x -= step;
        else x += step;
    }

    private void dex() {
        if(switx) x += step;
        else x -= step;
    }

    private void iny() {
        if(swity) y += (dy * step);
        else y -= (dy * step);
    }

    private void dey() {
        if(swity) y -= (dy * step);
        else y += (dy * step);
    }

}
