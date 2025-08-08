program c07;
var
  x, y: integer;

procedure swap(var a, b: integer);
var
  temp: integer;
begin
  { Procedimento para trocar valores }
  temp := a;
  a := b;
  b := temp
end;

procedure increment(var n: integer);
begin
  { Procedimento para incrementar }
  n := n + 1
end;

begin
  x := 10;
  y := 20;
  swap(x, y);        { x=20, y=10 }
  increment(x);      { x=21 }
end.