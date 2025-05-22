program erro_sintatico_1;
var
  a, b integer; // Erro: falta de dois pontos ":" antes do tipo
begin
  a := 10;
  b := 20;
  writeln('Soma: ', a + b);
end.
