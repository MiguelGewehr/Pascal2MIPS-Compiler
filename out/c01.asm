.data
newline: .asciiz "\n"

var_a: .word 0
var_b: .word 0
var_c: .word 0
var_x: .float 0.0
var_y: .float 0.0
float_const_0: .float 10.5
float_const_1: .float 3.2
.text
.globl main
main:
# Variáveis globais
li $t0, 10
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_a
li $t0, 3
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
add $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
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
sub $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
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
mul $t0, $t0, $t1
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
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
div $t0, $t1
mflo $t0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
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
div $t0, $t1
mfhi $t0
subu $sp, $sp, 4
sw $t0, 0($sp)
lw $t0, 0($sp)
addu $sp, $sp, 4
sw $t0, var_c
lwc1 $f0, float_const_0
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_x
lwc1 $f0, float_const_1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_y
lwc1 $f0, var_x
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_y
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f1, 0($sp)
addu $sp, $sp, 4
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
add.s $f0, $f0, $f1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_x
lwc1 $f0, var_x
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_y
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f1, 0($sp)
addu $sp, $sp, 4
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
sub.s $f0, $f0, $f1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_x
lwc1 $f0, var_x
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_y
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f1, 0($sp)
addu $sp, $sp, 4
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
mul.s $f0, $f0, $f1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_x
lwc1 $f0, var_x
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, var_y
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f1, 0($sp)
addu $sp, $sp, 4
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
div.s $f0, $f0, $f1
subu $sp, $sp, 4
swc1 $f0, 0($sp)
lwc1 $f0, 0($sp)
addu $sp, $sp, 4
swc1 $f0, var_x
li $v0, 10
syscall
