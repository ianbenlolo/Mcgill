
#include "list.h"

struct NODE{//declaring the node globally
  struct NODE* next;
  int data;
};

struct NODE* head;


//method to make a new lsit
void newList(){
    head = NULL;
}

void addNode(int value){    //adds nodes to list
    //initialize the new node with value
    struct NODE* node = (struct NODE*) malloc(sizeof(struct NODE));

    if (node == NULL){
        printf("Error: problem allocating memory for a node.\n");
        exit(-1);
    }
    node->data = value;

    if (head == NULL){    //if the linked list is empty this node will be the first
        head = node;
        node->next = NULL;
    }
    else{
      //we insert the node at front of list
      node->next = head;
      head = node;
    }
}
// prints list
void prettyPrint(){
    if (head == NULL){
        printf("Error: Empty list; no nodes to print.\n");
        exit(-1);
    }

    struct NODE* current = head;
    while(current->next != NULL){
        printf("%d, ", current->data);
        current = current->next;
    }
    printf("%d\n", current->data);
}

//frees memory in list
void freeMem(){
    // if the list isn't empty
    if (head != NULL){
        struct NODE* node = head;
        struct NODE* temp = NULL;
        while(node->next != NULL){
            temp = node;
            node = node->next;
            free(temp);
        }
    }
}
