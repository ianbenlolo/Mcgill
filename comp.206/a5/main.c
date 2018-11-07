/*
Ian Benlolo 260744397
comp 206 Assignment 5
*/

#include "list.h"

int main(){
  int i = 1;
  int data;

	newList(); //create new list
  printf("To end the program, enter a negative integer.\n \n");


	while(i){
		printf("Enter a positive integer: ");
		scanf("%d", &data);
		//get rid of any characters entered in value (after number)
		while(getchar() !='\n') {
    		continue;
    	}

		if (data >= 0){ //if its valid, add it to list
			addNode(data);
		}
		else { //stop the program
			i = 0;
		}
	}
	prettyPrint();
	freeMem();//to free all the alocated memory before quiting

	return(0);
}
