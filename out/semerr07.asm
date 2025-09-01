.data
newline: .asciiz "\n"

var_arr: .word 0, 0, 0, 0, 0
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
li $t0, 0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 20
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 6
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
j end_main_0

end_main_0:
li $v0, 10
syscall
