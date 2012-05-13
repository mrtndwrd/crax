soccer.o: soccer.c soccer.h players.h
	g++ -w -c soccer.c

soccer: soccer.o libeast.a libwest.a libeastcommon.a libwestcommon.a
	g++ -w soccer.o -o soccer -L./ -lncurses  -lm \
		-least -leastcommon -lwest -lwestcommon

soccerboth: soccer.o libeast.a libwest.a libcommon.a
	g++ -w soccer.o -o soccer -L./ -lncurses  -lm \
		-least -lcommon -lwest 

clean: 
	rm -f *.o
	rm -f *.a
	rm -f soccer
	rm -f core

reallyclean: clean
	cleanteams
