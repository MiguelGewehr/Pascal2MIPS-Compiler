program synerr03;
var
  x: integer;
begin
  x := 10;
  if x > 0 then
  begin
    x := 20;
  { ERRO: falta end correspondente }
end.