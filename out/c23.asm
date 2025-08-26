.data
newline: .asciiz "\n"
str_0: .asciiz "Nome: "
str_1: .asciiz "Idade: "
str_2: .asciiz "Altura: "
str_3: .asciiz "Ativo: "
str_4: .asciiz "Multiplos valores: "

var_name: .word 0
var_age: .word 0
var_height: .float 0.0
var_active: .word 0
float_const_0: .float 1.75
.text
.globl main
main:
# Variáveis globais
li $t0, 74
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_name
li $t0, 25
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_age
lwc1 $f0, float_const_0
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_height
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_active
la $t0, str_0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_name
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
la $t0, str_1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_age
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
lwc1 $f0, var_height
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
la $a0, newline
li $v0, 4
syscall
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_active
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
la $t0, str_4
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, var_name
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
lw $t0, var_age
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
lwc1 $f0, var_height
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f12, 0($sp)
addu $sp, $sp, 4
li $v0, 2
syscall
lw $t0, var_active
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
li $v0, 10
syscall
