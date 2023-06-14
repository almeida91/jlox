package jlox.ast.statements;

import jlox.ast.Expression;
import jlox.ast.Statement;
import jlox.ast.StatementVisitor;
import jlox.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VariableStatement implements Statement {

    private Token name;

    private Expression value;

    @Override
    public <T> T accept(StatementVisitor<T> visitor) {
        return visitor.visitVariableStatement(this);
    }
}
