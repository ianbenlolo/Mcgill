/*
Ian Benlolo 260744397

Comp 206 Assignment 6
6/12/17

To run: make; ./ass6
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include "producer.h"
#include "consumer.h"
#include <sys/wait.h>



int main(){
  FILE* turn=fopen("TURN.txt", "w+");
  fputc('0', turn);
  fclose(turn);

 int pid = fork();

  if (pid==-1)
  {
    printf("Problem forking.");
    exit(-1);
  }
  if (pid==0){
    wait(NULL); //wait for the parent(producer) to finish and then
    consumer();
  }
  else {
    producer();
  }


  return 1;
}
