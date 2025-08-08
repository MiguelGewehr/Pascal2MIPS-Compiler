program c03;
var
  p, q, result: boolean;
begin
  { Teste de operações lógicas }
  p := true;
  q := false;
  
  result := p and q;  { E lógico }
  result := p or q;   { OU lógico }
  result := not p;    { NÃO lógico }
  
  { Expressões compostas }
  result := (p and q) or (not p);
end.