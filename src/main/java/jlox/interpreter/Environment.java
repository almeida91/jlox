package jlox.interpreter;

import jlox.lexer.Token;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private Map<String, Object> values = new HashMap<>();

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        String varName = name.getLexeme();

        if (values.containsKey(varName)) {
            return values.get(varName);
        }

        throw new InterpreterException(name, String.format("Undefined variable '%s'.", varName));
    }

    public void put(Token name, Object value) {
        String varName = name.getLexeme();

        if (!values.containsKey(varName)) {
            throw new InterpreterException(name, String.format("Undefined variable '%s'.", varName));
        }

        values.put(varName, value);
    }
}
