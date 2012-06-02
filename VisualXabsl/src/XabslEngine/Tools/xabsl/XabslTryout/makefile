XABSLFILES=$(shell echo `ls ../Xabsl/XabslEngine/ -c1 | grep .cpp` | sed "s%Xabsl%Build/Xabsl%g" | sed "s%\.cpp%.o%g")

all: Build/libeast.a Build/libcommon.a

Build/Xabsl%.o: ../Xabsl/XabslEngine/Xabsl%.cpp ../Xabsl/XabslEngine/Xabsl%.h 
	@echo $@
	@if !(test -d Build); then mkdir Build; fi
	@g++ -c $< -o $@
	
Build/WorldState.o: WorldState.cpp WorldState.h
	@echo $@
	@if !(test -d Build); then mkdir Build; fi
	@g++ -c WorldState.cpp -o $@
	
Build/MyBasicBehaviors.o: MyBasicBehaviors.cpp MyBasicBehaviors.h
	@echo $@
	@if !(test -d Build); then mkdir Build; fi
	@g++ -c MyBasicBehaviors.cpp -o $@

Build/east.o: main.cpp WorldState.h MyBasicBehaviors.h Tools.h 
	@echo $@
	@if !(test -d Build); then mkdir Build; fi
	@g++ -I../ascii-soccer -c -DEAST_TEAM main.cpp -o $@

Build/common.o: common.c 
	@echo $@
	@if !(test -d Build); then mkdir Build; fi
	@g++ -I../ascii-soccer -c -DWEST_TEAM common.c -o $@

Build/libeast.a: Build/east.o Build/MyBasicBehaviors.o Build/WorldState.o $(XABSLFILES)
	@echo $@
	@if !(test -d Build); then mkdir Build; fi
	@ar rc $@ Build/east.o Build/MyBasicBehaviors.o Build/WorldState.o $(XABSLFILES)
	@ranlib $@

Build/libcommon.a: Build/common.o Build/east.o
	@echo $@
	@if !(test -d Build); then mkdir Build; fi
	@ar rc $@ Build/common.o 
	@ranlib $@


clean: 
	@rm -rf Build
