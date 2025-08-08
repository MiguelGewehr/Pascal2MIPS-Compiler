program c01;
var
  a, b, c: integer;
  x, y: real;
begin
  { Teste de operações aritméticas com inteiros }
  a := 10;
  b := 3;
  c := a + b;        { Soma }
  c := a - b;        { Subtração }
  c := a * b;        { Multiplicação }
  c := a div b;      { Divisão inteira }
  c := a mod b;      { Módulo }
  
  { Teste de operações aritméticas com reais }
  x := 10.5;
  y := 3.2;
  x := x + y;        { Soma de reais }
  x := x - y;        { Subtração de reais }
  x := x * y;        { Multiplicação de reais }
  x := x / y;        { Divisão real }
end.