package jlox.ast;

public interface Expression {
    <T> T accept(ExpressionVisitor<T> visitor);
}
