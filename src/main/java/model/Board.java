package model;

import model.enums.Color;
import model.enums.Type;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private Piece[][] board;

    public Board() {
        this.board = new Piece[8][8];
        initBoard();
    }

    private void initBoard(){
        for(int i = 0; i < 8; i++){
            for(int j = 0; j < 8; j++){
                if((i + j) % 2 != 0){
                    if (i < 3)
                        board[i][j] = new Piece(Color.BLACK, Type.SIMPLE);
                    else if (i > 4)
                        board[i][j] = new Piece(Color.WHITE, Type.SIMPLE);
                }
            }
        }
    }

    public Piece getPiece(int row, int column){
        return board[row][column];
    }

    private boolean inBounds(int row, int column){
        return row >= 0 && row < 8 && column >= 0 && column < 8;
    }

    private List<Move> getCaptures(int row, int col, Piece piece, int[] dirs) {
        List<Move> captures = new ArrayList<>();

        for (int dr : new int[]{-1, 1}) {
            for (int dc : new int[]{-1, 1}) {
                int midRow = row + dr;
                int midCol = col + dc;
                int landRow = row + 2 * dr;
                int landCol = col + 2 * dc;

                if (!inBounds(midRow, midCol) || !inBounds(landRow, landCol)) continue;

                Piece middle = board[midRow][midCol];
                if (middle == null) continue;
                if (middle.getColor() == piece.getColor()) continue;
                if (board[landRow][landCol] != null) continue;

                captures.add(Move.builder()
                        .fromRow(row).fromColumn(col)
                        .toRow(landRow).toColumn(landCol)
                        .isCapture(true)
                        .capturedRow(midRow)
                        .capturedColumn(midCol)
                        .build());
            }
        }

        return captures;
    }

    private int[] getDirections(Piece piece) {
        if (piece.getType() == Type.KING) {
            return new int[]{-1, 1};
        }
        return piece.getColor() == Color.WHITE ? new int[]{-1} : new int[]{1};
    }

    public List<Move> getValidMoves(int row, int col) {
        List<Move> moves = new ArrayList<>();
        Piece piece = board[row][col];

        if (piece == null) return moves;

        if (piece.getType() == Type.KING) {
            List<Move> captures = getKingCaptures(row, col, piece);
            if (!captures.isEmpty()) return captures;
            return getKingMoves(row, col);
        }

        int[] dirs = getDirections(piece);
        List<Move> captures = getCaptures(row, col, piece, dirs);
        if (!captures.isEmpty()) return captures;

        for (int dir : dirs) {
            int newRow = row + dir;
            if (inBounds(newRow, col - 1) && board[newRow][col - 1] == null) {
                moves.add(Move.builder().fromRow(row).fromColumn(col)
                        .toRow(newRow).toColumn(col - 1).isCapture(false).build());
            }
            if (inBounds(newRow, col + 1) && board[newRow][col + 1] == null) {
                moves.add(Move.builder().fromRow(row).fromColumn(col)
                        .toRow(newRow).toColumn(col + 1).isCapture(false).build());
            }
        }

        return moves;
    }


    public void makeMove(Move move) {
        Piece piece = board[move.getFromRow()][move.getFromColumn()];

        board[move.getToRow()][move.getToColumn()] = piece;
        board[move.getFromRow()][move.getFromColumn()] = null;

        if (move.isCapture()) {
            board[move.getCapturedRow()][move.getCapturedColumn()] = null;
        }

        if (piece.getType() == Type.SIMPLE) {
            if (piece.getColor() == Color.WHITE && move.getToRow() == 0) {
                piece.promote();
            }
            if (piece.getColor() == Color.BLACK && move.getToRow() == 7) {
                piece.promote();
            }
        }
    }

    public List<Move> getAllForcedCaptures(model.enums.Color color) {
        List<Move> captures = new ArrayList<>();
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor() == color) {
                    List<Move> moves = getValidMoves(row, col);
                    if (!moves.isEmpty() && moves.get(0).isCapture()) {
                        captures.addAll(moves);
                    }
                }
            }
        }
        return captures;
    }
    private List<Move> getKingMoves(int row, int col) {
        List<Move> moves = new ArrayList<>();
        for (int dr : new int[]{-1, 1}) {
            for (int dc : new int[]{-1, 1}) {
                int r = row + dr;
                int c = col + dc;
                while (inBounds(r, c) && board[r][c] == null) {
                    moves.add(Move.builder().fromRow(row).fromColumn(col)
                            .toRow(r).toColumn(c).isCapture(false).build());
                    r += dr;
                    c += dc;
                }
            }
        }
        return moves;
    }

    private List<Move> getKingCaptures(int row, int col, Piece piece) {
        List<Move> captures = new ArrayList<>();
        for (int dr : new int[]{-1, 1}) {
            for (int dc : new int[]{-1, 1}) {
                int r = row + dr;
                int c = col + dc;
                while (inBounds(r, c) && board[r][c] == null) {
                    r += dr;
                    c += dc;
                }
                if (!inBounds(r, c)) continue;
                Piece target = board[r][c];
                if (target.getColor() == piece.getColor()) continue;

                int landR = r + dr;
                int landC = c + dc;
                while (inBounds(landR, landC) && board[landR][landC] == null) {
                    captures.add(Move.builder().fromRow(row).fromColumn(col)
                            .toRow(landR).toColumn(landC)
                            .isCapture(true)
                            .capturedRow(r).capturedColumn(c)
                            .build());
                    landR += dr;
                    landC += dc;
                }
            }
        }
        return captures;
    }
}
