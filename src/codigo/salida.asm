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
	_prueba_main REAL4 ?
	_1f10s20 REAL4 1.10s20
	__new_line__ DB 13, 10, 0
	@imprimirFloat DQ ?
.CODE
START:
	FLD _1f10s20
	FSTP _prueba_main
	INVOKE ExitProcess, 0
END START