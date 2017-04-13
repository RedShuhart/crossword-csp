import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.Collections;

int rHeight = 0;
int blockSize;

int columns;
int rows;
int wordsNum;

boolean solved;
void setup(){
  size(900,900);
  rows = 20;
  columns = 20;
  wordsNum = 30;
  
  blockSize = (int) (Math.max(width, height) / Math.max(rows, columns));
  
  char[][] grid = CrosswordSolving.generateEmpty(rows,columns);
  ArrayList<Placement> partialSolution = new ArrayList<Placement>();

  String[] wordArray = ParsingUtil.parseLemmas("D:/lemma.al.txt");
  ArrayList<String> words = new ArrayList<String>(Arrays.asList(wordArray));
  Collections.shuffle(words);
  
  long startTime = System.nanoTime(); 
  //if(CrosswordSolving.solveBacktrack(partialSolution, grid,words,wordsNum)) solved = true;
  
  if(CrosswordSolving.solveForward(partialSolution, grid,words,wordsNum)) solved = true;
  long endTime = System.nanoTime();
  long duration = (endTime - startTime);
  println((float)duration / 1000000.0 );
  
  if(solved){
    drawGrid(grid,blockSize);
  } else {
    print("NO SOLUTION");
  }
  

  
  
  noLoop();
}

void drawGrid(char[][] grid, int blockSize){
    for(int i = 0; i < grid.length; i++){
    for(int j = 0; j < grid[i].length; j++){
      
      if(grid[i][j] == ' '){
        fill(130,130,100);
        noStroke();
        rect(j*blockSize, rHeight, blockSize, blockSize);
      }else {
        fill(255,255,255);
        stroke(2);
        rect(j*blockSize, rHeight, blockSize, blockSize);
        fill(0,0,0);
        textSize(blockSize/1.5);
        textAlign(CENTER, CENTER);
        text(grid[i][j], j*blockSize + blockSize/2, rHeight + blockSize/2);
      }
      
    }
    rHeight += blockSize;
  }
}