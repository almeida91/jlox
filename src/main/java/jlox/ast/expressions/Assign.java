package jlox.ast.expressions;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import jlox.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Assign implements Expression {

    private Token name;
    private Expression value;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitAssign(this);
    }
}
