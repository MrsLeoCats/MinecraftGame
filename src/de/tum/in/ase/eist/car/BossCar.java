package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.GameBoard;
import de.tum.in.ase.eist.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class BossCar extends Car{

    private static final int LINGERING_TP_TIME = 12;
    private static final double TP_CHANCE = 0.01;
    private static final String BOSS_CAR_IMAGE_FILE = "BossCar.png";

    private static final double MIN_SPEED_BOSS_CAR = 0.1;
    private static final double MAX_SPEED_BOSS_CAR = 0.5;
    private static final double BOSS_ACCELERATION = 0.02;

    private static final int BOSS_MELEE_ATTACK = 0;

    private static final int PROJECTILE_DAMAGE = 1;

    private static final int PROJECTILE_TIMEOUT = 100;
    private int tpDrawCounter = 0;

    private int ticksAlive = 0;
    private Point2D tpFromPos = null;

    public BossCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setMinSpeed(MIN_SPEED_BOSS_CAR);
        setMaxSpeed(MAX_SPEED_BOSS_CAR);
        setRandomSpeed();
        setIconLocation(BOSS_CAR_IMAGE_FILE);
        setAcceleration(BOSS_ACCELERATION);
        setAttack(BOSS_MELEE_ATTACK);
    }

    @Override
    public void drive(Dimension2D gameBoardSize) {
        if(ThreadLocalRandom.current().nextDouble() < TP_CHANCE) {
            randomTp();
        }
        super.drive(gameBoardSize);
    }

    private void randomTp() {
        Point2D current = getPosition();
        double dx = ThreadLocalRandom.current().nextDouble(40, 96);
        double dy = ThreadLocalRandom.current().nextDouble(40, 96);
        if(ThreadLocalRandom.current().nextBoolean()) {
            dx *= -1;
        }
        if(ThreadLocalRandom.current().nextBoolean()) {
            dy *= -1;
        }
        tpFromPos = new Point2D(current.getX(), current.getY());
        tpDrawCounter = LINGERING_TP_TIME;
        setPosition(current.getX() + dx, current.getY() + dy);
    }

    @Override
    public void onDraw(GraphicsContext context) {
        if (++ticksAlive % PROJECTILE_TIMEOUT == 0) {
            GameBoard board = GameBoard.getInstance();
            Car playerCar = board.getPlayerCar();
            Point2D pos = playerCar.getPosition();
            ProjectileCar projectile = new ProjectileCar(board.getSize(), getMiddle(), PROJECTILE_DAMAGE, getRotationTowards(pos));
            board.getCars().add(projectile);
        }
        if(tpDrawCounter <= 0) {
            return;
        }
        double hw = getSize().getWidth() / 2;
        double hh = getSize().getHeight() / 2;
        tpDrawCounter--;
        context.setFill(Color.PURPLE);
        context.setGlobalAlpha(0.4);
        context.setLineWidth(3);
        //context.rect(tpFromPos.getX(), tpFromPos.getY(), getSize().getWidth(), getSize().getHeight());
        context.fillRect(tpFromPos.getX(), tpFromPos.getY(), getSize().getWidth(), getSize().getHeight());
        context.strokeLine(tpFromPos.getX() + hw, tpFromPos.getY() + hh, getPosition().getX() + hw, getPosition().getY() + hh);
        context.setGlobalAlpha(1.0);
        context.setLineWidth(1);
    }
}
