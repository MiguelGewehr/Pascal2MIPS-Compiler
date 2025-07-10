program SomaExemplo;

(* Comentário explicativo sobre a função *)
var
  a, b, resultado: integer;

function Soma(x, y: integer): integer;
begin
  Soma := x + y;
end;

begin
  a := 10;
  b := 5;
  resultado := Soma(a, b);
  writeln('A soma de ', a, ' e ', b, ' é ', resultado);
end.
