program TestPrecedence;
var
  result: integer;
begin
  writeln('Teste de precedencia de operadores:');
  
  result := 2 + 3 * 4;
  writeln('2 + 3 * 4 = ', result);
  
  result := (2 + 3) * 4;
  writeln('(2 + 3) * 4 = ', result);
  
  result := 10 - 6 div 2;
  writeln('10 - 6 div 2 = ', result);
  
  result := (10 - 6) div 2;
  writeln('(10 - 6) div 2 = ', result)
end.