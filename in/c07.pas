program c07;
var
  result: integer;

function factorial(n: integer): integer;
begin
  writeln('Calculando fatorial de ', n);
  if n <= 1 then
    factorial := 1
  else
    factorial := n * factorial(n - 1)
end;

function max(a, b: integer): integer;
begin
  writeln('Comparando ', a, ' e ', b);
  if a > b then
    max := a
  else
    max := b
end;

begin
  writeln('=== Teste de Funções ===');
  
  result := factorial(5);
  writeln('Fatorial de 5 = ', result);
  
  result := max(10, 20);
  writeln('Máximo entre 10 e 20 = ', result);
  
  result := max(30, 15);
  writeln('Máximo entre 30 e 15 = ', result);
end.