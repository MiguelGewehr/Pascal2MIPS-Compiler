.data
newline: .asciiz "\n"
str_0: .asciiz "Operacoes com reais a="
str_1: .asciiz " b="
str_2: .asciiz "Adicao: "
str_3: .asciiz "Subtracao: "
str_4: .asciiz "Multiplicacao: "
str_5: .asciiz "Divisao: "

var_a: .float 0.0
var_b: .float 0.0
var_result: .float 0.0
float_const_0: .float 10.5
float_const_1: .float 3.2
.text
.globl main
main:
move $fp, $sp
# Variáveis globais
lwc1 $f0, float_const_0
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_a
lwc1 $f0, float_const_1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_b
la $t0, str_0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_a
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $t0, str_1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lwc1 $f0, var_b
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $a0, newline
li $v0, 4
syscall
lwc1 $f0, var_a
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_b
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
la $t0, str_2
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
lwc1 $f0, var_a
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_b
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f1, 0($sp)
addu $sp, $sp, 4
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
sub.s $f0, $f0, $f1
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
lwc1 $f0, var_a
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_b
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
la $t0, str_4
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
lwc1 $f0, var_a
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_b
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
j end_main_2

end_main_2:
li $v0, 10
syscall
