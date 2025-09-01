program c03;
var
  x, score: integer;
begin
  writeln('=== Estruturas Condicionais ===');
  x := 10;
  writeln('x = ', x);
  
  if x > 0 then
    writeln('x é positivo')
  else
    writeln('x é negativo ou zero');
    
  writeln('--- If aninhado ---');
  score := 85;
  writeln('Score = ', score);
  
  if score >= 90 then
    writeln('Conceito: Excelente')
  else
    if score >= 80 then
      writeln('Conceito: Bom')
    else
      writeln('Conceito: Regular');
end.