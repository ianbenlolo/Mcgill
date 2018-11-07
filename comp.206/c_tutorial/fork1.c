#include <stdlib.h>
#include <stdio.h>
#include <unistd.h>

int main(){

  printf("starting process:\n");
  int pid=fork();

  if (pid== -1) {printf("error");return 1;}
  else if(pid==0) printf("in the child process(pid=%d)\n", getpid());
  else {
    printf("in parent process(pid=%d)\n", getpid());
    wait(NULL);
  }

  printf("exiting process(pid=%d)\n", getpid());

  return 1;
}
