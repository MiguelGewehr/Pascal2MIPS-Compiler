program TestArrayParameter;

type
  TArray = array [1..5] of integer;

var
  MyArray: TArray;
  i: integer;

function SumArray(arr: TArray): integer;
var
  sum, j: integer;
begin
  sum := 0;
  for j := 1 to 5 do
    sum := sum + arr[j];
  SumArray := sum;
end;

begin
  { Preenche o array }
  for i := 1 to 5 do
    MyArray[i] := i * 10;
  
  { Chama a função passando o array como parâmetro }
  writeln('Soma dos elementos: ', SumArray(MyArray));
end.