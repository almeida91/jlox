package jlox.ast.expressions;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import jlox.lexer.Token;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class Call implements Expression {
    private Expression callee;
    private Token paren;
    private List<Expression> arguments;

    @Override
    public <T> T accept(ExpressionVisitor<T> visitor) {
        return visitor.visitCall(this);
    }
}
