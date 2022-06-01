package jlox.ast.statements;

import jlox.ast.Expression;
import jlox.ast.Statement;
import jlox.ast.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PrintStatement implements Statement {
    private Expression expression;

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitPrintStatement(this);
    }
}
