package jlox.ast.expressions;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Literal implements Expression {
    private Object value;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitLiteral(this);
    }
}
