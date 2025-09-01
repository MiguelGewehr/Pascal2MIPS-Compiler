.data
newline: .asciiz "\n"

var_result: .word 0
.text
.globl main
main:
move $fp, $sp
# Variáveis globais
li $t0, 5
subu $sp, $sp, 4
sw $t0, 0($sp)
jal func_factorial
addu $sp, $sp, 4
subu $sp, $sp, 4
sw $v0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
li $t0, 20
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 10
subu $sp, $sp, 4
sw $t0, 0($sp)
jal func_max
addu $sp, $sp, 8
subu $sp, $sp, 4
sw $v0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_result
j end_main_4


func_factorial:
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
sle $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
beq $t0, $zero, else_0
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $v0, 0($sp)
addu $sp, $sp, 4
j endif_1
else_0:
lw $t0, 8($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
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
sub $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
jal func_factorial
addu $sp, $sp, 4
subu $sp, $sp, 4
sw $v0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
mul $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $v0, 0($sp)
addu $sp, $sp, 4
endif_1:
func_factorial_end:
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8
jr $ra

func_max:
subu $sp, $sp, 8
sw $ra, 4($sp)
sw $fp, 0($sp)
move $fp, $sp
lw $t0, 8($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 12($fp)
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
beq $t0, $zero, else_2
lw $t0, 8($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $v0, 0($sp)
addu $sp, $sp, 4
j endif_3
else_2:
lw $t0, 12($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $v0, 0($sp)
addu $sp, $sp, 4
endif_3:
func_max_end:
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8
jr $ra
end_main_4:
li $v0, 10
syscall
