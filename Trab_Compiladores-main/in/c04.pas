{ Sample program in Pascal -
  Basic loop test 1.
}

program c04;
var
    x: integer;
begin
    x := 5;
    repeat
        write(x, ' ');
        x := x - 1;
    until x = 0;
end.