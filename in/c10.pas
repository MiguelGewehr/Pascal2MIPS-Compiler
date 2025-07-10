program c10;

(* Comentário de várias linhas
   que deve ser ignorado *)

var
  arr: array[1..5] of integer;
  i: integer;

function Square(n: integer): integer;
begin
  Square := n * n;
end;



begin
  for i := 1 to 5 do
    arr[i] := Square(i);
end.