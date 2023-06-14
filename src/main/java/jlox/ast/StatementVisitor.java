package jlox.ast;

import jlox.ast.statements.ExpressionStatement;
import jlox.ast.statements.PrintStatement;
import jlox.ast.statements.VariableStatement;

public interface StatementVisitor<T> {

    T visitPrintStatement(PrintStatement statement);
    T visitExpressionStatement(ExpressionStatement statement);
    T visitVariableStatement(VariableStatement statement);
}
