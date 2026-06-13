package ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class TimerView extends Label {

    private int seconds = 0;
    private Timeline timeline;
    private boolean running = false;

    public TimerView() {
        setText("00:00");
        setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            seconds++;
            updateLabel();
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
    }

    public void start() {
        if (!running) {
            timeline.play();
            running = true;
        }
    }

    public void stop() {
        timeline.pause();
        running = false;
    }

    public void reset() {
        stop();
        seconds = 0;
        updateLabel();
    }

    private void updateLabel() {
        int min = seconds / 60;
        int sec = seconds % 60;
        setText(String.format("%02d:%02d", min, sec));
    }

    public int getSeconds() {
        return seconds;
    }
}