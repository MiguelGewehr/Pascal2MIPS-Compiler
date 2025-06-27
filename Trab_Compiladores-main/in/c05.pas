{ Sample program in Pascal - 
  Basic loop test 2.
}

program c05;
var
  x: integer;
begin
  x := 1;
  repeat
    writeln(x);
    x := x + 1;
  until x > 5;
end.