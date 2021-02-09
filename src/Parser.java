/* Dyonne Maxime D. Macalino, CMPILER S12
 * Final Exam - LL1 Parser
 * */
import java.util.*;

/**
 * Rule class which represents the different rules
 */
class Rule {
    public String LHS;
    public List<String> RHS = new ArrayList<>();
}

/**
 * ParseTable class which represents the parsing table for this project
 */
class ParseTable {
    private HashMap<String, HashMap> table;
    private static ParseTable instance = null;
    /**
     * function to get parse table instance
     */
    public static ParseTable getInstance() {
        if (instance == null)
            instance = new ParseTable();
        return instance;
    }
    /**
     * parse table class
     */
    private ParseTable(){
        this.table = new HashMap<>();
        this.initialize();
    }
    /**
     * function to get initialize parse table
     */
    private void initialize() {
        HashMap<String, String> S = new HashMap<>();
        S.put("EPSILON", "C N");
        S.put("LPAREN", "C N");
        S.put("ALPHANUM", "C N");

        HashMap<String, String> C = new HashMap<>();
        C.put("EPSILON", "EPSILON");
        C.put("LPAREN", "A");
        C.put("ALPHANUM", "A");

        HashMap<String, String> N = new HashMap<>();
        N.put("UNION", "UNION C N");
        N.put("LPAREN", "A N");
        N.put("RPAREN", "");
        N.put("ALPHANUM", "A N");
        N.put("END", "");

        HashMap<String, String> A = new HashMap<>();
        A.put("LPAREN", "P B");
        A.put("ALPHANUM", "P B");

        HashMap<String, String> P = new HashMap<>();
        P.put("LPAREN", "LPAREN S RPAREN");
        P.put("ALPHANUM", "ALPHANUM");

        HashMap<String, String> B = new HashMap<>();
        B.put("UNION", "");
        B.put("LPAREN", "");
        B.put("RPAREN", "");
        B.put("ALPHANUM", "");
        B.put("STAR", "STAR");
        B.put("PLUS", "PLUS");
        B.put("QUESTION", "QUESTION");
        B.put("END", "");

        table.put("S", S);
        table.put("C", C);
        table.put("N", N);
        table.put("A", A);
        table.put("P", P);
        table.put("B", B);
    }

    /**
     * function to get productions
     */
    public String getProduction(String rule, String terminal){
        HashMap nonTerm = table.get(rule);
        return (String) nonTerm.get(terminal);
    }

    /**
     * checker for productions
     */
    public boolean isProduction(String rule){
        HashMap temp = table.get(rule);
        return temp != null;
    }
}


/**
 * Parser that implements LL (1) parsing
 */
public class Parser {
    private Stack<String> stack = new Stack<>();

    /**
     * function to create rules and return hashmap of rules
     * @param text String
     */
    public static HashMap<String, Rule> createRuleMap(String text){
        //store rules
        HashMap<String, Rule> rules = new HashMap<>();
        String[] productions = text.split(";\n");

        for (String p: productions) {
            String[] line = p.split(": ");
            Rule R = new Rule();
            R.LHS = line[0];
            if (line[1].contains("|")) {
                String[] prod = line[1].split(" \\| ");
                R.RHS.addAll(Arrays.asList(prod));
            }
            else
                R.RHS.add(line[1]);
            rules.put(line[0], R);
        }
        return rules;
    }

    /**
     * parses tokens
     */
    public String parse(ArrayList<Token> tokens){
        this.stack.clear();
        this.stack.push("$"); //push ending symbol
        this.stack.push("S"); //push starting symbol
        int token_pointer = 0;
        tokens.add(new Token("$"));
        while(!stack.isEmpty() && token_pointer < tokens.size()){
            if(tokens.get(token_pointer).getTokenType().equals("ERROR"))
                return "REJECT";
            else if(ParseTable.getInstance().isProduction(stack.peek())){
                String production = ParseTable.getInstance().getProduction(stack.peek(), tokens.get(token_pointer).getTokenType());
                if(production != null)
                    this.expand(production);
                else
                    return "REJECT";
            } else if(stack.peek().equals("")){
                stack.pop();
            } else if(stack.peek().equals("$")){
                if(tokens.get(token_pointer).getLexeme().equals("$")){
                    stack.pop();
                    token_pointer++;
                }
                else{
                    return "REJECT";
                }
            } else{
                if(stack.peek().equals(tokens.get(token_pointer).getTokenType())){
                    stack.pop();
                    token_pointer++;
                } else{
                    return "REJECT";
                }
            }
        }
        if(stack.isEmpty() && token_pointer >= tokens.size())
            return "ACCEPT";
        else
            return "REJECT";
    }

    /**
     * expand productions
     */
    public void expand(String production){
        if(stack.isEmpty())
            return;
        stack.pop();
        String[] temp = production.split(" ");
        for(int i = temp.length-1; i >= 0; i--)
            this.stack.push(temp[i]);
    }
}
