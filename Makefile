JAVAC = javac
JAVA = java
ANTLR_JAR = antlr-4.13.2-complete.jar
GRAMMAR_NAME = PascalLexer
GEN_DIR = gen

CLASSPATH = .:$(ANTLR_JAR)
ANTLR_CMD = $(JAVA) -jar $(ANTLR_JAR)
GRUN_CMD = $(JAVA) -cp $(CLASSPATH) org.antlr.v4.gui.TestRig

TEST_INPUTS = test_inputs
TEST_OUTPUTS = test_outputs

.PHONY: all clean test

all: antlr compile

antlr: $(GRAMMAR_NAME).g
	$(ANTLR_CMD) -o $(GEN_DIR) -no-listener -visitor $(GRAMMAR_NAME).g

compile:
	$(JAVAC) -cp $(CLASSPATH) $(GEN_DIR)/*.java

run:
	cd $(GEN_DIR) && $(GRUN_CMD) $(GRAMMAR_NAME) tokens -tokens

runfile:
	cd $(GEN_DIR) && $(GRUN_CMD) $(GRAMMAR_NAME) tokens -tokens ../$(INPUT_FILE)

test: all
	@mkdir -p $(TEST_OUTPUTS)
	@for file in $(TEST_INPUTS)/*.pas; do \
		echo "Testing $$(basename $$file)"; \
		cd $(GEN_DIR) && $(GRUN_CMD) $(GRAMMAR_NAME) tokens -tokens ../$$file > ../$(TEST_OUTPUTS)/$$(basename $$file .pas).out 2>&1; \
	done
	@echo "All tests completed. Outputs in $(TEST_OUTPUTS)/"

clean:
	rm -rf $(GEN_DIR)
	rm -rf $(TEST_OUTPUTS)
