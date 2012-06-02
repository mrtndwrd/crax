all:
	javac -Xlint:unchecked -cp 'JXabsl/bin;src' src/*.java
	mkdir -p bin
	mv src/*.class bin
	echo -e "#!/bin/sh\ncd bin && java -cp '../JXabsl/bin/;.' EngineRunner" > runXabsl
	chmod +x runXabsl

clean:
	rm -f runXabsl bin/*

