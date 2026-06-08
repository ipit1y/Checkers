package ui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.Board;
import model.Move;
import model.Piece;
import model.enums.Type;

import java.util.List;

public class BoardView extends Canvas {
    private static final int TILE_SIZE = 80;
    private static final int BOARD_SIZE = 8;

    private Board board;

    public BoardView(Board board) {
        super(TILE_SIZE * BOARD_SIZE, TILE_SIZE * BOARD_SIZE);
        this.board = board;
        draw();
    }

    public void draw() {
        drawBoard();
        drawPieces();
    }

    private void drawBoard() {
        GraphicsContext gc = getGraphicsContext2D();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if ((row + col) % 2 == 0) {
                    gc.setFill(Color.BEIGE);
                } else {
                    gc.setFill(Color.DARKGREEN);
                }
                gc.fillRect(col * TILE_SIZE, row * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
        }
    }

    private void drawPieces() {
        GraphicsContext gc = getGraphicsContext2D();
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Piece piece = board.getPiece(row, col);
                if (piece == null) continue;

                double x = col * TILE_SIZE;
                double y = row * TILE_SIZE;
                double padding = 10;

                if (piece.getColor() == model.enums.Color.WHITE) {
                    gc.setFill(Color.WHITE);
                } else {
                    gc.setFill(Color.BLACK);
                }
                gc.fillOval(x + padding, y + padding,
                        TILE_SIZE - 2 * padding, TILE_SIZE - 2 * padding);

                if (piece.getType() == Type.KING) {
                    gc.setFill(Color.GOLD);
                    gc.fillOval(x + padding * 2, y + padding * 2,
                            TILE_SIZE - 4 * padding, TILE_SIZE - 4 * padding);
                }
            }
        }
    }
    public void drawWithHighlight(int selRow, int selCol, List<Move> moves) {
        GraphicsContext gc = getGraphicsContext2D();
        drawBoard();

        gc.setFill(Color.YELLOW);
        gc.fillRect(selCol * TILE_SIZE, selRow * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        gc.setFill(Color.color(0, 1, 0, 0.4));
        for (Move move : moves) {
            gc.fillRect(move.getToColumn() * TILE_SIZE, move.getToRow() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);
        }

        drawPieces();
    }
    public void drawWithForcedHighlight(List<Move> forcedCaptures) {
        GraphicsContext gc = getGraphicsContext2D();
        drawBoard();

        gc.setFill(Color.color(1, 0.5, 0, 0.6)); // помаранчевий
        for (Move move : forcedCaptures) {
            gc.fillRect(move.getFromColumn() * TILE_SIZE, move.getFromRow() * TILE_SIZE,
                    TILE_SIZE, TILE_SIZE);
        }

        drawPieces();
    }
}