package codegen;

import ast.*;
import typing.Type;
import entries.*;
import tables.SymbolTable;
import tables.StrTable;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

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
    
    // Gerenciamento de escopo e funções
    private Stack<FunctionContext> functionStack = new Stack<>();
    private Map<String, FunctionInfo> functionInfo = new HashMap<>();
    private FunctionContext currentFunction = null;
    
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
    
    // Classe auxiliar para informações de função
    private static class FunctionInfo {
        public final String name;
        public final Type returnType;
        public final List<ParameterInfo> parameters;
        public final String label;
        public final boolean isFunction; // true para function, false para procedure
        
        public FunctionInfo(String name, Type returnType, List<ParameterInfo> parameters, 
                           String label, boolean isFunction) {
            this.name = name;
            this.returnType = returnType;
            this.parameters = parameters;
            this.label = label;
            this.isFunction = isFunction;
        }
    }
    
    // Classe auxiliar para informações de parâmetro
    private static class ParameterInfo {
        public final String name;
        public final Type type;
        public final boolean isByReference;
        public final int offset; // offset no stack frame
        
        public ParameterInfo(String name, Type type, boolean isByReference, int offset) {
            this.name = name;
            this.type = type;
            this.isByReference = isByReference;
            this.offset = offset;
        }
    }
    
    // Classe auxiliar para contexto de função
    private static class FunctionContext {
        public final String name;
        public final String label;
        public final boolean isFunction;
        public final Type returnType;
        public final Map<String, Integer> localVarOffsets = new HashMap<>();
        public int localVarSize = 0;
        public int parameterSize = 0;
        
        public FunctionContext(String name, String label, boolean isFunction, Type returnType) {
            this.name = name;
            this.label = label;
            this.isFunction = isFunction;
            this.returnType = returnType;
        }
    }
    
    /**
     * Gera o código MIPS para o programa fornecido.
     */
    public String generate(AST program, SymbolTable symTable, StrTable stringTable) {
        System.out.println("DEBUG: Entrando em generate()");
        this.symbolTable = symTable;
        this.strTable = stringTable;
        mipsCode.setLength(0); // Limpa o buffer
        labelCounter = 0;
        varLabels.clear();
        arrayInfo.clear();
        functionInfo.clear();
        functionStack.clear();
        stackOffset = 0;
        currentFunction = null;
        
        emitHeader();          // Emite cabeçalho MIPS
        visitNode(program);    // Visita todos os nós da AST
        emitFooter();          // Emite rodapé MIPS
        
        System.out.println("DEBUG: Saindo de generate()");
        return mipsCode.toString();
    }

    // Método auxiliar para converter string Pascal para MIPS
    private String convertPascalStringToMips(String pascalStr) {
        System.out.println("DEBUG: Entrando em convertPascalStringToMips()");
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
        
        System.out.println("DEBUG: Saindo de convertPascalStringToMips()");
        return "\"" + content + "\"";
    }

    // Versão melhorada do emitHeader usando o método auxiliar
    private void emitHeader() {
        System.out.println("DEBUG: Entrando em emitHeader()");
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
        // Inicializa frame pointer para main
        mipsCode.append("move $fp, $sp\n");
        System.out.println("DEBUG: Saindo de emitHeader()");
    }

    // Emite o rodapé do programa MIPS
    private void emitFooter() {
        System.out.println("DEBUG: Entrando em emitFooter()");
        mipsCode.append("li $v0, 10\n"); // Syscall para encerrar programa
        mipsCode.append("syscall\n");
        System.out.println("DEBUG: Saindo de emitFooter()");
    }

    // Gera um label único
    private String generateLabel(String prefix) {
        System.out.println("DEBUG: Entrando em generateLabel()");
        String label = prefix + "_" + (labelCounter++);
        System.out.println("DEBUG: Saindo de generateLabel()");
        return label;
    }

    // Operações de pilha para gerenciar temporários inteiros
    private void emitPushTemp(String reg) {
        System.out.println("DEBUG: Entrando em emitPushTemp()");
        mipsCode.append("subu $sp, $sp, 4\n");
        mipsCode.append("sw " + reg + ", 0($sp)\n");
        stackOffset += 4;
        System.out.println("DEBUG: Saindo de emitPushTemp()");
    }

    private void emitPopTemp(String reg) {
        System.out.println("DEBUG: Entrando em emitPopTemp()");
        mipsCode.append("lw " + reg + ", 0($sp)\n");
        mipsCode.append("addu $sp, $sp, 4\n");
        stackOffset -= 4;
        System.out.println("DEBUG: Saindo de emitPopTemp()");
    }

    // Operações de pilha para gerenciar temporários float
    private void emitPushFloat(String freg) {
        System.out.println("DEBUG: Entrando em emitPushFloat()");
        mipsCode.append("subu $sp, $sp, 4\n");
        mipsCode.append("swc1 " + freg + ", 0($sp)\n");
        stackOffset += 4;
        System.out.println("DEBUG: Saindo de emitPushFloat()");
    }

    private void emitPopFloat(String freg) {
        System.out.println("DEBUG: Entrando em emitPopFloat()");
        mipsCode.append("lwc1 " + freg + ", 0($sp)\n");
        mipsCode.append("addu $sp, $sp, 4\n");
        stackOffset -= 4;
        System.out.println("DEBUG: Saindo de emitPopFloat()");
    }

    // Despacha para o método apropriado baseado no tipo do nó
    private void visitNode(AST node) {
        System.out.println("DEBUG: Visitando nó: " + node.kind);
        if (node == null) {
            System.out.println("DEBUG: Nó nulo, ignorando.");
            return;
        }
        
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
            
            // Implementações para funções
            case FUNC_DECL_NODE -> visitFunctionDeclaration(node);
            case PROC_DECL_NODE -> visitProcedureDeclaration(node);
            case FUNC_CALL_NODE -> visitFunctionCall(node);
            case PARAM_LIST_NODE -> visitParameterList(node);
            case PARAM_NODE -> visitParameterDeclaration(node);
            
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
                System.out.println("DEBUG: Nó não reconhecido, visitando filhos: " + node.kind);
                for (int i = 0; i < node.getChildCount(); i++) {
                    visitNode(node.getChild(i));
                }
            }
        }
        System.out.println("DEBUG: Saindo de visitNode() para: " + node.kind);
    }

    private void visitProgram(AST node) {
        System.out.println("DEBUG: Entrando em visitProgram()");
        // Visita o bloco principal do programa
        if (node.getChildCount() > 0) {
            visitNode(node.getChild(0));
        }
        System.out.println("DEBUG: Saindo de visitProgram()");
    }

    private void visitBlock(AST node) {
        System.out.println("DEBUG: Entrando em visitBlock()");
        // Se estamos no programa principal, processa primeiro as outras seções
        if (currentFunction == null) {
            // Processa tudo exceto funções/procedimentos primeiro
            for (int i = 0; i < node.getChildCount(); i++) {
                AST child = node.getChild(i);
                if (child.kind != NodeKind.FUNC_DECL_NODE && child.kind != NodeKind.PROC_DECL_NODE) {
                    visitNode(child);
                }
            }
            
            // Adiciona salto para o fim antes das funções
            String endMainLabel = generateLabel("end_main");
            mipsCode.append("j " + endMainLabel + "\n\n");
            
            // Depois emite as funções/procedimentos
            for (int i = 0; i < node.getChildCount(); i++) {
                AST child = node.getChild(i); // CORREÇÃO: Redeclara child neste escopo
                if (child.kind == NodeKind.FUNC_DECL_NODE || child.kind == NodeKind.PROC_DECL_NODE) {
                    visitNode(child);
                }
            }
            
            // Label do fim do main
            mipsCode.append(endMainLabel + ":\n");
        } else {
            // Dentro de função/procedimento - processa normalmente
            for (int i = 0; i < node.getChildCount(); i++) {
                AST child = node.getChild(i); // CORREÇÃO: Redeclara child neste escopo também
                visitNode(child);
            }
        }
        System.out.println("DEBUG: Saindo de visitBlock()");
    }
    private void visitConstSection(AST node) {
        System.out.println("DEBUG: Entrando em visitConstSection()");
        // Constantes são processadas durante a análise semântica
        // Não precisamos gerar código especial aqui
        System.out.println("DEBUG: Saindo de visitConstSection()");
    }

    // IMPLEMENTAÇÃO: Declaração de função
    private void visitFunctionDeclaration(AST node) {
        System.out.println("DEBUG: Entrando em visitFunctionDeclaration() para: " + node.stringData);
        String funcName = node.stringData;
        Type returnType = node.type;
        String label = "func_" + funcName;
        
        // Cria contexto da função
        FunctionContext funcContext = new FunctionContext(funcName, label, true, returnType);
        functionStack.push(funcContext);
        currentFunction = funcContext;
        
        // Processa lista de parâmetros se existir
        List<ParameterInfo> parameters = new ArrayList<>();
        AST paramListNode = null;
        AST blockNode = null;
        
        // Identifica os filhos baseado na estrutura da AST
        for (int i = 0; i < node.getChildCount(); i++) {
            AST child = node.getChild(i);
            if (child.kind == NodeKind.PARAM_LIST_NODE) {
                paramListNode = child;
            } else if (child.kind == NodeKind.BLOCK_NODE) {
                blockNode = child;
            }
        }
        
        if (paramListNode != null) {
            parameters = processParameterList(paramListNode);
        }
        
        // Armazena informações da função
        functionInfo.put(funcName, new FunctionInfo(funcName, returnType, parameters, label, true));
        
        // Emite label da função
        mipsCode.append("\n" + label + ":\n");
        
        // Prólogo da função
        emitFunctionProlog(parameters);
        
        // Processa corpo da função (bloco)
        if (blockNode != null) {
            visitNode(blockNode);
        }
        
        // Epílogo da função (com label de fim)
        mipsCode.append(label + "_end:\n");
        emitFunctionEpilog();
        
        // Remove contexto da função
        functionStack.pop();
        currentFunction = functionStack.isEmpty() ? null : functionStack.peek();
        System.out.println("DEBUG: Saindo de visitFunctionDeclaration() para: " + node.stringData);
    }

    // IMPLEMENTAÇÃO: Declaração de procedimento
    private void visitProcedureDeclaration(AST node) {
        System.out.println("DEBUG: Entrando em visitProcedureDeclaration() para: " + node.stringData);
        String procName = node.stringData;
        String label = "proc_" + procName;
        
        // Cria contexto do procedimento
        FunctionContext procContext = new FunctionContext(procName, label, false, null);
        functionStack.push(procContext);
        currentFunction = procContext;
        
        // Processa lista de parâmetros se existir
        List<ParameterInfo> parameters = new ArrayList<>();
        AST paramListNode = null;
        AST blockNode = null;
        
        // Identifica os filhos baseado na estrutura da AST
        for (int i = 0; i < node.getChildCount(); i++) {
            AST child = node.getChild(i);
            if (child.kind == NodeKind.PARAM_LIST_NODE) {
                paramListNode = child;
            } else if (child.kind == NodeKind.BLOCK_NODE) {
                blockNode = child;
            }
        }
        
        if (paramListNode != null) {
            parameters = processParameterList(paramListNode);
        }
        
        // Armazena informações do procedimento
        functionInfo.put(procName, new FunctionInfo(procName, null, parameters, label, false));
        
        // Emite label do procedimento
        mipsCode.append("\n" + label + ":\n");
        
        // Prólogo do procedimento
        emitFunctionProlog(parameters);
        
        // Processa corpo do procedimento (bloco)
        if (blockNode != null) {
            visitNode(blockNode);
        }
        
        // Epílogo do procedimento (com label de fim)
        mipsCode.append(label + "_end:\n");
        emitFunctionEpilog();
        
        // Remove contexto do procedimento
        functionStack.pop();
        currentFunction = functionStack.isEmpty() ? null : functionStack.peek();
        System.out.println("DEBUG: Saindo de visitProcedureDeclaration() para: " + node.stringData);
    }

    // Lista de parâmetros
    private void visitParameterList(AST node) {
        System.out.println("DEBUG: Entrando em visitParameterList()");
        // Processado durante visitFunctionDeclaration/visitProcedureDeclaration
        // Não precisa fazer nada aqui
        System.out.println("DEBUG: Saindo de visitParameterList()");
    }

    // Declaração de parâmetro
    private void visitParameterDeclaration(AST node) {
        System.out.println("DEBUG: Entrando em visitParameterDeclaration()");
        // Processado durante processParameterList
        // Não precisa fazer nada aqui
        System.out.println("DEBUG: Saindo de visitParameterDeclaration()");
    }

    private List<ParameterInfo> processParameterList(AST paramListNode) {
        System.out.println("DEBUG: Entrando em processParameterList()");
        List<ParameterInfo> parameters = new ArrayList<>();
        int offset = 8; // Começa após $ra (4) e $fp (4)
        
        for (int i = 0; i < paramListNode.getChildCount(); i++) {
            AST paramNode = paramListNode.getChild(i);
            if (paramNode.kind == NodeKind.PARAM_NODE) {
                String paramName = paramNode.stringData;
                Type paramType = paramNode.type;
                boolean isByRef = paramNode.intData == 1; // 1 = var parameter, 0 = value parameter
                
                ParameterInfo paramInfo = new ParameterInfo(paramName, paramType, isByRef, offset);
                parameters.add(paramInfo);
                
                // CORREÇÃO: Armazenar o offset do parâmetro no contexto atual
                // Mas não usar localVarOffsets, pois isso confunde com variáveis locais
                if (currentFunction != null) {
                    // Os parâmetros são acessados através de offset positivo do $fp
                    // enquanto variáveis locais usam offset negativo
                    currentFunction.localVarOffsets.put(paramName, offset);
                }
                
                offset += 4; // Cada parâmetro ocupa 4 bytes
            }
        }
        
        if (currentFunction != null) {
            currentFunction.parameterSize = offset - 8;
        }
        
        System.out.println("DEBUG: Saindo de processParameterList()");
        return parameters;
    }
    // IMPLEMENTAÇÃO: Prólogo da função
    private void emitFunctionProlog(List<ParameterInfo> parameters) {
        System.out.println("DEBUG: Entrando em emitFunctionProlog()");
        // Salva registradores
        mipsCode.append("subu $sp, $sp, 8\n"); // Espaço para $ra e $fp
        mipsCode.append("sw $ra, 4($sp)\n");   // Salva return address
        mipsCode.append("sw $fp, 0($sp)\n");   // Salva frame pointer
        mipsCode.append("move $fp, $sp\n");    // Novo frame pointer
        
        // Reserva espaço para variáveis locais (será calculado dinamicamente)
        // Não fazemos isso aqui pois não sabemos ainda quantas variáveis locais haverá
        System.out.println("DEBUG: Saindo de emitFunctionProlog()");
    }

    // IMPLEMENTAÇÃO: Epílogo da função
    private void emitFunctionEpilog() {
        System.out.println("DEBUG: Entrando em emitFunctionEpilog()");
        // Se há variáveis locais alocadas, libera o espaço
        if (currentFunction != null && currentFunction.localVarSize > 0) {
            mipsCode.append("addu $sp, $sp, " + currentFunction.localVarSize + "\n");
        }
        
        // Restaura registradores
        mipsCode.append("lw $fp, 0($sp)\n");   // Restaura frame pointer
        mipsCode.append("lw $ra, 4($sp)\n");   // Restaura return address
        mipsCode.append("addu $sp, $sp, 8\n"); // Restaura stack
        
        // Retorna
        mipsCode.append("jr $ra\n");
        System.out.println("DEBUG: Saindo de emitFunctionEpilog()");
    }

    private void visitFunctionCall(AST node) {
        System.out.println("DEBUG: Entrando em visitFunctionCall() para: " + node.stringData);
        String funcName = node.stringData;
        FunctionInfo funcInfo = functionInfo.get(funcName);
        
        if (funcInfo != null) {
            // Verifica se há argumentos
            if (node.getChildCount() > 0) {
                AST argsNode = node.getChild(0);
                
                // Empilha argumentos na ordem correta (da direita para esquerda)
                if (argsNode != null && argsNode.getChildCount() > 0) {
                    for (int i = argsNode.getChildCount() - 1; i >= 0; i--) {
                        AST argNode = argsNode.getChild(i);
                        
                        if (i < funcInfo.parameters.size()) {
                            ParameterInfo paramInfo = funcInfo.parameters.get(i);
                            
                            if (paramInfo.isByReference) {
                                // Passagem por referência - empilha endereço
                                emitArgumentByReference(argNode);
                            } else {
                                // Passagem por valor - avalia expressão e empilha valor
                                visitNode(argNode);
                                // O resultado já foi empilhado pelo visitNode
                            }
                        } else {
                            // Mais argumentos que parâmetros - ainda avalia
                            visitNode(argNode);
                        }
                    }
                }
            }
            
            // Chama a função
            mipsCode.append("jal " + funcInfo.label + "\n");
            
            // Remove argumentos da pilha
            int actualArgsCount = 0;
            if (node.getChildCount() > 0 && node.getChild(0) != null) {
                actualArgsCount = node.getChild(0).getChildCount();
            }
            
            if (actualArgsCount > 0) {
                int argsSize = actualArgsCount * 4;
                mipsCode.append("addu $sp, $sp, " + argsSize + "\n");
            }
            
            // CORREÇÃO: Se for função, empilha o resultado
            if (funcInfo.isFunction && funcInfo.returnType != null) {
                if (funcInfo.returnType == Type.REAL) {
                    emitPushFloat("$f0");
                } else {
                    emitPushTemp("$v0");
                }
            }
        }
        System.out.println("DEBUG: Saindo de visitFunctionCall() para: " + node.stringData);
    }


    private void visitVarSection(AST node) {
        System.out.println("DEBUG: Entrando em visitVarSection()");
        if (currentFunction == null) {
            // Variáveis globais
            mipsCode.append("# Variáveis globais\n");
            for (int i = 0; i < node.getChildCount(); i++) {
                visitNode(node.getChild(i));
            }
        } else {
            // Variáveis locais - apenas atualiza offsets e reserva espaço
            for (int i = 0; i < node.getChildCount(); i++) {
                visitLocalVarDeclaration(node.getChild(i));
            }
            
            // Reserva espaço para variáveis locais se necessário
            if (currentFunction.localVarSize > 0) {
                mipsCode.append("subu $sp, $sp, " + currentFunction.localVarSize + "\n");
            }
        }
        System.out.println("DEBUG: Saindo de visitVarSection()");
    }

    private void visitLocalVarDeclaration(AST node) {
        System.out.println("DEBUG: Entrando em visitLocalVarDeclaration() para: " + node.stringData);
        String varName = node.stringData;
        
        // CORREÇÃO: Verifica se já é um parâmetro para não duplicar
        if (currentFunction != null) {
            FunctionInfo funcInfo = functionInfo.get(currentFunction.name);
            if (funcInfo != null) {
                for (ParameterInfo param : funcInfo.parameters) {
                    if (param.name.equals(varName)) {
                        // É um parâmetro, não uma variável local - não processa novamente
                        System.out.println("DEBUG: " + varName + " é um parâmetro, ignorando.");
                        return;
                    }
                }
            }
        }
        
        currentFunction.localVarSize += 4; // Incrementa o tamanho das variáveis locais
        int offset = -currentFunction.localVarSize; // Negativo pois cresce para baixo do $fp
        
        currentFunction.localVarOffsets.put(varName, offset);
        
        // Verifica se é array
        if (node.getChildCount() > 0 && node.getChild(0).kind == NodeKind.ARRAY_TYPE_NODE) {
            // Para arrays locais, aloca mais espaço
            AST arrayTypeNode = node.getChild(0);
            if (arrayTypeNode.getChildCount() >= 2) {
                AST rangeNode = arrayTypeNode.getChild(0);
                if (rangeNode.getChildCount() == 2) {
                    AST startNode = rangeNode.getChild(0);
                    AST endNode = rangeNode.getChild(1);
                    
                    if (startNode.kind == NodeKind.INT_VAL_NODE && endNode.kind == NodeKind.INT_VAL_NODE) {
                        int size = endNode.intData - startNode.intData + 1;
                        currentFunction.localVarSize += (size - 1) * 4; // -1 porque já contamos 4 bytes acima
                    }
                }
            }
        }
        System.out.println("DEBUG: Saindo de visitLocalVarDeclaration()");
    }

    private void visitVarDeclaration(AST node) {
        System.out.println("DEBUG: Entrando em visitVarDeclaration() para: " + node.stringData);
        if (currentFunction != null) {
            visitLocalVarDeclaration(node);
            System.out.println("DEBUG: Saindo de visitVarDeclaration() (variável local)");
            return;
        }
        
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
        System.out.println("DEBUG: Saindo de visitVarDeclaration() (variável global)");
    }
    
    private void visitArrayDeclaration(AST varDeclNode) {
        System.out.println("DEBUG: Entrando em visitArrayDeclaration() para: " + varDeclNode.stringData);
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
        System.out.println("DEBUG: Saindo de visitArrayDeclaration()");
    }
    
    private void insertVariableDeclaration(String label, Type type) {
        System.out.println("DEBUG: Entrando em insertVariableDeclaration()");
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
        System.out.println("DEBUG: Saindo de insertVariableDeclaration()");
    }
    
    private void insertArrayDeclaration(String label, int size, Type elementType) {
        System.out.println("DEBUG: Entrando em insertArrayDeclaration()");
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
        System.out.println("DEBUG: Saindo de insertArrayDeclaration()");
    }
    
    private void visitArrayType(AST node) {
        System.out.println("DEBUG: Entrando em visitArrayType()");
        // Processa o tipo array - os filhos já são visitados pelo visitNode
        for (int i = 0; i < node.getChildCount(); i++) {
            visitNode(node.getChild(i));
        }
        System.out.println("DEBUG: Saindo de visitArrayType()");
    }
    
    private void visitRange(AST node) {
        System.out.println("DEBUG: Entrando em visitRange()");
        // Processa o range do array - os valores são acessados diretamente
        // nos métodos que precisam deles
        for (int i = 0; i < node.getChildCount(); i++) {
            visitNode(node.getChild(i));
        }
        System.out.println("DEBUG: Saindo de visitRange()");
    }

    private void visitCompoundStatement(AST node) {
        System.out.println("DEBUG: Entrando em visitCompoundStatement()");
        // Visita o conteúdo do statement composto
        if (node.getChildCount() > 0) {
            visitNode(node.getChild(0));
        }
        System.out.println("DEBUG: Saindo de visitCompoundStatement()");
    }

    private void visitAssignment(AST node) {
        System.out.println("DEBUG: Entrando em visitAssignment()");
        if (node.getChildCount() != 2) {
            System.out.println("DEBUG: Falha em visitAssignment(), número de filhos incorreto.");
            return;
        }
        
        AST varNode = node.getChild(0);
        AST exprNode = node.getChild(1);
        
        // Verifica se é assignment para valor de retorno de função
        if (varNode.kind == NodeKind.VAR_USE_NODE && currentFunction != null && 
            currentFunction.isFunction && varNode.stringData.equals(currentFunction.name)) {
            
            // Assignment para valor de retorno da função
            visitNode(exprNode);
            
            if (currentFunction.returnType == Type.REAL) {
                emitPopFloat("$f0"); // Valor de retorno em $f0 para floats
            } else {
                emitPopTemp("$v0"); // Valor de retorno em $v0 para integers
            }
            System.out.println("DEBUG: Saindo de visitAssignment() (valor de retorno da função)");
            return;
        }
        
        // CORREÇÃO: Verifica se a expressão é uma chamada de função
        if (exprNode.kind == NodeKind.FUNC_CALL_NODE) {
            // É uma chamada de função - processa normalmente
            visitFunctionCall(exprNode);
            
            // O resultado já está empilhado, agora armazena na variável
            if (varNode.kind == NodeKind.VAR_USE_NODE) {
                if (varNode.type == Type.REAL) {
                    emitPopFloat("$f0");
                    storeIntoVariable(varNode, "$f0", true);
                } else {
                    emitPopTemp("$t0");
                    storeIntoVariable(varNode, "$t0", false);
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
        } else {
            // Expressão normal - avalia primeiro
            visitNode(exprNode);
            
            // Armazena na variável
            if (varNode.kind == NodeKind.VAR_USE_NODE) {
                if (varNode.type == Type.REAL) {
                    emitPopFloat("$f0");
                    storeIntoVariable(varNode, "$f0", true);
                } else {
                    emitPopTemp("$t0");
                    storeIntoVariable(varNode, "$t0", false);
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
        System.out.println("DEBUG: Saindo de visitAssignment()");
    }

    private void visitArrayAssignment(AST arrayNode, String valueReg) {
        System.out.println("DEBUG: Entrando em visitArrayAssignment() para: " + arrayNode.stringData);
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
        System.out.println("DEBUG: Saindo de visitArrayAssignment()");
    }

    private void visitArrayAssignmentFloat(AST arrayNode, String valueReg) {
        System.out.println("DEBUG: Entrando em visitArrayAssignmentFloat() para: " + arrayNode.stringData);
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
        System.out.println("DEBUG: Saindo de visitArrayAssignmentFloat()");
    }

    private void visitProcedureCall(AST node) {
        System.out.println("DEBUG: Entrando em visitProcedureCall() para: " + node.stringData);
        String procName = node.stringData;
        
        // Verifica se é procedimento built-in
        if (procName.equals("writeln") || procName.equals("write")) {
            visitBuiltinWrite(node);
        } else if (procName.equals("read") || procName.equals("readln")) {
            visitBuiltinRead(node);
        } else {
            // Procedimento definido pelo usuário
            FunctionInfo procInfo = functionInfo.get(procName);
            if (procInfo != null) {
                // Verifica se há argumentos antes de tentar acessá-los
                if (node.getChildCount() > 0) {
                    AST argsNode = node.getChild(0);
                    
                    // Verifica se há argumentos reais para processar
                    if (argsNode != null && argsNode.getChildCount() > 0) {
                        // Empilha argumentos na ordem reversa para facilitar acesso pelos parâmetros
                        for (int i = argsNode.getChildCount() - 1; i >= 0; i--) {
                            AST argNode = argsNode.getChild(i);
                            
                            // Verifica bounds antes de acessar parâmetros
                            if (i < procInfo.parameters.size()) {
                                ParameterInfo paramInfo = procInfo.parameters.get(i);
                                
                                if (paramInfo.isByReference) {
                                    // Passagem por referência - empilha endereço
                                    emitArgumentByReference(argNode);
                                } else {
                                    // Passagem por valor - avalia expressão
                                    visitNode(argNode);
                                }
                            } else {
                                // Caso tenha mais argumentos que parâmetros
                                visitNode(argNode);
                            }
                        }
                    }
                }
                
                // Chama o procedimento
                mipsCode.append("jal " + procInfo.label + "\n");
                
                // Remove argumentos da pilha
                int actualArgsCount = 0;
                if (node.getChildCount() > 0 && node.getChild(0) != null) {
                    actualArgsCount = node.getChild(0).getChildCount();
                }
                
                int argsSize = actualArgsCount * 4;
                if (argsSize > 0) {
                    mipsCode.append("addu $sp, $sp, " + argsSize + "\n");
                }
            }
        }
        System.out.println("DEBUG: Saindo de visitProcedureCall()");
    }

    private void visitBuiltinWrite(AST node) {
        System.out.println("DEBUG: Entrando em visitBuiltinWrite()");
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
        System.out.println("DEBUG: Saindo de visitBuiltinWrite()");
    }

    private void visitBuiltinRead(AST node) {
        System.out.println("DEBUG: Entrando em visitBuiltinRead()");
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
        System.out.println("DEBUG: Saindo de visitBuiltinRead()");
    }

    private void readIntoVariable(AST varNode) {
        System.out.println("DEBUG: Entrando em readIntoVariable() para: " + varNode.stringData);
        String varName = varNode.stringData;
        
        // Verifica se é variável local ou global
        if (currentFunction != null && currentFunction.localVarOffsets.containsKey(varName)) {
            int offset = currentFunction.localVarOffsets.get(varName);
            
            if (varNode.type == Type.REAL) {
                mipsCode.append("li $v0, 6\n");      // syscall para ler float
                mipsCode.append("syscall\n");
                mipsCode.append("swc1 $f0, " + offset + "($fp)\n");
            } else if (varNode.type == Type.INTEGER) {
                mipsCode.append("li $v0, 5\n");      // syscall para ler inteiro
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, " + offset + "($fp)\n");
            } else if (varNode.type == Type.CHAR) {
                mipsCode.append("li $v0, 12\n");     // syscall para ler caractere
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, " + offset + "($fp)\n");
            }
        } else {
            String label = varLabels.get(varName);
            if (label != null) {
                if (varNode.type == Type.REAL) {
                    mipsCode.append("li $v0, 6\n");      // syscall para ler float
                    mipsCode.append("syscall\n");
                    mipsCode.append("swc1 $f0, " + label + "\n");
                } else if (varNode.type == Type.INTEGER) {
                    mipsCode.append("li $v0, 5\n");      // syscall para ler inteiro
                    mipsCode.append("syscall\n");
                    mipsCode.append("sw $v0, " + label + "\n");
                } else if (varNode.type == Type.CHAR) {
                    mipsCode.append("li $v0, 12\n");     // syscall para ler caractere
                    mipsCode.append("syscall\n");
                    mipsCode.append("sw $v0, " + label + "\n");
                }
            }
        }
        System.out.println("DEBUG: Saindo de readIntoVariable()");
    }

    private void readIntoArrayElement(AST arrayNode) {
        System.out.println("DEBUG: Entrando em readIntoArrayElement() para: " + arrayNode.stringData);
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
        System.out.println("DEBUG: Saindo de readIntoArrayElement()");
    }
    
    private void visitIfStatement(AST node) {
        System.out.println("DEBUG: Entrando em visitIfStatement()");
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
        System.out.println("DEBUG: Saindo de visitIfStatement()");
    }

    private void visitWhileStatement(AST node) {
        System.out.println("DEBUG: Entrando em visitWhileStatement()");
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
        System.out.println("DEBUG: Saindo de visitWhileStatement()");
    }

    private void visitBinaryOp(AST node, String operation) {
        System.out.println("DEBUG: Entrando em visitBinaryOp() para: " + operation);
        if (node.getChildCount() != 2) return;
        
        // Verifica se é operação com floats
        if (node.type == Type.REAL) {
            visitFloatBinaryOp(node, operation);
            System.out.println("DEBUG: Saindo de visitBinaryOp() (float)");
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
        System.out.println("DEBUG: Saindo de visitBinaryOp() (integer)");
    }

    private void visitFloatBinaryOp(AST node, String operation) {
        System.out.println("DEBUG: Entrando em visitFloatBinaryOp() para: " + operation);
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
        System.out.println("DEBUG: Saindo de visitFloatBinaryOp()");
    }

    private void visitRealDivision(AST node) {
        System.out.println("DEBUG: Entrando em visitRealDivision()");
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopFloat("$f1");
        emitPopFloat("$f0");
        
        mipsCode.append("div.s $f0, $f0, $f1\n");
        
        emitPushFloat("$f0");
        System.out.println("DEBUG: Saindo de visitRealDivision()");
    }

    private void visitIntegerDivision(AST node) {
        System.out.println("DEBUG: Entrando em visitIntegerDivision()");
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        mipsCode.append("div $t0, $t1\n");
        mipsCode.append("mflo $t0\n");
        
        emitPushTemp("$t0");
        System.out.println("DEBUG: Saindo de visitIntegerDivision()");
    }

    private void visitModulo(AST node) {
        System.out.println("DEBUG: Entrando em visitModulo()");
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        mipsCode.append("div $t0, $t1\n");
        mipsCode.append("mfhi $t0\n"); // resto da divisão
        
        emitPushTemp("$t0");
        System.out.println("DEBUG: Saindo de visitModulo()");
    }

    private void visitLogicalAnd(AST node) {
        System.out.println("DEBUG: Entrando em visitLogicalAnd()");
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        mipsCode.append("and $t0, $t0, $t1\n");
        
        emitPushTemp("$t0");
        System.out.println("DEBUG: Saindo de visitLogicalAnd()");
    }

    private void visitLogicalOr(AST node) {
        System.out.println("DEBUG: Entrando em visitLogicalOr()");
        if (node.getChildCount() != 2) return;
        
        visitNode(node.getChild(0));
        visitNode(node.getChild(1));
        
        emitPopTemp("$t1");
        emitPopTemp("$t0");
        
        mipsCode.append("or $t0, $t0, $t1\n");
        
        emitPushTemp("$t0");
        System.out.println("DEBUG: Saindo de visitLogicalOr()");
    }

    private void visitLogicalNot(AST node) {
        System.out.println("DEBUG: Entrando em visitLogicalNot()");
        if (node.getChildCount() != 1) return;
        
        visitNode(node.getChild(0));
        emitPopTemp("$t0");
        
        mipsCode.append("seq $t0, $t0, $zero\n"); // set equal to zero
        
        emitPushTemp("$t0");
        System.out.println("DEBUG: Saindo de visitLogicalNot()");
    }

    private void visitComparison(AST node, String operation) {
        System.out.println("DEBUG: Entrando em visitComparison() para: " + operation);
        if (node.getChildCount() != 2) return;
        
        // Verifica se é comparação de floats
        AST left = node.getChild(0);
        AST right = node.getChild(1);
        
        if (left.type == Type.REAL || right.type == Type.REAL) {
            visitFloatComparison(node, operation);
            System.out.println("DEBUG: Saindo de visitComparison() (float)");
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
        System.out.println("DEBUG: Saindo de visitComparison() (integer)");
    }

    private void visitFloatComparison(AST node, String operation) {
        System.out.println("DEBUG: Entrando em visitFloatComparison() para: " + operation);
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
        System.out.println("DEBUG: Saindo de visitFloatComparison()");
    }

    private void visitIntValue(AST node) {
        System.out.println("DEBUG: Entrando em visitIntValue() para: " + node.intData);
        mipsCode.append("li $t0, " + node.intData + "\n");
        emitPushTemp("$t0");
        System.out.println("DEBUG: Saindo de visitIntValue()");
    }

    private void visitRealValue(AST node) {
        System.out.println("DEBUG: Entrando em visitRealValue() para: " + node.floatData);
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
        System.out.println("DEBUG: Saindo de visitRealValue()");
    }

    private void visitBoolValue(AST node) {
        System.out.println("DEBUG: Entrando em visitBoolValue() para: " + node.intData);
        mipsCode.append("li $t0, " + node.intData + "\n");
        emitPushTemp("$t0");
        System.out.println("DEBUG: Saindo de visitBoolValue()");
    }

    private void visitCharValue(AST node) {
        System.out.println("DEBUG: Entrando em visitCharValue() para: " + node.stringData);
        // Converte char para seu valor ASCII
        if (node.stringData != null && !node.stringData.isEmpty()) {
            char c = node.stringData.charAt(1); // Remove aspas
            mipsCode.append("li $t0, " + (int) c + "\n");
        } else {
            mipsCode.append("li $t0, 0\n");
        }
        emitPushTemp("$t0");
        System.out.println("DEBUG: Saindo de visitCharValue()");
    }

    private void visitStringValue(AST node) {
        System.out.println("DEBUG: Entrando em visitStringValue() para: " + node.stringData);
        // Encontra o índice da string na tabela
        if (strTable != null && node.stringData != null) {
            int index = strTable.indexOf(node.stringData);
            if (index >= 0) {
                mipsCode.append("la $t0, str_" + index + "\n");
                emitPushTemp("$t0");
            }
        }
        System.out.println("DEBUG: Saindo de visitStringValue()");
    }

    private void visitVariableUse(AST node) {
        System.out.println("DEBUG: Entrando em visitVariableUse() para: " + node.stringData);
        String varName = node.stringData;
        
        // Primeiro verifica se estamos dentro de uma função/procedimento
        if (currentFunction != null) {
            // CORREÇÃO: Verifica primeiro se é um parâmetro usando a lista de parâmetros
            FunctionInfo funcInfo = functionInfo.get(currentFunction.name);
            if (funcInfo != null) {
                for (ParameterInfo param : funcInfo.parameters) {
                    if (param.name.equals(varName)) {
                        if (param.isByReference) {
                            // Parâmetro por referência - carrega através do endereço armazenado
                            mipsCode.append("lw $t0, " + param.offset + "($fp)\n"); // carrega endereço do parâmetro
                            
                            if (node.type == Type.REAL) {
                                mipsCode.append("lwc1 $f0, 0($t0)\n"); // carrega valor através do endereço
                                emitPushFloat("$f0");
                            } else {
                                mipsCode.append("lw $t1, 0($t0)\n"); // carrega valor através do endereço
                                emitPushTemp("$t1");
                            }
                        } else {
                            // Parâmetro por valor - acessa diretamente do stack frame
                            if (node.type == Type.REAL) {
                                mipsCode.append("lwc1 $f0, " + param.offset + "($fp)\n");
                                emitPushFloat("$f0");
                            } else {
                                mipsCode.append("lw $t0, " + param.offset + "($fp)\n");
                                emitPushTemp("$t0");
                            }
                        }
                        System.out.println("DEBUG: Variável " + varName + " é um parâmetro. Saindo.");
                        return; // Encontrou o parâmetro, termina aqui
                    }
                }
            }
            
            // Se não é parâmetro, verifica se é variável local
            // As variáveis locais usam offsets negativos
            if (currentFunction.localVarOffsets.containsKey(varName)) {
                int offset = currentFunction.localVarOffsets.get(varName);
                
                // CORREÇÃO: Só usar este path se o offset for negativo (variável local)
                if (offset < 0) {
                    if (node.type == Type.REAL) {
                        mipsCode.append("lwc1 $f0, " + offset + "($fp)\n");
                        emitPushFloat("$f0");
                    } else {
                        mipsCode.append("lw $t0, " + offset + "($fp)\n");
                        emitPushTemp("$t0");
                    }
                    System.out.println("DEBUG: Variável " + varName + " é local. Saindo.");
                    return;
                }
            }
        }
        
        // Se não é variável local nem parâmetro, deve ser variável global
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
            // Fallback: se a variável não foi encontrada, carrega valor zero
            // Isso pode indicar um erro na análise semântica ou tabela de símbolos
            if (node.type == Type.REAL) {
                // Para floats, precisa criar uma constante zero
                String zeroLabel = generateLabel("zero_float");
                String currentText = mipsCode.toString();
                int dataEnd = currentText.indexOf(".text");
                if (dataEnd != -1) {
                    String dataPart = currentText.substring(0, dataEnd);
                    String textPart = currentText.substring(dataEnd);
                    
                    mipsCode.setLength(0);
                    mipsCode.append(dataPart);
                    mipsCode.append(zeroLabel + ": .float 0.0\n");
                    mipsCode.append(textPart);
                }
                mipsCode.append("lwc1 $f0, " + zeroLabel + "\n");
                emitPushFloat("$f0");
            } else {
                mipsCode.append("li $t0, 0\n");
                emitPushTemp("$t0");
            }
        }
        System.out.println("DEBUG: Saindo de visitVariableUse()");
    }

    private void visitArrayAccess(AST node) {
        System.out.println("DEBUG: Entrando em visitArrayAccess() para: " + node.stringData);
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
        System.out.println("DEBUG: Saindo de visitArrayAccess()");
    }

    private void visitIntegerToReal(AST node) {
        System.out.println("DEBUG: Entrando em visitIntegerToReal()");
        if (node.getChildCount() != 1) return;
        
        // Avalia o valor inteiro
        visitNode(node.getChild(0));
        emitPopTemp("$t0");
        
        // Converte inteiro para float
        mipsCode.append("mtc1 $t0, $f0\n");    // move para coprocessador
        mipsCode.append("cvt.s.w $f0, $f0\n"); // converte word para single
        
        emitPushFloat("$f0");
        System.out.println("DEBUG: Saindo de visitIntegerToReal()");
    }

    private void emitArgumentByReference(AST argNode) {
        System.out.println("DEBUG: Entrando em emitArgumentByReference()");
        if (argNode.kind == NodeKind.VAR_USE_NODE) {
            String varName = argNode.stringData;
            
            if (currentFunction != null && currentFunction.localVarOffsets.containsKey(varName)) {
                // Variável local - calcula endereço baseado em $fp
                int offset = currentFunction.localVarOffsets.get(varName);
                mipsCode.append("addi $t0, $fp, " + offset + "\n");
            } else {
                // Variável global - usa label
                String label = varLabels.get(varName);
                if (label != null) {
                    mipsCode.append("la $t0, " + label + "\n");
                } else {
                    mipsCode.append("li $t0, 0\n"); // Fallback
                }
            }
            
            emitPushTemp("$t0");
            
        } else if (argNode.kind == NodeKind.ARRAY_ACCESS_NODE) {
            // Referência para elemento de array
            String arrayName = argNode.stringData;
            AST indexNode = argNode.getChild(0);
            
            // Calcula o índice
            visitNode(indexNode);
            emitPopTemp("$t1");
            
            // Ajusta o índice
            ArrayInfo info = arrayInfo.get(arrayName);
            if (info != null && info.startIndex != 0) {
                mipsCode.append("addi $t1, $t1, " + (-info.startIndex) + "\n");
            }
            
            // Calcula endereço do elemento
            String arrayLabel = varLabels.get(arrayName);
            if (arrayLabel != null) {
                mipsCode.append("sll $t1, $t1, 2\n");
                mipsCode.append("la $t0, " + arrayLabel + "\n");
                mipsCode.append("add $t0, $t0, $t1\n");
                emitPushTemp("$t0");
            }
        }
        System.out.println("DEBUG: Saindo de emitArgumentByReference()");
    }

    private void storeIntoVariable(AST varNode, String sourceReg, boolean isFloat) {
        System.out.println("DEBUG: Entrando em storeIntoVariable() para: " + varNode.stringData);
        String varName = varNode.stringData;
        
        // Verifica se é parâmetro por referência
        if (currentFunction != null) {
            FunctionInfo funcInfo = functionInfo.get(currentFunction.name);
            if (funcInfo != null) {
                for (ParameterInfo param : funcInfo.parameters) {
                    if (param.name.equals(varName) && param.isByReference) {
                        // É parâmetro por referência - armazena através do endereço
                        mipsCode.append("lw $t2, " + param.offset + "($fp)\n"); // carrega endereço
                        
                        if (isFloat) {
                            mipsCode.append("swc1 " + sourceReg + ", 0($t2)\n");
                        } else {
                            mipsCode.append("sw " + sourceReg + ", 0($t2)\n");
                        }
                        System.out.println("DEBUG: Variável " + varName + " é um parâmetro por referência. Saindo.");
                        return;
                    } else if (param.name.equals(varName)) {
                        // Parâmetro por valor - armazena diretamente no stack frame
                        if (isFloat) {
                            mipsCode.append("swc1 " + sourceReg + ", " + param.offset + "($fp)\n");
                        } else {
                            mipsCode.append("sw " + sourceReg + ", " + param.offset + "($fp)\n");
                        }
                        System.out.println("DEBUG: Variável " + varName + " é um parâmetro por valor. Saindo.");
                        return;
                    }
                }
            }
            
            // Variável local normal
            if (currentFunction.localVarOffsets.containsKey(varName)) {
                int offset = currentFunction.localVarOffsets.get(varName);
                if (isFloat) {
                    mipsCode.append("swc1 " + sourceReg + ", " + offset + "($fp)\n");
                } else {
                    mipsCode.append("sw " + sourceReg + ", " + offset + "($fp)\n");
                }
                System.out.println("DEBUG: Variável " + varName + " é local. Saindo.");
                return;
            }
        }
        
        // Variável global
        String label = varLabels.get(varName);
        if (label != null) {
            if (isFloat) {
                mipsCode.append("swc1 " + sourceReg + ", " + label + "\n");
            } else {
                mipsCode.append("sw " + sourceReg + ", " + label + "\n");
            }
        }
        System.out.println("DEBUG: Saindo de storeIntoVariable()");
    }
}
