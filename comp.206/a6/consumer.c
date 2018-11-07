#include <stdio.h>
#include <stdlib.h>
#include "consumer.h"

void consumer(){
  FILE *fp, *datafile, *t;
  char c,TURN;
  int enable=1;
  while (enable){
    while((t = fopen("TURN.txt", "r+")) == NULL); // busy loop
    TURN = fgetc(t);
    if(TURN == '1'){ //it is consumer's turn
      while((fp=fopen("data.txt", "r"))==NULL);
      char c=fgetc(fp);
      while (c!=EOF) {
      printf("%c", c);
      c=fgetc(fp);

    }
    fclose(fp);
    fclose(t);
    enable = 0;
    }
  }
 // Change turns to the producer
  t=fopen("TURN.txt", "wt");
  TURN='0';
  fputc(TURN, t);
  fclose(t);
}
