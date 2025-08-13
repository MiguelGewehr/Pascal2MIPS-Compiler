program TestWhileComplex;
var
  x, y: integer;
begin
  x := 10;
  y := 1;
  
  writeln('Loop com condicao complexa:');
  
  while (x > 0) and (y <= 5) do
  begin
    writeln('x=', x, ' y=', y);
    x := x - 2;
    y := y + 1
  end;
  
  writeln('Final: x=', x, ' y=', y)
end.