program test20_function_return_error;

function get_number: integer;
var
  flag: boolean;
begin
  flag := true;
  get_number := flag  { ERRO: retornando boolean em função integer }
end;

begin
  write(get_number)
end.