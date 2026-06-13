package ui;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Board;
import model.GameState;

public class MenuView {

    private final Stage stage;

    public MenuView(Stage stage) {
        this.stage = stage;
    }

    public Scene getScene() {
        Text title = new Text("Checkers");
        title.setFont(Font.font(48));

        Button oneVsOne = new Button("1 vs 1");
        oneVsOne.setFont(Font.font(24));
        oneVsOne.setPrefWidth(200);

        Button vsAI = new Button("vs AI");
        vsAI.setFont(Font.font(24));
        vsAI.setPrefWidth(200);
        vsAI.setDisable(true); // поки AI не готовий

        oneVsOne.setOnAction(e -> startGame());

        VBox layout = new VBox(20, title, oneVsOne, vsAI);
        layout.setAlignment(Pos.CENTER);
        layout.setPrefSize(640, 640);

        return new Scene(layout);
    }

    private void startGame() {
        Board board = new Board();
        GameState gameState = new GameState();
        BoardView boardView = new BoardView(board);

        TimerView whiteTimer = new TimerView();
        TimerView blackTimer = new TimerView();

        whiteTimer.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
        blackTimer.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");

        new GameController(board, gameState, boardView, whiteTimer, blackTimer);

        javafx.scene.layout.HBox timerBar = new javafx.scene.layout.HBox();
        timerBar.setStyle("-fx-background-color: #333; -fx-padding: 10;");
        timerBar.setSpacing(20);
        timerBar.setAlignment(javafx.geometry.Pos.CENTER);

        javafx.scene.control.Label whiteLabel = new javafx.scene.control.Label("White: ");
        whiteLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");
        javafx.scene.control.Label blackLabel = new javafx.scene.control.Label("Black: ");
        blackLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        timerBar.getChildren().addAll(whiteLabel, whiteTimer, blackLabel, blackTimer);

        javafx.scene.layout.VBox root = new javafx.scene.layout.VBox(timerBar, boardView);
        stage.setScene(new javafx.scene.Scene(root));
    }
}