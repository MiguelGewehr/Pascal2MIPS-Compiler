program TestRealArithmetic;
var
  a, b: real;
  result: real;
begin
  a := 10.5;
  b := 3.2;
  
  writeln('Operacoes com reais a=', a, ' b=', b);
  
  result := a + b;
  writeln('Adicao: ', result);
  
  result := a - b;
  writeln('Subtracao: ', result);
  
  result := a * b;
  writeln('Multiplicacao: ', result);
  
  result := a / b;
  writeln('Divisao: ', result)
end.