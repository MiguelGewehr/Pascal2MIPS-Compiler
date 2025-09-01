# Compilador Pascal para MIPS

Um compilador completo da linguagem Pascal que gera código assembly MIPS, desenvolvido usando ANTLR4 e Java.

## Índice
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Pré-requisitos](#pré-requisitos)
- [Configuração do Ambiente](#configuração-do-ambiente)
- [Analisador Léxico](#analisador-léxico)
- [Analisador Sintático](#analisador-sintático)
- [Como Usar](#como-usar)

## Estrutura do Projeto

```
compilador-pascal/
├── tools/
│   ├── antlr-4.13.2-complete.jar
│   └── Mars4_5.jar
├── parser/                    # Arquivos gerados pelo ANTLR
├── bin/                      # Arquivos .class compilados
├── checker/                  # Analisador semântico
├── in/                       # Arquivos de teste (.pas)
├── out/                      # Código MIPS gerado (.asm)
├── PascalLexer.g            # Gramática do analisador léxico
├── PascalParser.g           # Gramática do analisador sintático
├── Main.java                # Programa principal
└── Makefile                 # Automação de build e testes
```

## Pré-requisitos

- **Java JDK 8+** - Compilador Java e JVM
- **ANTLR 4.13.2** - Gerador de analisadores
- **MARS 4.5** - Simulador MIPS (incluído em tools/)

### Verificação da Instalação

```bash
# Verificar Java
javac -version
java -version

# Os arquivos JAR necessários devem estar em tools/
ls tools/
```

## Configuração do Ambiente

O projeto usa Makefiles para automatizar todo o processo de build. Não é necessário configurar variáveis de ambiente manualmente.

## Analisador Léxico

O analisador léxico (lexer) é responsável por converter o código fonte Pascal em uma sequência de tokens. Foi implementado usando ANTLR4 com a gramática definida em `PascalLexer.g`.

### Tokens Reconhecidos

#### Palavras Reservadas
```pascal
and array begin case const div do downto else end
file for function goto if in label mod nil not
of or packed procedure program record repeat set
then to type until var while with true false
```

#### Símbolos Especiais
```pascal
+ - * / = < > [ ] . , : ; ' ( )
<> <= >= := .. (. .) @
```

#### Literais
- **Inteiros**: `42`, `0`, `123`
- **Reais**: `3.14`, `2.5e10`, `1.0E-5`
- **Caracteres**: `'a'`, `'X'`, `''''` (aspas simples)
- **Strings**: `'Hello'`, `'Pascal'`, `'It''s working'`
- **Identificadores**: `variable`, `myFunction`, `Counter_1`

#### Comentários
- **Estilo 1**: `{ comentário }`
- **Estilo 2**: `(* comentário *)`

### Exemplo de Análise Léxica

Para o código Pascal:
```pascal
program exemplo;
var x: integer;
begin
    x := 42;
    writeln(x)
end.
```

Os tokens gerados seriam:
```
[@0,0:6='program',<PROGRAM>,1:0]
[@1,8:14='exemplo',<IDENTIFIER>,1:8]
[@2,15:15=';',<SEMI>,1:15]
[@3,17:19='var',<VAR>,2:0]
[@4,21:21='x',<IDENTIFIER>,2:4]
[@5,22:22=':',<COLON>,2:5]
[@6,24:30='integer',<IDENTIFIER>,2:7]
[@7,31:31=';',<SEMI>,2:14]
[@8,33:37='begin',<BEGIN>,3:0]
[@9,43:43='x',<IDENTIFIER>,4:4]
[@10,45:46=':=',<ASSIGN>,4:6]
[@11,48:49='42',<INTEGER>,4:9]
[@12,50:50=';',<SEMI>,4:11]
[@13,56:62='writeln',<IDENTIFIER>,5:4]
[@14,63:63='(',<LPAREN>,5:11]
[@15,64:64='x',<IDENTIFIER>,5:12]
[@16,65:65=')',<RPAREN>,5:13]
[@17,67:69='end',<END>,6:0]
[@18,70:70='.',<DOT>,6:3]
[@19,72:71='<EOF>',<EOF>,7:0]
```

### Testando o Analisador Léxico

Para testar apenas o analisador léxico:

```bash
# Gerar apenas os tokens de um arquivo
make tokens FILE=in/exemplo.pas

# Exemplo de uso
make tokens FILE=in/c01.pas
```

## Analisador Sintático

O analisador sintático (parser) é responsável por analisar a estrutura gramatical do código Pascal e construir a árvore sintática abstrata (AST). Foi implementado usando ANTLR4 com a gramática definida em `PascalParser.g`.

### Estruturas Sintáticas Suportadas

#### Estrutura Básica do Programa
```pascal
program nome_programa;
{ declarações }
begin
    { comandos }
end.
```

#### Declarações
- **Constantes**: `const PI = 3.14;`
- **Tipos**: `type TArray = array[1..10] of integer;`
- **Variáveis**: `var x, y: integer; nome: string;`
- **Funções**: `function soma(a, b: integer): integer;`
- **Procedimentos**: `procedure escrever(msg: string);`

#### Comandos
- **Atribuição**: `x := 10;`
- **Chamada de procedimento**: `writeln('Hello');`
- **Estruturas condicionais**: `if...then...else`
- **Estruturas de repetição**: `while...do`, `for...do`, `repeat...until`
- **Blocos**: `begin...end`

#### Expressões
- **Aritméticas**: `a + b * c`, `(x - 1) div 2`
- **Relacionais**: `x < y`, `a = b`, `i <> 0`
- **Lógicas**: `(x > 0) and (y < 10)`
- **Chamadas de função**: `sqrt(x)`, `length(str)`

#### Tipos de Dados
- **Simples**: `integer`, `real`, `boolean`, `char`
- **Estruturados**: `array`, `record`, `set`
- **String**: `string`

### Exemplo de Análise Sintática

Para o programa Pascal:
```pascal
program exemplo;
var
    x, y: integer;
    resultado: real;

function soma(a, b: integer): integer;
begin
    soma := a + b
end;

begin
    x := 10;
    y := 20;
    resultado := soma(x, y) * 1.5;
    writeln('Resultado: ', resultado)
end.
```

A árvore sintática gerada mostrará a estrutura hierárquica:
```
program
├── PROGRAM
├── IDENTIFIER (exemplo)
├── SEMI
├── block
│   ├── variableDeclarationPart
│   │   ├── VAR
│   │   ├── variableDeclaration
│   │   │   ├── identifierList
│   │   │   │   ├── IDENTIFIER (x)
│   │   │   │   ├── COMMA
│   │   │   │   └── IDENTIFIER (y)
│   │   │   ├── COLON
│   │   │   └── type_
│   │   │       └── IDENTIFIER (integer)
│   │   └── variableDeclaration
│   │       ├── identifierList
│   │       │   └── IDENTIFIER (resultado)
│   │       ├── COLON
│   │       └── type_
│   │           └── IDENTIFIER (real)
│   ├── functionAndProcedureDeclarationPart
│   │   └── functionDeclaration
│   │       ├── FUNCTION
│   │       ├── IDENTIFIER (soma)
│   │       ├── formalParameterList
│   │       │   └── parameterGroup
│   │       │       ├── identifierList
│   │       │       │   ├── IDENTIFIER (a)
│   │       │       │   ├── COMMA
│   │       │       │   └── IDENTIFIER (b)
│   │       │       ├── COLON
│   │       │       └── type_
│   │       │           └── IDENTIFIER (integer)
│   │       ├── COLON
│   │       ├── resultType
│   │       │   └── IDENTIFIER (integer)
│   │       └── block
│   │           └── compoundStatement
│   │               ├── BEGIN
│   │               ├── statementList
│   │               │   └── statement
│   │               │       └── assignmentStatement
│   │               │           ├── variable
│   │               │           │   └── IDENTIFIER (soma)
│   │               │           ├── ASSIGN
│   │               │           └── expression
│   │               │               └── simpleExpression
│   │               │                   ├── term
│   │               │                   │   └── factor
│   │               │                   │       └── IDENTIFIER (a)
│   │               │                   ├── PLUS
│   │               │                   └── term
│   │               │                       └── factor
│   │               │                           └── IDENTIFIER (b)
│   │               └── END
│   └── compoundStatement
│       ├── BEGIN
│       ├── statementList
│       │   ├── statement
│       │   │   └── assignmentStatement
│       │   │       ├── variable
│       │   │       │   └── IDENTIFIER (x)
│       │   │       ├── ASSIGN
│       │   │       └── expression
│       │   │           └── simpleExpression
│       │   │               └── term
│       │   │                   └── factor
│       │   │                       └── INTEGER (10)
│       │   ├── SEMI
│       │   ├── statement
│       │   │   └── assignmentStatement
│       │   │       ├── variable
│       │   │       │   └── IDENTIFIER (y)
│       │   │       ├── ASSIGN
│       │   │       └── expression
│       │   │           └── simpleExpression
│       │   │               └── term
│       │   │                   └── factor
│       │   │                       └── INTEGER (20)
│       │   ├── SEMI
│       │   ├── statement
│       │   │   └── assignmentStatement
│       │   │       ├── variable
│       │   │       │   └── IDENTIFIER (resultado)
│       │   │       ├── ASSIGN
│       │   │       └── expression
│       │   │           └── simpleExpression
│       │   │               └── term
│       │   │                   ├── factor
│       │   │                   │   └── functionCall
│       │   │                   │       ├── IDENTIFIER (soma)
│       │   │                   │       ├── LPAREN
│       │   │                   │       ├── actualParameterList
│       │   │                   │       │   ├── actualParameter
│       │   │                   │       │   │   └── expression
│       │   │                   │       │   │       └── simpleExpression
│       │   │                   │       │   │           └── term
│       │   │                   │       │   │               └── factor
│       │   │                   │       │   │                   └── IDENTIFIER (x)
│       │   │                   │       │   ├── COMMA
│       │   │                   │       │   └── actualParameter
│       │   │                   │       │       └── expression
│       │   │                   │       │           └── simpleExpression
│       │   │                   │       │               └── term
│       │   │                   │       │                   └── factor
│       │   │                   │       │                       └── IDENTIFIER (y)
│       │   │                   │       └── RPAREN
│       │   │                   ├── STAR
│       │   │                   └── factor
│       │   │                       └── REAL (1.5)
│       │   ├── SEMI
│       │   └── statement
│       │       └── procedureStatement
│       │           ├── IDENTIFIER (writeln)
│       │           ├── LPAREN
│       │           ├── actualParameterList
│       │           │   ├── actualParameter
│       │           │   │   └── expression
│       │           │   │       └── simpleExpression
│       │           │   │           └── term
│       │           │   │               └── factor
│       │           │   │                   └── STRING ('Resultado: ')
│       │           │   ├── COMMA
│       │           │   └── actualParameter
│       │           │       └── expression
│       │           │           └── simpleExpression
│       │           │               └── term
│       │           │                   └── factor
│       │           │                       └── IDENTIFIER (resultado)
│       │           └── RPAREN
│       └── END
└── DOT
```

### Precedência de Operadores

O parser implementa a precedência correta dos operadores Pascal:

1. **Parênteses**: `()`
2. **Unários**: `not`, `-`, `+`
3. **Multiplicativos**: `*`, `/`, `div`, `mod`, `and`
4. **Aditivos**: `+`, `-`, `or`
5. **Relacionais**: `=`, `<>`, `<`, `<=`, `>`, `>=`, `in`

### Tratamento de Erros Sintáticos

O parser detecta e reporta erros sintáticos com informações de localização:

```
line 10:5 mismatched input 'begin' expecting 'end'
line 15:0 extraneous input ';' expecting <EOF>
```

### Testando o Analisador Sintático

```bash
# Gerar e visualizar a árvore sintática
make tree FILE=in/exemplo.pas

# Modo debug com interface gráfica
make debug FILE=in/exemplo.pas

# Verificar apenas se o parsing é bem-sucedido
make parse FILE=in/exemplo.pas
```

## Como Usar

### Compilação
```bash
# Compilar o projeto completo
make all
```

### Testando Componentes Individuais

#### Analisador Léxico
```bash
# Gerar apenas os tokens
make tokens FILE=in/programa.pas
```

#### Analisador Sintático
```bash
# Verificar se o parsing é bem-sucedido
make parse FILE=in/programa.pas

# Exibir a árvore sintática em texto
make tree FILE=in/programa.pas

# Visualizar a árvore sintática com interface gráfica
make debug FILE=in/programa.pas
```

### Compilação Completa
```bash
# Compilar um programa Pascal para MIPS
make run FILE=in/programa.pas

# Compilar e executar no simulador MARS
make test FILE=in/programa.pas
```

### Exemplos de Uso

```bash
# Analisar lexicamente um programa
make tokens FILE=in/c01.pas

# Ver a estrutura sintática
make tree FILE=in/c01.pas

# Compilar e testar
make test FILE=in/c01.pas
```

### Limpeza
```bash
# Remover arquivos gerados
make clean
```

### Estrutura dos Comandos Make

| Comando | Descrição | Exemplo |
|---------|-----------|---------|
| `make all` | Compila todo o projeto | `make all` |
| `make tokens FILE=arquivo` | Gera tokens do lexer | `make tokens FILE=in/teste.pas` |
| `make parse FILE=arquivo` | Verifica sintaxe | `make parse FILE=in/teste.pas` |
| `make tree FILE=arquivo` | Exibe árvore sintática | `make tree FILE=in/teste.pas` |
| `make debug FILE=arquivo` | Interface gráfica da árvore | `make debug FILE=in/teste.pas` |
| `make run FILE=arquivo` | Compila para MIPS | `make run FILE=in/teste.pas` |
| `make test FILE=arquivo` | Compila e executa no MARS | `make test FILE=in/teste.pas` |
| `make clean` | Remove arquivos gerados | `make clean` |