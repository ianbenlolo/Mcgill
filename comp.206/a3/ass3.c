#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <ctype.h>
int encrypt (char sentence[], int key);
int decrypt(char sentence[], int key);
int main()
{

	char sentence[10000];//leaving a lot of room for a long sentence
	printf("\n Sentence: ");
	scanf("%[^\n]s", sentence);//scanf until the escape character
	
	
	int i,key;
	for(i=0; i<strlen(sentence); i++){ //this block ensures that was entered as the sentence will not crash the code
		if(!(isspace(sentence[i])||isalpha(sentence[i]))){ //if we dont only have spaces or characters from the alphabet
			printf("\n Please enter valid input to encrypt!\n"); //print out a helpful message! 
			printf("This would be a sentence with no numbers or any special charaters.\n Thank you.\n");
			exit (1);
		}
	}
	printf("\n Key: ");
	scanf("%d", &key);
	key=key%26; //just in case they entered a value less than 0 or more than 26!!
	//the following calls onto my encryp and decrypt methods and prints everything as required!
	printf("\n Original message: %s \n", sentence);
	encrypt(sentence, key);
	printf("\n Encrypted message: %s \n",sentence );

	decrypt(sentence, key);
	printf("\n Decrypted message: %s  \n\n", sentence);
}


int encrypt (char sentence[], int key){
	int i=0;//counter for the loop
	key=26-key; //so that we could just add the key and not substract (easier that way) 
	for(i=0; i<strlen(sentence); i++){//iterating through every element in the sentence[]
		if(sentence[i]!=32){//if its not a space 
			if(sentence[i]>='A' && sentence[i]<='Z'){ //the letter is a capital
				sentence[i]=sentence[i]-'A'; //setting the letter better 0 and 26
				sentence[i]=(sentence[i]+key)%26+'A';//adding they key mod 26 because theres 26 letters in alphabet
			}else if(sentence[i]>='a' && sentence[i]<='z'){ //its between a and z 
				sentence[i]=sentence[i]-'a';
				sentence[i]=(sentence[i]+key)%26+'a';

			}
		}
	}
return 0; //end succesfully 
}
int decrypt (char sentence[], int key){
	//now we'd only need to add the original key to the encrypted message and we'd get the original message back
	int i=0; 
	for(int i=0; i<strlen(sentence); i++){//do the same as before
		if (sentence[i]==32) continue;
		else if(sentence[i]>='A'&&sentence[i]<='Z') sentence[i]=(sentence[i]-'A'+key)%26+'A';
		else if(sentence[i]>='a'&&sentence[i]<='z') sentence[i]=(sentence[i]-'a'+key)%26+'a';
	}
	return 0;
}

