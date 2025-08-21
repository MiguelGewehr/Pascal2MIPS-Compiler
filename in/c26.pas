program TesteAritmetica;
var
    a, b, c, resultado: integer;
begin
    a := 15;
    b := 4;
    c := 2;
    
    resultado := a + b * c - 3;
    writeln(resultado);
    
    resultado := a div b;
    writeln(resultado);
    
    resultado := a mod b;
    writeln(resultado);
end.