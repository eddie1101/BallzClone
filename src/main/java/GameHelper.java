import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;

public class GameHelper {

    private GameHelper(){}

    public static boolean wallCollision(Ball ball) {

        PVector vel = ball.getVel();

        if(ball.getPos().x <= 0) {
            ball.setVel(-vel.x, vel.y);
        } else if(ball.getPos().x >= 400) {
            ball.setVel(-vel.x, vel.y);
        }

        if(ball.getPos().y <= 0) {
            ball.setVel(vel.x, -vel.y);
        } else if(ball.getPos().y >= 800) {
            ball.setVel(vel.x, -vel.y);
        }

        return false;

    }

    public static boolean ballUpgradeCollision(Ball ball, BallUpgrade circle) {

        return ball.getPos().dist(circle.getPos()) < Ball.R + Grid.CIRCLE_R;

    }

    public static boolean boxCollision(Ball ball, Box box) {

        float bx = box.getPos().x - Grid.HALF_CELL_WIDTH, by = box.getPos().y - Grid.HALF_CELL_WIDTH,
                cx = ball.getPos().x, cy = ball.getPos().y,
                testx = cx, testy = cy;

        if(cx < bx) testx = bx;
        else if(cx > bx + Grid.CELL_WIDTH) testx = bx + Grid.CELL_WIDTH;


        if(cy < by) testy = by;
        else if(cy > by + Grid.CELL_WIDTH) testy = by + Grid.CELL_WIDTH;

        float distx = cx - testx;
        float disty = cy - testy;

        float distance = (float) Math.sqrt((distx * distx) + (disty + disty));

        return distance <= Math.sqrt(Ball.R);
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

    public static Griddable[] getPhysicalsInCollisionRange(Ball ball, Griddable[] physicals) {

        ArrayList<Griddable> physicalsInRange = new ArrayList<Griddable>();

        for(Griddable physical: physicals) {
            if(physical.getPos().dist(ball.getPos()) < 5 + Ball.R + Grid.CELL_WIDTH) {
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
