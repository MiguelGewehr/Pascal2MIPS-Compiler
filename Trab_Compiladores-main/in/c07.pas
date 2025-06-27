{ Sample program in Pascal - computes the GCD of two numbers }
program c07;
var
    u, v, temp: integer;
begin
    read(u);
    read(v); { input two integers }
    
    if v = 0 then
        { do nothing }
    else
        repeat
            temp := v;
            v := u - (u div v) * v; { usando div para divisão inteira }
            u := temp;
        until v = 0;
    
    write(u); { output gcd of original u & v }
end.