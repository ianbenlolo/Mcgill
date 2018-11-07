#include <stdio.h>
#include <stdlib.h>
#include <string.h>

const int NUM_STUDENTS=3;

struct STUDENT{
  char name[100];
  float gpa;

}students[NUM_STUDENTS];

int main(){
  const char* names[NUM_STUDENTS]={"BOB","Alice","Mary"};
  float gpas[3]={3.01,9.50, 3.04};
  int i;
  for(i=0;i < NUM_STUDENTS,i++){
    strcpy(students[i].name,names[i]);
    students[i].gpa=gpas[i];
  }
  FILE* fp=fopen("students.struct","wb");
  if(fp==NULL) return 1;


  struct STUDENT second_student;
  fseek(fp,sizeof(STUDENT *)*1, SEEK_SET); //*1 so that we get the first student
  fread(second_student, sizeof(struct STUDENT),1,fp);

  fclose(fp);

  printf("the second student was %s and his gpa was of %f\n", second_student.name,second_student.gpa);
  return 0;
}
