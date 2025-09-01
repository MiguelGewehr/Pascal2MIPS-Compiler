### synerr07.pas - Declaração de Array Inválida
program synerr07;
var
  arr: array[1,5] of integer;  { ERRO: sintaxe incorreta, deveria ser [1..5] }
begin
  arr[1] := 10;
end.