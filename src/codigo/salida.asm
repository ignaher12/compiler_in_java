.386
.MODEL flat, stdcall
.STACK 200h
option casemap :none
include \masm32\include\masm32rt.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
includelib \masm32\lib\user32.lib
dll_dllcrt0 PROTO C
printf PROTO C : VARARG
.DATA
	@aux5 DD ?
	@aux4 DD ?
	_c_main REAL4 ?
	_d_main DD ?
	_g_main DD ?
	_x_main_funcionTest DD ?
	_b_main DD ?
	_a_main_funcionTest DD ?
	_a_main DD ?
	_1f23sM23 REAL4 1.23e+23
	@aux3 DD ?
	@aux2 DD ?
	@aux1 DD ?
	_f_main DD ?
	__new_line__ DB 13, 10, 0
	@imprimirFloat DQ ?
	mensajeErrorDivCero db "Error: Division por cero", 10, 0
	mensajeErrorOverflow db "Error: Overflow en suma entre puntos flotantes", 10, 0
	maxFloat REAL4 3.402e38
	minFloat REAL4 -3.402e38
	mensajeErrorFueraDeRango db "Error: Valor fuera de rango del subtipo", 10, 0
.CODE
funcionTest:
	MOV EAX, _a_main_funcionTest
	MOV _x_main_funcionTest, EAX
	MOV EAX, __x_main_funcionTest
RET
START:
	FINIT
	MOV _a_main, 10
	MOV _b_main, 0001h
	FLD _1f23sM23
	FSTP _c_main
	MOV EAX, _d_main
	CDQ
	MOV ECX, 12
	IMUL ECX
	MOV @aux1, EAX
	MOV EAX, _a_main
	ADD EAX, @aux1
	MOV @aux2, EAX
	MOV EAX, @aux2
	MOV _a_main, EAX
	MOV EAX, _a_main
	MOV EBX, 13
	CMP EAX, EBX
	PUSHF
	POPF
	JNG etiqueta11
	MOV EAX, _b_main
	ADD EAX, 120Ah
	MOV @aux4, EAX
	MOV EAX, @aux4
	MOV _b_main, EAX
	JMP etiqueta12
etiqueta11:
etiqueta12:
	MOV EAX, _g_main
	MOV _a_main_funcionTest, EAX
	CALL funcionTest
	MOV @aux5, EAX 
	MOV EAX, @aux5
	MOV _f_main, EAX
FIN:
	INVOKE ExitProcess, 0
errorDivCero:
	INVOKE printf, ADDR mensajeErrorDivCero
	JMP FIN
errorOverflow:
	INVOKE printf, ADDR mensajeErrorOverflow
	JMP FIN
errorFueraDeRango:
	INVOKE printf, ADDR mensajeErrorFueraDeRango
	JMP FIN
END START