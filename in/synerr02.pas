program synerr02;
var
  x: integer;
begin
  x := 10;
  if x > 0    { ERRO: falta 'then' }
    x := 20;
end.