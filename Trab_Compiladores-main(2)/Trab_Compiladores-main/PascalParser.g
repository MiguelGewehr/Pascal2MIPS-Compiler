parser grammar PascalParser;

options {
	tokenVocab = PascalLexer;
}

program:
	program_header SEMI program_block DOT
;

program_header:
	PROGRAM IDENTIFIER (LPAREN program_parameter_list RPAREN)?
;

program_parameter_list:
	identifier_list
;

program_block:
	block
;

block:
	label_declaration_part?
	constant_definition_part?
	type_definition_part?
	variable_declaration_part?
	procedure_and_function_declaration_part?
	statement_part
;

label_declaration_part:
	LABEL label (COMMA label)* SEMI
;

label:
	INTEGER
;

constant_definition_part:
	CONST constant_definition (SEMI constant_definition)* SEMI?
;

constant_definition:
	IDENTIFIER EQUAL constant
;

constant:
	sign? (unsigned_number | constant_identifier)
	| character_string
;

sign:
	PLUS | MINUS
;

unsigned_number:
	unsigned_integer
	| unsigned_real
;

unsigned_integer:
	digit_sequence
;

digit_sequence:
	INTEGER
;

unsigned_real:  
	digit_sequence DOT fractional_part (E scale_factor)?
	| digit_sequence E scale_factor
;

fractional_part:
	digit_sequence
;

scale_factor:
	sign? digit_sequence
;

constant_identifier:
	IDENTIFIER
;

character_string:
	QUOTE string_element* QUOTE
;

string_element:
	APOSTROPHE_IMAGE
	| CHARACTER
;

type_definition_part:
	TYPE type_definition (SEMI type_definition)* SEMI?
;

type_definition:
	IDENTIFIER EQUAL type_denoter
;

type_denoter:
	type_identifier
	| new_type
;

type_identifier:
	IDENTIFIER
;

new_type:
	new_ordinal_type
	| new_structured_type
	| new_pointer_type
;

new_ordinal_type:
	enumerated_type
	| subrange_type
;

enumerated_type:
	LPAREN identifier_list RPAREN
;

identifier_list:
	IDENTIFIER (COMMA IDENTIFIER)*
;

subrange_type:
	constant DOTDOT constant
;

new_structured_type:
	(PACKED)? unpacked_structured_type
;

unpacked_structured_type:
	array_type
	| record_type
	| set_type
	| file_type
;

array_type:
	ARRAY LBRACK index_type (COMMA index_type)* RBRACK OF component_type
;

index_type:
	ordinal_type
;

ordinal_type:
	type_identifier
	| new_ordinal_type
;

component_type:
	type_denoter
;

record_type:
	RECORD field_list? END
;

field_list:
	fixed_part (SEMI variant_part)?
	| variant_part
;

fixed_part:
	record_section (SEMI record_section)*
;

record_section:
	identifier_list COLON type_denoter
;

variant_part:
	CASE variant_selector OF variant (SEMI variant)*
;

variant_selector:
	(tag_field COLON)? tag_type
;

tag_field:
	IDENTIFIER
;

tag_type:
	ordinal_type_identifier
;

ordinal_type_identifier:
	type_identifier
;

variant:
	case_constant_list COLON LPAREN field_list? RPAREN
;

case_constant_list:
	constant (COMMA constant)*
;

set_type:
	SET OF base_type
;

base_type:
	ordinal_type
;

file_type:
	FILE OF component_type
;

new_pointer_type:
	AT domain_type
;

domain_type:
	type_identifier
;

variable_declaration_part:
	VAR variable_declaration (SEMI variable_declaration)* SEMI?
;

variable_declaration:
	identifier_list COLON type_denoter
;

procedure_and_function_declaration_part:
	(procedure_declaration | function_declaration) SEMI+
;

procedure_declaration:
	procedure_heading SEMI
	(   directive
    	| procedure_block
    	| procedure_identification SEMI procedure_block
	)
;

procedure_heading:
	PROCEDURE IDENTIFIER formal_parameter_list?
;

formal_parameter_list:
	LPAREN formal_parameter_section (SEMI formal_parameter_section)* RPAREN
;

formal_parameter_section:
	value_parameter_specification
	| variable_parameter_specification
	| procedural_parameter_specification
	| functional_parameter_specification
	| conformant_array_parameter_specification
;

value_parameter_specification:
	identifier_list COLON type_identifier
;

variable_parameter_specification:
	VAR identifier_list COLON type_identifier
;

procedural_parameter_specification:
	procedure_heading
;

functional_parameter_specification:
	function_heading
;

conformant_array_parameter_specification:
	(VAR)? identifier_list COLON conformant_array_schema
;

conformant_array_schema:
	(PACKED)? ARRAY LBRACK index_type_specification (COMMA index_type_specification)* RBRACK OF (type_identifier | conformant_array_schema)
;

index_type_specification:
	IDENTIFIER DOTDOT IDENTIFIER COLON ordinal_type_identifier
;

procedure_identification:
	PROCEDURE procedure_identifier
;

procedure_identifier:
	IDENTIFIER
;

procedure_block:
	block
;

directive:
	IDENTIFIER  // 'forward' ou 'external'
;

function_declaration:
	function_heading SEMI
	(   directive
    	| function_block
    	| function_identification SEMI function_block
	)
;

function_heading:
	FUNCTION IDENTIFIER formal_parameter_list? COLON result_type
;

result_type:
	simple_type_identifier
	| pointer_type_identifier
;

simple_type_identifier:
	type_identifier
;

pointer_type_identifier:
	type_identifier
;

function_identification:
	FUNCTION function_identifier
;

function_identifier:
	IDENTIFIER
;

function_block:
	block
;

statement_part:
	compound_statement
;

compound_statement:
	BEGIN statement_sequence? END
;

statement_sequence:
	statement (SEMI statement)*
;

statement:
	(label COLON)?
	(   simple_statement
    	| structured_statement
	)
;

simple_statement:
	assignment_statement
	| procedure_statement
	| goto_statement
	| empty_statement
;

assignment_statement:
	(variable | function_identifier) ASSIGN expression
;

procedure_statement:
	procedure_identifier
	(   actual_parameter_list?
    	| read_parameter_list
    	| readln_parameter_list
    	| write_parameter_list
    	| writeln_parameter_list
	)
;

actual_parameter_list:
	LPAREN actual_parameter (COMMA actual_parameter)* RPAREN
;

actual_parameter:
	expression
	| variable
	| procedure_identifier
	| function_identifier
;

read_parameter_list:
	LPAREN (file_variable COMMA)? variable (COMMA variable)* RPAREN
;

readln_parameter_list:
	LPAREN (file_variable | variable) (COMMA variable)* RPAREN
;

write_parameter_list:
	LPAREN (file_variable COMMA)? write_parameter (COMMA write_parameter)* RPAREN
;

writeln_parameter_list:
	LPAREN (file_variable | write_parameter) (COMMA write_parameter)* RPAREN
;

write_parameter:
	expression (COLON expression (COLON expression)?)?
;

file_variable:
	variable
;

goto_statement:
	GOTO label
;

empty_statement:
	/* vazio */
;

structured_statement:
	compound_statement
	| conditional_statement
	| repetitive_statement
	| with_statement
;

conditional_statement:
	if_statement
	| case_statement
;

if_statement:
	IF expression THEN statement (ELSE statement)?
;

case_statement:
	CASE case_index OF case_list_element (SEMI case_list_element)* END
;

case_index:
	expression
;

case_list_element:
	case_constant_list COLON statement
;


repetitive_statement:
	repeat_statement
	| while_statement
	| for_statement
;

repeat_statement:
	REPEAT statement_sequence UNTIL expression
;

while_statement:
	WHILE expression DO statement
;

for_statement:
	FOR control_variable ASSIGN initial_value direction final_value DO statement
;

control_variable:
	IDENTIFIER
;

initial_value:
	expression
;

direction:
	TO
	| DOWNTO
;

final_value:
	expression
;

with_statement:
	WITH record_variable_list DO statement
;

record_variable_list:
	variable (COMMA variable)*
;

expression:
	simple_expression (relational_operator simple_expression)?
;

simple_expression:
	(PLUS | MINUS)? term (adding_operator term)*
;

term:
	factor (multiplying_operator factor)*
;

factor:
	variable
	| unsigned_constant
	| function_designator
	| set_constructor
	| LPAREN expression RPAREN
	| NOT factor
	| boolean_expression
;

function_designator:
	function_identifier actual_parameter_list?
;

set_constructor:
	LBRACK (member_designator (COMMA member_designator)*)? RBRACK
;

member_designator:
	expression (DOTDOT expression)?
;

relational_operator:
	EQUAL
	| NOTEQUAL
	| LESS
	| GREATER
	| LESSEQUAL
	| GREATEREQUAL
	| IN
;

adding_operator:
	PLUS
	| MINUS
	| OR
;

multiplying_operator:
	STAR
	| SLASH
	| DIV
	| MOD
	| AND
;

boolean_expression:
	expression
;

variable:
	IDENTIFIER variable_suffix*
;

variable_suffix:
	LBRACK expression (COMMA expression)* RBRACK   
	| DOT IDENTIFIER                           	
	| AT                                       	
;

unsigned_constant:
    COMMA
;

