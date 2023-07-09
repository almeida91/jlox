package jlox.interpreter;

import jlox.lexer.Token;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class Environment {
    private Map<String, Object> values = new HashMap<>();
    private Environment enclosing;

    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    public void define(String name, Object value) {
        values.put(name, value);
    }

    public Object get(Token name) {
        String varName = name.getLexeme();

        if (values.containsKey(varName)) {
            return values.get(varName);
        }

        if (enclosing != null) {
            return enclosing.get(name);
        }

        throw new InterpreterException(name, String.format("Undefined variable '%s'.", varName));
    }

    public void assign(Token name, Object value) {
        String varName = name.getLexeme();

        if (values.containsKey(varName)) {
            values.put(varName, value);
        } else if (enclosing != null) {
            enclosing.assign(name, value);
        } else {
            throw new InterpreterException(name, String.format("Undefined variable '%s'.", varName));
        }
    }
}
