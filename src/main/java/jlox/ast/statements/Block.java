package jlox.ast.statements;

import jlox.ast.Statement;
import jlox.ast.StatementVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Block implements Statement {
    private List<Statement> statements;

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitBlock(this);
    }
}
