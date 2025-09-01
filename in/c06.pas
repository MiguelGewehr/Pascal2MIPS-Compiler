program c06;
var
  x, y: integer;

procedure swap(var a, b: integer);
var
  temp: integer;
begin
  writeln('Trocando valores: a = ', a, ', b = ', b);
  temp := a;
  a := b;
  b := temp;
  writeln('Após troca: a = ', a, ', b = ', b);
end;

begin
  writeln('=== Teste de Procedimentos ===');
  x := 10;
  y := 20;
  
  writeln('Valores iniciais: x = ', x, ', y = ', y);
  swap(x, y);
  writeln('Valores finais: x = ', x, ', y = ', y);
end.
