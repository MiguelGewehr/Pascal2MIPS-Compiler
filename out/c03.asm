.data
newline: .asciiz "\n"

var_p: .word 0
var_q: .word 0
var_result: .word 0
.text
.globl main
main:
move $fp, $sp
# Variáveis globais
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_p
li $t0, 0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_q
lw $t0, var_p
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_q
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
and $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
lw $t0, var_p
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_q
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
or $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
lw $t0, var_p
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
seq $t0, $t0, $zero
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
lw $t0, var_p
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_q
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
and $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_p
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
seq $t0, $t0, $zero
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
or $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
j end_main_0

end_main_0:
li $v0, 10
syscall
