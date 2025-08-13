program TestConstants;
const
  MAX_VALUE = 100;
  PI = 3.14159;
  ACTIVE = true;
  SEPARATOR = '-';
var
  temp: integer;
begin
  writeln('Teste de constantes:');
  writeln('MAX_VALUE = ', MAX_VALUE);
  writeln('PI = ', PI);
  writeln('ACTIVE = ', ACTIVE);
  writeln('SEPARATOR = ', SEPARATOR);
  temp := MAX_VALUE;
  writeln('temp recebeu MAX_VALUE: ', temp)
end.