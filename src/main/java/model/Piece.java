package model;
import lombok.AllArgsConstructor;
import lombok.Getter;
import model.enums.Color;
import model.enums.Type;

@Getter
@AllArgsConstructor
public class Piece {
    private Color color;
    private Type type;

    public void promote(){
        this.type = Type.KING;
    }

    public Piece copy() {
        return new Piece(color, type);
    }
}
