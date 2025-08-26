package codegen;

import ast.*;
import typing.Type;
import entries.*;
import tables.SymbolTable;
import tables.StrTable;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * CodegenVisitor gera código MIPS para um programa Pascal.
 * 
 * FUNCIONALIDADES AINDA NÃO IMPLEMENTADAS:
 * - Chamadas de procedimentos definidos pelo usuário
 * - Procedimentos built-in read/readln para entrada de dados
 * - Subrotinas (procedures e functions) definidas pelo usuário
 * - Passagem de parâmetros por referência e valor
 * - Escopo local de variáveis em subrotinas
 */
public class CodegenVisitor {
    // Acumula o código MIPS gerado
    private StringBuilder mipsCode = new StringBuilder();
    
    // Tabela de símbolos para acessar informações das variáveis
    private SymbolTable symbolTable;
    private StrTable strTable;
    
    // Contadores para labels únicos
    private int labelCounter = 0;
    
    // Mapeamento de variáveis para seus labels MIPS
    private Map<String, String> varLabels = new HashMap<>();
    
    // Informações sobre arrays (nome -> [tamanho, tipo])
    private Map<String, ArrayInfo> arrayInfo = new HashMap<>();
    
    // Stack para gerenciar registradores temporários
    private int stackOffset = 0;
    
    // Classe auxiliar para informações de array
    private static class ArrayInfo {
        public final int size;
        public final Type elementType;
        public final int startIndex;
        
        public ArrayInfo(int size, Type elementType, int startIndex) {
            this.size = size;
            this.elementType = elementType;
            this.startIndex = startIndex;
        }
    }
    
    /**
     * Gera o código MIPS para o programa fornecido.
     */
    public String generate(AST program, SymbolTable symTable, StrTable stringTable) {
        this.symbolTable = symTable;
        this.strTable = stringTable;
        mipsCode.setLength(0); // Limpa o buffer
        labelCounter = 0;
        varLabels.clear();
        arrayInfo.clear();
        stackOffset = 0;
        
        emitHeader();          // Emite cabeçalho MIPS
        visitNode(program);    // Visita todos os nós da AST
        emitFooter();          // Emite rodapé MIPS
        
        return mipsCode.toString();
    }

    // Método auxiliar para converter string Pascal para MIPS
    private String convertPascalStringToMips(String pascalStr) {
        if (pascalStr == null) return "\"\"";
        
        // Remove aspas simples se presentes
        String content = pascalStr;
        if (pascalStr.startsWith("'") && pascalStr.endsWith("'")) {
            content = pascalStr.substring(1, pascalStr.length() - 1);
        }
        
        // Escapa caracteres especiais para MIPS
        content = content.replace("\\", "\\\\");  // Escape backslash
        content = content.replace("\"", "\\\"");  // Escape aspas duplas
        content = content.replace("\n", "\\n");   // Escape newline
        content = content.replace("\t", "\\t");   // Escape tab
        
        return "\"" + content + "\"";
    }

    // Versão melhorada do emitHeader usando o método auxiliar
    private void emitHeader() {
        mipsCode.append(".data\n");
        mipsCode.append("newline: .asciiz \"\\n\"\n");
        
        // Declara strings constantes
        if (strTable != null) {
            for (int i = 0; i < strTable.size(); i++) {
                String str = strTable.get(i);
                String mipsStr = convertPascalStringToMips(str);
                mipsCode.append("str_" + i + ": .asciiz " + mipsStr + "\n");
            }
        }
        
        mipsCode.append("\n.text\n.globl main\nmain:\n");
    }

    // Emite o rodapé do programa MIPS
    private void emitFooter() {
        mipsCode.append("li $v0, 10\n"); // Syscall para encerrar programa
        mipsCode.append("syscall\n");
    }

    // Gera um label único
    private String generateLabel(String prefix) {
        return prefix + "_" + (labelCounter++);
    }

    // Operações de pilha para gerenciar temporários inteiros
    private void emitPushTemp(String reg) {
        mipsCode.append("subu $sp, $sp, 4\n");
        mipsCode.append("sw " + reg + ", 0($sp)\n");
        stackOffset += 4;
    }

    private void emitPopTemp(String reg) {
        mipsCode.append("lw " + reg + ", 0($sp)\n");
        mipsCode.append("addu $sp, $sp, 4\n");
        stackOffset -= 4;
    }

    // Operações de pilha para gerenciar temporários float
    private void emitPushFloat(String freg) {
        mipsCode.append("subu $sp, $sp, 4\n");
        mipsCode.append("swc1 " + freg + ", 0($sp)\n");
        stackOffset += 4;
    }

    private void emitPopFloat(String freg) {
        mipsCode.append("lwc1 " + freg + ", 0($sp)\n");
        mipsCode.append("addu $sp, $sp, 4\n");
        stackOffset -= 4;
    }

    // Despacha para o método apropriado baseado no tipo do nó
    private void visitNode(AST node) {
        if (node == null) return;
        
        switch (node.kind) {
            case PROGRAM_NODE -> visitProgram(node);
            case BLOCK_NODE -> visitBlock(node);
            case CONST_SECTION_NODE -> visitConstSection(node);
            case VAR_SECTION_NODE -> visitVarSection(node);
            case VAR_DECL_NODE -> visitVarDeclaration(node);
            case ARRAY_TYPE_NODE -> visitArrayType(node);
            case RANGE_NODE -> visitRange(node);
            case COMPOUND_STMT_NODE -> visitCompoundStatement(node);
            case ASSIGN_NODE -> visitAssignment(node);
            case PROC_CALL_NODE -> visitProcedureCall(node);
            case IF_NODE -> visitIfStatement(node);
            case WHILE_NODE -> visitWhileStatement(node);
            case EMPTY_STMT_NODE -> { /* Não faz nada */ }
            
            // Expressões aritméticas
            case PLUS_NODE -> visitBinaryOp(node, "add");
            case MINUS_NODE -> visitBinaryOp(node, "sub");
            case TIMES_NODE -> visitBinaryOp(node, "mul");
            case DIVIDE_NODE -> visitRealDivision(node);  // Nova operação para divisão real
            case DIV_NODE -> visitIntegerDivision(node);
            case MOD_NODE -> visitModulo(node);
            
            // Expressões lógicas
            case AND_NODE -> visitLogicalAnd(node);
            case OR_NODE -> visitLogicalOr(node);
            case NOT_NODE -> visitLogicalNot(node);
            
            // Comparações
            case EQ_NODE -> visitComparison(node, "eq");
            case NEQ_NODE -> visitComparison(node, "ne");
            case LT_NODE -> visitComparison(node, "lt");
            case GT_NODE -> visitComparison(node, "gt");
            case LE_NODE -> visitComparison(node, "le");
            case GE_NODE -> visitComparison(node, "ge");
            
            // Valores
            case INT_VAL_NODE -> visitIntValue(node);
            case REAL_VAL_NODE -> visitRealValue(node);  // Novo caso para valores reais
            case BOOL_VAL_NODE -> visitBoolValue(node);
            case CHAR_VAL_NODE -> visitCharValue(node);
            case STR_VAL_NODE -> visitStringValue(node);
            
            // Variáveis
            case VAR_USE_NODE -> visitVariableUse(node);
            case ARRAY_ACCESS_NODE -> visitArrayAccess(node);
            
            // Conversões de tipo
            case I2R_NODE -> visitIntegerToReal(node);  // Nova conversão
            
            default -> {
                // Para nós não implementados, visita os filhos
                for (int i = 0; i < node.getChildCount(); i++) {
                    visitNode(node.getChild(i));
                }
            }
        }
    }

    private void visitProgram(AST node) {
        // Visita o bloco principal do programa
        if (node.getChildCount() > 0) {
            visitNode(node.getChild(0));
        }
    }

    private void visitBlock(AST node) {
        // Visita todos os filhos do bloco
        for (int i = 0; i < node.getChildCount(); i++) {
            visitNode(node.getChild(i));
        }
    }

    private void visitConstSection(AST node) {
        // Constantes são processadas durante a análise semântica
        // Não precisamos gerar código especial aqui
    }

    private void visitVarSection(AST node) {
        // Aloca espaço para variáveis na seção .data
        mipsCode.append("# Variáveis globais\n");
        for (int i = 0; i < node.getChildCount(); i++) {
            visitNode(node.getChild(i));
        }
    }

    private void visitVarDeclaration(AST node) {
        String varName = node.stringData;
        String label = "var_" + varName;
        varLabels.put(varName, label);
        
        // Verifica se é declaração de array
        if (node.getChildCount() > 0 && node.getChild(0).kind == NodeKind.ARRAY_TYPE_NODE) {
            visitArrayDeclaration(node);
        } else {
            // Declaração de variável simples
            insertVariableDeclaration(label, node.type);
        }
    }
    
    private void visitArrayDeclaration(AST varDeclNode) {
        String varName = varDeclNode.stringData;
        String label = varLabels.get(varName);
        AST arrayTypeNode = varDeclNode.getChild(0);
        
        if (arrayTypeNode.getChildCount() >= 2) {
            AST rangeNode = arrayTypeNode.getChild(0);
            // O tipo do elemento está no segundo filho do ARRAY_TYPE_NODE
            Type elementType = arrayTypeNode.getChild(1).type;
            
            // Extrai informações do range
            int startIndex = 0, endIndex = 0;
            if (rangeNode.getChildCount() == 2) {
                AST startNode = rangeNode.getChild(0);
                AST endNode = rangeNode.getChild(1);
                
                if (startNode.kind == NodeKind.INT_VAL_NODE) {
                    startIndex = startNode.intData;
                }
                if (endNode.kind == NodeKind.INT_VAL_NODE) {
                    endIndex = endNode.intData;
                }
            }
            
            int size = endIndex - startIndex + 1;
            arrayInfo.put(varName, new ArrayInfo(size, elementType, startIndex));
            
            // Insere declaração do array na seção .data
            insertArrayDeclaration(label, size, elementType);
        }
    }
    
    private void insertVariableDeclaration(String label, Type type) {
        // Insere declaração na seção .data (será movida para o local correto)
        String currentText = mipsCode.toString();
        int dataEnd = currentText.indexOf(".text");
        if (dataEnd != -1) {
            String dataPart = currentText.substring(0, dataEnd);
            String textPart = currentText.substring(dataEnd);
            
            mipsCode.setLength(0);
            mipsCode.append(dataPart);
            
            // Aloca espaço baseado no tipo
            if (type == Type.REAL) {
                mipsCode.append(label + ": .float 0.0\n");  // Variável float
            } else {
                mipsCode.append(label + ": .word 0\n");     // Variável inteira
            }
            
            mipsCode.append(textPart);
        }
    }
    
    private void insertArrayDeclaration(String label, int size, Type elementType) {
        // Insere declaração do array na seção .data
        String currentText = mipsCode.toString();
        int dataEnd = currentText.indexOf(".text");
        if (dataEnd != -1) {
            String dataPart = currentText.substring(0, dataEnd);
            String textPart = currentText.substring(dataEnd);
            
            mipsCode.setLength(0);
            mipsCode.append(dataPart);
            
            // Aloca espaço para o array baseado no tipo do elemento
            if (elementType == Type.REAL) {
                mipsCode.append(label + ": .float ");
                for (int i = 0; i < size; i++) {
                    mipsCode.append("0.0");
                    if (i < size - 1) mipsCode.append(", ");
                }
                mipsCode.append("\n");
            } else {
                mipsCode.append(label + ": .word ");
                for (int i = 0; i < size; i++) {
                    mipsCode.append("0");
                    if (i < size - 1) mipsCode.append(", ");
                }
                mipsCode.append("\n");
            }
            
            mipsCode.append(textPart);
        }
    }
    
    private void visitArrayType(AST node) {
        // Processa o tipo array - os filhos já são visitados pelo visitNode
        for (int i = 0; i < node.getChildCount(); i++) {
            visitNode(node.getChild(i));
        }
    }
    
    private void visitRange(AST node) {
        // Processa o range do array - os valores são acessados diretamente
        // nos métodos que precisam deles
        for (int i = 0; i < node.getChildCount(); i++) {
            visitNode(node.getChild(i));
        }
    }

    private void visitCompoundStatement(AST node) {
        // Visita o conteúdo do statement composto
        if (node.getChildCount() > 0) {
            visitNode(node.getChild(0));
        }
    }

    private void visitAssignment(AST node) {
        if (node.getChildCount() != 2) return;
        
        AST varNode = node.getChild(0);
        AST exprNode = node.getChild(1);
        
        // Avalia a expressão
        visitNode(exprNode);
        
        // Armazena na variável baseado no tipo
        if (varNode.kind == NodeKind.VAR_USE_NODE) {
            String varName = varNode.stringData;
            String label = varLabels.get(varName);
            if (label != null) {
                if (varNode.type == Type.REAL) {
                    emitPopFloat("$f0");
                    mipsCode.append("swc1 $f0, " + label + "\n");
                } else {
                    emitPopTemp("$t0");
                    mipsCode.append("sw $t0, " + label + "\n");
                }
            }
        } else if (varNode.kind == NodeKind.ARRAY_ACCESS_NODE) {
            if (varNode.type == Type.REAL) {
                emitPopFloat("$f0");
                visitArrayAssignmentFloat(varNode, "$f0");
            } else {
                emitPopTemp("$t0");
                visitArrayAssignment(varNode, "$t0");
            }
        }
    }

    private void visitArrayAssignment(AST arrayNode, String valueReg) {
        String arrayName = arrayNode.stringData;
        AST indexNode = arrayNode.getChild(0);
        
        // Calcula o índice
        visitNode(indexNode);
        emitPopTemp("$t1"); // índice em $t1
        
        // Ajusta o índice baseado no startIndex do array
        ArrayInfo info = arrayInfo.get(arrayName);
        if (info != null && info.startIndex != 0) {
            mipsCode.append("addi $t1, $t1, " + (-info.startIndex) + "\n");
        }
        
        // Calcula endereço: base + (índice * 4)
        String arrayLabel = varLabels.get(arrayName);
        if (arrayLabel != null) {
            mipsCode.append("sll $t1, $t1, 2\n"); // multiplica por 4
            mipsCode.append("la $t2, " + arrayLabel + "\n");
            mipsCode.append("add $t2, $t2, $t1\n");
            mipsCode.append("sw " + valueReg + ", 0($t2)\n");
        }
    }

    private void visitArrayAssignmentFloat(AST arrayNode, String valueReg) {
        String arrayName = arrayNode.stringData;
        AST indexNode = arrayNode.getChild(0);
        
        // Calcula o índice
        visitNode(indexNode);
        emitPopTemp("$t1"); // índice em $t1
        
        // Ajusta o índice baseado no startIndex do array
        ArrayInfo info = arrayInfo.get(arrayName);
        if (info != null && info.startIndex != 0) {
            mipsCode.append("addi $t1, $t1, " + (-info.startIndex) + "\n");
        }
        
        // Calcula endereço: base + (índice * 4)
        String arrayLabel = varLabels.get(arrayName);
        if (arrayLabel != null) {
            mipsCode.append("sll $t1, $t1, 2\n"); // multiplica por 4
            mipsCode.append("la $t2, " + arrayLabel + "\n");
            mipsCode.append("add $t2, $t2, $t1\n");
            mipsCode.append("swc1 " + valueReg + ", 0($t2)\n");
        }
    }

    private void visitProcedureCall(AST node) {
        String procName = node.stringData;
        
        // Trata apenas procedimentos built-in básicos
        if (procName.equals("writeln") || procName.equals("write")) {
            visitBuiltinWrite(node);
        } else if (procName.equals("read") || procName.equals("readln")) {
            visitBuiltinRead(node);
        }
    }

    private void visitBuiltinWrite(AST node) {
        String procName = node.stringData;
        
        // Processa argumentos
        if (node.getChildCount() > 0) {
            AST argsNode = node.getChild(0);
            for (int i = 0; i < argsNode.getChildCount(); i++) {
                visitNode(argsNode.getChild(i));
                
                // Determina o tipo baseado no nó da expressão
                AST argNode = argsNode.getChild(i);
                if (argNode.type == Type.REAL) {
                    emitPopFloat("$f12");
                    mipsCode.append("li $v0, 2\n"); // print float
                } else if (argNode.type == Type.INTEGER) {
                    emitPopTemp("$a0");
                    mipsCode.append("li $v0, 1\n"); // print integer
                } else if (argNode.type == Type.STRING) {
                    emitPopTemp("$a0");
                    mipsCode.append("li $v0, 4\n"); // print string
                } else {
                    emitPopTemp("$a0");
                    mipsCode.append("li $v0, 1\n"); // default to integer
                }
                mipsCode.append("syscall\n");
            }
        }
        
        if (procName.equals("writeln")) {
            // Imprime nova linha
            mipsCode.append("la $a0, newline\n");
            mipsCode.append("li $v0, 4\n");
            mipsCode.append("syscall\n");
        }
    }

    private void visitBuiltinRead(AST node) {
        String procName = node.stringData;
        
        // Processa argumentos (variáveis para ler)
        if (node.getChildCount() > 0) {
            AST argsNode = node.getChild(0);
            for (int i = 0; i < argsNode.getChildCount(); i++) {
                AST argNode = argsNode.getChild(i);
                
                // Verifica se é uma variável ou acesso a array
                if (argNode.kind == NodeKind.VAR_USE_NODE) {
                    readIntoVariable(argNode);
                } else if (argNode.kind == NodeKind.ARRAY_ACCESS_NODE) {
                    readIntoArrayElement(argNode);
                }
            }
        }
    }

    private void readIntoVariable(AST varNode) {
        String varName = varNode.stringData;
        String label = varLabels.get(varName);
        
        if (label != null) {
            if (varNode.type == Type.REAL) {
                // Lê um float
                mipsCode.append("li $v0, 6\n");      // syscall para ler float
                mipsCode.append("syscall\n");
                mipsCode.append("swc1 $f0, " + label + "\n"); // armazena o float lido
            } else if (varNode.type == Type.INTEGER) {
                // Lê um inteiro
                mipsCode.append("li $v0, 5\n");      // syscall para ler inteiro
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, " + label + "\n"); // armazena o inteiro lido
            } else if (varNode.type == Type.CHAR) {
                // Lê um caractere
                mipsCode.append("li $v0, 12\n");     // syscall para ler caractere
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, " + label + "\n"); // armazena o caractere lido
            }
        }
    }

    private void readIntoArrayElement(AST arrayNode) {
        String arrayName = arrayNode.stringData;
        AST indexNode = arrayNode.getChild(0);
        
        // Calcula o índice do array
        visitNode(indexNode);
        emitPopTemp("$t1"); // índice em $t1
        
        // Ajusta o índice baseado no startIndex do array
        ArrayInfo info = arrayInfo.get(arrayName);
        if (info != null && info.startIndex != 0) {
            mipsCode.append("addi $t1, $t1, " + (-info.startIndex) + "\n");
        }
        
        // Calcula endereço: base + (índice * 4)
        String arrayLabel = varLabels.get(arrayName);
        if (arrayLabel != null) {
            mipsCode.append("sll $t1, $t1, 2\n"); // multiplica por 4
            mipsCode.append("la $t2, " + arrayLabel + "\n");
            mipsCode.append("add $t2, $t2, $t1\n"); // $t2 = endereço do elemento
            
            // Lê o valor baseado no tipo do array
            if (arrayNode.type == Type.REAL) {
                mipsCode.append("li $v0, 6\n");       // syscall para ler float
                mipsCode.append("syscall\n");
                mipsCode.append("swc1 $f0, 0($t2)\n"); // armazena no elemento do array
            } else if (arrayNode.type == Type.INTEGER) {
                mipsCode.append("li $v0, 5\n");       // syscall para ler inteiro
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, 0($t2)\n");  // armazena no elemento do array
            } else if (arrayNode.type == Type.CHAR) {
                mipsCode.append("li $v0, 12\n");      // syscall para ler caractere
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, 0($t2)\n");  // armazena no elemento do array
            }
        }
    }
    private void visitIfStatement(AST node) {
        if (node.getChildCount() < 2) return;
        
        String elseLabel = generateLabel("else");
        String endLabel = generateLabel("endif");
        
        // Avalia condição
        visitNode(node.getChild(0));
        emitPopTemp("$t0");
        
        // Salta para else se falso
        mipsCode.append("beq $t0, $zero, " + elseLabel + "\n");
        
        // Statement THEN
        visitNode(node.getChild(1));
        mipsCode.append("j " + endLabel + "\n");
        
        // Label ELSE
        mipsCode.append(elseLabel + ":\n");
        
        // Statement ELSE (se existir)
        if (node.getChildCount() > 2) {
            visitNode(node.getChild(2));
        }
        
        mipsCode.append(endLabel + ":\n");
    }

    private void visitWhileStatement(AST node) {
        if (node.getChildCount() < 2) return;
        
        String loopLabel = generateLabel("loop");
        String endLabel = generateLabel("endloop");
        
        mipsCode.append(loopLabel + ":\n");
        
        // Avalia condição
        visitNode(node.getChild(0));
        emitPopTemp("$t0");
        
        // Salta para fim se falso
        mipsCode.append("beq $t0, $zero, " + endLabel + "\n");
        
        // Corpo do loop
        visitNode(node.getChild(1));
        
        // Volta para o início
        mipsCode.append("j " + loopLabel + "\n");
        mipsCode.append(endLabel + ":\n");
    }

    private void visitBinaryOp(AST node, String operation) {
        if (node.getChildCount() != 2) return;
        
        // Verifica se é operação com floats
        if (node.type == Type.REAL) {
            visitFloatBinaryOp(node, operation);
            return;
        }
        
        // Avalia operandos
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        // Pop em ordem reversa
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        // Executa operação
        mipsCode.append(operation + " $t0, $t0, $t1\n");
        
        // Empilha resultado
        emitPushTemp("$t0");
    }

    private void visitFloatBinaryOp(AST node, String operation) {
        if (node.getChildCount() != 2) return;
        
        // Avalia operandos
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        // Pop em ordem reversa
        emitPopFloat("$f1");
        emitPopFloat("$f0");
        
        // Executa operação float
        String floatOp = switch (operation) {
            case "add" -> "add.s";
            case "sub" -> "sub.s";
            case "mul" -> "mul.s";
            default -> "add.s";
        };
        
        mipsCode.append(floatOp + " $f0, $f0, $f1\n");
        
        // Empilha resultado
        emitPushFloat("$f0");
    }

    private void visitRealDivision(AST node) {
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopFloat("$f1");
        emitPopFloat("$f0");
        
        mipsCode.append("div.s $f0, $f0, $f1\n");
        
        emitPushFloat("$f0");
    }

    private void visitIntegerDivision(AST node) {
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        mipsCode.append("div $t0, $t1\n");
        mipsCode.append("mflo $t0\n");
        
        emitPushTemp("$t0");
    }

    private void visitModulo(AST node) {
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        mipsCode.append("div $t0, $t1\n");
        mipsCode.append("mfhi $t0\n"); // resto da divisão
        
        emitPushTemp("$t0");
    }

    private void visitLogicalAnd(AST node) {
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        mipsCode.append("and $t0, $t0, $t1\n");
        
        emitPushTemp("$t0");
    }

    private void visitLogicalOr(AST node) {
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        mipsCode.append("or $t0, $t0, $t1\n");
        
        emitPushTemp("$t0");
    }

    private void visitLogicalNot(AST node) {
        if (node.getChildCount() != 1) return;
        
        visitNode(node.getChild(0));
        emitPopTemp("$t0");
        
        mipsCode.append("seq $t0, $t0, $zero\n"); // set equal to zero
        
        emitPushTemp("$t0");
    }

    private void visitComparison(AST node, String operation) {
        if (node.getChildCount() != 2) return;
        
        // Verifica se é comparação de floats
        AST left = node.getChild(0);
        AST right = node.getChild(1);
        
        if (left.type == Type.REAL || right.type == Type.REAL) {
            visitFloatComparison(node, operation);
            return;
        }
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        String instruction = switch (operation) {
            case "eq" -> "seq";
            case "ne" -> "sne";
            case "lt" -> "slt";
            case "le" -> "sle";
            case "gt" -> "sgt";
            case "ge" -> "sge";
            default -> "seq";
        };
        
        mipsCode.append(instruction + " $t0, $t0, $t1\n");
        
        emitPushTemp("$t0");
    }

    private void visitFloatComparison(AST node, String operation) {
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopFloat("$f1");
        emitPopFloat("$f0");
        
        String instruction = switch (operation) {
            case "eq" -> "c.eq.s";
            case "ne" -> "c.eq.s";  // negamos depois
            case "lt" -> "c.lt.s";
            case "le" -> "c.le.s";
            case "gt" -> "c.lt.s";  // trocamos operandos
            case "ge" -> "c.le.s";  // trocamos operandos
            default -> "c.eq.s";
        };
        
        if (operation.equals("gt") || operation.equals("ge")) {
            mipsCode.append(instruction + " $f1, $f0\n"); // operandos trocados
        } else {
            mipsCode.append(instruction + " $f0, $f1\n");
        }
        
        // Converte resultado da comparação para inteiro
        String trueLabel = generateLabel("true_cmp");
        String endLabel = generateLabel("end_cmp");
        
        if (operation.equals("ne")) {
            mipsCode.append("bc1f " + trueLabel + "\n"); // salta se falso (diferente)
            mipsCode.append("li $t0, 0\n");
            mipsCode.append("j " + endLabel + "\n");
            mipsCode.append(trueLabel + ":\n");
            mipsCode.append("li $t0, 1\n");
        } else {
            mipsCode.append("bc1t " + trueLabel + "\n"); // salta se verdadeiro
            mipsCode.append("li $t0, 0\n");
            mipsCode.append("j " + endLabel + "\n");
            mipsCode.append(trueLabel + ":\n");
            mipsCode.append("li $t0, 1\n");
        }
        
        mipsCode.append(endLabel + ":\n");
        emitPushTemp("$t0");
    }

    private void visitIntValue(AST node) {
        mipsCode.append("li $t0, " + node.intData + "\n");
        emitPushTemp("$t0");
    }

    private void visitRealValue(AST node) {
        // Para valores reais, criamos uma constante float na seção .data
        String floatLabel = generateLabel("float_const");
        
        // Insere declaração na seção .data
        String currentText = mipsCode.toString();
        int dataEnd = currentText.indexOf(".text");
        if (dataEnd != -1) {
            String dataPart = currentText.substring(0, dataEnd);
            String textPart = currentText.substring(dataEnd);
            
            mipsCode.setLength(0);
            mipsCode.append(dataPart);
            mipsCode.append(floatLabel + ": .float " + node.floatData + "\n");
            mipsCode.append(textPart);
        }
        
        // Carrega o valor float
        mipsCode.append("lwc1 $f0, " + floatLabel + "\n");
        emitPushFloat("$f0");
    }

    private void visitBoolValue(AST node) {
        mipsCode.append("li $t0, " + node.intData + "\n");
        emitPushTemp("$t0");
    }

    private void visitCharValue(AST node) {
        // Converte char para seu valor ASCII
        if (node.stringData != null && !node.stringData.isEmpty()) {
            char c = node.stringData.charAt(1); // Remove aspas
            mipsCode.append("li $t0, " + (int) c + "\n");
        } else {
            mipsCode.append("li $t0, 0\n");
        }
        emitPushTemp("$t0");
    }

    private void visitStringValue(AST node) {
        // Encontra o índice da string na tabela
        if (strTable != null && node.stringData != null) {
            int index = strTable.indexOf(node.stringData);
            if (index >= 0) {
                mipsCode.append("la $t0, str_" + index + "\n");
                emitPushTemp("$t0");
            }
        }
    }

    private void visitVariableUse(AST node) {
        String varName = node.stringData;
        String label = varLabels.get(varName);
        
        if (label != null) {
            if (node.type == Type.REAL) {
                mipsCode.append("lwc1 $f0, " + label + "\n");
                emitPushFloat("$f0");
            } else {
                mipsCode.append("lw $t0, " + label + "\n");
                emitPushTemp("$t0");
            }
        } else {
            // Pode ser uma constante
            if (node.type == Type.REAL) {
                mipsCode.append("li.s $f0, 0.0\n");
                emitPushFloat("$f0");
            } else {
                mipsCode.append("li $t0, 0\n");
                emitPushTemp("$t0");
            }
        }
    }

    private void visitArrayAccess(AST node) {
        String arrayName = node.stringData;
        AST indexNode = node.getChild(0);
        
        // Calcula o índice
        visitNode(indexNode);
        emitPopTemp("$t1"); // índice
        
        // Ajusta o índice baseado no startIndex do array
        ArrayInfo info = arrayInfo.get(arrayName);
        if (info != null && info.startIndex != 0) {
            mipsCode.append("addi $t1, $t1, " + (-info.startIndex) + "\n");
        }
        
        // Calcula endereço: base + (índice * 4)
        String arrayLabel = varLabels.get(arrayName);
        if (arrayLabel != null) {
            mipsCode.append("sll $t1, $t1, 2\n"); // multiplica por 4
            mipsCode.append("la $t0, " + arrayLabel + "\n");
            mipsCode.append("add $t0, $t0, $t1\n");
            
            if (node.type == Type.REAL) {
                mipsCode.append("lwc1 $f0, 0($t0)\n");
                emitPushFloat("$f0");
            } else {
                mipsCode.append("lw $t0, 0($t0)\n");
                emitPushTemp("$t0");
            }
        }
    }

    private void visitIntegerToReal(AST node) {
        if (node.getChildCount() != 1) return;
        
        // Avalia o valor inteiro
        visitNode(node.getChild(0));
        emitPopTemp("$t0");
        
        // Converte inteiro para float
        mipsCode.append("mtc1 $t0, $f0\n");    // move para coprocessador
        mipsCode.append("cvt.s.w $f0, $f0\n"); // converte word para single
        
        emitPushFloat("$f0");
    }
}