program TesteLogica;
var
    a, b: boolean;
    resultado: boolean;
begin
    a := true;
    b := false;
    
    resultado := a and b;
    writeln(resultado);
    
    resultado := a or b;
    writeln(resultado);
    
    resultado := not a;
    writeln(resultado);
    
    resultado := not b;
    writeln(resultado);
end.