.386
.MODEL small
.STACK 200h

.DATA
	_c_main DD ?
	_a_main DD ?
	_b_main DD ?
	@aux1 DD ?
.CODE
START:
	MOV EAX, 101       ; Carga el valor inmediato 101 en el registro EAX
    MOV _a_main, EAX
	MOV EAX, _a_main
	ADD EAX, 11
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV _b_main, EAX
	MOV EAX, _a_main
	MOV EBX, 101
	CMP EAX, EBX
	PUSHF
	POPF
	JE etiqueta7
	MOV _a_main, 10
	JMP etiqueta9
etiqueta7:
	MOV _a_main, 11
etiqueta9:
END START