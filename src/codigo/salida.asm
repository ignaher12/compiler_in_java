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
	_c_main DD ?
	_a_main DD ?
	_testint_main_test DD ?
	_b_main_test DD ?
	@aux1 DD ?
	__new_line__ DB 13, 10, 0
.CODE
test:
	MOV EAX, _testint_main_test
RET
START:
	MOV EAX, _a_main
	MOV _b_main_test, EAX
	CALL test
	MOV @aux1, EAX 
	MOV EAX, @aux1
	MOV _a_main, EAX
	INVOKE ExitProcess, 0
END START