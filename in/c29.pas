program TesteIf;
var
    idade: integer;
begin
    idade := 18;
    
    if idade >= 18 then
        writeln('Maior de idade')
    else
        writeln('Menor de idade');
        
    if idade = 18 then
        writeln('Tem exatamente 18 anos');
end.