program c08;
var
  result: integer;

function factorial(n: integer): integer;
begin
  { Função recursiva para calcular fatorial }
  if n <= 1 then
    factorial := 1
  else
    factorial := n * factorial(n - 1)
end;

function max(a, b: integer): integer;
begin
  { Função para retornar o maior valor }
  if a > b then
    max := a
  else
    max := b
end;

begin
  result := factorial('c');    { O tipo do parametro esta errado }
  result := max(10, 20);     { result = 20 }
end.