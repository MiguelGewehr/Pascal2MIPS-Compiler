program test13_undeclared_variable;
var
  x: integer;
begin
  x := 10;
  y := 20;  { ERRO: variável y não foi declarada }
end.