program synerr04;
var
  i: integer;
begin
  i := 1;
  while i <= 5   { ERRO: falta 'do' }
    i := i + 1;
end.