{ Sample program in Pascal - computes factorial }
program c06;
var
    x: integer;
    fact: integer;
begin
    read(x); { input an integer }
    if 0 < x then { don't compute if x <= 0 }
    begin
        fact := 1;
        repeat
            fact := fact * x;
            x := x - 1;
        until x = 0;
        write(fact); { output factorial of x }
    end;
end.