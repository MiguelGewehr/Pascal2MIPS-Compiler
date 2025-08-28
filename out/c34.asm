.data
newline: .asciiz "\n"
str_0: .asciiz "The answer is: "

var_result: .word 0
.text
.globl main
main:
move $fp, $sp
# Variáveis globais
jal func_getNumber
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


func_getNumber:
subu $sp, $sp, 8
sw $ra, 4($sp)
sw $fp, 0($sp)
move $fp, $sp
li $t0, 42
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $v0, 0($sp)
addu $sp, $sp, 4
func_getNumber_end:
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8
jr $ra
end_main_0:
li $v0, 10
syscall
