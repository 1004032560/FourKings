#include <stdio.h>
int main()
{
	int n;
	char c;
	scanf("%d %c",&n,&c);
	int sum=1;
	int i=1;
	while(sum<=n)
	{
		sum+=2*(2*(i+1)-1);
		if(sum<=n)
		i++;
	}
	int j,k;
	for(j=0;j<i;j++)
	 {
		for(k=0;k<j;k++)
		printf(" ");
		for(k=0;k<2*(i-j)-1;k++)
		{
			printf("%c",c);
		}
		 printf("\n");
	 }
	for(j=2;j<=i;j++)
	{
		for(k=0;k<i-j;k++)
		printf(" ");
		for(k=0;k<2*j-1;k++)
		{
			printf("%c",c);
		}
		printf("\n");
	}
	printf("%d",n-(sum-2*(2*(i+1)-1)));
}
