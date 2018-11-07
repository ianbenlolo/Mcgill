#include <stdio.h>
#include<stdlib.h>
//Ian Benlolo 260744397
//Comp 206 ass4

int stlen(char *str) {//helper method to get length of string with pointer
	int i;
	for(i=0; /* *str != ',' || *str!=32 || */ *str!='\0'; i++, str++);
	return i;
}
int isSame(char *first, char *second){//helper method to compare two strings using pointers
	while(*first == *second){ //returns 0 is theyre the same
		if ( *first == '\0' || *second == '\0' ) break;//and -1 if theyre not;
			first++;
			second++;
	}
		if(*first == '\0'  && *second == '\0')return 0;
		else return -1; //if theyre both a space or a comma then we have the same name
}

void FindRecord(char *filename, char *name, char record[]){
	FILE *fp=fopen(filename, "r+" );
	char array[1000];
	char temp[20];
//checks line by line if we find the corret record
	while(!feof(fp)){
		fgets(array, 1000, fp);
		int j;
		int count = stlen(name);
		for(j=0; j < count ; j++){
			temp[j]=array[j];
		}
		if(isSame(name,temp)==0){
			int k;
			for(k=0;k<1000;k++) record[k]=array[k];
        }
    }
	fclose(fp);
}


void Replace(char *name, char *newname, char record[]){//takes the record we are looking for and replaces the name of the record to the new name
      char temp[1000];
      char* recordP=record;
      char* tempP=temp;
  // lengths of the names
      int Newlen=stlen(newname);
      int Origlen=stlen(name);
      switch (*(recordP+Origlen+1)){ //keeps everything but the old name into record P
          case 32: recordP=recordP+Origlen+2;break;
          case ',': recordP=recordP+Origlen+1; break;
      }
      for(int count=0;count<Newlen;count++){
          *tempP=*newname++;
          tempP++;
      }

      *tempP=',';
      tempP++;
      *tempP=' ';tempP++; //adding a space and comma

      int g=stlen(recordP);
      for(int i=0 ; i <= g ; i++){ //copying the name only
          *tempP = *recordP;
          recordP++;
          tempP++;
      }
      tempP = temp; //making the pointer point back to begining
      recordP=record;
			
      for(int t=0; t<=stlen(tempP);t++){
          *recordP= *tempP;
          tempP++;
          recordP++;
      }
  }

void SaveRecord(char *filename, char *name, char record[]){

FILE * fp=fopen(filename, "r+");
FILE * fp2=fopen("tmp.csv", "w+");

char array[1000];
char test[1000];
char comptemp[1000];
char after[1000];
FindRecord(filename, name,test);
while(!feof(fp)){
    fgets(comptemp,100,fp); //Stores the line in the CSV in comptemp array
    int result=isSame(test,comptemp);
    if(feof(fp)) break;
    //print to file
    if(result == -1) { //if theyre NOT the same we want to store the original line
        fprintf(fp2,"%s",comptemp);
        if(feof(fp)) break; //checking just in case

    }
    else{ //if they are the same we want to store the record[]
        fprintf(fp2,"%s",record);
        if(feof(fp)) break;
    }
}
fclose(fp);
fclose(fp2);
rename("tmp.csv", filename); //rename it to the original name which also overwrites

}


int main(){
	char name[1000];
	char replacement[1000];
	char record[1000];

	printf("What is the name you are looking for?  ");
	scanf("%s", name);
	printf("What is the replacement name?  ");
	scanf("%s", replacement);

//calling the methods in order
	FindRecord("phonebook.csv" , name, record);
	Replace(name,replacement,record);
	SaveRecord("phonebook.csv", name, record);
	//printf("end %s",record);

	return 0;

}
