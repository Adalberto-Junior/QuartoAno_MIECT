/*
 * ponteiros.c
 * 
 * Copyright 2024 Adalb <Adalb@LAPTOP-3N68JDSJ>
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, USA.
 * 
 * 
 */


#include <stdio.h>

void testStatic () {
	static  int i = 0;
	i = i + 2;
	printf("i: %d\n",i);
}
int main(int argc, char **argv)
{

	testStatic();
	int x = 10;
	int *p = &x;
	int *q = p;

	printf("x = %d\n", x);
	printf("&x = %p\n", &x);
	printf("p = %p\n", p);
	printf("&p = %p\n", &p);
	printf("q = %p\n", q);
	printf("&q = %p\n", &q);
	printf("*q = %d\n", *q);
	int vet[5] = {1,2,3,4,6};
	int *pp = vet;
	int **cp = &pp;
	printf("Ponteiro de ponteiro: %p\n",cp);
	for(int i = 0; i< 4; i++)
		printf("pp = %p\n", (&pp[i]));

	printf("Ponteiro de ponteiro1: %d\n",**(cp+4));
	testStatic();
	return 0;
}

