/* Dyonne Maxime D. Macalino, CMPILER S12
 * Final Exam - LL1 Parser
 * */
import java.io.*;
import java.net.URL;
import java.util.*;

/**
 * Scanner class for processing
 */
class Scanner {
    ArrayList<Token> process (String line){
        String[] temp = line.split("");
        ArrayList<String> words = new ArrayList<>();
        for (String l: temp){
            if(!l.equals(" ")){
                words.add(l);
            }
        }
        ArrayList<Token> tokenList = new ArrayList<>();
        for (String w: words) {
            if(!w.equals("")){
                tokenList.add(new Token(w));
            }
        }
        return tokenList;
    }
}

/**
 * Main class which is the entry class file for this project
 */
public class Main {
    /**
     * function to read grammar.txt
    */
    public static HashMap<String, Rule> fileReaderGrammar(HashMap<String, Rule> rules) throws IOException {
        /* read the grammar.txt line by line */
        URL path = Main.class.getResource("grammar.txt");
        File f = new File(path.getFile());
        BufferedReader reader = new BufferedReader(new FileReader(f));
        try {
            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
                line = reader.readLine();
            }
            rules = Parser.createRuleMap(stringBuilder.toString());
        } finally {
            reader.close();
        }
        return rules;
    }

    /**
     * function to read input.txt
    */
    public static void fileReader(ArrayList<Token> tokenList) throws IOException {
        /* read the input.txt line by line */
        URL path = Main.class.getResource("input.txt");
        File f = new File(path.getFile());
        BufferedReader reader = new BufferedReader(new FileReader(f));
        try {
            StringBuilder sb = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = reader.readLine();
            }
            tokenList.addAll(LexicalAnalyzer.process(sb.toString()));
        }
        finally {
            reader.close();
        }
    }

    /**
     * main function
     */
    public static void main(String[] args)throws Exception {
        ArrayList<Token> tokenList = new ArrayList<>();
        fileReader(tokenList); //read input
        InputStream inputStream = new FileInputStream("src/input.txt");
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        System.out.println("Parsing complete!");
        FileWriter fileWriter = new FileWriter("output.txt");
        String line;
        Scanner scanner = new Scanner();
        StringBuilder output = new StringBuilder();
        Parser parser = new Parser();
        while((line = bufferedReader.readLine()) != null){
            ArrayList<Token> tokens = scanner.process(line);
            output.append(line);
            String result = parser.parse(tokens);
            output.append(" - ").append(result).append("\n");
        }
        output = new StringBuilder(output.toString().trim());
        System.out.println(output);
        fileWriter.write(output.toString());
        inputStream.close();
        fileWriter.close();
    }
}

