program ErroAtribuicao;
var
  a: integer;
  b: real;
begin
  b := 5.5;
  a := b; (* ERRO: Tipos incompatíveis para o operador ':=', LHS é 'INTEGER' e RHS é 'REAL' *)
end.