PROGRAM Test5;
FUNCTION Factorial(n: INTEGER): INTEGER;
BEGIN
  IF n <= 1 THEN
    Factorial := 1
  ELSE
    Factorial := n * Factorial(n-1);
END;

VAR
  result: INTEGER;
BEGIN
  result := Factorial(5);
  WRITELN('5! = ', result);
  WRITELN('Strings com escapes: ', 'Don''t panic');
END.