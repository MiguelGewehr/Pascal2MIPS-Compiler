program lexerr01;
var
  x: integer;
begin
  { Caractere inválido @ }
  x := 10@;  { ERRO: caractere @ não é válido }
end.