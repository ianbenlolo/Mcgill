#include <stdlib.h>
#include <stdio.h>

#include "producer.h"


void producer(){
  FILE *fp, *datafile, *t;
  char c,TURN;

  while((t = fopen("TURN.txt", "r+")) == NULL); // busy loop
  TURN = fgetc(t);
  if (TURN == '0'){ //it is priducers turn
    while((fp=fopen("mydata.txt", "r"))==NULL);
    char c=fgetc(fp);
    while (c!=EOF)
    {
      datafile=fopen("data.txt", "a");
      //write into data.txt and close it
      if(datafile==NULL) {printf("Error opening/making data.txt file."); exit(-1);}
      fputc(c, datafile);
      fclose(datafile);
          // get next char
      c=fgetc(fp);
    }

    fclose(fp);
    fclose(t);
  }
  t=fopen("TURN.txt", "wt");
  TURN='1';
  fputc(TURN, t);
  fclose(t);
}
