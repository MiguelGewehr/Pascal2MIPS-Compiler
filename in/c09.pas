program c09;
var
  name: array[1..20] of char;
  age: integer;
  height: real;
begin
  { Teste de operações de E/S }
  write('Digite seu nome: ');
  { Simulação de leitura de string }
  name[1] := 'J';
  name[2] := 'o';
  name[3] := 'a';
  name[4] := 'o';
  
  write('Digite sua idade: ');
  read(age);
  
  write('Digite sua altura: ');
  read(height);
  
  writeln('Nome: ', name[1], name[2], name[3], name[4]);
  writeln('Idade: ', age);
  writeln('Altura: ', height);
end.