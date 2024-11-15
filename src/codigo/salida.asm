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
	_gordito2_main DD ?
	_c_main DD ?
	_gordito_main DD ?
	_b_main DQ ?
	_a_main DQ ?
	__new_line__ DB 13, 10, 0
.CODE
START:
	FLD _b_main
	FSTP _a_main
	FLD _b_main
	FSTP _a_main
	INVOKE ExitProcess, 0
END START