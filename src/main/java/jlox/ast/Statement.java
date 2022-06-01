package jlox.ast;

public interface Statement {
    <T> T accept(StatementVisitor<T> visitor);
}
