program c04;
var
  x: integer;
  positive: boolean;
begin
  { Teste de estrutura condicional simples }
  x := 10;
  if x > 0 then
    positive := true
  else
    positive := false;
    
  { Teste de if aninhado }
  if x > 0 then
  begin
    if x > 100 then
      write('Grande')
    else
      write('Pequeno positivo')
  end
  else
    write('Negativo ou zero');
end.