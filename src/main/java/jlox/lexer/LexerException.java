package jlox.lexer;

public class LexerException extends RuntimeException {

    private int line;

    public LexerException(int line, String message) {
        super(message);
        this.line = line;
    }
}
