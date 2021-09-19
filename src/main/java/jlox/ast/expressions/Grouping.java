package jlox.ast.expressions;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Grouping implements Expression {
    private Expression expression;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitGrouping(this);
    }
}
