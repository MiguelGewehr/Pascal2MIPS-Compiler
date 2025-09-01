.data
newline: .asciiz "\n"

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
li $t0, 5
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
seq $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
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
j end_main_0

end_main_0:
li $v0, 10
syscall
