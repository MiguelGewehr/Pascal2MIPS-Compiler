# Comando do compilador Java
JAVAC=javac

# Comando da JVM
JAVA=java

# Caminho para o JAR do ANTLR em labs/tools
ANTLR_PATH=tools/antlr-4.13.2-complete.jar

# Diretórios
GEN_PATH=parser# saída dos .java gerados pelo ANTLR
BIN_PATH=bin# saída dos .class

# Nome da gramática
GRAMMAR_NAME=Pascal
LEXER_FILE=$(GRAMMAR_NAME)Lexer.g
PARSER_FILE=$(GRAMMAR_NAME)Parser.g

# Configurações de classpath
CLASS_PATH_OPTION=-cp .:$(ANTLR_PATH):$(GEN_PATH):$(BIN_PATH)

# Comando do ANTLR com suporte a visitor
ANTLR4=$(JAVA) -jar $(ANTLR_PATH)

# TestRig (opcional, modo debug)
GRUN=$(JAVA) $(CLASS_PATH_OPTION) org.antlr.v4.gui.TestRig

# ======== Targets =========

# Gera arquivos do ANTLR e compila com javac
all: antlr javac
	@echo "Compilação concluída."

# Executa o ANTLR com visitor
antlr: $(LEXER_FILE) $(PARSER_FILE)
	$(ANTLR4) -no-listener -visitor -o $(GEN_PATH) $(LEXER_FILE) $(PARSER_FILE)

# Compila os arquivos Java
javac:
	rm -rf $(BIN_PATH)
	mkdir -p $(BIN_PATH)
	$(JAVAC) $(CLASS_PATH_OPTION) -d $(BIN_PATH) $(GEN_PATH)/*.java PascalSemanticVisitor.java Main.java

# Executa o Main.java com arquivo de entrada
run:
	$(JAVA) $(CLASS_PATH_OPTION) Main $(FILE)

# Debug opcional com GUI do ANTLR
debug:
	cd $(BIN_PATH) && $(GRUN) parser.$(GRAMMAR_NAME) program -gui $(FILE)

# Limpa arquivos gerados
clean:
	rm -rf $(GEN_PATH) $(BIN_PATH)
