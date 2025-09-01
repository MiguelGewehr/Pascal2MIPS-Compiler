.data
newline: .asciiz "\n"
str_0: .asciiz "Array original: "
str_1: .asciiz "Array ordenado: "

var_arr: .word 0, 0, 0, 0, 0
var_i: .word 0
var_j: .word 0
var_temp: .word 0
.text
.globl main
main:
move $fp, $sp
# Variáveis globais
li $t0, 64
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
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 34
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
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 25
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
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 12
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
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
li $t0, 22
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
li $t0, 5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
la $t0, str_0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
jal proc_print_array
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_i
loop_2:
lw $t0, var_i
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 4
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
beq $t0, $zero, endloop_3
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_j
loop_4:
lw $t0, var_j
subu $sp, $sp, 4
sw $t0, 0($sp)
li $t0, 5
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_i
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
lw $t0, 0($sp)
addu $sp, $sp, 4
sub $t0, $t0, $t1
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
beq $t0, $zero, endloop_5
lw $t0, var_j
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_arr
add $t0, $t0, $t1
lw $t0, 0($t0)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, var_j
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
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_arr
add $t0, $t0, $t1
lw $t0, 0($t0)
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
beq $t0, $zero, else_6
lw $t0, var_j
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_arr
add $t0, $t0, $t1
lw $t0, 0($t0)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_temp
lw $t0, var_j
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
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_arr
add $t0, $t0, $t1
lw $t0, 0($t0)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
lw $t0, var_j
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
lw $t0, var_temp
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
lw $t0, var_j
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
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t2, var_arr
add $t2, $t2, $t1
sw $t0, 0($t2)
j endif_7
else_6:
endif_7:
lw $t0, var_j
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
sw $t0, var_j
j loop_4
endloop_5:
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
j loop_2
endloop_3:
la $t0, str_1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 4
syscall
jal proc_print_array
j end_main_8


proc_print_array:
subu $sp, $sp, 8
sw $ra, 4($sp)
sw $fp, 0($sp)
move $fp, $sp
subu $sp, $sp, 4
li $t0, 1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
loop_0:
li $t0, 0
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
li $t0, 0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t1, 0($sp)
addu $sp, $sp, 4
addi $t1, $t1, -1
sll $t1, $t1, 2
la $t0, var_arr
add $t0, $t0, $t1
lw $t0, 0($t0)
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
li $t0, 32
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $a0, 0($sp)
addu $sp, $sp, 4
li $v0, 1
syscall
li $t0, 0
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
j loop_0
endloop_1:
la $a0, newline
li $v0, 4
syscall
proc_print_array_end:
addu $sp, $sp, 4
lw $fp, 0($sp)
lw $ra, 4($sp)
addu $sp, $sp, 8
jr $ra
end_main_8:
li $v0, 10
syscall
