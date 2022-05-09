package de.tum.in.ase.eist.audio;

import java.net.URL;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class handles the background music played during the game using a JavaFX {@link MediaPlayer}.
 */
public class AudioPlayer implements AudioPlayerInterface {

    private static AudioPlayer instance;

    private static final String BACKGROUND_MUSIC_FILE = "backgroundmusic.wav";
    private static final String CRASH_SOUND_FILE = "Crash.wav";

    /**
     * New Sounds I've implemented
     */
    private static final String TELEPORT_SOUND_FILE = "teleportsound.wav";
    private static final String HITZOMBIE_SOUND_FILE = "hitzombie.wav";
    private static final String HITENDERMAN_SOUND_FILE = "hitenderman.wav";
    private static final String HITSKELETON_SOUND_FILE = "hitskeleton.wav";
    private static final String BOSSSPAWN_SOUND_FILE = "bossspawn.wav";

    private static final String PLAYERDEATH_SOUND_FILE = "playerDeath.wav";

    private static final double TELEPORT_SOUND_VOLUME = 0.5;
    private static final double HITZOMBIE_SOUND_VOLUME = 0.5;
    private static final double HITENDERMAN_SOUND_VOLUME = 0.5;
    private static final double HITSKELETON_SOUND_VOLUME = 0.5;
    private static final double BOSSSPAWN_SOUND_VOLUME = 0.5;
    private static final double ARROW_SOUND_VOLUME = 0.5;

    private static final double PLAYERDEATH_SOUND_VOLUME = 0.5;
    private static final double CRASH_SOUND_VOLUME = 0.5;

    private final MediaPlayer musicPlayer;
    private final AudioClip crashPlayer;
    private final AudioClip teleportPlayer;
    private final AudioClip hitZombiePlayer;
    private final AudioClip hitEndermanPlayer;
    private final AudioClip hitSkeletonPlayer;
    private final AudioClip bossSpawnPlayer;

    private final AudioClip deathPlayer;

    public static AudioPlayer getInstance() {
        return instance;
    }

    /**
     * Constructs a new AudioPlayer by directly loading the background music and
     * crash sound files into a new MediaPlayer / AudioClip.
     */
    public AudioPlayer() {
        instance = this;
        this.musicPlayer = new MediaPlayer(loadAudioFile(BACKGROUND_MUSIC_FILE));
        this.crashPlayer = new AudioClip(convertNameToUrl(CRASH_SOUND_FILE));
        this.teleportPlayer = new AudioClip(convertNameToUrl(TELEPORT_SOUND_FILE));
        this.hitZombiePlayer = new AudioClip(convertNameToUrl(HITZOMBIE_SOUND_FILE));
        this.hitEndermanPlayer = new AudioClip(convertNameToUrl(HITENDERMAN_SOUND_FILE));
        this.hitSkeletonPlayer = new AudioClip(convertNameToUrl(HITSKELETON_SOUND_FILE));
        this.bossSpawnPlayer = new AudioClip(convertNameToUrl(BOSSSPAWN_SOUND_FILE));
        this.deathPlayer = new AudioClip(convertNameToUrl(PLAYERDEATH_SOUND_FILE));
    }

    @Override
    public void playBackgroundMusic() {
        if (isPlayingBackgroundMusic()) {
            return;
        }
        // Loop for the main music sound:
        this.musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        this.musicPlayer.play();
    }

    @Override
    public void stopBackgroundMusic() {
        if (isPlayingBackgroundMusic()) {
            this.musicPlayer.stop();
        }
    }

    @Override
    public boolean isPlayingBackgroundMusic() {
        return MediaPlayer.Status.PLAYING.equals(this.musicPlayer.getStatus());
    }

    @Override
    public void playCrashSound() {
        crashPlayer.play(CRASH_SOUND_VOLUME);
    }

    /**
     * my play sound commands
     */
    public void playTeleportSound() {
        teleportPlayer.play(TELEPORT_SOUND_VOLUME);
    }
    public void playHitZombieSound() {
        hitZombiePlayer.play(HITZOMBIE_SOUND_VOLUME);
    }
    public void playHitEndermanSound() {
        hitEndermanPlayer.play(HITENDERMAN_SOUND_VOLUME);
    }
    public void playHitSkeletonSound() {
        hitSkeletonPlayer.play(HITSKELETON_SOUND_VOLUME);
    }
    public void playBossSpawnSound() {
        bossSpawnPlayer.play(BOSSSPAWN_SOUND_VOLUME);
    }
    public void playPlayerDeathSound() {
        deathPlayer.play(PLAYERDEATH_SOUND_VOLUME);
    }

    private Media loadAudioFile(String fileName) {
        return new Media(convertNameToUrl(fileName));
    }

    private String convertNameToUrl(String fileName) {
        URL musicSourceUrl = getClass().getClassLoader().getResource(fileName);
        if (musicSourceUrl == null) {
            throw new IllegalArgumentException(
                    "Please ensure that your resources folder contains the appropriate files for this exercise.");
        }
        return musicSourceUrl.toExternalForm();
    }

    public static String getBackgroundMusicFile() {
        return BACKGROUND_MUSIC_FILE;
    }

    public static String getCrashSoundFile() {
        return CRASH_SOUND_FILE;
    }
}
