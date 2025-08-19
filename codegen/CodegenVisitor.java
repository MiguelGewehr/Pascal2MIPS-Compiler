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
 */
public class CodegenVisitor {
    // Acumula o código MIPS gerado
    private StringBuilder mipsCode = new StringBuilder();
    
    // Tabela de símbolos para acessar informações das variáveis
    private SymbolTable symbolTable;
    private StrTable strTable;
    
    // Contadores para labels únicos
    private int labelCounter = 0;
    private int stringLabelCounter = 0;
    
    // Mapeamento de variáveis para seus labels MIPS
    private Map<String, String> varLabels = new HashMap<>();
    
    // Stack para gerenciar registradores temporários
    private int stackOffset = 0;
    
    /**
     * Gera o código MIPS para o programa fornecido.
     */
    public String generate(AST program, SymbolTable symTable, StrTable stringTable) {
        this.symbolTable = symTable;
        this.strTable = stringTable;
        mipsCode.setLength(0); // Limpa o buffer
        labelCounter = 0;
        stringLabelCounter = 0;
        varLabels.clear();
        stackOffset = 0;
        
        emitHeader();          // Emite cabeçalho MIPS
        visitNode(program);    // Visita todos os nós da AST
        emitFooter();          // Emite rodapé MIPS
        
        return mipsCode.toString();
    }

    // Emite o cabeçalho do programa MIPS
    private void emitHeader() {
        mipsCode.append(".data\n");
        mipsCode.append("newline: .asciiz \"\\n\"\n");
        
        // Declara strings constantes
        if (strTable != null) {
            for (int i = 0; i < strTable.size(); i++) {
                String str = strTable.get(i);
                mipsCode.append("str_" + i + ": .asciiz " + str + "\n");
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

    // Operações de pilha para gerenciar temporários
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

    // Despacha para o método apropriado baseado no tipo do nó
    private void visitNode(AST node) {
        if (node == null) return;
        
        switch (node.kind) {
            case PROGRAM_NODE -> visitProgram(node);
            case BLOCK_NODE -> visitBlock(node);
            case CONST_SECTION_NODE -> visitConstSection(node);
            case VAR_SECTION_NODE -> visitVarSection(node);
            case VAR_DECL_NODE -> visitVarDeclaration(node);
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
            case DIVIDE_NODE -> visitRealDivision(node);
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
            case REAL_VAL_NODE -> visitRealValue(node);
            case BOOL_VAL_NODE -> visitBoolValue(node);
            case CHAR_VAL_NODE -> visitCharValue(node);
            case STR_VAL_NODE -> visitStringValue(node);
            
            // Variáveis
            case VAR_USE_NODE -> visitVariableUse(node);
            case ARRAY_ACCESS_NODE -> visitArrayAccess(node);
            
            // Conversões
            case I2R_NODE -> visitIntToReal(node);
            
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
        
        // Insere declaração na seção .data (será movida para o local correto)
        String currentText = mipsCode.toString();
        int dataEnd = currentText.indexOf(".text");
        if (dataEnd != -1) {
            String dataPart = currentText.substring(0, dataEnd);
            String textPart = currentText.substring(dataEnd);
            
            mipsCode.setLength(0);
            mipsCode.append(dataPart);
            mipsCode.append(label + ": .word 0\n");
            mipsCode.append(textPart);
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
        
        // Avalia a expressão (resultado fica na pilha)
        visitNode(exprNode);
        emitPopTemp("$t0");
        
        // Armazena na variável
        if (varNode.kind == NodeKind.VAR_USE_NODE) {
            String varName = varNode.stringData;
            String label = varLabels.get(varName);
            if (label != null) {
                mipsCode.append("sw $t0, " + label + "\n");
            }
        } else if (varNode.kind == NodeKind.ARRAY_ACCESS_NODE) {
            visitArrayAssignment(varNode, "$t0");
        }
    }

    private void visitArrayAssignment(AST arrayNode, String valueReg) {
        String arrayName = arrayNode.stringData;
        AST indexNode = arrayNode.getChild(0);
        
        // Calcula o índice
        visitNode(indexNode);
        emitPopTemp("$t1"); // índice em $t1
        
        // Calcula endereço: base + (índice * 4)
        String arrayLabel = varLabels.get(arrayName);
        if (arrayLabel != null) {
            mipsCode.append("sll $t1, $t1, 2\n"); // multiplica por 4
            mipsCode.append("la $t2, " + arrayLabel + "\n");
            mipsCode.append("add $t2, $t2, $t1\n");
            mipsCode.append("sw " + valueReg + ", 0($t2)\n");
        }
    }

    private void visitProcedureCall(AST node) {
        String procName = node.stringData;
        
        // Trata procedimentos built-in
        if (isBuiltinProcedure(procName)) {
            visitBuiltinProcedure(node);
        } else {
            // Chamada de procedimento definido pelo usuário
            // TODO: Implementar quando suporte a subrotinas for adicionado
            mipsCode.append("# Chamada de procedimento " + procName + " não implementada\n");
        }
    }

    private boolean isBuiltinProcedure(String name) {
        return name.equals("writeln") || name.equals("write") || 
               name.equals("readln") || name.equals("read");
    }

    private void visitBuiltinProcedure(AST node) {
        String procName = node.stringData;
        
        if (procName.equals("writeln") || procName.equals("write")) {
            // Processa argumentos
            if (node.getChildCount() > 0) {
                AST argsNode = node.getChild(0);
                for (int i = 0; i < argsNode.getChildCount(); i++) {
                    visitNode(argsNode.getChild(i));
                    emitPopTemp("$a0");
                    
                    // Determina o tipo baseado no nó da expressão
                    AST argNode = argsNode.getChild(i);
                    if (argNode.type == Type.INTEGER) {
                        mipsCode.append("li $v0, 1\n"); // print integer
                    } else if (argNode.type == Type.REAL) {
                        mipsCode.append("li $v0, 2\n"); // print float
                    } else if (argNode.type == Type.STRING) {
                        mipsCode.append("li $v0, 4\n"); // print string
                    } else {
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

    private void visitRealDivision(AST node) {
        // TODO: Implementar divisão real usando ponto flutuante
        visitBinaryOp(node, "div");
        mipsCode.append("mflo $t0\n");
        emitPushTemp("$t0");
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

    private void visitIntValue(AST node) {
        mipsCode.append("li $t0, " + node.intData + "\n");
        emitPushTemp("$t0");
    }

    private void visitRealValue(AST node) {
        // TODO: Implementar suporte completo a ponto flutuante
        // Por enquanto, converte para inteiro
        int intValue = (int) node.floatData;
        mipsCode.append("li $t0, " + intValue + "\n");
        emitPushTemp("$t0");
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
            mipsCode.append("lw $t0, " + label + "\n");
            emitPushTemp("$t0");
        } else {
            // Pode ser uma constante
            mipsCode.append("li $t0, 0\n"); // valor padrão
            emitPushTemp("$t0");
        }
    }

    private void visitArrayAccess(AST node) {
        String arrayName = node.stringData;
        AST indexNode = node.getChild(0);
        
        // Calcula o índice
        visitNode(indexNode);
        emitPopTemp("$t1"); // índice
        
        // Calcula endereço: base + (índice * 4)
        String arrayLabel = varLabels.get(arrayName);
        if (arrayLabel != null) {
            mipsCode.append("sll $t1, $t1, 2\n"); // multiplica por 4
            mipsCode.append("la $t0, " + arrayLabel + "\n");
            mipsCode.append("add $t0, $t0, $t1\n");
            mipsCode.append("lw $t0, 0($t0)\n");
            emitPushTemp("$t0");
        }
    }

    private void visitIntToReal(AST node) {
        // Por enquanto, não faz conversão real
        // Apenas visita o filho
        if (node.getChildCount() > 0) {
            visitNode(node.getChild(0));
        }
    }
}