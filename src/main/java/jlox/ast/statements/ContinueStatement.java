package jlox.ast.statements;

import jlox.ast.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContinueStatement implements LoopControlStatement {

    private String loopName;

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitContinueStatement(this);
    }
}
