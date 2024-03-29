package jlox.interpreter;

import jlox.ast.Expression;
import jlox.ast.ExpressionVisitor;
import jlox.ast.Statement;
import jlox.ast.StatementVisitor;
import jlox.ast.expressions.Assign;
import jlox.ast.expressions.Binary;
import jlox.ast.expressions.Call;
import jlox.ast.expressions.Get;
import jlox.ast.expressions.Grouping;
import jlox.ast.expressions.Literal;
import jlox.ast.expressions.Logical;
import jlox.ast.expressions.Set;
import jlox.ast.expressions.Super;
import jlox.ast.expressions.This;
import jlox.ast.expressions.Unary;
import jlox.ast.expressions.Variable;
import jlox.ast.statements.Block;
import jlox.ast.statements.BreakStatement;
import jlox.ast.statements.ContinueStatement;
import jlox.ast.statements.ExpressionStatement;
import jlox.ast.statements.IfStatement;
import jlox.ast.statements.LoopControlStatement;
import jlox.ast.statements.PrintStatement;
import jlox.ast.statements.VariableStatement;
import jlox.ast.statements.WhileStatement;
import jlox.lexer.Token;
import jlox.lexer.TokenType;

import java.util.List;

public class Interpreter implements ExpressionVisitor<Object>, StatementVisitor<Void> {

    private Environment environment = new Environment();

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
        Expression value = expression.getValue();
        environment.assign(expression.getName(), evaluate(value));
        return value;
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
        Object left = evaluate(expression.getLeft());

        if (expression.getOperator().getType() == TokenType.OR) {
            if (isTrue(left))  {
                return left;
            }
        } else {
            if (!isTrue(left)) {
                return left;
            }
        }

        return evaluate(expression.getRight());
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
                return -(double) right;
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
        return environment.get(expression.getName());
    }

    @Override
    public Void visitBlock(Block block) {
        executeBlock(block.getStatements(), new Environment(environment));
        return null;
    }

    @Override
    public Void visitIfStatement(IfStatement statement) {
        if (isTrue(evaluate(statement.getCondition()))) {
            execute(statement.getThenBranch());
        } else if (statement.getElseBranch() != null) {
            execute(statement.getElseBranch());
        }

        return null;
    }

    @Override
    public Void visitWhileStatement(WhileStatement statement) {
        environment.addLoop(statement.getLoopName());
        while (isTrue(evaluate(statement.getCondition())) && environment.isLoopRunning(statement.getLoopName())) {
            try {
                execute(statement.getBody());
            } catch (LoopException e) {
                LoopControlStatement loopControlStatement = e.getStatement();
                if (loopControlStatement.getLoopName().equals(statement.getLoopName())) {
                    if (loopControlStatement.getClass() == BreakStatement.class) {
                        break;
                    }
                    if (loopControlStatement.getClass() == ContinueStatement.class) {
                        continue;
                    }
                }
                throw e;
            } finally {
                if (statement.getAfterBody() != null) {
                    execute(statement.getAfterBody());
                }
            }
        }

        return null;
    }

    @Override
    public Void visitBreakStatement(BreakStatement statement) {
        throw new LoopException(statement);
    }

    @Override
    public Void visitContinueStatement(ContinueStatement statement) {
        throw new LoopException(statement);
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

    @Override
    public Void visitVariableStatement(VariableStatement statement) {
        Object value = null;

        if (statement.getInitializer() != null) {
            value = evaluate(statement.getInitializer());
        }

        environment.define(statement.getName().getLexeme(), value);

        return null;
    }

    private void executeBlock(List<Statement> statements, Environment blockEnvironment) {
        Environment previous = environment;

        try {
            environment = blockEnvironment;

            for (Statement statement : statements) {
                execute(statement);
            }
        } finally {
            environment = previous;
        }
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
