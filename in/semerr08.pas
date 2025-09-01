program semerr08;

function get_number: integer;
var
  flag: boolean;
begin
  flag := true;
  get_number := flag  { ERRO: retornando boolean em função integer }
end;

begin
  writeln(get_number)
end.