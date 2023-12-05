.model small
.386
.stack 64
.data
temp1 dword ?
temp2 dword ?
numero dword ?
.code
mov eax, 2
add eax, 44
mov TEMP1, eax
mov eax, TEMP1
mov numero, eax
mov ax,4c00h
int 21h
.exit
end
