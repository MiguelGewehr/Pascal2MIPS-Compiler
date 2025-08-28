program TestSimple;
var
    result: integer;

function double(x: integer): integer;
begin
    double := x * 2;
end;

begin
    result := double(7);
    writeln('Double of 7: ', result);
end.