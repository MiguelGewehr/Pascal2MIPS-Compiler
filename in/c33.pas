program TestProcedureNoParams;

var
    counter: integer;

procedure incrementCounter;
begin
    counter := counter + 1;
end;

begin
    counter := 0;
    incrementCounter;
    incrementCounter;
    writeln('Counter value: ', counter);
end.