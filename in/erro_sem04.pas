program ErroIf;
var
  contador: integer;
begin
  contador := 5;
  if contador then (* ERRO: A expressão do comando 'IF' deve ser do tipo BOOLEAN, mas é do tipo 'INTEGER' *)
  begin
    writeln('O contador é diferente de zero.');
  end;
end.