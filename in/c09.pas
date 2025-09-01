program c09;
var
  intNum: integer;
  realNum: real;
  result: real;
begin
  writeln('=== Teste de Conversões de Tipo ===');
  intNum := 5;
  realNum := 2.5;
  
  writeln('intNum = ', intNum);
  writeln('realNum = ', realNum);
  
  result := intNum + realNum;
  writeln('intNum + realNum = ', intNum, ' + ', realNum, ' = ', result);
  
  result := intNum / realNum;
  writeln('intNum / realNum = ', intNum, ' / ', realNum, ' = ', result);
  
  result := intNum * realNum;
  writeln('intNum * realNum = ', intNum, ' * ', realNum, ' = ', result);
  
  writeln('Conversão automática: integer -> real funcionando!');
end.