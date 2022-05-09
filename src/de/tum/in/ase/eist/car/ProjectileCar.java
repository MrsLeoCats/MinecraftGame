package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayDeque;

public class ProjectileCar extends Car {

    private static final int HP = 1;
    private static final int SIZE = 5;
    private static final String PROJECTILE_CAR_IMAGE_FILE = "ProjectileCar.png";
    private static final double PROJECTILE_SPEED = 4.0;
    private static final double ACCELERATION = 1.0;
    private static final double BASE = 0.2;
    private static final double DELTA = 0.1;

    protected final ArrayDeque<Point2D> path = new ArrayDeque<>();

    public ProjectileCar(Dimension2D gameBoardSize, Point2D position, int attack, int direction) {
        super(gameBoardSize, HP);
        setPosition(position.getX(), position.getY());
        setIconLocation(PROJECTILE_CAR_IMAGE_FILE);
        setMaxSpeed(PROJECTILE_SPEED);
        setSpeed(PROJECTILE_SPEED);
        setAcceleration(ACCELERATION);
        setAttack(attack);
        setDirection(direction);
        this.size = new Dimension2D(SIZE, SIZE);
        this.crunchesOnBorder = true;
        setKnockBackApplier(false);
        setHostile(false);
    }

    @Override
    public void onDraw(GraphicsContext context) {
        super.onDraw(context);
        path.add(new Point2D(getPosition().getX(), getPosition().getY()));
        if (path.size() >= 6) {
            path.poll();
        }
        context.setFill(Color.GRAY);
        for(int i = path.size() - 1; i >= 0; i--) {
            Point2D next = path.poll();
            if(next == null) {
                break;
            }
            context.setGlobalAlpha(BASE + DELTA * i);
            context.fillRect(next.getX(), next.getY(), getSize().getWidth(), getSize().getHeight());
            path.add(next);
        }
        context.setGlobalAlpha(ACCELERATION);
    }

}
