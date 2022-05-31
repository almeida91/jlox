package jlox.interpreter;

import jlox.lexer.Token;
import lombok.Getter;

public class InterpreterException extends RuntimeException {

    @Getter
    private Token token;

    public InterpreterException(Token token, String message) {
        super(message);
        this.token = token;
    }
}
