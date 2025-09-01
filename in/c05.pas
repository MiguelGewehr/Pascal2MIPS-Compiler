program c05;
var
  numbers: array[1..5] of integer;
  i: integer;
begin
  writeln('=== Teste de Arrays ===');
  writeln('Inicializando array...');
  
  numbers[1] := 10;
  numbers[2] := 20;
  numbers[3] := 30;
  
  writeln('numbers[1] = ', numbers[1]);
  writeln('numbers[2] = ', numbers[2]);
  writeln('numbers[3] = ', numbers[3]);
  
  i := numbers[1] + numbers[2];
  writeln('Soma numbers[1] + numbers[2] = ', i);
  
  writeln('--- Preenchendo array com loop ---');
  i := 1;
  while i <= 3 do
  begin
    numbers[i] := i * 10;
    writeln('numbers[', i, '] = ', numbers[i]);
    i := i + 1
  end
end.
