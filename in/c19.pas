program TestNestedIf;
var
  score: integer;
begin
  score := 85;
  writeln('Avaliacao com score=', score);
  
  if score >= 90 then
    writeln('Excelente')
  else
    if score >= 80 then
      writeln('Bom')
    else
      if score >= 70 then
        writeln('Regular')
      else
        writeln('Insuficiente')
end.