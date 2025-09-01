.data
newline: .asciiz "\n"
str_0: .asciiz "=== Operações Aritméticas ==="
str_1: .asciiz "Soma: "
str_2: .asciiz " + "
str_3: .asciiz " = "
str_4: .asciiz "Subtração: "
str_5: .asciiz " - "
str_6: .asciiz "Multiplicação: "
str_7: .asciiz " * "
str_8: .asciiz "Divisão inteira: "
str_9: .asciiz " div "
str_10: .asciiz "Módulo: "
str_11: .asciiz " mod "
str_12: .asciiz "--- Operações com reais ---"
str_13: .asciiz "Soma real: 10.5 + 3.2 = "
str_14: .asciiz "Divisão real: 10.5 / 3.2 = "

var_a: .word 0
var_b: .word 0
var_c: .word 0
var_x: .float 0.0
var_y: .float 0.0
float_const_0: .float 10.5
float_const_1: .float 3.2
float_const_2: .float 10.5
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
li $t0, 10
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_a
li $t0, 3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_b
lw $t0, var_a
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
add $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
la $t0, str_1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_a
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_c
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
lw $t0, var_a
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
sub $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
la $t0, str_4
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_a
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_c
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
lw $t0, var_a
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
mul $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
la $t0, str_6
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_a
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
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_c
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
lw $t0, var_a
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
div $t0, $t1
mflo $t0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
la $t0, str_8
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_a
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
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_c
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
lw $t0, var_a
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
div $t0, $t1
mfhi $t0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
la $t0, str_10
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_a
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_11
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_b
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_c
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
la $t0, str_12
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
la $a0, newline
li $v0, 4
syscall
lwc1 $f0, float_const_0
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_x
lwc1 $f0, float_const_1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_y
lwc1 $f0, var_x
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_y
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
swc1 $f0, var_x
la $t0, str_13
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_x
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $a0, newline
li $v0, 4
syscall
lwc1 $f0, float_const_2
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_x
lwc1 $f0, var_x
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_y
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
swc1 $f0, var_x
la $t0, str_14
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_x
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $a0, newline
li $v0, 4
syscall
j end_main_3

end_main_3:
li $v0, 10
syscall
