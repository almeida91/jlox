package jlox.ast;

import jlox.ast.expressions.*;
import jlox.ast.statements.*;
import jlox.lexer.Token;
import jlox.lexer.TokenType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    private final List<Token> tokens;
    private int current = 0;

    public Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    public List<Statement> parse() {
        List<Statement> statements = new LinkedList<>();

        while (!isAtEnd()) {
            statements.add(declaration());
        }

        return statements;
    }

    private Statement declaration() {
        try {
            if (match(TokenType.VAR)) {
                return variableDeclaration();
            }
            return statement();
        } catch (ParserException ex) {
            synchronize();
            return null;
        }
    }

    private Statement statement() {
        if (match(TokenType.PRINT)) {
            return printStatement();
        }

        if (match(TokenType.LEFT_BRACE)) {
            return new Block(block());
        }

        if (match(TokenType.IF)) {
            return ifStatement();
        }

        return expressionStatement();
    }

    private Statement ifStatement() {
        consume(TokenType.LEFT_PAREN, "Expect '(' after 'if'");
        Expression condition = expression();
        consume(TokenType.RIGHT_PAREN, "Expect ')' after if condition");

        Statement thenBranch = statement();
        Statement elseBranch = null;

        if (match(TokenType.ELSE)) {
            elseBranch = statement();
        }

        return new IfStatement(condition, thenBranch, elseBranch);
    }

    private Statement expressionStatement() {
        Expression expression = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after expression.");
        return new ExpressionStatement(expression);
    }

    private Statement printStatement() {
        Expression expression = expression();
        consume(TokenType.SEMICOLON, "Expect ';' after value.");
        return new PrintStatement(expression);
    }

    private List<Statement> block() {
        List<Statement> statements = new ArrayList<>();

        while (!check(TokenType.RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration());
        }

        consume(TokenType.RIGHT_BRACE, "Expect '}' after block");
        return statements;
    }

    private Statement variableDeclaration() {
        Token name = consume(TokenType.IDENTIFIER, "Expect variable name.");

        Expression initializer = null;
        if (match(TokenType.EQUAL)) {
            initializer = expression();
        }

        consume(TokenType.SEMICOLON, "Expect ';' after variable declaration.");
        return new VariableStatement(name, initializer);
    }

    private Expression assignment() {
        Expression expression = or();

        if (match(TokenType.EQUAL)) {
            Token equals = previous();
            Expression value = assignment();

            if (expression instanceof Variable) {
                Token name = ((Variable)expression).getName();
                return new Assign(name, value);
            }

            error(equals, "Invalid assignment target.");
        }

        return expression;
    }

    private Expression or() {
        Expression expression = and();

        while (match(TokenType.OR)) {
            Token operator = previous();
            Expression right = and();
            expression = new Logical(expression, operator, right);
        }

        return expression;
    }

    private Expression and() {
        Expression expression = equality();

        while (match(TokenType.AND)) {
            Token operator = previous();
            Expression right = equality();
            expression = new Logical(expression, operator, right);
        }

        return expression;
    }

    private Expression expression() {
        return assignment();
    }

    private Expression equality() {
        Expression expression = comparison();
        while (match(TokenType.BANG_EQUAL, TokenType.EQUAL_EQUAL)) {
            Token operator = previous();
            Expression right = comparison();
            expression = new Binary(expression, operator, right);
        }

        return expression;
    }

    private Expression comparison() {
        Expression expression = term();

        while (match(TokenType.GREATER, TokenType.GREATER_EQUAL, TokenType.LESS, TokenType.LESS_EQUAL)) {
            Token operator = previous();
            Expression right = term();
            expression = new Binary(expression, operator, right);
        }

        return expression;
    }

    private Expression term() {
        Expression expression = factor();

        while (match(TokenType.MINUS, TokenType.PLUS)) {
            Token operator = previous();
            Expression right = factor();
            expression = new Binary(expression, operator, right);
        }

        return expression;
    }

    private Expression factor() {
        Expression expression = unary();

        while (match(TokenType.SLASH, TokenType.STAR)) {
            Token operator = previous();
            Expression right = unary();
            expression = new Binary(expression, operator, right);
        }

        return expression;
    }

    private Expression unary() {
        if (match(TokenType.BANG, TokenType.MINUS)) {
            Token operator = previous();
            Expression right = unary();
            return new Unary(operator, right);
        }

        return primary();
    }

    private Expression primary() {
        if (match(TokenType.FALSE)) {
            return new Literal(false);
        }

        if (match(TokenType.TRUE)) {
            return new Literal(true);
        }

        if (match(TokenType.NIL)) {
            return new Literal(null);
        }

        if (match(TokenType.NUMBER, TokenType.STRING)) {
            return new Literal(previous().getLiteral());
        }

        if (match(TokenType.IDENTIFIER)) {
            return new Variable(previous());
        }

        if (match(TokenType.LEFT_PAREN)) {
            Expression expression = expression();
            consume(TokenType.RIGHT_PAREN, "Expect ')' after expression.");
            return new Grouping(expression);
        }

        throw error(peek(), "Expect expression.");
    }

    private void synchronize() {
        advance();

        while (!isAtEnd()) {
            if (previous().getType() == TokenType.SEMICOLON) {
                return;
            }

            switch (peek().getType()) {
                case CLASS:
                case FOR:
                case FUN:
                case IF:
                case PRINT:
                case RETURN:
                case VAR:
                case WHILE:
                    return;
            }

            advance();
        }
    }

    private Token consume(TokenType tokenType, String message) {
        if (check(tokenType)) {
            return advance();
        }

        throw error(peek(), message);
    }

    private RuntimeException error(Token peek, String message) {
        // TODO: error reporting
        return new ParserException(message);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private boolean match(TokenType... types) {
        for (TokenType type : types) {
            if (check(type)) {
                advance();
                return true;
            }
        }

        return false;
    }

    private Token advance() {
        if (!isAtEnd()) {
            current++;
        }

        return previous();
    }

    private boolean check(TokenType type) {
        if (isAtEnd()) {
            return false;
        }

        return peek().getType() == type;
    }

    private boolean isAtEnd() {
        return peek().getType() == TokenType.EOF;
    }

    private Token peek() {
        return tokens.get(current);
    }


}
