package jlox.ast.statements;

import jlox.ast.Expression;
import jlox.ast.Statement;
import jlox.ast.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class WhileStatement implements Statement {

    private Expression condition;
    private Statement body;

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitWhileStatement(this);
    }
}
