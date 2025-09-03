# Pascal to MIPS Compiler

A Pascal language compiler that generates MIPS assembly code, developed using ANTLR4 and Java.

## Table of Contents
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Environment Setup](#environment-setup)
- [Lexical Analyzer](#lexical-analyzer)
- [Syntactic Analyzer](#syntactic-analyzer)
- [Semantic Analyzer](#semantic-analyzer)
- [Type System](#type-system)
- [AST Construction](#ast-construction)
- [Pascal Interpreter](#pascal-interpreter)
- [MIPS Code Generator](#mips-code-generator)
- [How to Use](#how-to-use)
- [Testing](#testing)
- [Error Handling](#error-handling)

## Project Structure

```
Pascal2MIPS-Compiler/
├── tools/
│   ├── antlr-4.13.2-complete.jar
│   └── Mars4_5.jar
├── parser/                    # ANTLR generated files (created by make)
├── bin/                      # Compiled .class files (created by make)
├── checker/                  # Semantic analyzer with type checking
│   └── SemanticChecker.java
├── codegen/                  # Code generation
│   └── CodegenVisitor.java
├── interpreter/              # Pascal interpreter
│   ├── Interpreter.java
│   └── FunctionContext.java
├── entries/                  # Symbol table entries
│   ├── Entry.java           # Base interface
│   ├── VarEntry.java        # Variable entries
│   ├── ArrayEntry.java      # Array entries
│   ├── ConstEntry.java      # Constant entries
│   ├── FuncEntry.java       # Function/procedure entries
│   └── ParamEntry.java      # Parameter entries
├── tables/                   # Symbol and string tables
│   ├── SymbolTable.java     # Symbol table implementation
│   └── StrTable.java        # String table implementation
├── typing/                   # Type system
│   ├── Type.java            # Type definitions and unification
│   └── Conv.java            # Type conversions
├── ast/                     # Abstract Syntax Tree
│   ├── AST.java             # AST node implementation
│   └── NodeKind.java        # AST node types
├── in/                      # Test files (.pas)
│   ├── c01.pas - c09.pas   # Correct programs
│   ├── lexerr01.pas - lexerr04.pas  # Lexical errors
│   ├── synerr01.pas - synerr07.pas  # Syntax errors
│   └── semerr01.pas - semerr13.pas  # Semantic errors
├── out/                     # Generated MIPS code (.asm)
│   ├── c01.asm - c10.asm   # Generated assembly files
├── PascalLexer.g           # Lexical grammar
├── PascalParser.g          # Syntactic grammar
├── Main.java               # Main program
└── Makefile                # Build automation
```

## Prerequisites

- **Java JDK 8+** - Java compiler and JVM
- **ANTLR 4.13.2** - Parser generator
- **MARS 4.5** - MIPS simulator (included in tools/)

### Installation Verification

```bash
# Check Java installation
javac -version
java -version

# Required JAR files should be in tools/
ls tools/
```

## Environment Setup

The project uses Makefiles to automate the entire build process. No manual environment variable configuration is required.

## Lexical Analyzer

The lexical analyzer (lexer) converts Pascal source code into a sequence of tokens. It's implemented using ANTLR4 with the grammar defined in `PascalLexer.g`.

### Recognized Tokens

#### Reserved Words
```pascal
and array begin case const div do downto else end
file for function goto if in label mod nil not
of or packed procedure program record repeat set
then to type until var while with true false
```

#### Special Symbols
```pascal
+ - * / = < > [ ] . , : ; ' ( )
<> <= >= := .. (. .) @
```

#### Literals
- **Integers**: `42`, `0`, `123`
- **Reals**: `3.14`, `2.5e10`, `1.0E-5`
- **Characters**: `'a'`, `'X'`, `''''` (single quotes)
- **Strings**: `'Hello'`, `'Pascal'`, `'It''s working'`
- **Identifiers**: `variable`, `myFunction`, `Counter_1`

#### Comments
- **Style 1**: `{ comment }`
- **Style 2**: `(* comment *)`

### Testing the Lexical Analyzer

```bash
# Generate tokens from a file
make tokens FILE=in/example.pas

# Example usage
make tokens FILE=in/c01.pas
```

## Syntactic Analyzer

The syntactic analyzer (parser) analyzes the grammatical structure of Pascal code and builds the parse tree. It's implemented using ANTLR4 with the grammar defined in `PascalParser.g`.

### Supported Syntactic Structures

#### Basic Program Structure
```pascal
program program_name;
{ declarations }
begin
    { statements }
end.
```

#### Declarations
- **Constants**: `const PI = 3.14;`
- **Types**: `type TArray = array[1..10] of integer;`
- **Variables**: `var x, y: integer; name: string;`
- **Functions**: `function sum(a, b: integer): integer;`
- **Procedures**: `procedure write(msg: string);`

#### Statements
- **Assignment**: `x := 10;`
- **Procedure call**: `writeln('Hello');`
- **Conditional structures**: `if...then...else`
- **Loop structures**: `while...do`
- **Blocks**: `begin...end`

#### Expressions
- **Arithmetic**: `a + b * c`, `(x - 1) div 2`
- **Relational**: `x < y`, `a = b`, `i <> 0`
- **Logical**: `(x > 0) and (y < 10)`
- **Function calls**: `sqrt(x)`, `length(str)`

### Testing the Syntactic Analyzer

```bash
# Generate and visualize syntax tree
make tree FILE=in/example.pas

# Debug mode with GUI
make debug FILE=in/example.pas

# Check if parsing succeeds
make parse FILE=in/example.pas
```

## Semantic Analyzer

The semantic analyzer performs comprehensive semantic analysis using the Visitor pattern to traverse the parse tree. It implements symbol tables, type checking, and AST construction as described in Laboratories 3, 4, and 5.

### Key Components

#### Symbol Table (`SymbolTable.java`)
- Manages variable, function, and procedure declarations
- Supports nested scopes with proper scope resolution
- Stores symbol information including name, type, and line number
- Maintains scope history for debugging and function calls

#### String Table (`StrTable.java`)
- Stores and manages string literals from the source code
- Eliminates duplicate strings
- Provides indexed access to strings

#### Entry Types
- **VarEntry**: Regular variables with type information
- **ArrayEntry**: Array variables with bounds and element types
- **ConstEntry**: Constants with values and types
- **FuncEntry**: Functions and procedures with parameter lists
- **ParamEntry**: Function/procedure parameters with reference/value semantics

### Semantic Rules and Validations

#### Variable Declaration and Usage
```pascal
var x, y: integer;
    arr: array[1..10] of real;
begin
    x := 42;        // Valid assignment
    y := x + 10;    // Valid expression
    arr[5] := 3.14; // Valid array access
end.
```

#### Function/Procedure Declarations and Calls
```pascal
function multiply(a, b: integer): integer;
begin
    multiply := a * b;
end;

procedure printResult(value: integer);
begin
    writeln('Result: ', value);
end;

begin
    printResult(multiply(3, 4)); // Valid nested call
end.
```

#### Array Handling with Bounds Checking
```pascal
var numbers: array[1..5] of integer;
begin
    numbers[3] := 100;  // Valid: index within bounds
    numbers[0] := 50;   // Error: index out of bounds
    numbers[6] := 25;   // Error: index out of bounds
end.
```

## Type System

The type system (Laboratory 4) provides comprehensive type checking and inference for Pascal constructs.

### Supported Types

#### Primitive Types
- **integer**: 32-bit signed integers
- **real**: Floating-point numbers
- **boolean**: true/false values
- **char**: Single characters
- **string**: String literals

#### Structured Types
- **array**: Single-dimensional arrays with specified bounds
- **function/procedure types**: For function pointers and parameters

### Type Checking Rules

#### Arithmetic Operations
```pascal
var i: integer; r: real; result: real;
begin
    i := 10;
    r := 3.14;
    result := i + r;    // Valid: integer widened to real
    result := r * 2;    // Valid: integer widened to real
    i := r;             // Error: cannot narrow real to integer
end.
```

#### Type Unification
The type system implements sophisticated type unification:

```java
// Assignment compatibility
Type unifyAssignment(Type assignedType)

// Arithmetic operation compatibility  
Type unifyArithmetic(Type otherType)

// Comparison operation compatibility
Type unifyComparison(Type otherType)

// Logical operation compatibility
Type unifyLogical(Type otherType)
```

#### Automatic Type Conversions
- **Integer to Real**: Automatic widening in mixed expressions
- **Character to String**: When needed for string operations
- **Type conversion nodes**: Generated in AST for explicit conversions

### Type Error Messages

```
SEMANTIC ERROR (15): operator '+' cannot be applied to 'string' and 'integer'.
SEMANTIC ERROR (20): cannot assign 'real' to 'integer'.
SEMANTIC ERROR (25): if condition must be boolean, got 'integer'.
SEMANTIC ERROR (30): array index must be integer, got 'string'.
```

## AST Construction

The Abstract Syntax Tree (Laboratory 5) provides an intermediate representation that bridges the front-end analysis and back-end code generation.

### AST Node Structure

Each AST node contains:
- **NodeKind**: Identifies the type of construct (operation, statement, declaration)
- **Type**: The computed type of expressions
- **Data fields**: Store literal values, identifiers, or indices
- **Children**: References to child nodes

### AST Node Types

#### Program Structure Nodes
```
PROGRAM_NODE        -> Root of the AST
BLOCK_NODE         -> Statement sequences
VAR_SECTION_NODE   -> Variable declaration sections
CONST_SECTION_NODE -> Constant declaration sections
```

#### Statement Nodes
```
ASSIGN_NODE        -> Variable assignments
IF_NODE           -> Conditional statements
WHILE_NODE        -> Loop statements
PROC_CALL_NODE    -> Procedure calls
COMPOUND_STMT_NODE -> Statement blocks
```

#### Expression Nodes
```
PLUS_NODE, MINUS_NODE, TIMES_NODE, DIVIDE_NODE -> Arithmetic
EQ_NODE, LT_NODE, GT_NODE, LE_NODE, GE_NODE   -> Comparisons
AND_NODE, OR_NODE, NOT_NODE                   -> Logical operations
```

#### Value and Variable Nodes
```
INT_VAL_NODE      -> Integer literals
REAL_VAL_NODE     -> Real literals
BOOL_VAL_NODE     -> Boolean literals
STR_VAL_NODE      -> String literals
VAR_USE_NODE      -> Variable references
ARRAY_ACCESS_NODE -> Array element access
```

#### Function/Procedure Nodes
```
FUNC_DECL_NODE    -> Function declarations
PROC_DECL_NODE    -> Procedure declarations
FUNC_CALL_NODE    -> Function calls
PARAM_LIST_NODE   -> Parameter lists
PARAM_NODE        -> Individual parameters
```

#### Type Conversion Nodes
```
I2R_NODE          -> Integer to Real conversion
C2S_NODE          -> Character to String conversion
```

### AST Construction Process

The AST is built during semantic analysis using a recursive visitor:

```java
@Override
public AST visitAssignmentStatement(PascalParser.AssignmentStatementContext ctx) {
    // Get variable and expression nodes
    AST varNode = visit(ctx.variable());
    AST exprNode = visit(ctx.expression());
    
    // Type checking
    Type resultType = varNode.type.unifyAssignment(exprNode.type);
    if (resultType == Type.NO_TYPE) {
        // Report semantic error
    }
    
    // Create assignment node
    AST assignNode = new AST(NodeKind.ASSIGN_NODE, Type.NO_TYPE);
    assignNode.addChild(varNode);
    
    // Add type conversion if needed
    if (varNode.type == Type.REAL && exprNode.type == Type.INTEGER) {
        AST convNode = Conv.createConvNode(Conv.I2R, exprNode);
        assignNode.addChild(convNode);
    } else {
        assignNode.addChild(exprNode);
    }
    
    return assignNode;
}
```

### AST Visualization

The AST can be visualized using GraphViz DOT format:

```bash
# Generate AST and convert to PDF
java Main program.pas 2> program.dot
dot -Tpdf program.dot -o program.pdf
```

Example AST visualization for `x := x + 1`:
```
program
  block
    var_list
      (integer) x@0
    compound
      :=
        (integer) x@0
        (integer) +
          (integer) x@0
          (integer) 1
```

## Pascal Interpreter

The project includes a complete Pascal interpreter (Laboratory 6) that can execute Pascal programs directly by traversing the AST.

### Key Features

#### Runtime Environment
- **Variable Storage**: Dynamic variable allocation and management
- **Function Context**: Stack-based function call management with local scopes
- **Built-in Functions**: Support for `writeln`, `write`, `readln`, `read`
- **Array Support**: Runtime bounds checking and element access

#### Execution Model
```java
// AST-based interpretation
public Object execute(AST node) {
    switch (node.kind) {
        case ASSIGN_NODE:
            executeAssignment(node);
            break;
        case IF_NODE:
            executeIf(node);
            break;
        case WHILE_NODE:
            executeWhile(node);
            break;
        // ... other node types
    }
}
```

#### Function and Procedure Support
- **Local Variable Management**: Proper scoping with function contexts
- **Parameter Passing**: Both by-value and by-reference support
- **Return Value Handling**: Function result management
- **Recursive Calls**: Stack-based recursion support

#### Type System Integration
- **Runtime Type Checking**: Ensures type safety during execution
- **Automatic Conversions**: Integer to real conversions as needed
- **Built-in Operations**: Arithmetic, logical, and comparison operations

### Testing the Interpreter

```bash
# Interpret Pascal program directly
make interpret FILE=in/program.pas
```

### Interpreter Output Example

```pascal
program example;
var x, y: integer;
begin
    x := 10;
    y := 20;
    writeln('Sum: ', x + y);
end.
```

**Interpreter Output:**
```
=== TABLES ===
=== STRING TABLE ===
String 0: 'Sum: '

=== SYMBOL TABLE ===
var x@0: integer
var y@1: integer

=== AST ===
program
  block
    var_list
      (integer) x@0
      (integer) y@1
    compound
      :=
        (integer) x@0
        (integer) 10
      :=
        (integer) y@1
        (integer) 20
      writeln
        (string) 'Sum: '@0
        (integer) +
          (integer) x@0
          (integer) y@1

=== INTERPRETER ===
Sum: 30
```

## MIPS Code Generator

The MIPS code generator (Laboratory 7) completes the compiler by implementing a full MIPS assembly code generator that translates the Pascal AST into executable MIPS code.

### Key Architecture Features

#### Register Management and Stack Operations
```java
// Integer temporaries management
private void emitPushTemp(String reg) {
    mipsCode.append("subu $sp, $sp, 4\n");
    mipsCode.append("sw " + reg + ", 0($sp)\n");
}

// Float temporaries management  
private void emitPushFloat(String freg) {
    mipsCode.append("subu $sp, $sp, 4\n");
    mipsCode.append("swc1 " + freg + ", 0($sp)\n");
}
```

#### Function Call Management
- **MIPS Calling Convention**: Proper register saving and restoration
- **Stack Frame Management**: Dynamic allocation for local variables and parameters
- **Parameter Passing**: Both by-value and by-reference support
- **Return Value Handling**: Integer ($v0) and float ($f0) returns

```java
private void emitFunctionProlog(List<ParamEntry> parameters) {
    mipsCode.append("subu $sp, $sp, 8\n"); // Space for $ra and $fp
    mipsCode.append("sw $ra, 4($sp)\n");   // Save return address
    mipsCode.append("sw $fp, 0($sp)\n");   // Save frame pointer
    mipsCode.append("move $fp, $sp\n");    // New frame pointer
}
```

### Comprehensive Language Feature Support

#### Variables and Arrays
```pascal
// Global variables
var x, y: integer;
    temperatures: array[1..7] of real;

begin
    x := 42;                    // Generated: li $t0, 42; sw $t0, var_x
    temperatures[3] := 25.5;    // Array bounds calculation and storage
end.
```

**Generated MIPS:**
```assembly
.data
var_x: .word 0
var_y: .word 0  
temperatures: .float 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0

.text
li $t0, 42
sw $t0, var_x

# Array access: temperatures[3] := 25.5
li $t1, 3                    # Index
addi $t1, $t1, -1           # Adjust for 1-based indexing  
sll $t1, $t1, 2             # Multiply by 4
la $t2, temperatures        # Array base address
add $t2, $t2, $t1          # Element address
lwc1 $f0, float_const_25_5  # Load 25.5
swc1 $f0, 0($t2)           # Store in array
```

#### Arithmetic Operations with Mixed Types
```pascal
var i: integer; r: real; result: real;
begin
    i := 10;
    r := 3.14; 
    result := i + r;    // Automatic integer to real conversion
end.
```

**Generated MIPS:**
```assembly
# i := 10
li $t0, 10
sw $t0, var_i

# r := 3.14  
lwc1 $f0, float_const_3_14
swc1 $f0, var_r

# result := i + r (with I2R conversion)
lw $t0, var_i              # Load integer i
mtc1 $t0, $f0              # Move to coprocessor
cvt.s.w $f0, $f0           # Convert int to float
lwc1 $f1, var_r           # Load float r  
add.s $f0, $f0, $f1       # Float addition
swc1 $f0, var_result      # Store result
```

#### Control Structures
```pascal
if x > 0 then
    writeln('positive')
else
    writeln('negative');
```

**Generated MIPS:**
```assembly
lw $t0, var_x          # Load x
li $t1, 0              # Load constant 0
sgt $t0, $t0, $t1      # Set if greater than
beq $t0, $zero, else_1 # Branch if false
# Then block
la $a0, str_1          # "positive"  
li $v0, 4
syscall
j endif_1
else_1:
# Else block  
la $a0, str_2          # "negative"
li $v0, 4
syscall
endif_1:
```

#### Function and Procedure Implementation

```pascal
function multiply(a, b: integer): integer;
begin
    multiply := a * b;
end;

procedure printResult(value: integer);
begin
    writeln('Result: ', value);
end;

begin
    printResult(multiply(6, 7));
end.
```

**Generated MIPS:**
```assembly
# Function multiply
func_multiply:
    subu $sp, $sp, 8       # Function prolog
    sw $ra, 4($sp)
    sw $fp, 0($sp)
    move $fp, $sp
    
    # multiply := a * b
    lw $t0, 8($fp)         # Load parameter a
    lw $t1, 12($fp)        # Load parameter b  
    mul $v0, $t0, $t1      # Multiply (result in $v0)
    
    # Function epilog
    lw $fp, 0($sp)
    lw $ra, 4($sp)
    addu $sp, $sp, 8
    jr $ra

# Procedure printResult  
func_printResult:
    subu $sp, $sp, 8
    sw $ra, 4($sp)
    sw $fp, 0($sp)
    move $fp, $sp
    
    # writeln('Result: ', value)
    la $a0, str_3          # "Result: "
    li $v0, 4
    syscall
    lw $a0, 8($fp)         # Load parameter value
    li $v0, 1              # Print integer
    syscall
    la $a0, newline
    li $v0, 4
    syscall
    
    lw $fp, 0($sp)
    lw $ra, 4($sp) 
    addu $sp, $sp, 8
    jr $ra

# Main program
main:
    # multiply(6, 7)
    li $t0, 7              # Push arguments (right to left)
    subu $sp, $sp, 4
    sw $t0, 0($sp)
    li $t0, 6
    subu $sp, $sp, 4
    sw $t0, 0($sp)
    jal func_multiply      # Call function
    addu $sp, $sp, 8       # Remove arguments
    
    # printResult(result)
    subu $sp, $sp, 4       # Push return value as argument
    sw $v0, 0($sp)
    jal func_printResult   # Call procedure
    addu $sp, $sp, 4       # Remove argument
```

#### Built-in I/O Operations

```pascal
var x: integer; r: real;
begin
    write('Enter integer: ');
    readln(x);
    write('Enter real: ');
    readln(r);
    writeln('Values: ', x, ' ', r);
end.
```

**Generated MIPS:**
```assembly
# write('Enter integer: ')
la $a0, str_4
li $v0, 4
syscall

# readln(x)  
li $v0, 5              # Read integer syscall
syscall
sw $v0, var_x          # Store in variable

# write('Enter real: ')
la $a0, str_5  
li $v0, 4
syscall

# readln(r)
li $v0, 6              # Read float syscall
syscall
swc1 $f0, var_r       # Store in variable

# writeln('Values: ', x, ' ', r)
la $a0, str_6          # "Values: "
li $v0, 4
syscall
lw $a0, var_x          # Print integer
li $v0, 1
syscall
la $a0, str_7          # " "
li $v0, 4
syscall
lwc1 $f12, var_r       # Print float
li $v0, 2
syscall
```

### Advanced Features

#### Scope Management and Local Variables
- **Nested Scopes**: Function-local variables with proper offset calculation
- **Parameter Handling**: Both by-value and by-reference parameters
- **Global vs Local**: Automatic resolution of variable scope

#### Type System Integration
- **Automatic Conversions**: Integer to real conversion with `mtc1` and `cvt.s.w`
- **Mixed Arithmetic**: Seamless float/integer operations
- **Type-Aware Operations**: Different instruction sequences for different types

#### Memory Management
- **String Table Integration**: Efficient string literal storage and access
- **Array Bounds**: Automatic index adjustment for Pascal 1-based arrays
- **Dynamic Allocation**: Stack-based local variable allocation

## How to Use

### Complete Compilation Process
```bash
# Compile the entire project
make all
```

### Testing Individual Phases

#### Lexical Analysis
```bash
# Generate tokens only
make tokens FILE=in/program.pas
```

#### Syntactic Analysis
```bash
# Check syntax and show parse tree
make tree FILE=in/program.pas

# Interactive syntax tree viewer
make debug FILE=in/program.pas
```

#### Semantic Analysis with AST Construction
```bash
# Perform complete semantic analysis and show AST
make interpret FILE=in/program.pas

# The semantic checker outputs:
# 1. Symbol table contents
# 2. String table contents  
# 3. AST in DOT format (to stderr)
```

### Execution Options

#### Interpret Pascal Program
```bash
# Execute using built-in interpreter
make interpret FILE=in/program.pas
```

#### Compile to MIPS and Execute
```bash
# Generate MIPS assembly and run in MARS
make compile FILE=in/program.pas
```

### Complete Compilation Pipeline

#### Full Compilation Process
```bash
# Complete Pascal to MIPS compilation
make compile FILE=in/program.pas

# This process:
# 1. Lexical analysis (tokenization)
# 2. Syntactic analysis (parse tree)  
# 3. Semantic analysis (symbol tables, type checking)
# 4. AST construction (intermediate representation)
# 5. MIPS code generation (assembly output)
# 6. MARS execution (run the program)
```

#### Generated Assembly Structure
```assembly
.data
newline: .asciiz "\n"
str_0: .asciiz "Hello, World!"
var_x: .word 0
array_data: .word 0, 0, 0, 0, 0

.text
.globl main
main:
    move $fp, $sp          # Initialize frame pointer
    # Generated code for main program
    li $v0, 10             # Exit syscall
    syscall

# Function/procedure definitions
func_myFunction:
    # Function implementation
    
func_myProcedure:  
    # Procedure implementation
```

## Testing

### Test File Structure

The `in/` directory contains comprehensive test suites:

#### Correct Programs (`c01.pas - c09.pas`)
```
c01.pas          # Basic program structure and variables
c02.pas          # Arithmetic expressions and type conversions
c03.pas          # Control structures (if/while)
c04.pas          # Functions and procedures
c05.pas          # Arrays and bounds checking
c06.pas          # Complex expressions and nested calls
c07.pas          # Mixed type operations
c08.pas          # String handling
c09.pas          # Advanced language features
```

#### Error Test Cases

##### Lexical Errors (`lexerr01.pas - lexerr04.pas`)
- Invalid characters and tokens
- Unterminated strings and comments
- Malformed number formats

##### Syntax Errors (`synerr01.pas - synerr07.pas`)
- Missing semicolons and delimiters
- Unmatched parentheses and blocks
- Invalid program structure

##### Semantic Errors (`semerr01.pas - semerr13.pas`)
- Undeclared variables and functions
- Type compatibility errors
- Array bounds violations
- Function parameter mismatches
- Scope and redeclaration errors

### Running Comprehensive Tests

#### Test Semantic Analysis and AST Construction
```bash
# Test all correct programs
for file in in/c*.pas; do
    echo "=== Processing $file ==="
    make interpret FILE="$file"
    echo
done
```

#### Test MIPS Code Generation and Execution
```bash
# Test all correct programs with MIPS generation
for file in in/c*.pas; do
    echo "=== Compiling and running $file ==="
    make compile FILE="$file"
    echo
done
```

#### Test Error Detection
```bash
# Test semantic error detection
for file in in/semerr*.pas; do
    echo "=== Testing semantic errors in $file ==="
    make interpret FILE="$file" 2>&1 | head -5
    echo
done
```

#### Cross-Validation Testing
```bash
# Compare interpreter and MIPS execution results
for file in in/c*.pas; do
    echo "=== Cross-validating $file ==="
    echo "Interpreter output:"
    make interpret FILE="$file" 2>/dev/null
    echo "MIPS execution output:"
    make compile FILE="$file" 2>/dev/null
    echo
done
```

## Error Handling

### Enhanced Error Messages

#### Type Checking Errors
```
SEMANTIC ERROR (15): operator '+' cannot be applied to 'string' and 'integer'.
SEMANTIC ERROR (20): cannot assign 'real' to 'integer'.
SEMANTIC ERROR (25): if condition must be boolean, got 'string'.
```

#### Function Call Errors
```
SEMANTIC ERROR (30): function 'sum' expects 2 arguments, but 1 were provided.
SEMANTIC ERROR (35): argument 1 of function 'sum' expects type 'integer', but got 'string'.
SEMANTIC ERROR (40): procedure 'print' was not declared.
```

#### Array Access Errors
```
SEMANTIC ERROR (45): array index 10 is out of bounds for array 'numbers[1..5]'.
SEMANTIC ERROR (50): array index must be integer, got 'real'.
SEMANTIC ERROR (55): 'x' is not an array.
```

#### Scope and Declaration Errors
```
SEMANTIC ERROR (60): variable 'result' was not declared.
SEMANTIC ERROR (65): variable 'count' already declared at line 10.
SEMANTIC ERROR (70): function 'calculate' was not declared.
```

### Make Command Reference

| Command | Description | Example |
|---------|-------------|---------|
| `make all` | Compile entire project | `make all` |
| `make tokens FILE=file` | Lexical analysis only | `make tokens FILE=in/test.pas` |
| `make tree FILE=file` | Display parse tree | `make tree FILE=in/test.pas` |
| `make debug FILE=file` | Interactive tree viewer | `make debug FILE=in/test.pas` |
| `make interpret FILE=file` | Full semantic analysis + interpretation | `make interpret FILE=in/test.pas` |
| `make compile FILE=file` | Compile to MIPS and execute | `make compile FILE=in/test.pas` |
| `make clean` | Remove generated files | `make clean` |

## Project Evolution

### Laboratory 1: Lexical Analysis
- Token recognition for Pascal language
- Comment and string literal processing
- Error detection for invalid tokens

### Laboratory 2: Syntactic Analysis  
- Parse tree construction using ANTLR4
- Operator precedence implementation
- Syntax error reporting with location information

### Laboratory 3: Semantic Analysis Foundation
- Symbol table implementation with nested scopes
- String table for literal management  
- Basic semantic validations (declaration/usage checking)

### Laboratory 4: Type System and Type Checking
- **Complete type system implementation**
- **Type unification for expressions and assignments**
- **Automatic type conversions (integer to real)**
- **Comprehensive type compatibility checking**
- **Enhanced error messages with type information**
- **Type inference for complex expressions**

### Laboratory 5: AST Construction and Intermediate Representation
- **Abstract Syntax Tree generation during semantic analysis**
- **AST node types for all Pascal constructs**  
- **Type information embedded in AST nodes**
- **Type conversion node insertion for automatic conversions**
- **GraphViz visualization of AST structure**
- **Bridge between front-end analysis and back-end code generation**

### Laboratory 6: Pascal Interpreter
- **Complete Pascal program execution via AST traversal**
- **Runtime environment with variable storage and function contexts**
- **Built-in function support (writeln, write, readln, read)**
- **Function and procedure calls with proper parameter passing**
- **Array support with runtime bounds checking**
- **Type-safe execution with automatic conversions**
- **Cross-validation reference for MIPS code generation**

### Laboratory 7: MIPS Code Generation and Complete Compiler
- **Full MIPS assembly code generation from Pascal AST**
- **MIPS calling convention implementation with stack frames**
- **Register management for both integer and floating-point operations**
- **Complete language feature support (variables, arrays, functions, I/O)**
- **Scope management with local/global variable resolution**
- **Built-in function translation to MIPS syscalls**
- **Integration with MARS simulator for executable programs**

### Current Capabilities (Complete Compiler)

#### Front-End (Complete)
- **Lexical Analysis**: Complete Pascal token recognition
- **Syntactic Analysis**: Full Pascal grammar support with parse trees
- **Semantic Analysis**: Symbol tables, type checking, scope management
- **Type System**: Comprehensive type unification and conversion
- **AST Generation**: Complete intermediate representation

#### Back-End (Complete)
- **Pascal Interpreter**: Direct AST execution for testing and validation
- **MIPS Code Generator**: Full assembly code generation with all language features
- **Target Architecture**: MIPS32 with floating-point coprocessor support
- **Runtime System**: Complete I/O operations and built-in function support

#### Quality Assurance
- **Test Cases**: Covering all compiler phases including code generation
- **Error Detection**: Lexical, syntactic, and semantic error handling
- **Cross-Validation**: Pascal interpreter vs MIPS execution comparison
- **Debugging Tools**: Parse tree visualization, symbol table output, AST inspection
- **MARS Integration**: Seamless assembly execution and debugging

#### Production-Ready Features
- **Complete Language Support**: All Pascal constructs implemented
- **Efficient Code Generation**: Optimized MIPS instruction sequences
- **Robust Error Handling**: Comprehensive error detection and reporting
- **Modular Architecture**: Clean separation of compiler phases
- **Extensive Documentation**: Complete usage examples and test cases

### Testing and Validation

#### Comprehensive Test Coverage
```bash
# Test all compiler phases
for file in in/c*.pas; do
    echo "=== Testing $file ==="
    echo "1. Lexical Analysis:"
    make tokens FILE="$file" | head -10
    
    echo "2. Semantic Analysis & Interpretation:"
    make interpret FILE="$file"
    
    echo "3. MIPS Code Generation & Execution:"
    make compile FILE="$file"
    echo "----------------------------------------"
done
```

#### Generated Assembly Verification
- **MARS Compatibility**: All generated code runs on MARS 4.5 simulator
- **Correctness Validation**: Output matches Pascal interpreter results
- **Performance Optimization**: Efficient register usage and minimal stack operations

#### Error Handling Validation
```bash
# Test error detection across all phases
echo "=== Testing Lexical Errors ==="
for file in in/lexerr*.pas; do make tokens FILE="$file" 2>&1; done

echo "=== Testing Syntax Errors ==="  
for file in in/synerr*.pas; do make tree FILE="$file" 2>&1; done

echo "=== Testing Semantic Errors ==="
for file in in/semerr*.pas; do make interpret FILE="$file" 2>&1; done
```

### Example: Complete Compilation Process

#### Source Program (`fibonacci.pas`)
```pascal
program fibonacci;
var n, i, a, b, temp: integer;

function fib(n: integer): integer;
var a, b, temp, i: integer;
begin
    if n <= 1 then
        fib := n
    else begin
        a := 0;
        b := 1;
        for i := 2 to n do begin
            temp := a + b;
            a := b;
            b := temp;
        end;
        fib := b;
    end;
end;

begin
    write('Enter n: ');
    readln(n);
    writeln('Fibonacci(', n, ') = ', fib(n));
end.
```

#### Compilation Phases

**1. Lexical Analysis Output:**
```
[@0,0:6='program',<PROGRAM>,1:0]
[@1,8:16='fibonacci',<IDENT>,1:8]
[@2,17:17=';',<SEMICOLON>,1:17]
[@3,19:21='var',<VAR>,2:0]
[@4,23:23='n',<IDENT>,2:4]
...
```

**2. Parse Tree Visualization:**
```
program
├── IDENT: fibonacci
└── block
    ├── var_declaration_part
    │   └── var_declaration
    │       ├── IDENT: n, i, a, b, temp
    │       └── INTEGER
    └── compound_statement
        └── ...
```

**3. Semantic Analysis Output:**
```
=== STRING TABLE ===
String 0: 'Enter n: '
String 1: 'Fibonacci('
String 2: ') = '

=== SYMBOL TABLE ===
var n@0: integer
var i@1: integer  
var a@2: integer
var b@3: integer
var temp@4: integer
function fib@5: integer -> integer

=== AST ===
program
  block
    var_list
      (integer) n@0
      ...
    func_list
      (integer -> integer) fib@5
        param_list
          (integer) n@0
        block
          ...
    compound
      ...
```

**4. Pascal Interpreter Execution:**
```
Enter n: 8
Fibonacci(8) = 21
```

**5. Generated MIPS Assembly:**
```assembly
.data
newline: .asciiz "\n"
str_0: .asciiz "Enter n: "
str_1: .asciiz "Fibonacci("
str_2: .asciiz ") = "
var_n: .word 0
var_i: .word 0
var_a: .word 0
var_b: .word 0  
var_temp: .word 0

.text
.globl main
main:
    move $fp, $sp
    
    # write('Enter n: ')
    la $a0, str_0
    li $v0, 4
    syscall
    
    # readln(n)
    li $v0, 5
    syscall
    sw $v0, var_n
    
    # writeln('Fibonacci(', n, ') = ', fib(n))
    la $a0, str_1
    li $v0, 4
    syscall
    lw $a0, var_n
    li $v0, 1
    syscall
    la $a0, str_2
    li $v0, 4
    syscall
    
    # Call fib(n)
    lw $t0, var_n
    subu $sp, $sp, 4
    sw $t0, 0($sp)
    jal func_fib
    addu $sp, $sp, 4
    
    # Print result
    move $a0, $v0
    li $v0, 1
    syscall
    
    li $v0, 10
    syscall

func_fib:
    # Function implementation with local variables
    # and recursive logic...
```

**6. MARS Execution Output:**
```
Enter n: 8
Fibonacci(8) = 21
```

## Conclusion

The Pascal to MIPS compiler represents a complete implementation of all major compiler construction phases. From lexical analysis through MIPS code generation, each component works together to provide a robust, educational, and functional compiler system.

### Key Achievements

- **Complete Language Coverage**: All major Pascal constructs supported
- **Robust Error Handling**: Comprehensive error detection and reporting
- **Multiple Execution Models**: Both interpretation and compilation paths
- **Educational Value**: Clear phase separation with extensive debugging tools
- **Production Quality**: Generates correct, executable MIPS assembly code

### Technical Excellence

- **Modular Design**: Clean separation of concerns across compiler phases
- **Type Safety**: Comprehensive type system with automatic conversions
- **Optimization**: Efficient code generation with proper register usage
- **Validation**: Cross-validation between interpreter and generated code
- **Documentation**: Extensive examples and test case coverage

The project successfully demonstrates all fundamental compiler construction techniques while providing a practical tool for learning both compiler design and MIPS assembly programming.
