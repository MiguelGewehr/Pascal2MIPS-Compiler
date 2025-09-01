program c02;
var
  a, b: integer;
  p, q, result: boolean;
begin
  writeln('=== Operações de Comparação ===');
  a := 10;
  b := 5;
  writeln('a = ', a, ', b = ', b);
  
  result := a = b;
  writeln('a = b: ', result);
  
  result := a <> b;
  writeln('a <> b: ', result);
  
  result := a < b;
  writeln('a < b: ', result);
  
  result := a >= b;
  writeln('a >= b: ', result);
  
  writeln('--- Operações Lógicas ---');
  p := true;
  q := false;
  writeln('p = ', p, ', q = ', q);
  
  result := p and q;
  writeln('p AND q: ', result);
  
  result := p or q;
  writeln('p OR q: ', result);
  
  result := not p;
  writeln('NOT p: ', result);
end.
