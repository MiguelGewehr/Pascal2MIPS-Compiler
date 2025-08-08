program test14_redeclaration;
var
  x: integer;
  x: real;    { ERRO: variável x já foi declarada }
begin
  x := 10;
end.