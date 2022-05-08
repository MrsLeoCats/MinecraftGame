package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Point2D;
import de.tum.in.ase.eist.car.Car;

public class CombatCollision extends Collision {
    public CombatCollision(Car car1, Car car2) {
        super(car1, car2);
    }

    public boolean detectCollision() {
        Point2D p1 = getCar1().getPosition();
        Dimension2D d1 = getCar1().getSize();

        Point2D p2 = getCar2().getPosition();
        Dimension2D d2 = getCar2().getSize();

        boolean above = p1.getY() + d1.getHeight() < p2.getY();
        boolean below = p1.getY() > p2.getY() + d2.getHeight();
        boolean right = p1.getX() + d1.getWidth() < p2.getX();
        boolean left = p1.getX() > p2.getX() + d2.getWidth();

        return !above && !below && !right && !left;
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
            getCar1().knockBack();
        }
        if (getCar1().isKnockBackApplier()) {
            getCar2().knockBack();
        }

        return winner;
    }
}
