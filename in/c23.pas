program TestWriteProcs;
var
  name: char;
  age: integer;
  height: real;
  active: boolean;
begin
  name := 'J';
  age := 25;
  height := 1.75;
  active := true;
  
  write('Nome: ');
  writeln(name);
  
  write('Idade: ');
  writeln(age);
  
  write('Altura: ');
  writeln(height);
  
  write('Ativo: ');
  writeln(active);
  
  writeln('Multiplos valores: ', name, age, height, active)
end.