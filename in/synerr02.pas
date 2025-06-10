program synerr02;
var
  x, y, produto: integer;
begin
  x = 7; { Erro: deve ser ":="}
  y = 8; { Erro: deve ser ":=}
  produto := x * y;
  writeln('Produto: ', produto);
end.
