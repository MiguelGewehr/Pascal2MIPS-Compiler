{ Programa básico com diversos elementos léxicos }
PROGRAM Test9;
VAR
  x, y: INTEGER;
  msg: STRING;
BEGIN
  x := 10;
  y := x * 2;
  msg := 'Hello ''World''';
  IF x <> y THEN
    WRITELN(msg)
  ELSE
    WRITELN('Valores iguais');
END.