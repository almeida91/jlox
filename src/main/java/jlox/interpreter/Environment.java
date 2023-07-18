package jlox.interpreter;

import jlox.lexer.Token;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
public class Environment {
    private Map<String, Object> values = new HashMap<>();
    private Environment enclosing;
    private LinkedHashSet<String> loopsRunning = new LinkedHashSet<>();

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

    public boolean isLoopRunning(String loopName) {
        if (loopsRunning.contains(loopName)) {
            return true;
        }

        return enclosing != null && enclosing.isLoopRunning(loopName);
    }

    public void addLoop(String loopName) {
        loopsRunning.add(loopName);
    }

    public String getCurrentLoopName() {
        return loopsRunning.stream().reduce((first, second) -> second)
                .or(() -> enclosing != null ? Optional.of(enclosing.getCurrentLoopName()) : Optional.empty())
                .orElseThrow(() -> new InterpreterException(null, "No loop provided"));

    }


}
