ass6 : producer.o consumer.o main.o
	gcc -o ass6 producer.o consumer.o main.o
	make clean
producer.o : producer.c producer.h
	gcc -c producer.c
consumer.o : consumer.c consumer.h
	gcc -c consumer.c
main.o : main.c producer.h consumer.h
	gcc -c main.c
clean:
	rm -rf *.o data.txt TURN.txt
