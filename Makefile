# Comando do compilador Java
JAVAC=javac
# Comando da JVM
JAVA=java
# Caminho para o JAR do ANTLR em labs/tools
ANTLR_PATH=tools/antlr-4.13.2-complete.jar
# Caminho para o MARS (ADICIONAR ESTA LINHA)
MARS_PATH=tools/Mars4_5.jar
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
# MARS (ADICIONAR ESTA LINHA)
MARS=$(JAVA) -jar $(MARS_PATH)
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
	$(JAVAC) $(CLASS_PATH_OPTION) -d $(BIN_PATH) $(GEN_PATH)/*.java checker/SemanticChecker.java Main.java
# Executa o Main.java com arquivo de entrada
run:
	$(JAVA) $(CLASS_PATH_OPTION) Main $(FILE)
# Executa apenas o analisador léxico (gera tokens)
tokens:
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make tokens FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make tokens FILE=in/c01.pas"; \
		exit 1; \
	fi
	$(GRUN) parser.$(GRAMMAR_NAME) program -tokens $(FILE)

# Gera e exibe a árvore sintática em modo texto
tree:
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make tree FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make tree FILE=in/c01.pas"; \
		exit 1; \
	fi
	$(GRUN) parser.$(GRAMMAR_NAME) program -tree $(FILE)

# Verifica se o parsing é bem-sucedido (sem output da árvore)
parse:
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make parse FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make parse FILE=in/c01.pas"; \
		exit 1; \
	fi
	$(GRUN) parser.$(GRAMMAR_NAME) program $(FILE)

# Debug opcional com GUI do ANTLR
debug:
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make debug FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make debug FILE=in/c01.pas"; \
		exit 1; \
	fi
	$(GRUN) parser.$(GRAMMAR_NAME) program -gui $(FILE)
# Limpa arquivos gerados
clean:
	rm -rf $(GEN_PATH) $(BIN_PATH)
# Gera código MIPS e executa no MARS automaticamente
test: run
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make test FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make test FILE=in/c01.pas"; \
		exit 1; \
	fi
	@ASM_FILE=$$(basename $(FILE) .pas).asm; \
	if [ -f "out/$$ASM_FILE" ]; then \
		echo "Executando $$ASM_FILE no MARS..."; \
		$(MARS) out/$$ASM_FILE; \
	else \
		echo "Arquivo out/$$ASM_FILE não encontrado."; \
		exit 1; \
	fi