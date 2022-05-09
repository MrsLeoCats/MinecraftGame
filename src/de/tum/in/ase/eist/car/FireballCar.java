package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class FireballCar extends ProjectileCar {

    private int bounces = 0;

    public FireballCar(Dimension2D gameBoardSize, Point2D position, int attack, int direction) {
        super(gameBoardSize, position, attack, direction);
        this.crunchesOnBorder = false;
    }

    @Override
    public void onWallCollide() {
        bounces++;
        if(bounces == 2) {
            this.crunchesOnBorder = true;
        }
    }

    @Override
    public void onDraw(GraphicsContext context) {
        super.onDraw(context);
        path.add(new Point2D(getPosition().getX(), getPosition().getY()));
        if(path.size() >= 6) {
            path.poll();
        }
        context.setFill(Color.ORANGE);
        for(int i = 0; i < path.size(); i++) {
            Point2D next = path.poll();
            context.setGlobalAlpha(0.18 + 0.08 * i);
            context.fillRect(next.getX(), next.getY(), getSize().getWidth(), getSize().getHeight());
            path.add(next);
        }
        context.setGlobalAlpha(1.0);
    }

}
