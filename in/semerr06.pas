program semerr06;
var
  x: integer;
begin
  x := 10;
  if x then           { ERRO: condição deve ser boolean }
    x := 20;
  
  while x do          { ERRO: condição deve ser boolean }
    x := x - 1;
end.