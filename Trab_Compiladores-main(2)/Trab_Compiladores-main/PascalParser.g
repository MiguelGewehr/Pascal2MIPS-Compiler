parser grammar PascalLexer;
 
options {
    tokenVocab = PascalLexer;
}

program:
    program_header SEMI program_block DOT
;

program_header:
    PROGRAM IDENTIFIER
;

program_block:
    block
;

block:
    label_declaration_part? constant_declaration_part type_definition_part variable_declaration_part procedure_and_function_declaration_part statement_declaration_part
;
label_declaration_part:
    LABEL DIGIT (COMMA DIGIT)* SEMI
;

constant_declaration_part:
    (CONST constant_definition SEMI)*
;

constant_definition:
    IDENTIFIER EQUAL constant
;

constant:
    sign? (unsigned_number | constant_identifier) | character_string
;

sign:
    PLUS | MINUS
;

unsigned_number:
    unsigned_integer | unsigned_real
;

unsigned_integer:
    digit_sequence    
;

digit_sequence:
    DIGIT+
;

unsigned_real:  
    digit_sequence DOT fractional_part (E scale_factor)? | digit_sequence E scale_factor
;

fractional_part:
    digit_sequence
;

scale_factor:
    sign?  digit_sequence
;

constant_identifier:
    IDENTIFIER
;

character_string:
    QUOTE string_element+ QUOTE
;

string_element:
    '"' | CHARACTER
;

type_definition_part:
    (TYPE (type_definition SEMI)+)?
;

type_definition:
    IDENTIFIER EQUAL type_denoter
;

type_denoter: 
    type_identifier | new_type
;

type_identifier: 
    IDENTIFIER
;

new_type:
    new_ordinal_type | new_structured_type | new_pointer_type
;

new_ordinal_type:
    enumerated_type | subrange_type
;

enumerated_type:
    LPAREN identifier_list RPAREN 
;

identifier_list:
    IDENTIFIER COMMA (IDENTIFIER COMMA)*
;

subrange_type:
    constant DOTDOT constant
;

new_structured_type:

;