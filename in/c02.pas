program c02;
var
  a, b: integer;
  result: boolean;
begin
  { Teste de operações de comparação }
  a := 10;
  b := 5;
  
  result := a = b;   { Igualdade }
  result := a <> b;  { Diferença }
  result := a < b;   { Menor que }
  result := a > b;   { Maior que }
  result := a <= b;  { Menor ou igual }
  result := a >= b;  { Maior ou igual }
end.