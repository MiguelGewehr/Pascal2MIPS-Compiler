program c08;
const
  MAX_VALUE = 100;
  PI = 3.14159;
var
  age: integer;
  height: real;
  temp: integer;
begin
  writeln('=== Teste de E/S e Constantes ===');
  writeln('Constantes definidas:');
  writeln('MAX_VALUE = ', MAX_VALUE);
  writeln('PI = ', PI);
  
  writeln('Digite sua idade: ');
  readln(age);
  writeln('Você digitou: ', age, ' anos');
  
  writeln('Digite sua altura (em metros): ');
  readln(height);
  writeln('Sua altura é: ', height, ' metros');
  
  temp := MAX_VALUE;
  writeln('Valor da constante atribuído a temp: ', temp);
  
  if age >= 18 then
    writeln('Você é maior de idade')
  else
    writeln('Você é menor de idade');
end.