.data
newline: .asciiz "\n"
str_0: .asciiz "=== Teste de Conversões de Tipo ==="
str_1: .asciiz "intNum = "
str_2: .asciiz "realNum = "
str_3: .asciiz "intNum + realNum = "
str_4: .asciiz " + "
str_5: .asciiz " = "
str_6: .asciiz "intNum / realNum = "
str_7: .asciiz " / "
str_8: .asciiz "intNum * realNum = "
str_9: .asciiz " * "
str_10: .asciiz "Conversão automática: integer -> real funcionando!"

var_intNum: .word 0
var_realNum: .float 0.0
var_result: .float 0.0
float_const_0: .float 2.5
.text
.globl main
main:
move $fp, $sp
# Variáveis globais
la $t0, str_0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
la $a0, newline
li $v0, 4
syscall
li $t0, 5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_intNum
lwc1 $f0, float_const_0
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_realNum
la $t0, str_1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_intNum
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
la $t0, str_2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_realNum
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $a0, newline
li $v0, 4
syscall
lw $t0, var_intNum
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
mtc1 $t0, $f0
cvt.s.w $f0, $f0
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_realNum
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f1, 0($sp)
addu $sp, $sp, 4
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
add.s $f0, $f0, $f1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_result
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_intNum
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_4
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_realNum
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $t0, str_5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_result
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $a0, newline
li $v0, 4
syscall
lw $t0, var_intNum
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
mtc1 $t0, $f0
cvt.s.w $f0, $f0
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_realNum
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f1, 0($sp)
addu $sp, $sp, 4
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
div.s $f0, $f0, $f1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_result
la $t0, str_6
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_intNum
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_7
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_realNum
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $t0, str_5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_result
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $a0, newline
li $v0, 4
syscall
lw $t0, var_intNum
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
mtc1 $t0, $f0
cvt.s.w $f0, $f0
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_realNum
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f1, 0($sp)
addu $sp, $sp, 4
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
mul.s $f0, $f0, $f1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_result
la $t0, str_8
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_intNum
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_9
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_realNum
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $t0, str_5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_result
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $a0, newline
li $v0, 4
syscall
la $t0, str_10
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
la $a0, newline
li $v0, 4
syscall
j end_main_1

end_main_1:
li $v0, 10
syscall
