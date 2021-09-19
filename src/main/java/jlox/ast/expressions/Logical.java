package jlox.ast.expressions;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import jlox.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Logical implements Expression {
    private Expression left;
    private Token operator;
    private Expression right;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitLogical(this);
    }
}
