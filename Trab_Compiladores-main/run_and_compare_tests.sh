#!/bin/bash

# Configurações
TEST_DIR="in"
OUTPUT_DIR="saida_atual"
GABARITO_DIR="gabarito"

# Cria pastas se não existirem
mkdir -p "$OUTPUT_DIR"
mkdir -p "$GABARITO_DIR"

# Função para rodar os testes e salvar a saída
run_tests() {
    local output_dir=$1
    echo "Rodando testes e salvando em: $output_dir/"
    for test_file in "$TEST_DIR"/*.pas; do
        base_name=$(basename "$test_file" .pas)
        output_file="$output_dir/${base_name}_output.txt"
        echo "Processando: $test_file"
        make run FILE="$test_file" > "$output_file" 2>&1
    done
}

# Executa os testes e salva na pasta de saída atual
run_tests "$OUTPUT_DIR"

# Compara com o gabarito (se existir)
echo -e "\nComparando com o gabarito:"
for output_file in "$OUTPUT_DIR"/*_output.txt; do
    base_name=$(basename "$output_file" "_output.txt")
    gabarito_file="$GABARITO_DIR/${base_name}_output.txt"

    echo -e "\n=== Diferenças para $base_name ==="
    if [ -f "$gabarito_file" ]; then
        diff -u "$gabarito_file" "$output_file"
        if [ $? -eq 0 ]; then
            echo "✅ Saída idêntica ao gabarito."
        else
            echo "❌ Diferenças encontradas!"
        fi
    else
        echo "⚠️  Arquivo de gabarito não encontrado ($gabarito_file)"
    fi
done

echo -e "\nConcluído! Saídas atuais em: $OUTPUT_DIR/"
