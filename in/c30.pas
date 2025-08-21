program TesteWhile;
var
    contador: integer;
begin
    contador := 1;
    
    while contador <= 5 do
    begin
        writeln(contador);
        contador := contador + 1;
    end;
end.