program semerr12;
const
  ZERO = 0;
var
  result: integer;
begin
  result := 10 div ZERO;  { ERRO: divisão por zero }
end.
