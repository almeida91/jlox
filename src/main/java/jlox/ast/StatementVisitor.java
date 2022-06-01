package jlox.ast;

import jlox.ast.statements.ExpressionStatement;
import jlox.ast.statements.PrintStatement;

public interface StatementVisitor<T> {

    T visitPrintStatement(PrintStatement statement);
    T visitExpressionStatement(ExpressionStatement statement);
}
