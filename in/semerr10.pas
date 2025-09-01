program semerr10;
var
  result: integer;

procedure doSomething;
begin
  writeln('Doing something')
end;

begin
  result := doSomething;  { ERRO: procedimento não retorna valor }
end.