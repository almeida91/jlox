package jlox.interpreter;

import jlox.lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Object> values = new HashMap<>();

    public void define(String name, Object value) {
        values.put(name, value);
    }

    Object get(Token name) {
        String lexeme = name.getLexeme();

        if (values.containsKey(lexeme)) {
            return values.get(lexeme);
        }

        throw new InterpreterException(name, String.format("Undefined variable '%s'.", lexeme));
    }
}
