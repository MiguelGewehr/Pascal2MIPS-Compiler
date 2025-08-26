.data
newline: .asciiz "\n"

var_numbers: .word 0, .word 0, .word 0, .word 0, .word 0, .word 0
var_i: .word 0
.text
.globl main
main:
# Variáveis globais
li $t0, 10
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t2, var_numbers
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 20
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t2, var_numbers
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 30
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 3
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t2, var_numbers
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 40
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 4
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t2, var_numbers
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 50
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t2, var_numbers
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 60
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 6
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t2, var_numbers
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t0, var_numbers
add $t0, $t0, $t1
lw $t0, 0($t0)
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t0, var_numbers
add $t0, $t0, $t1
lw $t0, 0($t0)
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
sw $t0, var_i
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_i
loop_0:
lw $t0, var_i
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 5
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
beq $t0, $zero, endloop_1
lw $t0, var_i
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 10
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
lw $t0, var_i
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
subi $t1, $t1, 1
sll $t1, $t1, 2
la $t2, var_numbers
add $t2, $t2, $t1
sw $t0, 0($t2)
lw $t0, var_i
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 1
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
sw $t0, var_i
j loop_0
endloop_1:
li $v0, 10
syscall
