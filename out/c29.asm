.data
newline: .asciiz "\n"
str_0: .asciiz "Maior de idade"
str_1: .asciiz "Menor de idade"
str_2: .asciiz "Tem exatamente 18 anos"

var_idade: .word 0
.text
.globl main
main:
# Variáveis globais
li $t0, 18
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_idade
lw $t0, var_idade
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 18
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
beq $t0, $zero, else_0
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
j endif_1
else_0:
la $t0, str_1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
la $a0, newline
li $v0, 4
syscall
endif_1:
lw $t0, var_idade
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 18
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
beq $t0, $zero, else_2
la $t0, str_2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
la $a0, newline
li $v0, 4
syscall
j endif_3
else_2:
endif_3:
li $v0, 10
syscall
