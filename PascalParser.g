parser grammar PascalParser;

options { tokenVocab=PascalLexer; }

program: PROGRAM IDENTIFIER (LPAREN identifierList RPAREN)? SEMI block DOT;

identifierList: IDENTIFIER (COMMA IDENTIFIER)*;

block: 
    labelSection?
    constSection?
    typeSection?
    varSection?
    subroutineDeclarationPart*
    compoundStatement;

labelSection: LABEL label (COMMA label)* SEMI;
label: INTEGER;

constSection: CONST constDefinition (SEMI constDefinition)* SEMI;
constDefinition: IDENTIFIER EQUAL constant;

constant: signedNumber | CHARACTER | STRING | IDENTIFIER;
signedNumber: (PLUS | MINUS)? (INTEGER | REAL);

typeSection: TYPE typeDefinition (SEMI typeDefinition)* SEMI;
typeDefinition: IDENTIFIER EQUAL typeDenoter;
typeDenoter: IDENTIFIER;

varSection: VAR varDeclaration (SEMI varDeclaration)* SEMI;
varDeclaration: identifierList COLON typeDenoter;

subroutineDeclarationPart: procedureDeclaration | functionDeclaration;
procedureDeclaration: PROCEDURE IDENTIFIER (LPAREN formalParameterList RPAREN)? SEMI block SEMI;
functionDeclaration: FUNCTION IDENTIFIER (LPAREN formalParameterList RPAREN)? COLON IDENTIFIER SEMI block SEMI;

formalParameterList: formalParameterSection (SEMI formalParameterSection)*;
formalParameterSection: identifierList COLON IDENTIFIER;

compoundStatement: BEGIN statementList END;
statementList: statement (SEMI statement)*;

statement: 
    compoundStatement
    | assignmentStatement
    | procedureCall
    | ifStatement
    | whileStatement
    | emptyStatement;

assignmentStatement: variable ASSIGN expression;
procedureCall: IDENTIFIER (LPAREN expressionList? RPAREN)?;
ifStatement: IF expression THEN statement (ELSE statement)?;
whileStatement: WHILE expression DO statement;
emptyStatement:;

expression: simpleExpression ((EQUAL | NOTEQUAL | LESS | GREATER | LESSEQUAL | GREATEREQUAL) simpleExpression)?;
simpleExpression: term ((PLUS | MINUS | OR) term)*;
term: factor ((STAR | SLASH | DIV | MOD | AND) factor)*;
factor: 
    IDENTIFIER
    | INTEGER
    | REAL
    | CHARACTER
    | STRING
    | LPAREN expression RPAREN
    | NOT factor;

variable: IDENTIFIER; 

expressionList: expression (COMMA expression)*;
