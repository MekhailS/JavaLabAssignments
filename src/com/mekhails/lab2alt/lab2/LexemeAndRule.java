package com.mekhails.lab2alt.lab2;

class LexemeAndRule
{
    private ILexeme lexeme;
    private Rule rule;

    public LexemeAndRule(ILexeme lexeme_, Rule rule_)
        { lexeme = lexeme_; rule = rule_; }

    Rule getRule() { return rule; }

    ILexeme getLexeme() { return lexeme; }
}
