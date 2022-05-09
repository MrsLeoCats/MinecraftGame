package de.tum.in.ase.eist.car;

import java.util.concurrent.ThreadLocalRandom;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/**
 * Abstract class for cars. Objects for this class cannot be instantiated.
 */
public abstract class Car {

    private static final double DECELERATION = 0.5;
    private static final double STAND_BACK_DECELERATION = 0.25;
    private static final double KNOCK_BACK_SPEED = 3.0;
    private static final int DEFAULT_MAX_HP = 10;
    private static final int DEFAULT_ATTACK = 1;
    private static final int DEFAULT_DEFENSE = 0;
    private static final double DEFAULT_ACCELERATION = 0.01;

    protected static final int MAX_ANGLE = 360;
    protected static final int HALF_ANGLE = MAX_ANGLE / 2;

    protected static final int DEFAULT_CAR_WIDTH = 32;
    protected static final int DEFAULT_CAR_HEIGHT = 32;
    private static final double ZERO_DOT_4 = 0.4;
    private static final double ONE = 1.0;
    private static final int TEN = 10;
    private static final int ZERO = 0;
    private static final int TWO = 2;

    private int hp;
    private final int maxHp;
    private double minSpeed;
    private double maxSpeed;
    private double speed;
    private boolean crunched;
    private double acceleration;
    private int attack;
    private int defense;
    protected boolean knockBackApplier = true;
    protected boolean hostile = true;
    protected int dmgCounter = 0;

    private Point2D position;
    /**
     * The direction as degree within a circle, a value between 0 (inclusive) and
     * 360 (exclusive).
     */
    private int direction;

    private String iconLocation;
    protected Dimension2D size = new Dimension2D(DEFAULT_CAR_WIDTH, DEFAULT_CAR_HEIGHT);
    protected boolean crunchesOnBorder = false;

    /**
     * Constructor, taking the maximum coordinates of the game board. Each car gets
     * a random X and Y coordinate, a random direction and a random speed.
     * <p>
     * The position of the car cannot be larger then the dimensions of the game
     * board.
     *
     * @param gameBoardSize dimensions of the game board
     */
    protected Car(Dimension2D gameBoardSize) {
        this(gameBoardSize, DEFAULT_MAX_HP);
    }

    protected Car(Dimension2D gameBoardSize, int hp) {
        setRandomPosition(gameBoardSize);
        setRandomDirection();
        this.hp = hp;
        this.maxHp = hp;
        acceleration = DEFAULT_ACCELERATION;
        attack = DEFAULT_ATTACK;
        defense = DEFAULT_DEFENSE;
    }

    public boolean isKnockBackApplier() {
        return knockBackApplier;
    }

    /**
     * Sets the cars position to a random value in between the boundaries of the
     * game board.
     *
     * @param gameBoardSize dimensions of the game board
     */
    protected void setRandomPosition(Dimension2D gameBoardSize) {
        double carX = calculateRandomDouble(ZERO, gameBoardSize.getWidth() - size.getWidth());
        double carY = calculateRandomDouble(ZERO, gameBoardSize.getHeight() - size.getHeight());
        this.position = new Point2D(carX, carY);
    }

    protected void setRandomDirection() {
        this.direction = calculateRandomInt(ZERO, MAX_ANGLE);
    }

    /**
     * Sets the speed of the car to a random value based on its minimum and maximum
     * speed.
     */
    protected void setRandomSpeed() {
        // We pass this.maxSpeed + 1 to include the value of maxSpeed
        this.speed = calculateRandomDouble(this.minSpeed, this.maxSpeed);
    }

    protected void setAcceleration(double value) {
        this.acceleration = value;
    }

    public void onCollision() {

    }

    /**
     * Drives the car and updates its position and possibly its direction.
     * <p>
     * The X and Y coordinates of the new position are based on the current
     * position, direction and speed.
     *
     * @param gameBoardSize dimensions of the game board that are the bounds inside
     *                      which the car is allowed to move.
     */
    public void drive(Dimension2D gameBoardSize) {
        if (this.crunched) {
            return;
        }
        incrementSpeed();

        double maxX = gameBoardSize.getWidth();
        double maxY = gameBoardSize.getHeight();
        // calculate delta between old coordinates and new ones based on speed and
        // direction
        double deltaX = this.speed * Math.sin(Math.toRadians(this.direction));
        double deltaY = this.speed * Math.cos(Math.toRadians(this.direction));
        double newX = this.position.getX() + deltaX;
        double newY = this.position.getY() + deltaY;

        // calculate position in case the boarder of the game board has been reached
        if (newX < ZERO) {
            onWallCollide();
            if (crunchesOnBorder) {
                crunch();
                return;
            }
            newX = -newX;
            this.direction = MAX_ANGLE - this.direction;
        } else if (newX + this.size.getWidth() > maxX) {
            onWallCollide();
            if (crunchesOnBorder) {
                crunch();
                return;
            }
            newX = TWO * maxX - newX - TWO * this.size.getWidth();
            this.direction = MAX_ANGLE - this.direction;
        }

        if (newY < ZERO) {
            onWallCollide();
            if (crunchesOnBorder) {
                crunch();
                return;
            }
            newY = -newY;
            this.direction = HALF_ANGLE - this.direction;
            if (this.direction < ZERO) {
                this.direction = MAX_ANGLE + this.direction;
            }
        } else if (newY + this.size.getHeight() > maxY) {
            onWallCollide();
            if (crunchesOnBorder) {
                crunch();
                return;
            }
            newY = TWO * maxY - newY - TWO * this.size.getHeight();
            this.direction = HALF_ANGLE - this.direction;
            if (this.direction < ZERO) {
                this.direction = MAX_ANGLE + this.direction;
            }
        }
        // set coordinates
        this.position = new Point2D(newX, newY);
    }

    public void onWallCollide() {

    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getRotationTowards(Point2D position) {
        double dirX = position.getX() - getPosition().getX();
        double dirY = position.getY() - getPosition().getY();
        int dir = (int) Math.toDegrees(Math.atan2(dirX, dirY));
        if (dir < ZERO) {
            dir += MAX_ANGLE;
        }
        return dir;
    }

    public Point2D getMiddle() {
        return new Point2D(getPosition().getX() + getSize().getWidth() / 2, getPosition().getY() + getSize().getHeight() / 2);
    }

    public void onDraw(GraphicsContext context) {
        this.dmgCounter--;
        if (dmgCounter > ZERO) {
            context.setGlobalAlpha(ZERO_DOT_4);
            context.setFill(Color.RED);
            context.fillRect(position.getX(), position.getY(), size.getWidth(), size.getHeight());
            context.setGlobalAlpha(ONE);
        }
    }

    /**
     * Sets the car's direction.
     *
     * @param direction the new direction in degrees (must be between 0 and 360)
     * @throws IllegalArgumentException if the new direction is lower than 0 or
     *                                  higher than 360.
     */
    public void setDirection(int direction) {
        if (direction < ZERO || direction >= MAX_ANGLE) {
            throw new IllegalArgumentException("Direction must be between 0 (inclusive) and 360 (exclusive)");
        }
        this.direction = direction;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getDirection() {
        return this.direction;
    }

    public double getSpeed() {
        return this.speed;
    }

    /**
     * Increments the car's speed, won't exceed the maximum speed.
     */
    public void incrementSpeed() {
        if (this.speed < this.maxSpeed) {
            this.speed += speed < ZERO ? STAND_BACK_DECELERATION : acceleration;
        }
        if (speed > maxSpeed) {
            decrementSpeed();
        }
    }

    /**
     * Decrements the car's speed, won't fall below the minimum speed.
     */
    public void decrementSpeed() {
        if (speed > maxSpeed) {
            setSpeed(Math.max(maxSpeed, speed - DECELERATION));
        }
    }

    public String getIconLocation() {
        return this.iconLocation;
    }

    /**
     * Sets the image path of the car.
     *
     * @param iconLocation the path to the image file
     * @throws NullPointerException if iconLocation is null
     */
    protected void setIconLocation(String iconLocation) {
        if (iconLocation == null) {
            throw new NullPointerException("The chassis image of a car cannot be null.");
        }
        this.iconLocation = iconLocation;
    }

    public double getHealthPercent() {
        return ONE / maxHp * hp;
    }

    public void heal(int amount) {
        hp += amount;
        hp = Math.min(hp, maxHp);
    }

    public void damage(int amount) {
        if (amount > ZERO) {
            dmgCounter = TEN;
        }
        hp -= amount;
        hp = Math.max(ZERO, hp);
    }

    public boolean isDead() {
        return hp == 0;
    }

    public void kill() {
        damage(hp);
    }

    public Point2D getPosition() {
        return this.position;
    }

    public void setPosition(double x, double y) {
        this.position = new Point2D(x, y);
    }

    public Dimension2D getSize() {
        return this.size;
    }

    public void setSize(Dimension2D size) {
        this.size = size;
    }

    public void crunch() {
        this.crunched = true;
        this.speed = ZERO;
    }

    public boolean isCrunched() {
        return this.crunched;
    }

    public double getMaxSpeed() {
        return this.maxSpeed;
    }

    public double getMinSpeed() {
        return this.minSpeed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    public void setMinSpeed(double minSpeed) {
        this.minSpeed = minSpeed;
    }

    /**
     * Calculates a new random int value between minValue (inclusive) and the
     * provided maxValue (exclusive).
     *
     * @param minValue the inclusive lower bound
     * @param maxValue the exclusive upper bound
     * @return a random int value
     */
    protected static int calculateRandomInt(int minValue, int maxValue) {
        return ThreadLocalRandom.current().nextInt(minValue, maxValue);
    }

    /**
     * Calculates a new random double value between minValue (inclusive) and the
     * provided maxValue (exclusive).
     *
     * @param minValue the inclusive lower bound
     * @param maxValue the exclusive upper bound
     * @return a random double value
     */
    protected static double calculateRandomDouble(double minValue, double maxValue) {
        return ThreadLocalRandom.current().nextDouble(minValue, maxValue);
    }

    public void knockBack(Point2D from) {
        setDirection(getRotationTowards(from));
        setSpeed(-KNOCK_BACK_SPEED);
    }

    public boolean isHostile() {
        return hostile;
    }
}
