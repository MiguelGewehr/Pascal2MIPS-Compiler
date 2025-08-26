program TesteReadArray;
var
    numero: integer;
    numeros: array[1..3] of integer;
    i: integer;

begin
    { Lendo uma variável simples }
    writeln('Digite um número inteiro:');
    readln(numero);
    writeln('Número digitado: ', numero);
    
    { Lendo valores para o array usando WHILE }
    writeln('Digite 3 números inteiros para o array:');
    i := 1;
    while i <= 3 do
    begin
        write('Número ', i, ': ');
        readln(numeros[i]);
        i := i + 1;
    end;
    
    { Mostrando os valores do array usando WHILE }
    writeln('Números digitados no array:');
    i := 1;
    while i <= 3 do
    begin
        writeln('numeros[', i, '] = ', numeros[i]);
        i := i + 1;
    end;
end.