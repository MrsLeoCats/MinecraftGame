package de.tum.in.ase.eist.gameview;

import java.net.URL;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import de.tum.in.ase.eist.Dimension2D;
import de.tum.in.ase.eist.GameBoard;
import de.tum.in.ase.eist.GameOutcome;
import de.tum.in.ase.eist.Point2D;
import de.tum.in.ase.eist.audio.AudioPlayer;
import de.tum.in.ase.eist.car.Car;
import de.tum.in.ase.eist.usercontrol.MouseSteering;
import javafx.application.Platform;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * This class implements the user interface for steering the player car. The
 * user interface is implemented as a Thread that is started by clicking the
 * start button on the tool bar and stops by the stop button.
 */
public class GameBoardUI extends Canvas {

    /**
     * The update period of the game in ms, this gives us 25 fps.
     */
    private static final double HP_BAR_HEIGHT = 4.0;
    private static final int UPDATE_PERIOD = 1000 / 25;
    private static final int DEFAULT_WIDTH = (int) (500 * 1.5);
    private static final int DEFAULT_HEIGHT = (int) (300 * 1.5);
    private static final Dimension2D DEFAULT_SIZE = new Dimension2D(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    private static final String BG_IMAGE_LOC = "background.jpg";
    private static final String INV_SLOT_LOC = "inventory.png";

    private static final int SX = 16;
    private static final int LX = 50;
    private static final int LY = 410;
    private static final int WW = 44;
    private static final int ABC = 390;
    private static final int AA = 140;
    private static final int BV = 380;
    private static final int CD = 32;
    private static final int GD = 144;
    private static final int AB = 384;
    private static final int HD = 24;
    private static final int NL = 0;
    private static final int ONE = 1;

    public static Dimension2D getPreferredSize() {
        return DEFAULT_SIZE;
    }

    /**
     * Timer responsible for updating the game every frame that runs in a separate
     * thread.
     */
    private Timer gameTimer;

    private GameBoard gameBoard;

    private final GameToolBar gameToolBar;

    private MouseSteering mouseSteering;

    private HashMap<String, Image> imageCache;

    private final GameTimer gameScoreTimer = new GameTimer();

    public GameBoardUI(GameToolBar gameToolBar) {
        this.gameToolBar = gameToolBar;
        setup();
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public MouseSteering getMouseSteering() {
        return mouseSteering;
    }

    /**
     * Removes all existing cars from the game board and re-adds them. Player car is
     * reset to default starting position. Renders graphics.
     */
    public void setup() {
        setupGameBoard();
        setupImageCache();
        this.gameToolBar.updateToolBarStatus(false);
        paint();
    }

    private void setupGameBoard() {
        Dimension2D size = getPreferredSize();
        this.gameBoard = new GameBoard(size);
        this.gameBoard.setAudioPlayer(new AudioPlayer());
        widthProperty().set(size.getWidth());
        heightProperty().set(size.getHeight());
        this.mouseSteering = new MouseSteering(this.gameBoard.getPlayerCar());
        this.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent clickEvent) -> {
            this.mouseSteering.mousePressed(clickEvent);
        });
    }

    private void setupImageCache() {
        this.imageCache = new HashMap<>();
        for (Car car : this.gameBoard.getCars()) {
            // Loads the image into the cache
            getImage(car.getIconLocation());
        }
        String playerImageLocation = this.gameBoard.getPlayerCar().getIconLocation();
        getImage(playerImageLocation);
    }

    /**
     * Returns the car's image. If no image is present in the cache, a new image is created.
     *
     * @param carImageFilePath an image file path that needs to be available in the
     *                         resources folder of the project
     */
    private Image getImage(String carImageFilePath) {
        return this.imageCache.computeIfAbsent(carImageFilePath, this::createImage);
    }

    /**
     * Loads the car's image.
     *
     * @param carImageFilePath an image file path that needs to be available in the
     *                         resources folder of the project
     */
    private Image createImage(String carImageFilePath) {
        URL carImageUrl = getClass().getClassLoader().getResource(carImageFilePath);
        if (carImageUrl == null) {
            throw new IllegalArgumentException(
                    "Please ensure that your resources folder contains the appropriate files for this exercise.");
        }
        return new Image(carImageUrl.toExternalForm());
    }

    /**
     * Starts the GameBoardUI Thread, if it wasn't running. Starts the game board,
     * which causes the cars to change their positions (i.e. move). Renders graphics
     * and updates tool bar status.
     */
    public void startGame() {
        if (!this.gameBoard.isRunning()) {
            this.gameBoard.startGame();
            this.gameToolBar.updateToolBarStatus(true);
            startTimer();
            paint();
            gameScoreTimer.startTimer();
        }
    }

    private void startTimer() {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                updateGame();
            }
        };
        if (this.gameTimer != null) {
            this.gameTimer.cancel();
        }
        this.gameTimer = new Timer();
        this.gameTimer.scheduleAtFixedRate(timerTask, UPDATE_PERIOD, UPDATE_PERIOD);
    }

    private void updateGame() {
        if (gameBoard.isRunning()) {
            // updates car positions and re-renders graphics
            this.gameBoard.update();
            // when this.gameBoard.getOutcome() is OPEN, do nothing
            if (this.gameBoard.getGameOutcome() == GameOutcome.LOST) {
                showAsyncAlert("Oh.. you lost. \nTime: " + gameScoreTimer.toReadable());
                this.stopGame();
            } else if (this.gameBoard.getGameOutcome() == GameOutcome.WON) {
                showAsyncAlert("Congratulations! You won!! \nTime: " + gameScoreTimer.toReadable());
                this.stopGame();
            }
            paint();
        }
    }

    /**
     * Stops the game board and set the tool bar to default values.
     */
    public void stopGame() {
        if (this.gameBoard.isRunning()) {
            this.gameBoard.stopGame();
            this.gameToolBar.updateToolBarStatus(false);
            this.gameTimer.cancel();
        }
    }

    /**
     * Render the graphics of the whole game by iterating through the cars of the
     * game board at render each of them individually.
     */
    private void paint() {
        // getGraphicsContext2D().setFill(BACKGROUND_COLOR);
        // getGraphicsContext2D().fillRect(0, 0, getWidth(), getHeight());
        paintBackground();

        for (Car car : this.gameBoard.getCars()) {
            if (car.isCrunched()) {
                continue;
            }
            paintCar(car);
        }
        // render player car
        paintCar(this.gameBoard.getPlayerCar());
        paintScore();
        paintTimer();
        paintInv();
    }



    private void paintScore() {
        int score = gameBoard.getPlayer().getScore();
        getGraphicsContext2D().setFill(Color.BLACK);
        getGraphicsContext2D().setFont(new Font("Arial", SX));
        getGraphicsContext2D().fillText("Kills: " + score, LX, LY);
    }

    private void paintTimer() {
        getGraphicsContext2D().setFill(Color.BLACK);
        getGraphicsContext2D().setFont(new Font("Arial", SX));
        getGraphicsContext2D().fillText("Time: " + gameScoreTimer.toReadable(), WW, ABC);
    }

    private void paintInv() {
        getGraphicsContext2D().drawImage(getImage(INV_SLOT_LOC), AA, BV, CD, CD);
        getGraphicsContext2D().drawImage(getImage(gameBoard.getPlayer().getItem().image()), GD, AB, HD, HD);
    }

    private void paintBackground() {
        getGraphicsContext2D().drawImage(getImage(BG_IMAGE_LOC), NL, NL, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    /**
     * Show image of a car at the current position of the car.
     *
     * @param car to be drawn
     */
    private void paintCar(Car car) {
        Point2D carPosition = car.getPosition();

        Image image = getImage(car.getIconLocation());
        double width = car.getSize().getWidth();

        getGraphicsContext2D().drawImage(image, carPosition.getX(), carPosition.getY(), width, car.getSize().getHeight());

        car.onDraw(getGraphicsContext2D());

        if (car.getMaxHp() <= ONE) {
            return;
        }

        getGraphicsContext2D().setFill(Color.RED);
        getGraphicsContext2D().fillRect(carPosition.getX(), carPosition.getY() + ONE, width, HP_BAR_HEIGHT);
        getGraphicsContext2D().setFill(Color.GREEN);
        getGraphicsContext2D().fillRect(carPosition.getX(), carPosition.getY() + ONE, width * car.getHealthPercent(), HP_BAR_HEIGHT);
        getGraphicsContext2D().setFill(Color.BLACK);
        getGraphicsContext2D().rect(carPosition.getX(), carPosition.getY() + ONE, width, HP_BAR_HEIGHT);
    }

    /**
     * Method used to display alerts in moveCars().
     *
     * @param message you want to display as a String
     */
    private void showAsyncAlert(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(message);
            alert.showAndWait();
            this.setup();
        });
    }
}
