import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class GameHelper {

    private GameHelper(){}

    public static boolean wallCollision(Ball ball) {

        if(ball.getPos().x - Math.sqrt(Ball.R) <= 0) {
            ball.vel.x *= -1;
        } else if(ball.getPos().x + Math.sqrt(Ball.R) >= 392) {
            ball.vel.x *= -1;
        }

        if(ball.getPos().y - Math.sqrt(Ball.R) <= 0) {
            ball.vel.y *= -1;
        }
//        else if(ball.getPos().y + Math.sqrt(Ball.R) >= 800) {
//            ball.vel.y *= -1;
//        }

        return false;

    }

    public static boolean ballUpgradeCollision(Ball ball, BallUpgrade circle) {

        return ball.getPos().dist(circle.getPos()) < (Ball.R + BallUpgrade.OUTER_R) / 2;

    }

//    public static boolean boxCollision(Ball ball, Box box) {
//        float bx = box.getPos().x - Grid.HALF_CELL_WIDTH, by = box.getPos().y - Grid.HALF_CELL_WIDTH,
//                cx = ball.pos.x, cy = ball.pos.y,
//                testx = cx, testy = cy;
//
//        if(cx + Math.sqrt(Ball.R) < bx) testx = bx;
//        else if(cx - Math.sqrt(Ball.R)> bx + Grid.CELL_WIDTH) testx = bx + Grid.CELL_WIDTH;
//
//
//        if(cy + Math.sqrt(Ball.R) < by) testy = by;
//        else if(cy - Math.sqrt(Ball.R) > by + Grid.CELL_WIDTH) testy = by + Grid.CELL_WIDTH;
//
//        float distx = cx - testx;
//        float disty = cy - testy;
//
//        float distance = (float) Math.sqrt((distx * distx) + (disty + disty));
//        return distance <= Math.sqrt(Ball.R);
//    }

    public static boolean boxCollision(Ball ball, Box box) {

        double cdx = Math.abs(ball.pos.x - box.pos.x);
        double cdy = Math.abs(ball.pos.y - box.pos.y);

        if(cdx > Grid.HALF_CELL_WIDTH + (Ball.R / 2)) return false;
        if(cdy > Grid.HALF_CELL_WIDTH + (Ball.R / 2)) return false;

        if(cdx <= Grid.HALF_CELL_WIDTH) return true;
        if(cdy <= Grid.HALF_CELL_WIDTH) return true;

        double cornerDistSq =
                Math.pow(cdx - Grid.HALF_CELL_WIDTH, 2) +
                Math.pow(cdy - Grid.HALF_CELL_WIDTH, 2);

        return cornerDistSq <= Ball.R;
    }

    public static int findBoxCollisionSide(Ball ball, Box box) {

        float cx = ball.getPos().x, cy = ball.getPos().y,
                bx = box.getPos().x, by = box.getPos().y;

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

    public static Griddable[] getPhysicalsInCollisionRange(Ball ball, Griddable[] physicals, int offset) {

        ArrayList<Griddable> physicalsInRange = new ArrayList<Griddable>();

        for(Griddable physical: physicals) {
            if(physical.getPos().dist(ball.getPos()) < offset + Ball.R + Grid.CELL_WIDTH) {
                physicalsInRange.add(physical);
            }
        }

        Griddable[] retval = new Griddable[physicalsInRange.size()];

        return physicalsInRange.toArray(retval);

    }

    public static PVector fromPolar(PVector vec) {
        float r = vec.x;
        float thRad = (float) Math.toRadians(vec.y);
        return new PVector( (float) (r * Math.cos(thRad)), (float) (r * Math.sin(thRad)));
    }

    public static PVector toPolar(PVector vec) {
        return new PVector(vec.x * vec.x + vec.y * vec.y, (float) Math.toDegrees(Math.atan2(vec.y, vec.x)));
    }

}
