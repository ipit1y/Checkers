package ui;

import javafx.scene.control.Alert;
import model.Board;
import model.GameState;
import model.Move;
import model.enums.*;

import java.util.List;

public class GameController {
    private final Board board;
    private final GameState gameState;
    private final BoardView boardView;

    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<Move> validMoves;

    public GameController(Board board, GameState gameState, BoardView boardView) {
        this.board = board;
        this.gameState = gameState;
        this.boardView = boardView;

        boardView.setOnMouseClicked(event -> {
            int col = (int) (event.getX() / 80);
            int row = (int) (event.getY() / 80);
            handleClick(row, col);
        });
    }

    private void handleClick(int row, int col) {
        if (selectedRow != -1) {
            Move move = findMove(row, col);
            if (move != null) {
                board.makeMove(move);
                gameState.checkWinCondition(board);

                if (gameState.getStatus() != GameStatus.IN_PROGRESS) {
                    showWinDialog();
                    return;
                }
                if (move.isCapture()) {
                    List<Move> continuationMoves = board.getValidMoves(move.getToRow(), move.getToColumn());
                    if (!continuationMoves.isEmpty() && continuationMoves.get(0).isCapture()) {
                        selectedRow = move.getToRow();
                        selectedCol = move.getToColumn();
                        validMoves = continuationMoves;
                        boardView.drawWithHighlight(selectedRow, selectedCol, validMoves);
                        return;
                    }
                }

                gameState.switchPlayer();
                clearSelection();
                boardView.draw();
                return;
            }

            clearSelection();
        }

        List<Move> forcedCaptures = board.getAllForcedCaptures(gameState.getCurrentPlayer());

        var piece = board.getPiece(row, col);
        if (piece != null && piece.getColor() == gameState.getCurrentPlayer()) {
            List<Move> moves = board.getValidMoves(row, col);

            if (!forcedCaptures.isEmpty() && (moves.isEmpty() || !moves.get(0).isCapture())) {
                boardView.drawWithForcedHighlight(forcedCaptures);
                return;
            }

            selectedRow = row;
            selectedCol = col;
            validMoves = moves;
            boardView.drawWithHighlight(selectedRow, selectedCol, validMoves);
        } else {
            if (!forcedCaptures.isEmpty()) {
                boardView.drawWithForcedHighlight(forcedCaptures);
            } else {
                boardView.draw();
            }
        }
    }

    private Move findMove(int row, int col) {
        if (validMoves == null) return null;
        return validMoves.stream()
                .filter(m -> m.getToRow() == row && m.getToColumn() == col)
                .findFirst()
                .orElse(null);
    }

    private void clearSelection() {
        selectedRow = -1;
        selectedCol = -1;
        validMoves = null;
    }

    private void showWinDialog() {
        String message = switch (gameState.getStatus()) {
            case WHITE_WINS -> "White wins!";
            case BLACK_WINS -> "Black wins!";
            default -> "Draw!";
        };

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}