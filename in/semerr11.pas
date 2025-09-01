program semerr11;

function add(a, b: integer): integer;
begin
  add := a + b
end;

begin
  writeln(add(10));      { ERRO: função espera 2 parâmetros, recebeu 1 }
  writeln(add(1, 2, 3)); { ERRO: função espera 2 parâmetros, recebeu 3 }
end.
