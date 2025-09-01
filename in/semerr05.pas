program semerr05;
var
  x: integer;
  flag: boolean;
  result: boolean;
begin
  x := 10;
  flag := true;
  result := x = flag;  { ERRO: não pode comparar integer com boolean }
end.