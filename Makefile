JAVAC=javac
JAVA=java

# Caminho para o JAR do ANTLR
ANTLR_PATH=tools/antlr-4.13.2-complete.jar
# Caminho para o MARS
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
	$(JAVAC) $(CLASS_PATH_OPTION) -d $(BIN_PATH) $(GEN_PATH)/*.java checker/SemanticChecker.java interpreter/Interpreter.java Main.java

# Executa o interpretador Pascal
interpret:
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make interpret FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make interpret FILE=in/c01.pas"; \
		exit 1; \
	fi
	$(JAVA) $(CLASS_PATH_OPTION) Main -i $(FILE)

# Compila Pascal para MIPS e executa
compile:
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make compile FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make compile FILE=in/c01.pas"; \
		exit 1; \
	fi
	$(JAVA) $(CLASS_PATH_OPTION) Main -c $(FILE)
	@ASM_FILE=$$(basename $(FILE) .pas).asm; \
	if [ -f "out/$$ASM_FILE" ]; then \
		echo "Executando $$ASM_FILE no MARS..."; \
		$(MARS) out/$$ASM_FILE; \
	else \
		echo "Arquivo out/$$ASM_FILE não encontrado."; \
		exit 1; \
	fi

# Executa apenas o analisador léxico (gera tokens)
tokens:
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make tokens FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make tokens FILE=in/c01.pas"; \
		exit 1; \
	fi
	$(GRUN) parser.$(GRAMMAR_NAME) program -tokens $(FILE)

tree:
	@if [ -z "$(FILE)" ]; then \
		echo "Uso: make debug FILE=caminho/arquivo.pas"; \
		echo "Exemplo: make debug FILE=in/c01.pas"; \
		exit 1; \
	fi
	$(GRUN) parser.$(GRAMMAR_NAME) program -gui $(FILE)

# Limpa arquivos gerados
clean:
	rm -rf $(GEN_PATH) $(BIN_PATH)