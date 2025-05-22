program Test3;

var
  msg: string;
  ch: char;
  a, b: real;

begin
  msg := 'Hello, world!';
  ch := 'A';
  a := 3.14;
  b := 2e+2;

  if (a >= b) or (ch <> 'B') then
    msg := msg + '!';
end.
