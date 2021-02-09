/* Dyonne Maxime D. Macalino, CMPILER S12
 * Final Exam - LL1 Parser
 * */
import javafx.util.Pair;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Token class which holds the type of the parsed token
 */
class Token {
    /**
     * Types for this lexical analyzer to analyze
     */
    enum TokenType {
        EPSILON,
        LPAREN,
        RPAREN,
        QUEST,
        UNION,
        ALPHANUM,
        PLUS,
        STAR,
        END,
        ERROR
    }

    public TokenType tokenType;
    public String lexeme;
    private static final int s0 = 0;
    private static final int s1 = 1;
    private static final int s2 = 2;
    private static final int s3 = 3;
    private static final int s4 = 4;
    private static final int s5 = 5;
    private static final int s6 = 6;
    private static final int s7 = 7;
    private static final int s8 = 8;
    private static final int s9 = 9;
    private static final int dead = -1;

    private static int state = s0;

    /**
     * Token constructor
     * @param line String
     */
    public Token (String line){
        this.tokenType = identifyToken(line);
        this.lexeme = line;
    }

    /**
     * Returns the token's type in String format
     * @return tokenType
     */
    public String getTokenType() {
        return tokenType.toString();
    }

    /**
     * Returns the token's lexeme
     * @return lexeme
     */
    public String getLexeme() {
        return lexeme;
    }

    /**
     * Initialize token state
     */
    public static void initialize() {
        state = s0;
    }

    /**
     * Returns the corresponding token type
     * @param line String
     */
    public static TokenType identifyToken(String line) {
        initialize();
        for (int i = 0; i < line.length(); i++) {
            char input = line.charAt(i);
            state = matchTransition(state, input);
        }
        switch(state){
            case s1: return TokenType.STAR;
            case s2: return TokenType.PLUS;
            case s3: return TokenType.UNION;
            case s4: return TokenType.QUEST;
            case s5: return TokenType.LPAREN;
            case s6: return TokenType.RPAREN;
            case s7: return TokenType.EPSILON;
            case s8: return TokenType.ALPHANUM;
            case s9: return TokenType.END;
            default:{
                return TokenType.ERROR;
            }
        }
    }

    /**
     * Returns the int value for the transition
     * @param state int
     * @param input char
     */
    static private int matchTransition(int state, char input) {
        switch (state) {
            case s0: switch (input) {
                case '*': return s1;
                case '+': return s2;
                case 'U': return s3;
                case '?': return s4;
                case '(': return s5;
                case ')': return s6;
                case 'E': return s7;
                case '0':
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                case 'a':
                case 'b':
                case 'c':
                case 'd':
                case 'e':
                case 'f':
                case 'g':
                case 'h':
                case 'i':
                case 'j':
                case 'k':
                case 'l':
                case 'm':
                case 'n':
                case 'o':
                case 'p':
                case 'q':
                case 'r':
                case 's':
                case 't':
                case 'u':
                case 'v':
                case 'w':
                case 'x':
                case 'y':
                case 'z': return s8;
                case '$': return s9;
                default: return dead;
            }
            default: return dead;
        }
    }

    @Override
    public String toString() {
        if(this.tokenType == TokenType.ALPHANUM)
            return this.tokenType.toString();
        else
            return this.lexeme;
    }
}

/**
 * Lexical analyzer class
 */
public class LexicalAnalyzer {
    /**
     * Lexically analyzes input, prints out tokens in output, and process to return tokenList
     */
    static ArrayList<Token> process(String sourceCode){
        //delimiter: whitespace
        String[] words = sourceCode.split(" ");
        ArrayList<Token> tokenList = new ArrayList<>();
        String token = "";
        for (String w: words){
            if(!w.isEmpty()) {
                for (int i=0; i<w.length(); i++){
                    if((w.charAt(i) < 48 || w.charAt(i) > 57) && w.charAt(i)!='\n'){
                        if(!token.isEmpty()) {
                            Token t = new Token(token);
                            tokenList.add(t);
                            token = "";
                        }
                        Token t = new Token(w.charAt(i)+"");
                        tokenList.add(t);
                    }
                    else if (w.charAt(i) == '\n'){
                        if(!token.isEmpty()) {
                            Token t = new Token(token);
                            tokenList.add(t);
                            token = "";
                        }
                        Token t = new Token(w.charAt(i)+"");
                        tokenList.add(t);
                    }
                    else{
                        token = token.concat(w.charAt(i)+"");
                    }
                }
            }
        }
        return tokenList;
    }
}

