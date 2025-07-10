program TrocarValores;

(* Comentário sobre o procedimento *)
var
  x, y: integer;

procedure Troca(a, b: integer);
var
  temp: integer;
begin
  temp := a;
  a := b;
  b := temp;
end;

begin
  x := 10;
  y := 20;
  writeln('Antes da troca: x = ', x, ', y = ', y);
  Troca(x, y);
  writeln('Após a troca: x = ', x, ', y = ', y);
end.
