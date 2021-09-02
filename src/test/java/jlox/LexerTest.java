package jlox;

import jlox.lexer.Scanner;
import jlox.lexer.Token;
import jlox.lexer.TokenType;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertEquals;

public class LexerTest {

    @Test
    public void testComment() {
        Scanner scanner = new Scanner("/* aaaaa" +
                "aaaa" +
                "aaaa" +
                "*/");

        List<Token> tokens = scanner.scanTokens();

        assertThat(tokens, hasSize(1));

        assertEquals(TokenType.EOF, tokens.get(0).getType());
    }
}
