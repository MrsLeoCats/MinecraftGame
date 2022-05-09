package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.GameBoard;
import de.tum.in.ase.eist.Point2D;
import de.tum.in.ase.eist.audio.AudioPlayer;
import javafx.scene.canvas.GraphicsContext;

public class SlowCar extends Car {

    private static final String SKELETON_CAR_IMAGE_FILE = "SkeletonCar.jpg";

    private static final double MIN_SPEED_SLOW_CAR = 0.5;
    private static final double MAX_SPEED_SLOW_CAR = 1.0;
    private static final double SKELETON_ACCELERATION = 0.033;
    private static final int SKELETON_MELEE_ATTACK = 0;
    private static final int PROJECTILE_DAMAGE = 1;
    private static final int PROJECTILE_TIMEOUT = 100;

    private int ticksAlive = 0;

    public SlowCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setMinSpeed(MIN_SPEED_SLOW_CAR);
        setMaxSpeed(MAX_SPEED_SLOW_CAR);
        setRandomSpeed();
        setIconLocation(SKELETON_CAR_IMAGE_FILE);
        setAcceleration(SKELETON_ACCELERATION);
        setAttack(SKELETON_MELEE_ATTACK);
    }

    @Override
    public void onDraw(GraphicsContext context) {
        super.onDraw(context);
        if (++ticksAlive % PROJECTILE_TIMEOUT != 0) {
            return;
        }
        GameBoard board = GameBoard.getInstance();
        Car playerCar = board.getPlayerCar();
        Point2D pos = playerCar.getPosition();
        ProjectileCar projectile = new ProjectileCar(board.getSize(), getMiddle(), PROJECTILE_DAMAGE, getRotationTowards(pos));
        board.getCars().add(projectile);
    }

    @Override
    public void onCollision() {
        GameBoard.getInstance().getAudioPlayer().playHitSkeletonSound();
    }

}
