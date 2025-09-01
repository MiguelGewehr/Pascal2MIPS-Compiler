program semerr03;
var
  x: integer;
  flag: boolean;
begin
  x := 10;
  flag := true;
  x := flag;  { ERRO: não pode atribuir boolean a integer }
end.