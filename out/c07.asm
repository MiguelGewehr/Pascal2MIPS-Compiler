.data
newline: .asciiz "\n"

var_x: .word 0
var_y: .word 0
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
sw $t0, var_x
li $t0, 20
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_y
lw $t0, var_y
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_x
subu $sp, $sp, 4
sw $t0, 0($sp)
jal proc_swap
addu $sp, $sp, 8
lw $t0, var_x
subu $sp, $sp, 4
sw $t0, 0($sp)
jal proc_increment
addu $sp, $sp, 4
j end_main_0


proc_swap:
subu $sp, $sp, 8
sw $ra, 4($sp)
sw $fp, 0($sp)
move $fp, $sp
subu $sp, $sp, 4
lw $t0, 8($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
lw $t0, 12($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, 8($fp)
li $t0, 0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, 12($fp)
proc_swap_end:
addu $sp, $sp, 4
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8
jr $ra

proc_increment:
subu $sp, $sp, 8
sw $ra, 4($sp)
sw $fp, 0($sp)
move $fp, $sp
lw $t0, 8($fp)
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
sw $t0, 8($fp)
proc_increment_end:
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8
jr $ra
end_main_0:
li $v0, 10
syscall
