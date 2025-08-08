program c05;
var
  i, sum: integer;
begin
  { Teste de laço while simples }
  i := 1;
  sum := 0;
  while i <= 10 do
  begin
    sum := sum + i;
    i := i + 1
  end;
  
  { Teste de while aninhado }
  i := 1;
  while i <= 3 do
  begin
    sum := 0;
    while sum < 10 do
      sum := sum + i;
    i := i + 1
  end
end.