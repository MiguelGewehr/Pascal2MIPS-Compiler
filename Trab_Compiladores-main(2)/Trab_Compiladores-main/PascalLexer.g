lexer grammar PascalLexer;

//fragments
fragment DIGIT : [0-9];
fragment LETTER : [a-zA-Z];

fragment A : [aA];
fragment B : [bB];
fragment C : [cC];
fragment D : [dD];
fragment E : [eE];
fragment F : [fF];
fragment G : [gG];
fragment H : [hH];
fragment I : [iI];
fragment J : [jJ];
fragment K : [kK];
fragment L : [lL];
fragment M : [mM];
fragment N : [nN];
fragment O : [oO];
fragment P : [pP];
fragment Q : [qQ];
fragment R : [rR];
fragment S : [sS];
fragment T : [tT];
fragment U : [uU];
fragment V : [vV];
fragment W : [wW];
fragment X : [xX];
fragment Y : [yY];
fragment Z : [zZ];




// Word symbols (keywords)
AND       : A N D;
ARRAY     : A R R A Y;
BEGIN     : B E G I N;
CASE      : C A S E;
CONST     : C O N S T;
DIV       : D I V;
DO        : D O;
DOWNTO    : D O W N T O;
ELSE      : E L S E;
END       : E N D;
FILE      : F I L E;
FOR       : F O R;
FUNCTION  : F U N C T I O N;
GOTO      : G O T O;
IF        : I F;
IN        : I N;
LABEL     : L A B E L;
MOD       : M O D;
NIL       : N I L;
NOT       : N O T;
OF        : O F;
OR        : O R;
PACKED    : P A C K E D;
PROCEDURE : P R O C E D U R E;
PROGRAM   : P R O G R A M;
RECORD    : R E C O R D;
REPEAT    : R E P E A T;
SET       : S E T;
THEN      : T H E N;
TO        : T O;
TYPE      : T Y P E;
UNTIL     : U N T I L;
VAR       : V A R;
WHILE     : W H I L E;
WITH      : W I T H;
WS     : [ \t\n]+ -> skip ;

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


// Literais
INTEGER : DIGIT+;

REAL
    : DIGIT+ '.' DIGIT+ (EXPONENT)?
    | DIGIT+ EXPONENT
    ;

fragment EXPONENT : [eE] [+-]? DIGIT+;

CHARACTER
    : '\'' ( ~('\'' | '\r' | '\n') | '\'\'' ) '\''
    ;

STRING
    : '\'' ( ~('\'' | '\r' | '\n') | '\'\'' )* '\''
    ;

IDENTIFIER
    : LETTER (LETTER | DIGIT | '_')*
    ;

// Comentários
COMMENT1 : '{' .*? '}' -> skip;
COMMENT2 : '(*' .*? '*)' -> skip;