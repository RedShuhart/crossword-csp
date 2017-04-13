public class Placement {
    enum Direction {ACROSS, DOWN};
    String word;
    Direction direction;
    int[] position;

    public Placement(String word, Direction direction,int row, int col){
        this.position = new int[2];
        this.position[0] = row;
        this.position[1] = col;
        this.word = word;
        this.direction = direction;
    }

}