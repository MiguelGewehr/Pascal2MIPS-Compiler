program TestWhileSimple;
var
  i: integer;
begin
  i := 1;
  writeln('Contagem de 1 a 5:');
  
  while i <= 5 do
  begin
    writeln('i = ', i);
    i := i + 1
  end;
  
  writeln('Fim da contagem')
end.