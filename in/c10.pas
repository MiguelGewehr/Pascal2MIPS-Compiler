program c10;
var
  arr: array[1..5] of integer;
  i, j, temp: integer;

procedure print_array;
var
  k: integer;
begin
  k := 1;
  while k <= 5 do
  begin
    write(arr[k], ' ');
    k := k + 1
  end;
  writeln
end;

begin
  { Inicialização do array }
  arr[1] := 64;
  arr[2] := 34;
  arr[3] := 25;
  arr[4] := 12;
  arr[5] := 22;
  
  write('Array original: ');
  print_array;
  
  { Bubble sort }
  i := 1;
  while i <= 4 do
  begin
    j := 1;
    while j <= 5 - i do
    begin
      if arr[j] > arr[j + 1] then
      begin
        temp := arr[j];
        arr[j] := arr[j + 1];
        arr[j + 1] := temp
      end;
      j := j + 1
    end;
    i := i + 1
  end;
  
  write('Array ordenado: ');
  print_array
end.