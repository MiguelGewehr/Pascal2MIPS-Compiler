program TesteComparacoes;
var
    x, y: integer;
    resultado: boolean;
begin
    x := 10;
    y := 20;
    
    resultado := x = y;
    writeln(resultado);
    
    resultado := x < y;
    writeln(resultado);
    
    resultado := x > y;
    writeln(resultado);
    
    resultado := x <= y;
    writeln(resultado);
    
    resultado := x >= y;
    writeln(resultado);
    
    resultado := x <> y;
    writeln(resultado);
end.