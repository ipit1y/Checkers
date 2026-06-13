package ai;

import model.Board;
import model.Move;
import model.enums.Color;

import java.util.List;

public class AIPlayer {

    private final Color color;
    private final Color opponent;
    private static final int DEPTH = 10;

    public AIPlayer(Color color) {
        this.color = color;
        this.opponent = (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public Color getColor() {
        return color;
    }

    public Move getBestMove(Board board) {
        List<Move> moves = board.getAllMovesForColor(color);
        if (moves.isEmpty()) return null;

        Move bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for (Move move : moves) {
            Board copy = board.copy();
            copy.makeMove(move);
            int score = minimax(copy, DEPTH - 1, false,
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int minimax(Board board, int depth, boolean maximizing, int alpha, int beta) {
        if (depth == 0) {
            return evaluate(board);
        }

        Color current = maximizing ? color : opponent;
        List<Move> moves = board.getAllMovesForColor(current);

        if (moves.isEmpty()) {
            return maximizing ? -1000 : 1000;
        }

        if (maximizing) {
            int maxScore = Integer.MIN_VALUE;
            for (Move move : moves) {
                Board copy = board.copy();
                copy.makeMove(move);
                int score = minimax(copy, depth - 1, false, alpha, beta);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, score);
                if (beta <= alpha) break; // відсікання
            }
            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Move move : moves) {
                Board copy = board.copy();
                copy.makeMove(move);
                int score = minimax(copy, depth - 1, true, alpha, beta);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, score);
                if (beta <= alpha) break; // відсікання
            }
            return minScore;
        }
    }

    private int evaluate(Board board) {
        int score = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                var piece = board.getPiece(row, col);
                if (piece == null) continue;

                int value = (piece.getType() == model.enums.Type.KING) ? 50 : 10;

                if (piece.getType() == model.enums.Type.SIMPLE) {
                    if (piece.getColor() == model.enums.Color.BLACK) {
                        value += row;
                    } else {
                        value += (7 - row);
                    }
                }

                if (col >= 2 && col <= 5) {
                    value += 1;
                }

                if (col == 0 || col == 7) {
                    value += 2;
                }

                if (piece.getColor() == color) {
                    score += value;
                } else {
                    score -= value;
                }
            }
        }

        return score;
    }
}