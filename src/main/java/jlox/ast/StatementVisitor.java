package jlox.ast;

import jlox.ast.statements.*;

public interface StatementVisitor<T> {

    T visitPrintStatement(PrintStatement statement);
    T visitExpressionStatement(ExpressionStatement statement);
    T visitVariableStatement(VariableStatement statement);
    T visitBlock(Block block);
    T visitIfStatement(IfStatement statement);
}
