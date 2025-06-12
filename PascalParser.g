parser grammar PascalParser;

options { tokenVocab=PascalLexer; }

program: 
    PROGRAM IDENTIFIER (LPAREN identifierList RPAREN)? SEMI block DOT
;

identifierList: 
    IDENTIFIER (COMMA IDENTIFIER)*
;

block: 
    labelSection? constSection? typeSection? varSection? subroutineDeclarationPart* compoundStatement
;

labelSection: 
    LABEL label (COMMA label)* SEMI
;

label: 
    INTEGER
;

constSection: 
    CONST constDefinition (SEMI constDefinition)* SEMI
;

constDefinition: 
    IDENTIFIER EQUAL constant
;

constant: 
    signedNumber | CHARACTER | STRING | IDENTIFIER
;

signedNumber: 
    (PLUS | MINUS)? (INTEGER | REAL)
;

typeSection: 
    TYPE typeDefinition (SEMI typeDefinition)* SEMI
;

typeDefinition: 
    IDENTIFIER EQUAL typeDenoter
;

typeDenoter: 
    simpleType | arrayType
;

simpleType:
    INTEGER_TYPE | CHARACTER_TYPE | REAL_TYPE | STRING_TYPE | IDENTIFIER
;

arrayType:
    ARRAY LBRACK indexRange RBRACK OF typeDenoter
;

indexRange:
    signedNumber DOTDOT signedNumber
;

varSection: 
    VAR varDeclaration (SEMI varDeclaration)* SEMI
;

varDeclaration: 
    identifierList COLON typeDenoter
;

subroutineDeclarationPart: 
    procedureDeclaration | functionDeclaration
;

procedureDeclaration: 
    PROCEDURE IDENTIFIER (LPAREN formalParameterList RPAREN)? SEMI block SEMI
;

functionDeclaration: 
    FUNCTION IDENTIFIER (LPAREN formalParameterList RPAREN)? COLON typeDenoter SEMI block SEMI
;

formalParameterList: 
    formalParameterSection (SEMI formalParameterSection)*
;

formalParameterSection: 
    identifierList COLON typeDenoter
;

compoundStatement: 
    BEGIN statementList END
;

statementList: 
    statement (SEMI statement)*
;

statement: 
    compoundStatement
    | assignmentStatement
    | procedureCall
    | ifStatement
    | whileStatement
    | emptyStatement
    | repeatStatement
    | forStatement 
;

assignmentStatement: 
    variable ASSIGN expression
;

procedureCall: 
    IDENTIFIER (LPAREN expressionList? RPAREN)?
;

ifStatement: 
    IF expression THEN statement (ELSE statement)?
;

whileStatement: 
    WHILE expression DO statement
;

emptyStatement:
;


expression
    : expression (EQUAL | NOTEQUAL | LESS | GREATER | LESSEQUAL | GREATEREQUAL) expression      #exprComparison
    | expression (PLUS | MINUS | OR) expression                                                 #exprAddSub
    | expression (STAR | SLASH | DIV | MOD | AND) expression                                    #exprMulDiv
    | (PLUS | MINUS | NOT) expression                                                           #exprUnary
    | primaryExpression                                                                         #exprPrimary
;


primaryExpression:
    IDENTIFIER (LPAREN expressionList? RPAREN)?
    | INTEGER
    | REAL
    | CHARACTER
    | STRING
    | LPAREN expression RPAREN
;

variable: 
    IDENTIFIER (LBRACK expression RBRACK)?
; 

expressionList: 
    expressionItem (COMMA expressionItem)*
;

expressionItem: 
    expression | formattedExpression
;

formattedExpression: 
    expression COLON INTEGER COLON INTEGER
;

repeatStatement: 
    REPEAT statementList UNTIL expression
;

forStatement:
    FOR assignmentStatement (TO | DOWNTO) expression DO statement
;