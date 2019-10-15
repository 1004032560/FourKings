#include<stdio.h>
#include<stdlib.h>
#include<malloc.h>

#define MAXSIZE 100
typedef struct
{
    int data[MAXSIZE];
    int top;
}SeqStack,*PSeqStack;
PSeqStack Init_SeqStack(void)
{
    PSeqStack S;
    S=(PSeqStack)malloc(sizeof(SeqStack));
    if(S)
        S->top=-1;
    return S;

}
int Empty_SeqStack(PSeqStack S)
{
    if(S->top==-1)
        return 1;
    else
        return 0;
}
int Push_SeqStack(PSeqStack S,int x)
{
    if(S->top==MAXSIZE-1)
        return 0;
    else
    {
        S->top++;
        S->data[S->top]=x;
        return 1;
    }
}
int Pop_SeqStack(PSeqStack S,int *x)
{
    if(Empty_SeqStack(S))
        return 0;
    else
    {
        *x=S->data[S->top];
        S->top--;
        return 1;
    }
}
void Destory_SeqStack(PSeqStack *S)
{
    if(*S)
        free(*S);
    *S=NULL;
    return;
}
int conversion(int n,int r)
{
    PSeqStack S;
    S=Init_SeqStack();
    int x;
    while(n)
    {
        Push_SeqStack(S,n%r);
        n=n/r;
    }
    while(!Empty_SeqStack(S))
    {
        Pop_SeqStack(S,&x);
        switch(x)
        {
            case 10:printf("A");
               break;
            case 11:printf("B");
               break;
            case 12:printf("C");
               break;
            case 13:printf("D");
               break;
            case 14:printf("F");
               break;
            case 15:printf("F");
               break;
            default:printf("%d",x);
        }
    }
    printf("\n");
    return 0;
}
int main()
{
    PSeqStack S;
    S=Init_SeqStack();
    int n,r;
    printf("请输入要转换的十进制数：");
    scanf("%d",&n);
    printf("请输入要转换的进制数(2,8,16)：");
    scanf("%d",&r);
    printf("将十进制数%d转换为%d进制是：\n",n,r);
    conversion(n,r);

}
