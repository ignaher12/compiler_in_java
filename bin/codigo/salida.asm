.386
.MODEL flat, stdcall
.STACK 200h
option casemap :none
include \masm32\include\windows.inc
include \masm32\include\kernel32.inc
include \masm32\include\masm32.inc
include \masm32\include\user32.inc
includelib \masm32\lib\kernel32.lib
includelib \masm32\lib\masm32.lib
includelib \masm32\lib\user32.lib
.DATA
	_c_main DD ?
	_a_main DD ?
	@aux3 DD ?
	_dasassa DB "dasassa", 0
	_b_main DD ?
	@aux1 DD ?
.CODE
START:
	MOV _a_main, 101
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
	MOV EAX, _a_main
	ADD EAX, 10
	MOV @aux3, EAX
	INVOKE MessageBox, NULL, addr @aux3, addr @aux3, MB_OK
	INVOKE MessageBox, NULL, addr _dasassa, addr _dasassa, MB_OK
	INVOKE ExitProcess, 0
END START