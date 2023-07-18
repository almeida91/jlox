package jlox.ast.statements;

import jlox.ast.Statement;

public interface LoopControlStatement extends Statement {

    String getLoopName();
}
