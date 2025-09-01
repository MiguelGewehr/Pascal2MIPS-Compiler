.data
newline: .asciiz "\n"
str_0: .asciiz "Comparacoes com a="
str_1: .asciiz " b="
str_2: .asciiz "a = b: "
str_3: .asciiz "a <> b: "
str_4: .asciiz "a < b: "
str_5: .asciiz "a > b: "
str_6: .asciiz "a <= b: "
str_7: .asciiz "a >= b: "

var_a: .word 0
var_b: .word 0
var_result: .word 0
.text
.globl main
main:
move $fp, $sp
# Variáveis globais
li $t0, 10
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_a
li $t0, 20
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_b
la $t0, str_0
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
la $t0, str_1
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
seq $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
la $t0, str_2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_result
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
sne $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_result
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
slt $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
la $t0, str_4
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_result
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
sgt $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
la $t0, str_5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_result
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
sle $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
la $t0, str_6
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_result
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
sge $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
la $t0, str_7
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_result
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
j end_main_0

end_main_0:
li $v0, 10
syscall
