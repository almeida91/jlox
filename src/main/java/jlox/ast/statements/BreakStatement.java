package jlox.ast.statements;

import jlox.ast.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BreakStatement implements LoopControlStatement {

    private String loopName;

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitBreakStatement(this);
    }
}
