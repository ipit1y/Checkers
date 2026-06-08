package model;

import lombok.Getter;
import model.enums.Color;
import model.enums.GameStatus;

@Getter
public class GameState {
    private Color currentPlayer;
    private GameStatus status;

    public GameState() {
        currentPlayer = Color.WHITE;
        status = GameStatus.IN_PROGRESS;
    }

    public void switchPlayer() {
        currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public void checkWinCondition(Board board) {
        boolean whiteHasMoves = hasAnyMoves(board, Color.WHITE);
        boolean blackHasMoves = hasAnyMoves(board, Color.BLACK);

        if (!whiteHasMoves) {
            status = GameStatus.BLACK_WINS;
        } else if (!blackHasMoves) {
            status = GameStatus.WHITE_WINS;
        }
    }

    private boolean hasAnyMoves(Board board, Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece != null && piece.getColor() == color) {
                    if (!board.getValidMoves(row, col).isEmpty()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}