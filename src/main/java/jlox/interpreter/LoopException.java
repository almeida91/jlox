package jlox.interpreter;

import jlox.ast.statements.LoopControlStatement;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoopException extends RuntimeException {

    private LoopControlStatement statement;
}
