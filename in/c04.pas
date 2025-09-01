program c04;
var
  i, sum: integer;
begin
  writeln('=== Laços While ===');
  writeln('Calculando soma de 1 a 5:');
  
  i := 1;
  sum := 0;
  while i <= 5 do
  begin
    writeln('i = ', i, ', soma parcial = ', sum);
    sum := sum + i;
    i := i + 1
  end;
  writeln('Soma final: ', sum);
  
  writeln('--- While aninhado ---');
  i := 1;
  while i <= 2 do
  begin
    writeln('Loop externo i = ', i);
    sum := 0;
    while sum < 10 do
    begin
      sum := sum + i;
      writeln('  Loop interno: sum = ', sum);
    end;
    i := i + 1
  end;
  writeln('Loops concluídos');
end.