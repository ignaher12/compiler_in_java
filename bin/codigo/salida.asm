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
<<<<<<< Updated upstream
	@aux4 DQ ?
	_d_main DD ?
	_hola_main DD ?
	_b_main DD ?
	_12f021 DQ 12.021
	_a_main DD ?
	@aux3 DD ?
	@aux2 DD ?
	@aux1 DD ?
	_c_main DD ?
	_101f24 DQ 101.24
	_b_main_test DD ?
	_dasassa DB "dasassa", 0
	_enterito_main DD ?
	_10f0 DQ 10.0
	_testint_main_test DD ?
	_f_main DQ ?
	__new_line__ DB 13, 10, 0
.CODE
START:
	MOV _a_main, 101.24
	MOV _b_main, 101
	MOV EAX, _b_main
	MOV EBX, 101
	CMP EAX, EBX
	PUSHF
	MOV EAX, _a_main
	MOV EBX, _c_main
	CMP EAX, EBX
	PUSHF
	POPF
	JNE etiqueta12
	POPF
	JNE etiqueta12
	POPF
	JNE etiqueta12
	POPF
	JNE etiqueta12
	MOV _a_main, 10
	JMP etiqueta14
etiqueta12:
	MOV _a_main, 11
etiqueta14:
	MOV EAX, _a_main
	ADD EAX, 10
	MOV @aux3, EAX
	INVOKE printf, cfm$("%d\n"), @aux3
	INVOKE printf, ADDR _dasassa
	INVOKE printf, ADDR __new_line__
	FLD _12f021
	FLD _10f0
	FADD
	FSTP @aux4
	FLD @aux4
	FSTP _f_main
	INVOKE printf, cfm$("%.5Lf\n"), _f_main
=======
	_c_main DD ?
	_x_main_holahola DD ?
	_b_main DD ?
	_a_main DD ?
	__new_line__ DB 13, 10, 0
.CODE
holahola:
	MOV _x_main_holahola, 10
	MOV _c_main, 41
START:
	MOV _a_main, 101
	MOV _c_main, 1
	MOV EAX, _holahola
	MOV _b_main, EAX
>>>>>>> Stashed changes
	INVOKE ExitProcess, 0
END START