program TesteFuncao;

var
    resultado: integer;

{ Função que dobra um número }
function dobro(x: integer): integer;
begin
    dobro := x * 2;
end;

{ Programa principal }
begin
    resultado := dobro(5);
    writeln('Dobro de 5: ', resultado);
end.