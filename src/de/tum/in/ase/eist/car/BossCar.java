package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.GameBoard;
import de.tum.in.ase.eist.Point2D;
import de.tum.in.ase.eist.audio.AudioPlayer;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.concurrent.ThreadLocalRandom;

public class BossCar extends Car {

    private static final int LINGERING_TP_TIME = 12;
    private static final double TP_CHANCE = 0.01;
    private static final String BOSS_CAR_IMAGE_FILE = "BossCar.png";

    private static final double MIN_SPEED_BOSS_CAR = 0.1;
    private static final double MAX_SPEED_BOSS_CAR = 1.0;
    private static final double BOSS_ACCELERATION = 0.033;

    private static final int BOSS_MELEE_ATTACK = 0;

    private static final int PROJECTILE_DAMAGE = 1;

    private static final int PROJECTILE_TIMEOUT = 100;
    private int tpDrawCounter = 0;

    private int ticksAlive = 0;
    private Point2D tpFromPos = null;
    private int rotCounter = 0;

    public BossCar(Dimension2D gameBoardSize) {
        super(gameBoardSize, 100);
        setMinSpeed(MIN_SPEED_BOSS_CAR);
        setMaxSpeed(MAX_SPEED_BOSS_CAR);
        setRandomSpeed();
        setIconLocation(BOSS_CAR_IMAGE_FILE);
        setAcceleration(BOSS_ACCELERATION);
        setAttack(BOSS_MELEE_ATTACK);
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
        double dx = ThreadLocalRandom.current().nextDouble(40, 96);
        double dy = ThreadLocalRandom.current().nextDouble(40, 96);
        if (ThreadLocalRandom.current().nextBoolean()) {
            dx *= -1;
        }
        if (ThreadLocalRandom.current().nextBoolean()) {
            dy *= -1;
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
        if (++ticksAlive % PROJECTILE_TIMEOUT == 0) {
            GameBoard board = GameBoard.getInstance();
            Car playerCar = board.getPlayerCar();
            Point2D pos = playerCar.getPosition();
            int rot = getRotationTowards(pos);
            int rotP = rot + 10;
            int rotN = rot - 10;
            if(rotN < 0) {
                rotN += 360;
            }
            ProjectileCar projectileA = new FireballCar(board.getSize(), getMiddle(), PROJECTILE_DAMAGE, rotP);
            ProjectileCar projectileB = new FireballCar(board.getSize(), getMiddle(), PROJECTILE_DAMAGE, rot);
            ProjectileCar projectileC = new FireballCar(board.getSize(), getMiddle(), PROJECTILE_DAMAGE, rotN);
            board.getCars().add(projectileA);
            board.getCars().add(projectileB);
            board.getCars().add(projectileC);
        }
        if(ticksAlive % PROJECTILE_TIMEOUT * 10 == 0) {
            rotCounter = 12;
        }
        if(rotCounter > 0 && ticksAlive % 5 == 0) {
            rotCounter--;
            GameBoard board = GameBoard.getInstance();
            ProjectileCar projectileB = new FireballCar(board.getSize(), getMiddle(), PROJECTILE_DAMAGE, rotCounter * 30);
            board.getCars().add(projectileB);
        }
        if (tpDrawCounter > 0) {
            double hw = getSize().getWidth() / 2;
            double hh = getSize().getHeight() / 2;
            tpDrawCounter--;
            context.setFill(Color.PURPLE);
            context.setGlobalAlpha(0.40001);
            context.setLineWidth(2);
            context.fillRect(tpFromPos.getX(), tpFromPos.getY(), getSize().getWidth(), getSize().getHeight());
            context.strokeLine(tpFromPos.getX() + hw, tpFromPos.getY() + hh, getPosition().getX() + hw, getPosition().getY() + hh);
            context.setGlobalAlpha(1.0);
            context.setLineWidth(1);
        }
    }
}
