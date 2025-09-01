.data
newline: .asciiz "\n"
str_0: .asciiz "Digite seu nome: "
str_1: .asciiz "Digite sua idade: "
str_2: .asciiz "Digite sua altura: "
str_3: .asciiz "Nome: "
str_4: .asciiz "Idade: "
str_5: .asciiz "Altura: "

var_name: .word 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0
var_age: .word 0
var_height: .float 0.0
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
li $t0, 74
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_name
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 111
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_name
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 97
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_name
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 111
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 4
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_name
add $t2, $t2, $t1
sw $t0, 0($t2)
la $t0, str_1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
li $v0, 5
syscall
sw $v0, var_age
la $t0, str_2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
li $v0, 6
syscall
swc1 $f0, var_height
la $t0, str_3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_name
add $t0, $t0, $t1
lw $t0, 0($t0)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
li $t0, 2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_name
add $t0, $t0, $t1
lw $t0, 0($t0)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
li $t0, 3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_name
add $t0, $t0, $t1
lw $t0, 0($t0)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
li $t0, 4
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_name
add $t0, $t0, $t1
lw $t0, 0($t0)
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
la $t0, str_5
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
j end_main_0

end_main_0:
li $v0, 10
syscall
