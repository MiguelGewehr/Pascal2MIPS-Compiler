lexer grammar PascalLexer;

fragment LETTER: [a-zA-Z];
fragment DIGIT: [0-9];

//Special-symbols
PLUS: '+';
MINUS: '-';
STAR: '*';
SLASH: '/';
EQUAL: '=';
LESS: '<';
GREATER: '>';
LBRACK: '[';
RBRACK: ']';
DOT: '.';
COMMA: ',';
COLON: ':';
SEMI: ';';
QUOTE: '\'';
LPAREN: '(';
RPAREN: ')';
NOTEQUAL: '<>';
LESSEQUAL: '<=';
GREATEREQUAL: '>=';
ASSIGN: ':=';
DOTDOT: '..';
LBRACK_ALT: '(.';
RBRACK_ALT: '.)';
AT: '@';

// Word symbols (keywords)
AND: 'and';
ARRAY: 'array';
BEGIN: 'begin';
CASE: 'case';
CONST: 'const';
DIV: 'div';
DO: 'do';
DOWNTO: 'downto';
ELSE: 'else';
END: 'end';
FILE: 'file';
FOR: 'for';
FUNCTION: 'function';
GOTO: 'goto';
IF: 'if';
IN: 'in';
LABEL: 'label';
MOD: 'mod';
NIL: 'nil';
NOT: 'not';
OF: 'of';
OR: 'or';
PACKED: 'packed';
PROCEDURE: 'procedure';
PROGRAM: 'program';
RECORD: 'record';
REPEAT: 'repeat';
SET: 'set';
THEN: 'then';
TO: 'to';
TYPE: 'type';
UNTIL: 'until';
VAR: 'var';
WHILE: 'while';
WITH: 'with';

//Identifiers
IDENTIFIER: LETTER (LETTER | DIGIT)*;

//Directives (treated same as identifiers but checked in parser)
DIRECTIVE: LETTER (LETTER | DIGIT)*;

//Numbers
fragment SIGN: PLUS | MINUS;
fragment DIGIT_SEQUENCE: DIGIT+;
fragment FRACTIONAL_PART: DIGIT_SEQUENCE;
fragment SCALE_FACTOR: SIGN? DIGIT_SEQUENCE;

UNSIGNED_INTEGER: DIGIT_SEQUENCE;
UNSIGNED_REAL: 
    DIGIT_SEQUENCE '.' FRACTIONAL_PART ('e' SCALE_FACTOR)?
    | DIGIT_SEQUENCE 'e' SCALE_FACTOR;
    
SIGNED_INTEGER: SIGN DIGIT_SEQUENCE;
SIGNED_REAL: SIGN (DIGIT_SEQUENCE '.' FRACTIONAL_PART ('e' SCALE_FACTOR)?
                  | DIGIT_SEQUENCE 'e' SCALE_FACTOR);

//Labels
LABEL_NUMBER: DIGIT_SEQUENCE;

//Character-strings
CHARACTER_STRING: '\'' (~['\r\n] | '\'\'')* '\'';

//Token separators
COMMENT: '{' .*? '}' | '(*' .*? '*)' -> skip;
WS: [ \t\r\n]+ -> skip;

// Handle unexpected characters
ERROR_CHAR: .;