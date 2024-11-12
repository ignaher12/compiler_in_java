include \masm32\include\masm32rt.inc

.data

hello_world db "hello", 0

.code

start:
    push offser hello_world
    call StdOut

end start