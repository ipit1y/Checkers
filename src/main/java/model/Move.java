package model;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Move {
    private int fromRow;
    private int fromColumn;
    private int toRow;
    private int toColumn;
    private boolean isCapture;
    private int capturedRow;
    private int capturedColumn;

}
