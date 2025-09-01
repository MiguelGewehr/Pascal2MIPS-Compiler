package codegen;

import entries.*;
import typing.Type;
import ast.*;
import tables.StrTable;
import tables.SymbolTable;

import java.util.*;

public class CodegenVisitor {
    // Acumula o código MIPS gerado
    private StringBuilder mipsCode = new StringBuilder();
    
    private StrTable strTable;
    
    // Contadores para labels únicos
    private int labelCounter = 0;
    
    // Mapeamento de variáveis para seus labels MIPS
    private Map<String, String> varLabels = new HashMap<>();
    
    // Informações sobre arrays (nome -> [tamanho, tipo])
    private Map<String, ArrayEntry> arrayInfo = new HashMap<>();
    
    // Gerenciamento de escopo e funções
    private Stack<FunctionContext> functionStack = new Stack<>();
    private Map<String, FuncEntry> functionInfo = new HashMap<>();
    private FunctionContext currentFunction = null;

    // Ajuda a identificar o bloco principal do programa para tratamento especial.
    private boolean isTopLevelBlock = false;
    
    // Classe auxiliar para contexto de função
    private static class FunctionContext {
        public final String name;
        public final boolean isFunction;
        public final Type returnType;
        public final Map<String, Integer> localVarOffsets = new HashMap<>();
        public int localVarSize = 0;
        
        public FunctionContext(String name, boolean isFunction, Type returnType) {
            this.name = name;
            this.isFunction = isFunction;
            this.returnType = returnType;
        }
    }
    
    /**
     * Gera o código MIPS para o programa fornecido.
     */
    public String generate(AST program, SymbolTable symbolTable, StrTable stringTable) {
        // Initialize state
        strTable = stringTable;
        mipsCode = new StringBuilder();
        labelCounter = 0;
        varLabels = new HashMap<>();
        arrayInfo = new HashMap<>();
        functionInfo = new HashMap<>();
        functionStack = new Stack<>();
        currentFunction = null;
        isTopLevelBlock = true;
        
        // Generate code
        emitHeader();
        visitNode(program);
        emitFooter();
        
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
        // Inicializa frame pointer para main
        mipsCode.append("move $fp, $sp\n");
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
    }

    private void emitPopTemp(String reg) {
        mipsCode.append("lw " + reg + ", 0($sp)\n");
        mipsCode.append("addu $sp, $sp, 4\n");
    }

    // Operações de pilha para gerenciar temporários float
    private void emitPushFloat(String freg) {
        mipsCode.append("subu $sp, $sp, 4\n");
        mipsCode.append("swc1 " + freg + ", 0($sp)\n");
    }

    private void emitPopFloat(String freg) {
        mipsCode.append("lwc1 " + freg + ", 0($sp)\n");
        mipsCode.append("addu $sp, $sp, 4\n");
    }

    // Despacha para o método apropriado baseado no tipo do nó
    private void visitNode(AST node) {
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
            
            // Conversões de tipo
            case I2R_NODE -> visitIntegerToReal(node);  
            
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
        if (currentFunction == null && isTopLevelBlock) {
            this.isTopLevelBlock = false; 
    
            List<AST> subroutines = new ArrayList<>();
            AST mainCompoundStmt = null;
    
            // --- PASSADA 1: Processar declarações de dados (Consts e Vars) ---
            // Isso popula o mapa `varLabels` para que as sub-rotinas saibam sobre as variáveis globais.
            for (int i = 0; i < node.getChildCount(); i++) {
                AST child = node.getChild(i);
                if (child.kind == NodeKind.CONST_SECTION_NODE || child.kind == NodeKind.VAR_SECTION_NODE) {
                    visitNode(child);
                } else if (child.kind == NodeKind.FUNC_DECL_NODE || child.kind == NodeKind.PROC_DECL_NODE) {
                    subroutines.add(child);
                } else if (child.kind == NodeKind.COMPOUND_STMT_NODE) {
                    mainCompoundStmt = child;
                }
            }
            
            // --- PASSADA 2: Processar declarações de sub-rotinas ---
            // Agora que as variáveis globais são conhecidas, podemos processar as sub-rotinas.
            // O código MIPS delas será armazenado em um buffer para ser inserido depois do código principal.
            StringBuilder procedureCodeBuffer = new StringBuilder();
            StringBuilder originalMipsCode = mipsCode;
            mipsCode = procedureCodeBuffer; // Redireciona a saída de código para o buffer
    
            for (AST subroutine : subroutines) {
                visitNode(subroutine);
            }
    
            mipsCode = originalMipsCode; // Restaura o buffer de saída original
    
            // --- PASSADA 3: Processar o corpo principal do programa ---
            // Agora que `functionInfo` está populado, as chamadas de procedimento funcionarão.
            if (mainCompoundStmt != null) {
                visitNode(mainCompoundStmt);
            }
    
            // --- Montagem Final do Código ---
            // Insere um salto para pular o código das sub-rotinas.
            String endMainLabel = generateLabel("end_main");
            mipsCode.append("j " + endMainLabel + "\n\n");
            
            // Anexa o código das sub-rotinas que estava no buffer.
            mipsCode.append(procedureCodeBuffer.toString());
            
            // Adiciona o label de destino do salto.
            mipsCode.append(endMainLabel + ":\n");
    
        } else {
            // Se for um bloco dentro de uma função/procedimento, processa os filhos em ordem.
            for (int i = 0; i < node.getChildCount(); i++) {
                visitNode(node.getChild(i));
            }
        }
    }
    
    private void visitConstSection(AST node) {
        // Constantes são processadas durante a análise semântica
        // Não precisamos gerar código especial aqui
    }

    // Declaração de função
    private void visitFunctionDeclaration(AST node) {
        String funcName = node.stringData;
        Type returnType = node.type;
        String label = "func_" + funcName;
        
        // Cria contexto da função
        FunctionContext funcContext = new FunctionContext(funcName, true, returnType);
        functionStack.push(funcContext);
        currentFunction = funcContext;
        
        // Processa lista de parâmetros se existir
        List<ParamEntry> parameters = new ArrayList<>();
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
        functionInfo.put(funcName, new FuncEntry(funcName, node.intData, returnType, parameters));
        
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
    }

    // Declaração de procedimento
    private void visitProcedureDeclaration(AST node) {
        String procName = node.stringData;
        String label = "func_" + procName;  // Usando 'func_' para consistência com as funções
        
        // Cria contexto do procedimento
        FunctionContext procContext = new FunctionContext(procName, false, null);
        functionStack.push(procContext);
        currentFunction = procContext;
        
        // Processa lista de parâmetros se existir
        List<ParamEntry> parameters = new ArrayList<>();
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
        functionInfo.put(procName, new FuncEntry(procName, node.intData, null, parameters));
        
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
    }

    // Lista de parâmetros
    private void visitParameterList(AST node) {
        // Processado durante visitFunctionDeclaration/visitProcedureDeclaration
        // Não precisa fazer nada aqui
    }

    // Declaração de parâmetro
    private void visitParameterDeclaration(AST node) {
        // Processado durante processParameterList
        // Não precisa fazer nada aqui
    }

    private List<ParamEntry> processParameterList(AST paramListNode) {
        List<ParamEntry> parameters = new ArrayList<>();
        int offset = 8; // Começa após $ra (4) e $fp (4)
    
        // A estrutura da AST para parâmetros é: PARAM_LIST_NODE -> PARAM_LIST_NODE (seção) -> PARAM_NODE
        // Portanto, precisamos de um loop aninhado para atravessar as seções e depois os parâmetros.
        for (int i = 0; i < paramListNode.getChildCount(); i++) {
            AST paramSectionNode = paramListNode.getChild(i);
            if (paramSectionNode.kind == NodeKind.PARAM_LIST_NODE) {
                for (int j = 0; j < paramSectionNode.getChildCount(); j++) {
                    AST paramNode = paramSectionNode.getChild(j);
                    if (paramNode.kind == NodeKind.PARAM_NODE) {
                        String paramName = paramNode.stringData;
                        Type paramType = paramNode.type;
                        // Assumindo que a informação 'by reference' está no intData do nó da AST
                        boolean isByRef = paramNode.intData == 1; 
                        
                        ParamEntry paramInfo = new ParamEntry(paramName, paramNode.intData, paramType, isByRef);
                        parameters.add(paramInfo);
                        
                        if (currentFunction != null) {
                            // Armazena o offset do parâmetro para acesso posterior.
                            // Parâmetros têm offsets positivos em relação ao $fp.
                            currentFunction.localVarOffsets.put(paramName, offset);
                        }
                        
                        offset += 4; // Cada parâmetro (valor ou endereço) ocupa 4 bytes
                    }
                }
            }
        }
        
        return parameters;
    }
    // Prólogo da função
    private void emitFunctionProlog(List<ParamEntry> parameters) {
        // Salva registradores
        mipsCode.append("subu $sp, $sp, 8\n"); // Espaço para $ra e $fp
        mipsCode.append("sw $ra, 4($sp)\n");   // Salva return address
        mipsCode.append("sw $fp, 0($sp)\n");   // Salva frame pointer
        mipsCode.append("move $fp, $sp\n");    // Novo frame pointer
        
        // Reserva espaço para variáveis locais (será calculado dinamicamente)
        // Não fazemos isso aqui pois não sabemos ainda quantas variáveis locais haverá
    }

    // Epílogo da função
    private void emitFunctionEpilog() {
        // Se há variáveis locais alocadas, libera o espaço
        if (currentFunction != null && currentFunction.localVarSize > 0) {
            mipsCode.append("addu $sp, $sp, " + currentFunction.localVarSize + "\n");
        }
        
        // Restaura registradores
        mipsCode.append("lw $fp, 0($sp)\n");   // Restaura frame pointer
        mipsCode.append("lw $ra, 4($sp)\n");   // Restaura return address
        mipsCode.append("addu $sp, $sp, 8\n"); // Restaura stack pointer
        mipsCode.append("jr $ra\n");           // Retorna
    }

    private void visitFunctionCall(AST node) {
        String funcName = node.stringData;
        FuncEntry funcInfo = functionInfo.get(funcName);
        
        if (funcInfo != null) {
            // Verifica se há argumentos
            if (node.getChildCount() > 0) {
                AST argsNode = node.getChild(0);
                
                // Empilha argumentos na ordem correta (da direita para esquerda)
                if (argsNode != null && argsNode.getChildCount() > 0) {
                    for (int i = argsNode.getChildCount() - 1; i >= 0; i--) {
                        AST argNode = argsNode.getChild(i);
                        
                        if (i < funcInfo.getParameters().size()) {
                            ParamEntry paramInfo = funcInfo.getParameters().get(i);
                            
                            if (paramInfo.isReference()) {
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
            mipsCode.append("jal func_" + funcInfo.getName() + "\n");
            
            // Remove argumentos da pilha
            int actualArgsCount = 0;
            if (node.getChildCount() > 0 && node.getChild(0) != null) {
                actualArgsCount = node.getChild(0).getChildCount();
            }
            
            if (actualArgsCount > 0) {
                int argsSize = actualArgsCount * 4;
                mipsCode.append("addu $sp, $sp, " + argsSize + "\n");
            }
            
            // Se for função, empilha o resultado
            if (funcInfo.getEntryType() != null) {
                if (funcInfo.getEntryType() == Type.REAL) {
                    emitPushFloat("$f0");
                } else {
                    emitPushTemp("$v0");
                }
            }
        }
    }


    private void visitVarSection(AST node) {
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
    }

    private void visitLocalVarDeclaration(AST node) {
        String varName = node.stringData;
        
        // Verifica se já é um parâmetro para não duplicar
        if (currentFunction != null) {
            FuncEntry funcInfo = functionInfo.get(currentFunction.name);
            if (funcInfo != null) {
                for (ParamEntry param : funcInfo.getParameters()) {
                    if (param.getName().equals(varName)) {
                        // É um parâmetro, não uma variável local - não processa novamente
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
    }

    private void visitVarDeclaration(AST node) {
        if (currentFunction != null) {
            visitLocalVarDeclaration(node);
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
            arrayInfo.put(varName, new ArrayEntry(varName, varDeclNode.intData, elementType, startIndex, endIndex));
            
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
        if (node.getChildCount() != 2) {
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
            return;
        }
        
        // Verifica se a expressão é uma chamada de função
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
    }

    private void visitArrayAssignment(AST arrayNode, String valueReg) {
        String arrayName = arrayNode.stringData;
        AST indexNode = arrayNode.getChild(0);
        
        // Calcula o índice
        visitNode(indexNode);
        emitPopTemp("$t1"); // índice em $t1
        
        // Ajusta o índice baseado no startIndex do array
        ArrayEntry info = arrayInfo.get(arrayName);
        if (info != null && info.getStartIndex() != 0) {
            mipsCode.append("addi $t1, $t1, " + (-info.getStartIndex()) + "\n");
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
        ArrayEntry info = arrayInfo.get(arrayName);
        if (info != null && info.getStartIndex() != 0) {
            mipsCode.append("addi $t1, $t1, " + (-info.getStartIndex()) + "\n");
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
        // Verifica se é procedimento built-in
        if (node.stringData.equalsIgnoreCase("writeln") || node.stringData.equalsIgnoreCase("write")) {
            visitBuiltinWrite(node);
        } else if (node.stringData.equalsIgnoreCase("read") || node.stringData.equalsIgnoreCase("readln")) {
            visitBuiltinRead(node);
        } else {
            // Verifica se é uma chamada de função ou procedimento
            String name = node.stringData;
            FuncEntry entry = functionInfo.get(name);
            
            if (entry != null) {
                // Processa argumentos na ordem inversa (da direita para esquerda)
                if (node.getChildCount() > 0) {
                    AST argsNode = node.getChild(0);
                    if (argsNode != null && argsNode.getChildCount() > 0) {
                        for (int i = argsNode.getChildCount() - 1; i >= 0; i--) {
                            AST argNode = argsNode.getChild(i);
                            visitNode(argNode);
                        }
                    }
                }
                
                // Chama o procedimento/função usando func_ como prefixo
                mipsCode.append("jal func_" + name + "\n");
                
                // Remove argumentos da pilha
                if (node.getChildCount() > 0) {
                    AST argsNode = node.getChild(0);
                    if (argsNode != null) {
                        int argsSize = argsNode.getChildCount() * 4;
                        if (argsSize > 0) {
                            mipsCode.append("addu $sp, $sp, " + argsSize + "\n");
                        }
                    }
                }
                
                // Se for uma função sendo usada como procedimento, descarta o valor de retorno
                if (entry.getEntryType() != null) {
                    emitPopTemp("$t0"); // Remove o valor de retorno da pilha
                }
            }
        }
    }

    private void visitBuiltinWrite(AST node) {
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
        
        if (node.stringData.equalsIgnoreCase("writeln")) {
            // Imprime nova linha
            mipsCode.append("la $a0, newline\n");
            mipsCode.append("li $v0, 4\n");
            mipsCode.append("syscall\n");
        }
    }

    private void visitBuiltinRead(AST node) {
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
        
        // Verifica se é variável local ou global
        if (currentFunction != null && currentFunction.localVarOffsets.containsKey(varName)) {
            int offset = currentFunction.localVarOffsets.get(varName);
            
            if (varNode.type == Type.REAL) {
                mipsCode.append("li $v0, 6\n");     // syscall para ler float
                mipsCode.append("syscall\n");
                mipsCode.append("swc1 $f0, " + offset + "($fp)\n");
            } else if (varNode.type == Type.INTEGER) {
                mipsCode.append("li $v0, 5\n");     // syscall para ler inteiro
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, " + offset + "($fp)\n");
            } else if (varNode.type == Type.CHAR) {
                mipsCode.append("li $v0, 12\n");    // syscall para ler caractere
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, " + offset + "($fp)\n");
            }
        } else {
            String label = varLabels.get(varName);
            if (label != null) {
                if (varNode.type == Type.REAL) {
                    mipsCode.append("li $v0, 6\n");     // syscall para ler float
                    mipsCode.append("syscall\n");
                    mipsCode.append("swc1 $f0, " + label + "\n");
                } else if (varNode.type == Type.INTEGER) {
                    mipsCode.append("li $v0, 5\n");     // syscall para ler inteiro
                    mipsCode.append("syscall\n");
                    mipsCode.append("sw $v0, " + label + "\n");
                } else if (varNode.type == Type.CHAR) {
                    mipsCode.append("li $v0, 12\n");    // syscall para ler caractere
                    mipsCode.append("syscall\n");
                    mipsCode.append("sw $v0, " + label + "\n");
                }
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
        ArrayEntry info = arrayInfo.get(arrayName);
        if (info != null && info.getStartIndex() != 0) {
            mipsCode.append("addi $t1, $t1, " + (-info.getStartIndex()) + "\n");
        }
        
        // Calcula endereço: base + (índice * 4)
        String arrayLabel = varLabels.get(arrayName);
        if (arrayLabel != null) {
            mipsCode.append("sll $t1, $t1, 2\n"); // multiplica por 4
            mipsCode.append("la $t2, " + arrayLabel + "\n");
            mipsCode.append("add $t2, $t2, $t1\n"); // $t2 = endereço do elemento
            
            // Lê o valor baseado no tipo do array
            if (arrayNode.type == Type.REAL) {
                mipsCode.append("li $v0, 6\n");      // syscall para ler float
                mipsCode.append("syscall\n");
                mipsCode.append("swc1 $f0, 0($t2)\n"); // armazena no elemento do array
            } else if (arrayNode.type == Type.INTEGER) {
                mipsCode.append("li $v0, 5\n");      // syscall para ler inteiro
                mipsCode.append("syscall\n");
                mipsCode.append("sw $v0, 0($t2)\n");  // armazena no elemento do array
            } else if (arrayNode.type == Type.CHAR) {
                mipsCode.append("li $v0, 12\n");     // syscall para ler caractere
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
;
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

    private void visitFloatComparison(AST node, String operation) {;
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
        
        // Antes de tratar como variável, verifica se é uma função.
        if (functionInfo.containsKey(varName)) {
            FuncEntry funcInfo = functionInfo.get(varName);
            Type returnType = funcInfo.getEntryType();
            if (returnType != null) {
                // É uma chamada de função sem parâmetros, tratada como "uso de variável" pelo parser.
                mipsCode.append("jal func_" + funcInfo.getName() + "\n"); // Chama a função
                
                // O valor de retorno da função está em $v0 (int) ou $f0 (real).
                // Empilhamos esse valor para que a operação de atribuição possa usá-lo.
                if (returnType == Type.REAL) {
                    emitPushFloat("$f0");
                } else {
                    emitPushTemp("$v0");
                }
                return; // Impede a execução do resto do código do método.
            }
        }

        // Primeiro verifica se estamos dentro de uma função/procedimento
        if (currentFunction != null) {
            // Verifica primeiro se é um parâmetro usando a lista de parâmetros
            FuncEntry funcInfo = functionInfo.get(currentFunction.name);
            if (funcInfo != null) {
                for (ParamEntry param : funcInfo.getParameters()) {
                    if (param.getName().equals(varName)) {
                        int offset = currentFunction.localVarOffsets.get(param.getName());
                        if (param.isReference()) {
                            // Parâmetro por referência - carrega através do endereço armazenado
                            mipsCode.append("lw $t0, " + offset + "($fp)\n"); // carrega endereço do parâmetro
                            
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
                                mipsCode.append("lwc1 $f0, " + offset + "($fp)\n");
                                emitPushFloat("$f0");
                            } else {
                                mipsCode.append("lw $t0, " + offset + "($fp)\n");
                                emitPushTemp("$t0");
                            }
                        }
                        return; // Encontrou o parâmetro, termina aqui
                    }
                }
            }
            
            // Se não é parâmetro, verifica se é variável local
            // As variáveis locais usam offsets negativos
            if (currentFunction.localVarOffsets.containsKey(varName)) {
                int offset = currentFunction.localVarOffsets.get(varName);
                
                // Só usar este path se o offset for negativo (variável local)
                if (offset < 0) {
                    if (node.type == Type.REAL) {
                        mipsCode.append("lwc1 $f0, " + offset + "($fp)\n");
                        emitPushFloat("$f0");
                    } else {
                        mipsCode.append("lw $t0, " + offset + "($fp)\n");
                        emitPushTemp("$t0");
                    }
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
    }

    private void visitArrayAccess(AST node) {
        String arrayName = node.stringData;
        AST indexNode = node.getChild(0);
        
        // Calcula o índice
        visitNode(indexNode);
        emitPopTemp("$t1"); // índice
        
        // Ajusta o índice baseado no startIndex do array
        ArrayEntry info = arrayInfo.get(arrayName);
        if (info != null && info.getStartIndex() != 0) {
            mipsCode.append("addi $t1, $t1, " + (-info.getStartIndex()) + "\n");
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
        mipsCode.append("mtc1 $t0, $f0\n");   // move para coprocessador
        mipsCode.append("cvt.s.w $f0, $f0\n"); // converte word para single
        
        emitPushFloat("$f0");
    }

    private void emitArgumentByReference(AST argNode) {
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
            ArrayEntry info = arrayInfo.get(arrayName);
            if (info != null && info.getStartIndex() != 0) {
                mipsCode.append("addi $t1, $t1, " + (-info.getStartIndex()) + "\n");
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
    }

    private void storeIntoVariable(AST varNode, String sourceReg, boolean isFloat) {
        String varName = varNode.stringData;
        
        // Verifica se é parâmetro por referência
        if (currentFunction != null) {
            FuncEntry funcInfo = functionInfo.get(currentFunction.name);
            if (funcInfo != null) {
                for (ParamEntry param : funcInfo.getParameters()) {
                    if (param.getName().equals(varName) && param.isReference()) {
                        // É parâmetro por referência - armazena através do endereço
                        int offset = currentFunction.localVarOffsets.get(param.getName()); // Get the offset from FunctionContext
                        mipsCode.append("lw $t2, " + offset + "($fp)\n"); // carrega endereço
                        
                        if (isFloat) {
                            mipsCode.append("swc1 " + sourceReg + ", 0($t2)\n");
                        } else {
                            mipsCode.append("sw " + sourceReg + ", 0($t2)\n");
                        }
                        return;
                    } else if (param.getName().equals(varName)) {
                        // Parâmetro por valor - armazena diretamente no stack frame
                        int offset = currentFunction.localVarOffsets.get(param.getName());
                        if (isFloat) {
                            mipsCode.append("swc1 " + sourceReg + ", " + offset + "($fp)\n");
                        } else {
                            mipsCode.append("sw " + sourceReg + ", " + offset + "($fp)\n");
                        }
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
    }
}
