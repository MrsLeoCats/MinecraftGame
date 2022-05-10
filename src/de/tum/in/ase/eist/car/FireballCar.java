package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FireballCar extends ProjectileCar {

    private static final int MAX_BOUNCES = 2;
    private static final int POLL_SIZE = 6;
    private static final double BASE_ALPHA = 0.18;
    private static final double DELTA_ALPHA = 0.08;
    private static final double FULL_ALPHA = 1.0;

    private int bounces = 0;

    public FireballCar(Dimension2D gameBoardSize, Point2D position, int attack, int direction) {
        super(gameBoardSize, position, attack, direction);
        setCrunchesOnBorder(false);
    }

    @Override
    public void onWallCollide() {
        bounces++;
        if (bounces == MAX_BOUNCES) {
            setCrunchesOnBorder(true);
        }
    }

    @Override
    public void onDraw(GraphicsContext context) {
        super.onDraw(context);
        getPath().add(new Point2D(getPosition().getX(), getPosition().getY()));
        if (getPath().size() >= POLL_SIZE) {
            getPath().poll();
        }
        context.setFill(Color.ORANGE);
        for (int i = 0; i < getPath().size(); i++) {
            Point2D next = getPath().poll();
            if (next == null) {
                continue;
            }
            context.setGlobalAlpha(BASE_ALPHA + DELTA_ALPHA * i);
            context.fillRect(next.getX(), next.getY(), getSize().getWidth(), getSize().getHeight());
            getPath().add(next);
        }
        context.setGlobalAlpha(FULL_ALPHA);
    }

}
