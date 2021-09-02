package jlox.lexer;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Token {
    private TokenType type;
    private String lexeme;
    private Object literal;
    private int line;

    @Override
    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
