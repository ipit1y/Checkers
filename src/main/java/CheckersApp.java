import javafx.application.Application;
import javafx.stage.Stage;
import ui.MenuView;

public class CheckersApp extends Application {

    @Override
    public void start(Stage stage) {
        MenuView menuView = new MenuView(stage);
        stage.setTitle("Checkers");
        stage.setScene(menuView.getScene());
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}