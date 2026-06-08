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
        new GameController(board, gameState, boardView);

        javafx.scene.layout.StackPane root = new javafx.scene.layout.StackPane(boardView);
        stage.setScene(new Scene(root));
    }
}