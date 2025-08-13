program TestComparisons;
var
  a, b: integer;
  result: boolean;
begin
  a := 10;
  b := 20;
  
  writeln('Comparacoes com a=', a, ' b=', b);
  
  result := a = b;
  writeln('a = b: ', result);
  
  result := a <> b;
  writeln('a <> b: ', result);
  
  result := a < b;
  writeln('a < b: ', result);
  
  result := a > b;
  writeln('a > b: ', result);
  
  result := a <= b;
  writeln('a <= b: ', result);
  
  result := a >= b;
  writeln('a >= b: ', result)
end.