program c01;
var
  a, b, c: integer;
  x, y: real;
begin
  writeln('=== Operações Aritméticas ===');
  a := 10;
  b := 3;
  
  c := a + b;
  writeln('Soma: ', a, ' + ', b, ' = ', c);
  
  c := a - b;
  writeln('Subtração: ', a, ' - ', b, ' = ', c);
  
  c := a * b;
  writeln('Multiplicação: ', a, ' * ', b, ' = ', c);
  
  c := a div b;
  writeln('Divisão inteira: ', a, ' div ', b, ' = ', c);
  
  c := a mod b;
  writeln('Módulo: ', a, ' mod ', b, ' = ', c);
  
  writeln('--- Operações com reais ---');
  x := 10.5;
  y := 3.2;
  
  x := x + y;
  writeln('Soma real: 10.5 + 3.2 = ', x);
  
  x := 10.5;
  x := x / y;
  writeln('Divisão real: 10.5 / 3.2 = ', x);
end.