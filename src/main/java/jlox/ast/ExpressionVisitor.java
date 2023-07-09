package jlox.ast;

import jlox.ast.expressions.*;
import jlox.ast.statements.Block;

public interface ExpressionVisitor<T> {
    T visitAssign(Assign expression);
    T visitBinary(Binary expression);
    T visitCall(Call expression);
    T visitGet(Get expression);
    T visitGrouping(Grouping expression);
    T visitLiteral(Literal expression);
    T visitLogical(Logical expression);
    T visitSet(Set expression);
    T visitSuper(Super expression);
    T visitThis(This expression);
    T visitUnary(Unary expression);
    T visitVariable(Variable expression);

}
