package de.tum.in.ase.eist;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

import de.tum.in.ase.eist.audio.AudioPlayerInterface;
import de.tum.in.ase.eist.car.*;
import de.tum.in.ase.eist.collision.Collision;
import de.tum.in.ase.eist.collision.CombatCollision;

/**
 * Creates all car objects, detects collisions, updates car positions, notifies
 * player about victory or defeat.
 */
public class GameBoard {


    private static final int NUMBER_OF_ZOMBIE_CARS = 4;
    private static final int NUMBER_OF_SKELETON_CARS = 3;
    private static final int NUMBER_OF_ENDERMAN_CARS = 1;
    private static final int SPAWN_TICKS = 50;
    private static final int DT1 = 3;
    private static final double CHANCE1 = 0.01;
    private static final double[] CHANCES = {0.45, 0.8};
    private static final int SPARTA = 300;

    private static GameBoard instance;

    public static GameBoard getInstance() {
        return instance;
    }

    private int spawnTicks = SPARTA;
    private int ticksAlive = 0;
    private boolean bossSpawned = false;

    /**
     * List of all active cars, does not contain player car.
     */
    private final List<Car> cars = new CopyOnWriteArrayList<>();

    /**
     * The player object with player's car.
     */
    private final Player player;

    /**
     * AudioPlayer responsible for handling music and game sounds.
     */
    private AudioPlayerInterface audioPlayer;

    /**
     * Dimension of the GameBoard.
     */
    private final Dimension2D size;

    /**
     * true if game is running, false if game is stopped.
     */
    private boolean running;

    /**
     * List of all loser cars (needed for testing, DO NOT DELETE THIS)
     */
    private final List<Car> loserCars = new ArrayList<>();

    /**
     * The outcome of this game from the players perspective. The game's outcome is open at the beginning.
     */
    private GameOutcome gameOutcome = GameOutcome.OPEN;

    /**
     * Creates the game board based on the given size.
     *
     * @param size of the game board
     */
    public GameBoard(Dimension2D size) {
        instance = this;
        this.size = size;
        PlayerCar playerCar = new PlayerCar(size);
        this.player = new Player(playerCar);
        this.player.setup();
        createCars();
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Creates as many cars as specified by {@link #NUMBER_OF_SKELETON_CARS} and adds
     * them to the cars list.
     */
    private void createCars() {
        // TODO Backlog Item 6: Add a new car type
        for (int i = 0; i < NUMBER_OF_SKELETON_CARS; i++) {
            this.cars.add(new SlowCar(this.size));
        }
        for (int i = 0; i < NUMBER_OF_ZOMBIE_CARS; i++) {
            this.cars.add(new FastCar(this.size));
        }
        for (int i = 0; i < NUMBER_OF_ENDERMAN_CARS; i++) {
            this.cars.add(new EndermanCar(this.size));
        }
    }

    public Dimension2D getSize() {
        return size;
    }

    /**
     * Returns if game is currently running.
     *
     * @return true if the game is currently running, false otherwise
     */
    public boolean isRunning() {
        return this.running;
    }

    /**
     * Sets whether the game should be currently running.
     * <p>
     * Also used for testing on Artemis.
     *
     * @param running true if the game should be running
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    public GameOutcome getGameOutcome() {
        return gameOutcome;
    }

    /**
     * Returns all cars on the game board except the player's car as a list.
     *
     * @return the list of all non-player cars
     */
    public List<Car> getCars() {
        return this.cars;
    }

    public Car getPlayerCar() {
        return this.player.getCar();
    }

    public AudioPlayerInterface getAudioPlayer() {
        return this.audioPlayer;
    }

    public void setAudioPlayer(AudioPlayerInterface audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Updates the position of each car.
     */
    public void update() {
        ticksAlive++;
        moveCars();
        checkSpawns();
    }



    private void checkSpawns() {
        if (ticksAlive % spawnTicks == 0 && !bossSpawned) {
            randomSpawn();
            if (spawnTicks > SPAWN_TICKS) {
                spawnTicks -= DT1;
            }
        }
        if (ThreadLocalRandom.current().nextDouble() < CHANCE1) {
            cars.add(new CollectableHpCar(size));
        }
    }

    private void randomSpawn() {
        double value = ThreadLocalRandom.current().nextDouble();
        if (value < CHANCES[0]) {
            cars.add(new FastCar(size));
        } else if (value < CHANCES[1]) {
            cars.add(new SlowCar(size));
        } else {
            cars.add(new EndermanCar(size));
        }
    }

    /**
     * Starts the game. Cars start to move and background music starts to play.
     */
    public void startGame() {
        playMusic();
        this.running = true;
        Item.reset();
    }

    /**
     * Stops the game. Cars stop moving and background music stops playing.
     */
    public void stopGame() {
        stopMusic();
        this.running = false;
    }

    /**
     * Starts the background music.
     */
    public void playMusic() {
        this.audioPlayer.playBackgroundMusic();
    }

    /**
     * Stops the background music.
     */
    public void stopMusic() {
        this.audioPlayer.stopBackgroundMusic();
    }

    /**
     * @return list of loser cars
     */
    public List<Car> getLoserCars() {
        return this.loserCars;
    }

    /**
     * Moves all cars on this game board one step further.
     */
    public void moveCars() {
        // update the positions of the player car and the autonomous cars
        for (Car car : this.cars) {
            car.drive(size);
        }
        this.player.getCar().drive(size);

        // iterate through all cars (except player car) and check if it is crunched
        for (Car car : cars) {
            if (car.isCrunched()) {
                // because there is no need to check for a collision
                continue;
            }

            // TODO Backlog Item 16: Add a new collision type
            /*
             * Hint: Make sure to create a subclass of the class Collision and store it in
             * the new Collision package. Create a new collision object and check if the
             * collision between player car and autonomous car evaluates as expected
             */

            Collision collision = new CombatCollision(player.getCar(), car);

            if (collision.isCrash()) {

                car.onCollision();
                player.getCar().onCollision();

                Car winner = collision.evaluate();
                if (winner == null) {
                    continue;
                }
                Car loser = collision.evaluateLoser();
                printWinner(winner);
                loserCars.add(loser);
                cars.remove(loser);

                if (loser instanceof CollectableCar collectableCar) {
                    collectableCar.onPickup(player);
                }
                if (!(loser instanceof ProjectileCar)) {
                    incrementScore(winner);
                }

                // TODO Backlog Item 11: The loser car is crunched and stops driving
                loser.crunch();
                // TODO Backlog Item 11: The player gets notified when he looses or wins the game
                /*
                 * Hint: you should set the attribute gameOutcome accordingly. Use 'isWinner()'
                 * below for your implementation
                 */
                if (getPlayerCar().isCrunched()) {
                    gameOutcome = GameOutcome.LOST;
                    audioPlayer.playPlayerDeathSound();
                } else if (isWinner()) {
                    if (bossSpawned) {
                        gameOutcome = GameOutcome.WON;
                    } else {
                        System.out.println("Boss Spawned");
                        cars.add(new BossCar(size));
                        bossSpawned = true;
                        audioPlayer.playBossSpawnSound();
                    }
                }
            }
        }
    }

    /**
     * If all other cars are crunched, the player wins.
     *
     * @return true if the game is over and the player won, false otherwise
     */
    private boolean isWinner() {
        for (Car car : getCars()) {
            if (car.isHostile() && !car.isCrunched()) {
                return false;
            }
        }
        return true;
    }

    private void incrementScore(Car winner) {
        if (winner == this.player.getCar()) {
            player.setScore(player.getScore() + 1);
        }
    }

    private void printWinner(Car winner) {
        if (winner == this.player.getCar()) {
            System.out.println("The player's car won the collision!");
        } else if (winner != null) {
            System.out.println(winner.getClass().getSimpleName() + " won the collision!");
        } else {
            System.err.println("Winner car was null!");
        }
    }
}
