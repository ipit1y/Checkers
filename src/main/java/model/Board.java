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

    public List<Move> getAllValidMoves(int row, int column){
        List<Move> moves = new ArrayList<>();
        Piece piece = board[row][column];

        if(piece == null){
            return moves;
        }

        
    }
}
