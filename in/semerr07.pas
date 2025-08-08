program test19_array_bounds_error;
var
  arr: array[1..5] of integer;
begin
  arr[0] := 10;   { ERRO: índice 0 fora dos limites [1..5] }
  arr[6] := 20;   { ERRO: índice 6 fora dos limites [1..5] }
end.