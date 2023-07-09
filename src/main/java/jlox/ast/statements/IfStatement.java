package jlox.ast.statements;

import jlox.ast.Expression;
import jlox.ast.Statement;
import jlox.ast.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class IfStatement implements Statement {

    private Expression condition;
    private Statement thenBranch;
    private Statement elseBranch;

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitIfStatement(this);
    }
}
