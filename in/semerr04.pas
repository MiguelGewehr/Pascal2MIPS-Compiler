program test16_type_error_operation;
var
  x: integer;
  flag: boolean;
  result: integer;
begin
  x := 10;
  flag := true;
  result := x + flag;  { ERRO: não pode somar integer com boolean }
end.