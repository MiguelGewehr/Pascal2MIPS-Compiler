.data
newline: .asciiz "\n"
str_0: .asciiz "Double of 7: "

var_result: .word 0
.text
.globl main
main:
move $fp, $sp
# Variáveis globais
li $t0, 7
subu $sp, $sp, 4
sw $t0, 0($sp)
jal func_double
addu $sp, $sp, 4
subu $sp, $sp, 4
sw $v0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
la $t0, str_0
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


func_double:
subu $sp, $sp, 8
sw $ra, 4($sp)
sw $fp, 0($sp)
move $fp, $sp
lw $t0, 8($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 2
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
mul $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $v0, 0($sp)
addu $sp, $sp, 4
func_double_end:
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8
jr $ra
end_main_0:
li $v0, 10
syscall
