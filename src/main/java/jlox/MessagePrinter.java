package jlox;

import jlox.lexer.Token;
import jlox.lexer.TokenType;

public class MessagePrinter {

    public void error(Token token, String message) {
        if (token.getType() == TokenType.EOF) {
            report(token.getLine(), " at end", message);
        } else {
            report(token.getLine(), " at '" + token.getLexeme() + "´", message);
        }
    }

    private void report(int line, String value, String message) {

    }
}
