package jlox.ast.expressions;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import jlox.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Set implements Expression {
    private Expression object;
    private Token name;
    private Expression value;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitSet(this);
    }
}
