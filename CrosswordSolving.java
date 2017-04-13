import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.Collections;
import java.util.TreeSet;

/**
 * Created by Ivan on 4/9/2017.
 */
public class CrosswordSolving {

    public Placement[] solutionList;
    public char[][] solutionGrid;


    public static CrossPosition[] getCrossPostion(Placement placement, String word){
        ArrayList<CrossPosition> positions = new ArrayList<CrossPosition>();
        char[] c1 = placement.word.toCharArray();
        char[] c2 = word.toCharArray();
        for (int i = 0; i < c1.length; i++) {
            for (int j = 0; j < c2.length; j++) {
                if(c1[i] == c2[j]){
                    if(placement.direction == Placement.Direction.DOWN){
                        positions.add(new CrossPosition(placement.position[0] + i, placement.position[1], j, Placement.Direction.ACROSS));
                    }
                    if(placement.direction == Placement.Direction.ACROSS) {
                        positions.add(new CrossPosition(placement.position[0], placement.position[1] + i, j, Placement.Direction.DOWN));
                    }
                }
            }
        }
        return positions.toArray( new CrossPosition[0]);
    }


    public static boolean checkCell(Position position, char[][] grid){
        if(position.row >= grid.length || position.col >= grid[1].length || position.row < 0 || position.col < 0 ) return true;
        if(grid[position.row][position.col] == ' ') return true;
        return false;
    }

    public static boolean checkNeughbours(ArrayList<Placement> placements, Placement placement, char[][] grid){

        Position[] occupied = getOccupied(placement);
        //System.out.println(placement.word);
        //System.out.println(Arrays.toString(occupied));
        Position[] crossed = getAllCrossings(placements,placement);
        //System.out.println(Arrays.toString(crossed));
        if(placement.direction == Placement.Direction.DOWN){
            for (int i = 0; i < occupied.length; i++) {
                for (int j = 0; j < crossed.length; j++) {
                    if(!(occupied[i].row == crossed[j].row && occupied[i].col == crossed[j].col)){
                        if(!checkCell(new Position(occupied[i].row,occupied[i].col + 1), grid)) return false;
                        if(!checkCell(new Position(occupied[i].row,occupied[i].col - 1), grid)) return false;
                    }
                    if(i == 0 && !checkCell(new Position(occupied[i].row - 1,occupied[i].col), grid)) return false;
                    if(i == occupied.length - 1 && !checkCell(new Position(occupied[i].row + 1,occupied[i].col), grid))return false;
                }

            }
        }
        if(placement.direction == Placement.Direction.ACROSS){
            for (int i = 0; i < occupied.length; i++) {
                for (int j = 0; j < crossed.length; j++) {
                    if(!(occupied[i].row == crossed[j].row && occupied[i].col == crossed[j].col)){
                        if(!checkCell(new Position(occupied[i].row - 1,occupied[i].col), grid)) return false;
                        if(!checkCell(new Position(occupied[i].row + 1,occupied[i].col), grid)) return false;
                    }
                    if(i == 0 && !checkCell(new Position(occupied[i].row ,occupied[i].col - 1), grid)) return false;
                    if(i == occupied.length - 1 && !checkCell(new Position(occupied[i].row,occupied[i].col + 1), grid))return false;
                }

            }
        }
        return true;
    }

    public static Position[] getOccupied(Placement placement){
        ArrayList<Position> positions = new ArrayList<>();
        if(placement.direction == Placement.Direction.DOWN){
            for (int i = 0; i < placement.word.length() ; i++) {
                positions.add(new Position(placement.position[0] + i, placement.position[1]));
            }
            return positions.toArray(new Position[0]);

        }
        if(placement.direction == Placement.Direction.ACROSS){
            for (int i = 0; i < placement.word.length() ; i++) {
                positions.add(new Position(placement.position[0], placement.position[1] + i));
            }
            return positions.toArray(new Position[0]);
        }
        return positions.toArray(new Position[0]);
    }

    public  static Position[] getAllCrossings(ArrayList<Placement> placements, Placement placement ){
        Position[] positionsPlacement = getOccupied(placement);
        ArrayList<Position> positions = new ArrayList<>();
        for (Placement place: placements) {
            Position[] positionsPlace = getOccupied(place);
            for (int i = 0; i < positionsPlace.length ; i++) {
                for (int j = 0; j < positionsPlacement.length; j++) {
                    if(positionsPlace[i].col == positionsPlacement[j].col && positionsPlace[i].row == positionsPlacement[j].row) positions.add(positionsPlacement[j]);
                }
            }
        }
        return positions.toArray(new Position[0]);
    }

    public static boolean checkGrid(char[][] array, String word, CrossPosition crossPosition){

        if (crossPosition.direction == Placement.Direction.DOWN) {
           if(!(crossPosition.row + word.length() - crossPosition.charNum > array.length) && !(crossPosition.row - crossPosition.charNum < 0)){
               for (int i = 0; i < word.length() ; i++) {
                   if(array[crossPosition.row - crossPosition.charNum + i][crossPosition.column] != ' ' && array[crossPosition.row - crossPosition.charNum + i][crossPosition.column] != word.charAt(i)){
                       return false;
                   }
               }
               return true;
           }

        }
        if (crossPosition.direction == Placement.Direction.ACROSS) {
           if(!(crossPosition.column + word.length() - crossPosition.charNum > array[1].length) && !(crossPosition.column - crossPosition.charNum < 0)){
               for (int i = 0; i < word.length() ; i++) {
                   if(array[crossPosition.row][crossPosition.column - crossPosition.charNum + i] != ' ' && array[crossPosition.row][crossPosition.column - crossPosition.charNum + i]  != word.charAt(i)){
                       return false;
                   }
               }
               return true;
           }
        }
        return  false;
    }

    public static void addToGrid(char[][] array, Placement placement){
        if(placement.direction == Placement.Direction.DOWN){
            for (int i = 0; i < placement.word.length(); i++) {
                array[placement.position[0] + i][placement.position[1]] = placement.word.charAt(i);
            }
        }
        if (placement.direction == Placement.Direction.ACROSS){
            for (int i = 0; i < placement.word.length(); i++) {
                array[placement.position[0]][placement.position[1] + i] = placement.word.charAt(i);
            }
        }
    }

    public  static  void removeFromGrid(char[][] array, ArrayList<Placement> placements, Placement placement){
        Position[] crossedPositions = getAllCrossings(placements,placement);
        Position[] ocupied = getOccupied(placement);
        boolean remove;
        for (int i = 0; i < ocupied.length; i++) {
            remove = true;
            for (int j = 0; j < crossedPositions.length; j++) {
                if(crossedPositions[j].col == ocupied[i].col && crossedPositions[j].row == ocupied[i].row) remove = false;
            }
            if(remove) array[ocupied[i].row][ocupied[i].col] = ' ';
        }

    }

    public static char[][] generateEmpty(int rows, int columns){
        char[][] grid = new char[rows][columns];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j <grid[i].length ; j++) {
                grid[i][j] = ' ';
            }
        }
        return grid;
    }

    public static void drawGrid(char[][] grid){
        for (int i = 0; i <grid.length ; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
    }

    
     public static ArrayList<String>  getComplement(ArrayList<String> words, ArrayList<Placement> placements){
        ArrayList<String> complenment = new ArrayList<>();
        ArrayList<String> newWords = new ArrayList<>(words);
        for (Placement placement: placements) {
            complenment.add(placement.word);
        }
        newWords.removeAll(complenment);
        return newWords;
    }
    
    public static Placement getRandom(char[][] grid, ArrayList<String> words, int row, int col){
        ArrayList<String> newWords = new ArrayList<>(words);
        Collections.shuffle(newWords);
        for (String word: newWords) {
            if(placeable(grid,word, row, col)) return new Placement(word, Placement.Direction.ACROSS, row, col);
        }
        return null;
    }

    public static boolean placeable(char[][] grid, String word, int row, int col){
        return (col + word.length() < grid[1].length) && row < grid.length;
    }


    public static boolean solveBacktrack(ArrayList<Placement> partialSolution, char[][] grid, ArrayList<String> initialDomain, int num){
        ArrayList<String> words = getComplement(initialDomain, partialSolution);
        //System.out.println(words.size());
        if(partialSolution.size() == num){
            return true;
        }
        if(partialSolution.isEmpty()){
            Placement placement = getRandom(grid,words,0,0);
            partialSolution.add(placement);
            addToGrid(grid, placement);
           // drawGrid(grid);
            words.remove(placement.word);
            if (solveBacktrack(partialSolution, grid, words, num)) return true;
            //drawGrid(grid);
            partialSolution.remove(partialSolution.size() - 1);
            removeFromGrid(grid, partialSolution, placement);
        } else{
        for (int i = 0; i < words.size(); i++) {
            for (int j = 0; j < partialSolution.size(); j++) {
                CrossPosition[] crossPositions = getCrossPostion(partialSolution.get(j), words.get(i));
                if(crossPositions.length > 0){
                    for (CrossPosition crossPosition: crossPositions) {
                        if(checkGrid(grid, words.get(i), crossPosition)){
                            if(crossPosition.direction == Placement.Direction.DOWN){
                                Placement placement = new Placement(words.get(i), Placement.Direction.DOWN, crossPosition.row - crossPosition.charNum, crossPosition.column );
                                if(checkNeughbours(partialSolution,placement,grid)) {
                                    partialSolution.add(placement);
                                    addToGrid(grid, placement);
                                    //drawGrid(grid);
                                    //System.out.println(partialSolution.size());
                                    words.remove(i);
                                    if (solveBacktrack(partialSolution, grid, initialDomain, num)) return true;
                                    //drawGrid(grid);
                                    partialSolution.remove(partialSolution.size() - 1);
                                    removeFromGrid(grid, partialSolution, placement);
                                }

                            }
                            if(crossPosition.direction == Placement.Direction.ACROSS){
                                Placement placement = new Placement(words.get(i), Placement.Direction.ACROSS, crossPosition.row, crossPosition.column - crossPosition.charNum );
                                if(checkNeughbours(partialSolution,placement,grid)) {
                                    partialSolution.add(placement);
                                    addToGrid(grid, placement);
                                    //drawGrid(grid);
                                    //System.out.println(partialSolution.size());
                                    words.remove(i);
                                    if (solveBacktrack(partialSolution, grid, initialDomain, num)) return true;
                                    //drawGrid(grid);
                                    partialSolution.remove(partialSolution.size() - 1);
                                    removeFromGrid(grid, partialSolution, placement);
                                }

                            }
                        }

                    }
                }
            }
        }
       }
        return false;
    }
    
        public static boolean solveForward(ArrayList<Placement> partialSolution, char[][] grid, ArrayList<String> domain, int num){
        //drawGrid(grid);
        do{
            if(partialSolution.size() == num) return true;

            Placement toAdd = null;
            if(partialSolution.isEmpty()){
                toAdd = getRandom(grid,domain,0,0);
            } else{
              loop:
                for (int i = 0; i < domain.size(); i++) {
                    for (int j = 0; j < partialSolution.size(); j++) {
                        CrossPosition[] crossPositions = getCrossPostion(partialSolution.get(j), domain.get(i));
                        if (crossPositions.length > 0) {
                            for (CrossPosition crossPosition : crossPositions) {
                                if (checkGrid(grid, domain.get(i), crossPosition)) {
                                    if (crossPosition.direction == Placement.Direction.DOWN) {
                                        Placement placement = new Placement(domain.get(i), Placement.Direction.DOWN, crossPosition.row - crossPosition.charNum, crossPosition.column);
                                        if (checkNeughbours(partialSolution, placement, grid)) {
                                            toAdd = placement;
                                            break loop;
                                        }
                                    }
                                    if (crossPosition.direction == Placement.Direction.ACROSS) {
                                        Placement placement = new Placement(domain.get(i), Placement.Direction.ACROSS, crossPosition.row, crossPosition.column - crossPosition.charNum);
                                        if (checkNeughbours(partialSolution, placement, grid)) {
                                            toAdd = placement;
                                            break loop;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if(toAdd == null) return false;

            ArrayList<String> newDomain = updateDomain(partialSolution,grid,toAdd,domain);

            if(!newDomain.isEmpty()){
                if(solveForward(partialSolution,grid,newDomain, num)) return true;
                removeFromGrid(grid,partialSolution,partialSolution.get(partialSolution.size() -1));
                partialSolution.remove(partialSolution.size() - 1);
            }
        }while (!domain.isEmpty());
        return false;
    }

    public static  ArrayList<String> updateDomain(ArrayList<Placement> partialSolution, char[][] grid, Placement toAdd, ArrayList<String> domain ){
        partialSolution.add(toAdd);
        addToGrid(grid, toAdd);
        domain.remove(toAdd.word);

        TreeSet<String> collector = new TreeSet<>();
        ArrayList<String> newDomain = new ArrayList<>();

        for (int i = 0; i < domain.size(); i++) {
            for (int j = 0; j < partialSolution.size(); j++) {
                CrossPosition[] crossPositions = getCrossPostion(partialSolution.get(j), domain.get(i));
                if (crossPositions.length > 0) {
                    for (CrossPosition crossPosition : crossPositions) {
                        if (checkGrid(grid, domain.get(i), crossPosition)) {
                            if (crossPosition.direction == Placement.Direction.DOWN) {
                                Placement placement = new Placement(domain.get(i), Placement.Direction.DOWN, crossPosition.row - crossPosition.charNum, crossPosition.column);
                                if (checkNeughbours(partialSolution, placement, grid)) {
                                    collector.add(placement.word);
                                }
                            }
                            if (crossPosition.direction == Placement.Direction.ACROSS) {
                                Placement placement = new Placement(domain.get(i), Placement.Direction.ACROSS, crossPosition.row, crossPosition.column - crossPosition.charNum);
                                if (checkNeughbours(partialSolution, placement, grid)) {
                                    collector.add(placement.word);
                                }
                            }
                        }
                    }
                }
            }
        }

        newDomain.addAll(collector);
        //Collections.shuffle(newDomain);
        return newDomain;
    }



}