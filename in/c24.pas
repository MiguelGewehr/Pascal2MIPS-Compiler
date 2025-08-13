program TestTypeConversion;
var
  intNum: integer;
  realNum: real;
  result: real;
begin
  intNum := 5;
  realNum := 2.5;
  
  writeln('Teste de conversoes de tipo:');
  writeln('intNum = ', intNum);
  writeln('realNum = ', realNum);
  
  result := intNum + realNum;
  writeln('intNum + realNum = ', result);
  
  result := intNum * realNum;
  writeln('intNum * realNum = ', result);
  
  result := intNum / realNum;
  writeln('intNum / realNum = ', result)
end.