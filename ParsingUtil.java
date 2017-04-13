import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;

public class ParsingUtil {

    public static String[] parseLemmas(String filePath ){
        File lemmasFile = new File(filePath);
        Set<String> words = new TreeSet<>();
        try {
            int  k = 0;
            Scanner sc = new Scanner(lemmasFile);
            while (sc.hasNextLine()){
                k++;
                String currentLine = sc.nextLine();
                String[] wordsLine = currentLine.split(" ");
                if(wordsLine[3].equals("adv") || wordsLine[3].equals("v")|| wordsLine[3].equals("a")|| wordsLine[3].equals("n")){
                    words.add(wordsLine[2]);
                }
            }
            //System.out.println(k);
            sc.close();
            return words.toArray(new String[0]);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}