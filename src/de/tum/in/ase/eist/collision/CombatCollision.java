package de.tum.in.ase.eist.collision;

import de.tum.in.ase.eist.Point2D;
import de.tum.in.ase.eist.car.Car;

public class CombatCollision extends DefaultCollision {
    public CombatCollision(Car car1, Car car2) {
        super(car1, car2);
    }

    public Car evaluate() {

        // TODO Backlog Item 11: Collisions follow the "right before left" rule, and thus right-most
        // cars on the screen win the collisions

        Point2D p1 = getCar1().getPosition();
        Point2D p2 = getCar2().getPosition();

        Car winnerCar;
        if (p1.getX() > p2.getX()) {
            winnerCar = this.getCar2();
        } else {
            winnerCar = this.getCar1();
        }
        return winnerCar;
    }
}
