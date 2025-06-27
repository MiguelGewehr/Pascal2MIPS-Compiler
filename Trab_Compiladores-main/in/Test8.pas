(* Teste com números reais, comentários aninhados e caracteres especiais *)
PROGRAM Test8;
CONST
  pi = 3.1415e-2;
  ch = '''';
VAR
  a: ARRAY [1..10] OF REAL;
BEGIN
  a[1] := pi + 2.5E3;
  { Este é um { comentário } aninhado }
  WRITELN('Caractere: ', ch, ' Valor: ', a[1]:0:2);
END.