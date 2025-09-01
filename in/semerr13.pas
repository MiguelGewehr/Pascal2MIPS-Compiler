program semerr13;
var
  c: char;
  result: integer;
begin
  c := 'A';
  result := c + 10;  { ERRO: não pode somar char com integer }
end.