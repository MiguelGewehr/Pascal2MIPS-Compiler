program ErroNaoDeclarada;
var
  x: integer;
begin
  x := y + 1; (* ERRO: Identificador 'y' não declarado *)
end.