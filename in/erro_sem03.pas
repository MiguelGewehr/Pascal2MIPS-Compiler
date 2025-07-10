program ErroExpressao;
var
  a: integer;
  b: boolean;
  c: integer;
begin
  a := 10;
  b := true;
  c := a + b; (* ERRO: Tipos incompatíveis para o operador '+', LHS é 'INTEGER' e RHS é 'BOOLEAN' *)
end.