package jlox.ast;

import jlox.ast.expressions.*;
import jlox.lexer.Token;

import java.util.LinkedList;

public class AstPrinter implements ExpressionVisitor<String> {

    @Override
    public String visitAssign(Assign expression) {
        return parenthesize(expression.getName(), expression.getValue());
    }

    @Override
    public String visitBinary(Binary expression) {
        return parenthesize(expression.getOperator(), expression.getLeft(), expression.getRight());
    }

    @Override
    public String visitCall(Call expression) {
        LinkedList<Expression> expressions = new LinkedList<>();
        expressions.add(expression.getCallee());
        expressions.addAll(expression.getArguments());
        return parenthesize("call", expressions.toArray());
    }

    @Override
    public String visitGet(Get expression) {
        return parenthesize(".", expression.getObject(), expression.getName());
    }

    @Override
    public String visitGrouping(Grouping expression) {
        return parenthesize("group", expression.getExpression());
    }

    @Override
    public String visitLiteral(Literal expression) {
        if (expression.getValue() == null) {
            return "nil";
        }
        return expression.getValue().toString();
    }

    @Override
    public String visitLogical(Logical expression) {
        return parenthesize(expression.getOperator(), expression.getLeft(), expression.getRight());
    }

    @Override
    public String visitSet(Set expression) {
        return parenthesize("=", expression.getObject(), expression.getName(), expression.getValue());
    }

    @Override
    public String visitSuper(Super expression) {
        return parenthesize("super", expression.getMethod());
    }

    @Override
    public String visitThis(This expression) {
        return "this";
    }

    @Override
    public String visitUnary(Unary expression) {
        return parenthesize(expression.getOperator(), expression.getRight());
    }

    @Override
    public String visitVariable(Variable expression) {
        return expression.getName().getLexeme();
    }

    private String parenthesize(Token token, Object... items) {
        return parenthesize(token.getLexeme(), items);
    }

    private String parenthesize(String name, Object... items) {
        StringBuilder builder = new StringBuilder();

        builder.append("(")
                .append(name);

        for (Object item : items) {
            builder.append(" ");

            if (item instanceof Expression) {
                Expression expression = (Expression) item;
                builder.append(expression.accept(this));
            } else if (item instanceof Token) {
                Token token = (Token) item;
                builder.append(token.getLexeme());
            } else {
                builder.append(item.toString());
            }
        }

        builder.append(")");

        return builder.toString();
    }
}
