
# Cria a pasta gabarito se não existir
mkdir -p gabarito

# Itera sobre todos os arquivos .pas na pasta in
for test_file in in/*.pas; do
    # Obtém o nome base do arquivo (sem o caminho e extensão)
    base_name=$(basename "$test_file" .pas)
    
    # Define o nome do arquivo de saída
    output_file="gabarito/${base_name}_output.txt"
    
    # Executa o teste e redireciona a saída para o arquivo
    echo "Processando arquivo: $test_file"
    make run FILE="$test_file" > "$output_file" 2>&1
    
    echo "Saída salva em: $output_file"
    echo "----------------------------------------"
done

echo "Todos os testes foram executados e as saídas foram salvas na pasta gabarito/"
