program synerr01;
var
  x: integer
  y: integer;  { ERRO: falta ponto e vírgula após declaração de x }
begin
  x := 10;
  if x > 0    { ERRO: falta 'then' }
    x := 20;
end.