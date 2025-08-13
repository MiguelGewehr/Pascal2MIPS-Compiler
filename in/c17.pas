program TestLogical;
var
  p, q: boolean;
  result: boolean;
begin
  p := true;
  q := false;
  
  writeln('Operacoes logicas com p=', p, ' q=', q);
  
  result := p and q;
  writeln('p AND q = ', result);
  
  result := p or q;
  writeln('p OR q = ', result);
  
  result := not p;
  writeln('NOT p = ', result);
  
  result := not q;
  writeln('NOT q = ', result)
end.