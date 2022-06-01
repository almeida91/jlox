package jlox.interpreter;

import jlox.MessagePrinter;
import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import jlox.ast.Statement;
import jlox.ast.StatementVisitor;
import jlox.ast.expressions.*;
import jlox.ast.statements.ExpressionStatement;
import jlox.ast.statements.PrintStatement;
import jlox.lexer.Token;

import java.util.List;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Void> {

    public void interpret(Expression expression) {
        try {
            Object value = evaluate(expression);
            System.out.println(stringfy(value));
        } catch (InterpreterException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void interpret(List<Statement> statements) {
        try {
            statements.forEach(this::execute);
        } catch (InterpreterException ex) {
            // TODO: error handling
            System.out.println(ex.getMessage());
        }
    }

    private void execute(Statement statement) {
        statement.accept(this);
    }

    @Override
    public Object visitAssign(Assign expression) {
        return null;
    }

    @Override
    public Object visitBinary(Binary expression) {
        Object left = evaluate(expression.getLeft());
        Object right = evaluate(expression.getRight());

        switch (expression.getOperator().getType()) {
            case MINUS:
                checkNumberOperands(expression.getOperator(), left, right);
                return (double) left - (double) right;
            case SLASH:
                checkNumberOperands(expression.getOperator(), left, right);
                return (double) left / (double) right;
            case STAR:
                checkNumberOperands(expression.getOperator(), left, right);
                return (double) left * (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return left + (String) right;
                }

                throw new InterpreterException(expression.getOperator(), "Operands must be either two numbers or two strings.");
            case GREATER:
                checkNumberOperands(expression.getOperator(), left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expression.getOperator(), left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expression.getOperator(), left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expression.getOperator(), left, right);
                return (double) left <= (double) right;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            default:
                return null;
        }
    }

    @Override
    public Object visitCall(Call expression) {
        return null;
    }

    @Override
    public Object visitGet(Get expression) {
        return null;
    }

    @Override
    public Object visitGrouping(Grouping expression) {
        return evaluate(expression.getExpression());
    }

    @Override
    public Object visitLiteral(Literal expression) {
        return expression.getValue();
    }

    @Override
    public Object visitLogical(Logical expression) {
        return null;
    }

    @Override
    public Object visitSet(Set expression) {
        return null;
    }

    @Override
    public Object visitSuper(Super expression) {
        return null;
    }

    @Override
    public Object visitThis(This expression) {
        return null;
    }

    @Override
    public Object visitUnary(Unary expression) {
        Object right = evaluate(expression.getRight());

        switch (expression.getOperator().getType()) {
            case MINUS:
                checkNumberOperand(expression.getOperator(), right);
                return - (double) right;
            case BANG:
                return !isTrue(right);
            default:
                return null;
        }
    }

    private boolean isTrue(Object object) {
        if (object == null) {
            return false;
        }

        if (object instanceof Boolean) {
            return (boolean) object;
        }

        return true;
    }

    @Override
    public Object visitVariable(Variable expression) {
        return null;
    }

    @Override
    public Void visitPrintStatement(PrintStatement statement) {
        Object value = evaluate(statement.getExpression());
        System.out.println(stringfy(value));
        return null;
    }

    @Override
    public Void visitExpressionStatement(ExpressionStatement statement) {
        evaluate(statement.getExpression());
        return null;
    }

    private Object evaluate(Expression expression) {
        return expression.accept(this);
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }

        if (left == null) {
            return false;
        }

        return left.equals(right);
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double) {
            return;
        }

        throw new InterpreterException(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator, Object left, Object right) {
        if (left instanceof Double && right instanceof Double) {
            return;
        }

        throw new InterpreterException(operator, "Operands must be a number.");
    }

    private String stringfy(Object value) {
        if (value == null) {
            return "nil";
        }

        if (value instanceof Double) {
            String text = value.toString();

            if (text.endsWith(".0")) {
                return text.substring(0, text.length() - 2);
            }

            return text;
        }

        return value.toString();
    }
}
