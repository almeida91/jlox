package jlox.ast.expressions;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import jlox.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Variable implements Expression {
    private Token name;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitVariable(this);
    }
}
