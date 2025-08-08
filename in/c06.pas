program c06;
var
  numbers: array[1..5] of integer;
  i: integer;
begin
  { Teste de declaração e uso de arrays }
  numbers[1] := 10;
  numbers[2] := 20;
  numbers[3] := 30;
  numbers[4] := 40;
  numbers[5] := 50;
  
  { Acesso aos elementos }
  i := numbers[1] + numbers[2];
  
  { Loop com array }
  i := 1;
  while i <= 5 do
  begin
    numbers[i] := i * 10;
    i := i + 1
  end
end.