program TestExpressionAssignments;
var
  a, b, result: integer;
begin
  a := 10;
  b := 5;
  
  result := a + b;
  writeln('a + b = ', result);
  
  result := a - b;
  writeln('a - b = ', result);
  
  result := a * b;
  writeln('a * b = ', result);
  
  result := a div b;
  writeln('a div b = ', result)
end.