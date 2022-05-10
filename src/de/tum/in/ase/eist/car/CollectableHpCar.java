package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Player;

import java.util.concurrent.ThreadLocalRandom;

public class CollectableHpCar extends CollectableCar {

    private static final int SIXTEEN = 16;
    private static final int TEN = 10;
    private static final int NOD = 0;
    private static final double ONE = 1.0;
    private static final double ZERO = 0.0;
    private static final double DOT_25 = 0.25;

    public CollectableHpCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setIconLocation("HeartCar.png");
        setSize(new Dimension2D(SIXTEEN, SIXTEEN));
        setSpeed(ONE);
        setAcceleration(ZERO);
        setDirection(NOD);
        setPosition(ThreadLocalRandom.current().nextDouble(NOD, gameBoardSize.getWidth() - SIXTEEN), TEN);
    }

    @Override
    public void onPickup(Player player) {
        player.getCar().heal((int) (player.getCar().getMaxHp() * DOT_25));
    }
}
