program RecursaoMinima;

function simples(n: integer): integer;
begin
    if n = 0 then
        simples := 0
    else
        simples := simples(n - 1);
end;

begin
    simples(3); {Chamada recursiva básica}
    writeln('Recursão completa!');
end.