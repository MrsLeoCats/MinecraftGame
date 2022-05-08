package de.tum.in.ase.eist.car;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Player;

import java.util.concurrent.ThreadLocalRandom;

public class CollectableHpCar extends CollectableCar {

    public CollectableHpCar(Dimension2D gameBoardSize) {
        super(gameBoardSize);
        setIconLocation("HeartCar.png");
        size = new Dimension2D(16, 16);
        setSpeed(1.0);
        setAcceleration(0.0);
        setDirection(0);
        setPosition(ThreadLocalRandom.current().nextDouble(0, gameBoardSize.getWidth() - 16), 10);
    }

    @Override
    public void onPickup(Player player) {
        player.getCar().heal((int) (player.getCar().getMaxHp() * 0.25));
    }
}
