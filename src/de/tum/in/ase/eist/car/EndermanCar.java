package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.GameBoard;
import de.tum.in.ase.eist.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class EndermanCar extends Car {

    private static final int LINGERING_TP_TIME = 12;
    private static final double TP_CHANCE = 0.01;
    private static final String ENDERMAN_CAR_IMAGE_FILE = "EndermanCar.jpg";

    private static final int MIN = 48;
    private static final int MAX = 96;
    private static final double MIN_SPEED_ENDERMAN_CAR = 0.1;
    private static final double MAX_SPEED_ENDERMAN_CAR = 0.5;
    private static final double ENDERMAN_ACCELERATION = 0.02;

    private static final double STILL_ALPHA_0_DOT_4 = 0.4;
    private static final double POST_A = 1.0;
    private static final int[] NUMS = {0, 1, 2};
    private static final int TRE = 3;
    private static final int ON = 1;

    private int tpDrawCounter = NUMS[0];
    private Point2D tpFromPos = null;

    public EndermanCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setMinSpeed(MIN_SPEED_ENDERMAN_CAR);
        setMaxSpeed(MAX_SPEED_ENDERMAN_CAR);
        setRandomSpeed();
        setIconLocation(ENDERMAN_CAR_IMAGE_FILE);
        setAcceleration(ENDERMAN_ACCELERATION);
    }

    @Override
    public void drive(Dimension2D gameBoardSize) {
        if (ThreadLocalRandom.current().nextDouble() < TP_CHANCE) {
            randomTp();
        }
        super.drive(gameBoardSize);
    }

    private void randomTp() {
        Point2D current = getPosition();
        double dx = ThreadLocalRandom.current().nextDouble(MIN, MAX);
        double dy = ThreadLocalRandom.current().nextDouble(MIN, MAX);
        if (ThreadLocalRandom.current().nextBoolean()) {
            dx *= -NUMS[1];
        }
        if (ThreadLocalRandom.current().nextBoolean()) {
            dy *= -NUMS[1];
        }
        tpFromPos = new Point2D(current.getX(), current.getY());
        tpDrawCounter = LINGERING_TP_TIME;
        setPosition(current.getX() + dx, current.getY() + dy);
        GameBoard.getInstance().getAudioPlayer().playTeleportSound();
    }


    @Override
    public void onCollision() {
        GameBoard.getInstance().getAudioPlayer().playHitEndermanSound();
    }

    @Override
    public void onDraw(GraphicsContext context) {
        super.onDraw(context);
        if (tpDrawCounter <= NUMS[0]) {
            return;
        }
        double hw = getSize().getWidth() / NUMS[2];
        double hh = getSize().getHeight() / NUMS[2];
        tpDrawCounter--;
        context.setFill(Color.PURPLE);
        context.setGlobalAlpha(STILL_ALPHA_0_DOT_4);
        context.setLineWidth(TRE);
        //context.rect(tpFromPos.getX(), tpFromPos.getY(), getSize().getWidth(), getSize().getHeight());
        context.fillRect(tpFromPos.getX(), tpFromPos.getY(), getSize().getWidth(), getSize().getHeight());
        context.strokeLine(tpFromPos.getX() + hw, tpFromPos.getY() + hh, getPosition().getX() + hw, getPosition().getY() + hh);
        context.setGlobalAlpha(POST_A);
        context.setLineWidth(ON);
    }

}
