program semerr09;

function square(x: integer): integer;
begin
  square := x * x
end;

begin
  writeln(square(true));    { ERRO: parâmetro boolean para função que espera integer }
end.
