PROGRAM Test7;
TYPE
  Colors = (RED, GREEN, BLUE);
  Matrix = ARRAY [1..3, 1..3] OF INTEGER;
VAR
  m: Matrix;
  c: Colors;
BEGIN
  m[1][1] := 1;
  m[2,2] := 2; { Forma alternativa de índice }
  c := GREEN;
  
  CASE c OF
    RED: WRITELN('Vermelho');
    GREEN: WRITELN('Verde');
    BLUE: WRITELN('Azul');
  END;
  
  WRITELN('Matriz[1,1] = ', m[1,1]);
  WRITELN('Operadores: ', (3 + 4 * 2) MOD 5);
END.