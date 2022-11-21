import com.fathzer.soft.javaluator.DoubleEvaluator;
// External library and class, explained later
import java.io.*;
import java.util.ArrayList;

public class TwentyFourWriter {
    private final File file;
    private final String difficulty;
    public static final String EASY_DIFF = "Easy";
    public static final String MEDIUM_DIFF = "Medium";
    public static final String HARD_DIFF = "Hard";

    public TwentyFourWriter (String difficulty) {
        if (difficulty.equals(EASY_DIFF) || difficulty.equals(MEDIUM_DIFF) || difficulty.equals(HARD_DIFF)) {
            this.difficulty = difficulty;
        } else {
            throw new IllegalArgumentException("Only 'Easy', 'Medium', or 'Hard' allowed");
        }
        file = new File("TwentyFourNumbers" + difficulty + ".txt");
        if (!file.exists()) {
            try {
                System.out.println(file.createNewFile());
                System.out.println("File created!!! - " + file.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeNums();
    }

    private static void removeDuplicates (ArrayList<ArrayList<Double>> list) {
        ArrayList<ArrayList<Double>> currListPerms;
        for (int i = 0; i < list.size()-1; i++) {
            currListPerms = getAllPerms(list.get(i));
            for (ArrayList<Double> currListPerm : currListPerms) {
                for (int k = (i + 1); k < list.size(); k++) {
                    if (currListPerm.equals(list.get(k))) {
                        list.remove(k);
                        k = k - 1;
                    }
                }
            }
        }
        System.out.println("Duplicates removed!");
    }

    private void writeNums() {
        try (RandomAccessFile raf = new RandomAccessFile(file,"rws")) {
            ArrayList<ArrayList<Double>> allNums = calcNums();
            ArrayList<Double> currNumArray;
            String currNumArrayString;
            removeDuplicates(allNums);
            for (ArrayList<Double> allNum : allNums) {
                currNumArray = allNum;
                currNumArrayString = "";
                for (int j = 0; j < currNumArray.size(); j++) {
                    if (j != currNumArray.size() - 1) {
                        currNumArrayString = currNumArrayString + currNumArray.get(j) + ",";
                    } else {
                        currNumArrayString = currNumArrayString + currNumArray.get(j) + "\n";
                    }
                }
                raf.write(currNumArrayString.getBytes());
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public static void fixLineLength(String fileName, String newFileName) {
        // Default line length that works with getLine method
        fixLineLength(23,fileName,newFileName);
    }
    
    private static void fixLineLength(int lineLength, String fileName, String newFileName) {
        File file = new File(fileName);
        try (
                RandomAccessFile raf1 = new RandomAccessFile(file, "rws");
                RandomAccessFile raf2 = new RandomAccessFile(newFileName, "rws")
        ) {
            raf1.seek(0);
            String line = raf1.readLine();
            ArrayList<String> linesOfFile = new ArrayList<>();
            while (line != null) {
                String newLine = line;
                while (newLine.length()<lineLength) {
                    newLine = newLine + " ";
                }
                newLine = newLine + "\n";
                System.out.println(line);
                System.out.println("new line: " + newLine);
                linesOfFile.add(newLine);
                line = raf1.readLine();
            }
            File newFile = new File(newFileName);
            System.out.println(newFile.createNewFile());

            for (String s : linesOfFile) {
                raf2.write(s.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getLine (int lineNumber, File file) throws IOException {
        return getLine(lineNumber,file.getAbsolutePath());
    }

    public static String getLine (int lineNumber, String filePath) throws IOException {
        // https://stackoverflow.com/questions/5200187/convert-inputstream-to-bufferedreader
        BufferedReader br = new BufferedReader(new InputStreamReader(GameFrame.class.getResourceAsStream(filePath)));
        for (int i = 0; i < lineNumber; i++) {
            br.readLine();
        }
        //raf.seek(lineNumber* 24L);
        return (br.readLine().trim());
    }

    private ArrayList<ArrayList<Double>> calcNums() {
        char[] symbols;
        int cap;
        double difference;
        switch (difficulty) {
            case EASY_DIFF -> {
                cap = 10;
                difference = 1;
                symbols = new char[2];
                assignSymbols(symbols, EASY_DIFF);
            }
            case MEDIUM_DIFF -> {
                cap = 15;
                difference = 1;
                symbols = new char[3];
                assignSymbols(symbols, MEDIUM_DIFF);
            }
            default -> { // HARD_DIFF
                cap = 9;
                difference = 0.5;
                symbols = new char[4];
                assignSymbols(symbols, HARD_DIFF);
            }
        }
        // This code makes it incredibly easy to make changes depending on my client's needs.
        // This is what he thought was appropriate for each difficulty.

        ArrayList<Double> currNumbers = new ArrayList<>();
        ArrayList<ArrayList<Double>> currPerms;
        ArrayList<Double> currPerm;
        ArrayList<ArrayList<Double>> twentyFourCombinations = new ArrayList<>();
        for (double a = 1; a <= cap; a = a + difference) {
            for (double b = 1; b <= cap; b = b + difference) {
                for (double c = 1; c <= cap; c = c + difference) {
                    for (double d = 1; d <= cap; d = d + difference) {
                        currNumbers.clear();
                        currNumbers.add(a);
                        currNumbers.add(b);
                        currNumbers.add(c);
                        currNumbers.add(d);
                        currPerms = getAllPerms(currNumbers);
                        for (ArrayList<Double> perm : currPerms) {
                            currPerm = perm;
                            for (int j = 0; j <= 10; j++) {
                                if (equalsTwentyFour(j, symbols, currPerm)) {
                                    printDoubleArray(currPerm);
                                    if ((a + b + c + d) == 24) {
                                        // Will only add + only cards if the difficulty is Easy
                                        if (difficulty.equals("Easy")) {
                                            twentyFourCombinations.add(currPerm);
                                        }
                                    } else {
                                        twentyFourCombinations.add(currPerm);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Nums calculated!");
        return twentyFourCombinations;
    }

    private static void printDoubleArray(ArrayList<Double> list) {
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size()-1) {
                System.out.print(list.get(i) + " ");
            } else {
                System.out.print(list.get(i) + "\n");
            }
        }
    }
    
    public static String getSolution(double a, double b, double c, double d, String difficulty) {
        // This is an extension that my client let me know he wanted after our interview.
        // For when a certain card cannot be solved by any team, I will be
        // using this method to present a sample solution.

        // The reason why this code needs a difficulty parameter for a solution is because each
        // difficulty has access to different symbols ('+','-','*','/').
        // Because of this, when I am using it, I will need a solution which will fit the
        // difficulty of the card (eg no multiplication solutions for Easy difficulty cards.)

        char[] symbols;
        switch (difficulty) {
            case EASY_DIFF -> {
                symbols = new char[2];
                assignSymbols(symbols,EASY_DIFF);
            }
            case MEDIUM_DIFF -> {
                symbols = new char[3];
                assignSymbols(symbols,MEDIUM_DIFF);
            }
            default -> { // HARD_DIFF
                symbols = new char[4];
                assignSymbols(symbols,HARD_DIFF);
            }
        }
        DoubleEvaluator evaluator = new DoubleEvaluator(); // Imported library to convert Strings to mathematical equations
        ArrayList<Double> currNumbers = new ArrayList<>();
        currNumbers.add(a);
        currNumbers.add(b);
        currNumbers.add(c);
        currNumbers.add(d);
        ArrayList<ArrayList<Double>> currPerms = getAllPerms(currNumbers);
        for (ArrayList<Double> currPerm : currPerms) {
            for (int i = 0; i <= 10; i++) {
                if (equalsTwentyFour(i,symbols,currPerm)) {
                    String equation = getEquation(i, currPerm);
                    String newEquation;
                    for (char s1 : symbols) {
                        for (char s2 : symbols) {
                            for (char s3 : symbols) {
                                newEquation = equation.replace("s1", String.valueOf(s1));
                                newEquation = newEquation.replace("s2", String.valueOf(s2));
                                newEquation = newEquation.replace("s3", String.valueOf(s3));
                                if (evaluator.evaluate(newEquation) == 24) {
                                    newEquation = newEquation.replace("+", " + ");
                                    newEquation = newEquation.replace("-", " - ");
                                    newEquation = newEquation.replace("*", " * ");
                                    newEquation = newEquation.replace("/", " / ");
                                    return (newEquation + " = 24");
                                }
                            }
                        }
                    }
                }
            }
        }
        return "Error"; // Should be unreachable since the only cards I will be using this method on will be
        // ones with a guaranteed solution
    }

    private static void assignSymbols(char[] symbols, String difficulty) {
        switch (difficulty) {
            case EASY_DIFF -> {
                symbols[0] = '+';
                symbols[1] = '-';
            }
            case MEDIUM_DIFF -> {
                symbols[0] = '+';
                symbols[1] = '-';
                symbols[2] = '*';
            }
            default -> { // HARD_DIFF
                symbols[0] = '+';
                symbols[1] = '-';
                symbols[2] = '*';
                symbols[3] = '/';
            }
        }
    }

    private static boolean equalsTwentyFour(int bracketCombination, char[] symbols, ArrayList<Double> nums) {
        DoubleEvaluator evaluator = new DoubleEvaluator(); // Imported library to convert Strings to mathematical equations
        String equation = getEquation(bracketCombination, nums);
        String newEquation;
        for (char s1 : symbols) {
            for (char s2 : symbols) {
                for (char s3 : symbols) {
                    newEquation = equation.replace("s1", String.valueOf(s1));
                    newEquation = newEquation.replace("s2", String.valueOf(s2));
                    newEquation = newEquation.replace("s3", String.valueOf(s3));
                    if (evaluator.evaluate(newEquation) == 24) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static String getEquation(int bracketCombination, ArrayList<Double> nums) {
        String equation = "";
        double a = nums.get(0);
        double b = nums.get(1);
        double c = nums.get(2);
        double d = nums.get(3);

        switch (bracketCombination) {
            case 0 -> // (a ? b ? c ? d)
                    equation = "(" + a + "s1" + b + "s2" + c + "s3" + d + ")";
            case 1 -> // (a ? b ? c) ? d
                    equation = "(" + a + "s1" + b + "s2" + c + ")" + "s3" + d;
            case 2 -> // a ? (b ? c ? d)
                    equation = a + "s1" + "(" + b + "s2" + c + "s3" + d + ")";
            case 3 -> // (a ? b) ? (c ? d)
                    equation = "(" + a + "s1" + b + ")" + "s2" + "(" + c + "s3" + d + ")";
            case 4 -> // (a ? b) ? c ? d
                    equation = "(" + a + "s1" + b + ")" + "s2" + c + "s3" + d;
            case 5 -> // a ? (b ? c) ? d
                    equation = a + "s1" + "(" + b + "s2" + c + ")" + "s3" + d;
            case 6 -> // a ? b ? (c ? d)
                    equation = a + "s1" + b + "s2" + "(" + c + "s3" + d + ")";
            case 7 -> // ((a ? b) ? c) ? d
                    equation = "((" + a + "s1" + b + ")" + "s2" + c + ")" + "s3" + d;
            case 8 -> // a ? (b ? (c ? d))
                    equation = a + "s1" + "(" + b + "s2" + "(" + c + "s3" + d + "))";
            case 9 -> // a ? ((b ? c) ? d)
                    equation = a + "s1" + "((" + b + "s2" + c + ")" + "s3" + d + ")";
            case 10 -> // (a ? (b ? c)) ? d
                    equation = "(" + a + "s1" + "(" + b + "s2" + c + "))" + "s3" + d;
        } // These are all the 'legal' bracket combinations for four numbers
        // that give different, unique answers

        return equation;
    }

    private static ArrayList<ArrayList<Double>> getAllPerms(ArrayList<Double> nums) {
        ArrayList<ArrayList<Double>> permList = new ArrayList<>();
        ArrayList<Double> onePerm = new ArrayList<>();
        getPerms(nums,permList,onePerm);
        return permList;
    }

    private static void getPerms(ArrayList<Double> nums, ArrayList<ArrayList<Double>> results, ArrayList<Double> current) {
        if (nums.size() == current.size()) {
            ArrayList<Double> temp = new ArrayList<>(current);
            results.add(temp);
        }
        for (int i = 0; i < nums.size(); i++) {

            // if (!current.contains(nums.get(i)))

            // The above method will not work for this! (I used it originally)
            // Instead of looking for a reference (which is what is needed here),
            // it will compare the actual value of the Doubles.
            // This causes the code to break here for arrays with 2 or more identical numbers.
            // I felt very clever figuring this out. I made my own method which uses
            // '==' for comparison (this looks for reference, not value) instead.

            if (!contains(current,nums.get(i))) {
                current.add(nums.get(i));
                getPerms(nums, results, current);
                current.remove(current.size() - 1);
            }
        }

    }

    public static boolean contains(ArrayList<Double> arr, Double dbl) {
        for(int i = 0; i < arr.size(); i++) {
            if (arr.get(i) == dbl) { // '==' is deliberate!
                return true;
            }
        }
        return false;
    }
}
