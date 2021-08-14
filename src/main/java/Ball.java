import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.HashMap;

public class Ball extends Drawable {

    public static final float R = 15;

    private static int r = 0, g = 0, b = 0, count = 0;

    private HashMap<Integer, Box> colliding;

    private int expireTime = Integer.MAX_VALUE;
    private boolean expiring = false;

    protected PVector pos;
    protected PVector vel;

    private PVector target;
    private boolean travelling = false;

    public Ball(PApplet pApplet) {
        super(pApplet);
        pos = new PVector();
        vel = new PVector();
        colliding = new HashMap<Integer, Box>();
    }

    public Ball setPos(float x, float y) {
        this.pos = new PVector(x, y);
        return this;
    }

    public PVector getPos() {
        return this.pos;
    }

    public PVector samplePos() {
        return new PVector(pos.x + vel.x, pos.y + vel.y);
    }

    public boolean isCollidingWith(Box box) {
        return this.colliding.containsKey(box.cell);
    }

    public void addColliding(Box box) {
        this.colliding.put(box.cell, box);
    }

    public void removeColliding(Box box) {
        this.colliding.remove(box.cell);
    }

    public Ball setVel(float x, float y) {
        this.vel = new PVector(x, y);
        return this;
    }

    public PVector getVel() {
        return this.vel;
    }

    public void travelTo(PVector target, float speed) {
        setVel((target.x - this.pos.x) * speed , (target.y - this.pos.y) * speed);
        this.target = target;
        travelling = true;
    }

    public void expireIn(int frames) {
        expireTime = frames;
        expiring = true;
    }

    public void refresh() {
        this.expiring = false;
        this.expireTime = Integer.MAX_VALUE;
    }

    public int getExpireTime() {
        return expireTime;
    }

    public boolean isTravelling() {
        return this.travelling;
    }

    public void update() {
        pos.add(vel);

        if(travelling && this.pos.dist(target) <= 0.25) {
            setVel(0, 0);
            setPos(target.x, target.y);
            travelling = false;
        }

        if(expiring) {
            expireTime--;
        }

    }

    public void draw() {

        pApplet.stroke(255, 120, 150);
        pApplet.fill(255, 120, 150);
        pApplet.ellipseMode(PConstants.CENTER);

        if(expireTime > 10) {
            pApplet.ellipse(pos.x, pos.y, R, R);
        } else {
            float r = R * ((float) expireTime / 10);
            pApplet.ellipse(pos.x, pos.y, r,r );
        }

    }

}
