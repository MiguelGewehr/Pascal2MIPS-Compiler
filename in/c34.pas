program TestFunctionNoParams;

var
    result: integer;

function getNumber: integer;
begin
    getNumber := 42;
end;

begin
    result := getNumber;
    writeln('The answer is: ', result);
end.