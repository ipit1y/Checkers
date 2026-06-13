package ui;

import ai.AIPlayer;
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
    private final TimerView whiteTimer;
    private final TimerView blackTimer;


    private final AIPlayer aiPlayer;

    private int selectedRow = -1;
    private int selectedCol = -1;
    private List<Move> validMoves;


    public GameController(Board board, GameState gameState, BoardView boardView,
                          TimerView whiteTimer, TimerView blackTimer) {
        this(board, gameState, boardView, whiteTimer, blackTimer, null);
    }


    public GameController(Board board, GameState gameState, BoardView boardView,
                          TimerView whiteTimer, TimerView blackTimer, AIPlayer aiPlayer) {
        this.board = board;
        this.gameState = gameState;
        this.boardView = boardView;
        this.whiteTimer = whiteTimer;
        this.blackTimer = blackTimer;
        this.aiPlayer = aiPlayer;

        whiteTimer.start();

        boardView.setOnMouseClicked(event -> {
            int col = (int) (event.getX() / 80);
            int row = (int) (event.getY() / 80);
            handleClick(row, col);
        });
    }

    private void handleClick(int row, int col) {
        if (aiPlayer != null && gameState.getCurrentPlayer() == aiPlayer.getColor()) {
            return;
        }

        if (selectedRow != -1) {
            Move move = findMove(row, col);
            if (move != null) {
                board.makeMove(move);
                gameState.checkWinCondition(board);
                if (gameState.getStatus() != GameStatus.IN_PROGRESS) {
                    stopAllTimers();
                    boardView.draw();
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
                switchTimers();
                clearSelection();
                boardView.draw();

                if (aiPlayer != null && gameState.getCurrentPlayer() == aiPlayer.getColor()) {
                    makeAIMove();
                }
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

    private void makeAIMove() {
        Move move = aiPlayer.getBestMove(board);
        if (move == null) {
            gameState.checkWinCondition(board);
            stopAllTimers();
            showWinDialog();
            return;
        }

        board.makeMove(move);
        gameState.checkWinCondition(board);
        boardView.draw();

        if (gameState.getStatus() != GameStatus.IN_PROGRESS) {
            stopAllTimers();
            showWinDialog();
            return;
        }

        // повторний бій для AI
        if (move.isCapture()) {
            List<Move> continuationMoves = board.getValidMoves(move.getToRow(), move.getToColumn());
            if (!continuationMoves.isEmpty() && continuationMoves.get(0).isCapture()) {
                makeAIMove(); // б'є ще раз
                return;
            }
        }

        gameState.switchPlayer();
        switchTimers();
        boardView.draw();
    }

    private void switchTimers() {
        if (gameState.getCurrentPlayer() == Color.WHITE) {
            blackTimer.stop();
            whiteTimer.start();
        } else {
            whiteTimer.stop();
            blackTimer.start();
        }
    }

    private void stopAllTimers() {
        whiteTimer.stop();
        blackTimer.stop();
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
            case WHITE_WINS -> "White wins! Time: " + formatTime(whiteTimer.getSeconds());
            case BLACK_WINS -> "Black wins! Time: " + formatTime(blackTimer.getSeconds());
            default -> "Draw!";
        };
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Over");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String formatTime(int seconds) {
        return String.format("%02d:%02d", seconds / 60, seconds % 60);
    }
}