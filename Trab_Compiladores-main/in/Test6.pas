PROGRAM Test6;
LABEL 100, 200;
TYPE
  Range = 1..100;
  Point = RECORD
    x, y: REAL;
  END;
VAR
  p: Point;
BEGIN
  100:
  p.x := 10.5;
  p.y := p.x / 2;
  IF p.y >= 5.25 THEN
    GOTO 200;
  WRITELN('Ponto: (', p.x:0:1, ',', p.y:0:1, ')');
  200:
END.