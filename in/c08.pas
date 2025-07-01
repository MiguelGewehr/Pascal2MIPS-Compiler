{ Sample program in Pascal -
  silly program to handle some corner cases.
}

program c09;
var
    xxxx: integer;
    yyy: integer;
    longidentifier: integer;
    mensagem: string;  { Declaração da string }

begin
    mensagem := 'Esta é uma string declarada previamente.';
    
    writeln('Silly program.');
    writeln(4 + 3 * 2);             { Should write 10 not 14. }
    writeln('Silly program.');     { Should produce one entry in table. }
    
    xxxx := 4;
    longidentifier := 4;
    yyy := longidentifier;
    
    writeln((xxxx * yyy) div longidentifier); { Should write 4. }

    writeln(mensagem);  { Impressão da string declarada }
end.
