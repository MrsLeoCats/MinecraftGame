package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.car.Car;

public class CombatCollision extends Collision {
    public CombatCollision(Car car1, Car car2) {
        super(car1, car2);
    }

    public boolean detectCollision() {
        return new DefaultCollision(getCar1(), getCar2()).detectCollision();
    }

    public Car evaluate() {
        int c1Attack = getCar1().getAttack();
        int c2Attack = getCar2().getAttack();

        int c1Defense = getCar1().getDefense();
        int c2Defense = getCar2().getDefense();

        int c1Toc2Dmg = Math.max(0, c1Attack - c2Defense);
        int c2Toc1Dmg = Math.max(0, c2Attack - c1Defense);

        Car winner = null;

        getCar2().damage(c1Toc2Dmg);

        if (getCar2().isDead()) {
            winner = getCar1();
        }

        getCar1().damage(c2Toc1Dmg);

        if (getCar1().isDead()) {
            winner = getCar2();
        }

        if (getCar2().isKnockBackApplier()) {
            getCar1().knockBack(getCar2().getMiddle());
        }
        if (getCar1().isKnockBackApplier()) {
            getCar2().knockBack(getCar1().getMiddle());
        }

        return winner;
    }
}
