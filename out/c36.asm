.data
newline: .asciiz "\n"
str_0: .asciiz "Entrando na função com n = "
str_1: .asciiz "Recursão completa!"

.text
.globl main
main:
move $fp, $sp
li $t0, 3
subu $sp, $sp, 4
sw $t0, 0($sp)
jal proc_simples
addu $sp, $sp, 4
la $t0, str_1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
la $a0, newline
li $v0, 4
syscall
j end_main_2


func_simples:
subu $sp, $sp, 8
sw $ra, 4($sp)
sw $fp, 0($sp)
move $fp, $sp
la $t0, str_0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
lw $t0, 8($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
la $a0, newline
li $v0, 4
syscall
lw $t0, 8($fp)
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 0
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
beq $t0, $zero, else_0
li $t0, 0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $v0, 0($sp)
addu $sp, $sp, 4
j endif_1
else_0:
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
jal func_simples
addu $sp, $sp, 4
subu $sp, $sp, 4
sw $v0, 0($sp)
lw $v0, 0($sp)
addu $sp, $sp, 4
endif_1:
func_simples_end:
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8
jr $ra
end_main_2:
li $v0, 10
syscall
