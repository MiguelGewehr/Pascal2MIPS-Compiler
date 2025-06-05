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
    

