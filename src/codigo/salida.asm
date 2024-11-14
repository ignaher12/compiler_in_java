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
	@aux4 DQ ?
	_b_main DD ?
	_12f021 DQ 12.021
	_x_main DD ?
	_a_main DQ ?
	@aux3 DQ ?
	@aux2 DD ?
	@aux1 DD ?
	_c_main DQ ?
	_101f24 DQ 101.24
	_b_main_test DD ?
	_dasassa DB "dasassa", 0
	_enterito_main DD ?
	_10f4 DQ 10.4
	_11f1 DQ 11.1
	_10f1 DQ 10.1
	_10f0 DQ 10.0
	_testint_main_test DD ?
	_f_main DQ ?
	__new_line__ DB 13, 10, 0
.CODE
test:
	MOV _testint_main_test, 14
START:
	FLD _101f24
	FSTP _a_main
	MOV _b_main, 101
	MOV EAX, _b_main
	MOV EBX, 101
	CMP EAX, EBX
	PUSHF
	FLD _a_main
	FLD _c_main
	FCOMP
	PUSHF
	POPF
	JNE etiqueta8
	POPF
	JNE etiqueta8
	FLD _10f1
	FSTP _a_main
	JMP etiqueta10
etiqueta8:
	FLD _11f1
	FSTP _a_main
etiqueta10:
	FLD _a_main
	FLD _10f4
	FADD
	FSTP @aux3
	INVOKE printf, cfm$("%.5Lf\n"), @aux3
	INVOKE printf, ADDR _dasassa
	INVOKE printf, ADDR __new_line__
	FLD _12f021
	FLD _10f0
	FADD
	FSTP @aux4
	FLD @aux4
	FSTP _f_main
	INVOKE printf, cfm$("%.5Lf\n"), _f_main
hola:
	MOV _x_main, 1
	INVOKE ExitProcess, 0
END START