package jlox.ast.expressions;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import jlox.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class This implements Expression {
    private Token keyword;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitThis(this);
    }
}
