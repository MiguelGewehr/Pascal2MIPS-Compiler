# Pascal to MIPS Compiler

A complete Pascal language compiler that generates MIPS assembly code, developed using ANTLR4 and Java.

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
- [Code Generator](#code-generator)
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

The project includes a complete Pascal interpreter that can execute Pascal programs directly by traversing the AST.

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

### Testing the Interpreter

```bash
# Interpret Pascal program directly
make interpret FILE=in/program.pas
```

## Code Generator

The code generator translates the Pascal AST into MIPS assembly code for execution on the MARS simulator.

### Key Features

#### MIPS Code Generation
- **Register Management**: Efficient use of MIPS registers
- **Memory Allocation**: Stack-based variable storage
- **Function Calls**: MIPS calling convention implementation
- **Control Structures**: Translation of if/while statements to MIPS jumps

### Testing Code Generation

```bash
# Compile Pascal to MIPS assembly and run
make compile FILE=in/program.pas
```

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

#### Test Error Detection
```bash
# Test semantic error detection
for file in in/semerr*.pas; do
    echo "=== Testing semantic errors in $file ==="
    make interpret FILE="$file" 2>&1 | head -5
    echo
done
```

#### Test Type System
```bash
# Test type checking with mixed operations
make interpret FILE=in/c02.pas  # Type conversions
make interpret FILE=in/c05.pas  # Array type checking
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
| `make parse FILE=file` | Syntax analysis only | `make parse FILE=in/test.pas` |
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

### Current Capabilities

#### Front-End (Complete)
- **Lexical Analysis**: Complete Pascal token recognition
- **Syntactic Analysis**: Full Pascal grammar support with parse trees
- **Semantic Analysis**: Symbol tables, type checking, scope management
- **Type System**: Comprehensive type unification and conversion
- **AST Generation**: Complete intermediate representation

#### Back-End
- **Pascal Interpreter**: Direct AST execution
- **MIPS Code Generator**: Assembly code generation (in progress)

#### Quality Assurance
- **66+ Test Cases**: Covering all compiler phases
- **Error Detection**: Lexical, syntactic, and semantic error handling
- **Debugging Tools**: Parse tree visualization, symbol table output
- **AST Visualization**: GraphViz integration for AST inspection

The compiler now provides a complete front-end implementation with sophisticated type checking and AST construction, ready for advanced back-end optimizations and code generation.