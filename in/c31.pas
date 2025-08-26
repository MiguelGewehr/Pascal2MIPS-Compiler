program TestArrayInt;

var
    { Array de inteiros com índice de 1 a 3 }
    numeros: array[1..3] of integer;
    i: integer;

begin
    { Inicializando o array }
    numeros[1] := 10;
    numeros[2] := 20;
    numeros[3] := 30;
    
    { Acessando e imprimindo valores do array usando WHILE }
    writeln('Valores do array:');
    i := 1;
    while i <= 3 do
    begin
        writeln('numeros[', i, '] = ', numeros[i]);
        i := i + 1;
    end;
    
    { Modificando um elemento }
    numeros[2] := numeros[1] + numeros[3];
    writeln('Novo valor de numeros[2]: ', numeros[2]);
end.