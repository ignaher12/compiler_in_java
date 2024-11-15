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
	@aux4 DD ?
	_c_main DD ?
	_a_main DD ?
	@aux3 DD ?
	@aux2 DD ?
	_b_main DD ?
	@aux1 DD ?
	__new_line__ DB 13, 10, 0
	@imprimirFloat DQ ?
.CODE
START:
	MOV _a_main, -10
	MOV _b_main, 10
	INVOKE printf, cfm$("%d\n"), _a_main
	INVOKE printf, cfm$("%d\n"), _b_main
	MOV EAX, _b_main
	ADD EAX, _a_main
	MOV @aux1, EAX
	MOV EAX, @aux1
	MOV _b_main, EAX
	INVOKE printf, cfm$("%d\n"), _b_main
	MOV EAX, _a_main
	ADD EAX, _a_main
	MOV @aux2, EAX
	MOV EAX, @aux2
	MOV _b_main, EAX
	INVOKE printf, cfm$("%d\n"), _b_main
	MOV EAX, _b_main
	MOV EDX, 0
	MOV ECX, _a_main
	MUL ECX
	MOV @aux3, EAX
	MOV EAX, @aux3
	MOV _c_main, EAX
	INVOKE printf, cfm$("%d\n"), _c_main
	MOV EAX, _c_main
	MOV EDX, 0
	MOV ECX, 20
	DIV ECX
	MOV @aux4, EAX
	MOV EAX, @aux4
	MOV _c_main, EAX
	INVOKE printf, cfm$("%d\n"), _c_main
	MOV _c_main, 200
	INVOKE ExitProcess, 0
END START