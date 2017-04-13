public class CrossPosition {
    Placement.Direction direction;
    int row;
    int column;
    int charNum;
    public CrossPosition(int row, int column, int charNum, Placement.Direction direction){
        this.direction = direction;
        this.row = row;
        this.column = column;
        this.charNum = charNum;
    }
}