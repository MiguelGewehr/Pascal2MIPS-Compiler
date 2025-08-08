program test18_non_boolean_condition;
var
  x: integer;
begin
  x := 10;
  if x then           { ERRO: condição deve ser boolean, não integer }
    x := 20;
  
  while x do          { ERRO: condição deve ser boolean, não integer }
    x := x - 1;
end.