# makefile
#
# author: Martin Loetzsch
#
# compiles the XABSL example agent team, comiles an opponent team, compiles the ascii-soccer environment,
# and linkes all together.
#
# The standard opponent team is "spaniels". You can change this by invoking the makefile with
#  make opponent-team=other_team_directory

opponent-team=rollers

compile-xabsl-files=ascii-soccer/intermediate-code.dat
compile-soccer=ascii-soccer/soccer.exe
copy-xabsl-team=ascii-soccer/libeastcommon.a ascii-soccer/libeast.a
compile-xabsl-team=XabslExampleAgentTeam/Build/libcommon.a XabslExampleAgentTeam/Build/libeast.a
copy-opponent-team=ascii-soccer/last-linked-opponent-team-was-$(opponent-team) ascii-soccer/libwestcommon.a ascii-soccer/libwest.a
compile-opponent-team=ascii-soccer/teams/$(opponent-team)/libcommon.a ascii-soccer/teams/$(opponent-team)/libwest.a


all: $(compile-xabsl-files) $(compile-soccer)

$(compile-xabsl-files): XabslExampleAgentTeam/xabsl-src/*.xabsl XabslExampleAgentTeam/xabsl-src/Options/*.xabsl
	@echo compiling XABSL source files and generating the intermediate code
	@make -s -C Xabsl/compiler
	@make -s -C XabslExampleAgentTeam/xabsl-src

$(compile-soccer): $(copy-xabsl-team) $(copy-opponent-team)
	@echo compiling and linking soccer
	@make -s -C ascii-soccer soccer

$(copy-xabsl-team): $(compile-xabsl-team)
	@echo copying files for Xabsl example agent team
	@cp XabslExampleAgentTeam/Build/libcommon.a ascii-soccer/libeastcommon.a
	@cp XabslExampleAgentTeam/Build/libeast.a ascii-soccer

$(compile-xabsl-team): XabslExampleAgentTeam/*.h XabslExampleAgentTeam/*.cpp
	@echo compiling the Xabsl example agent team
	@make -s -C XabslExampleAgentTeam

$(copy-opponent-team): $(compile-opponent-team)
	@echo copying files for "$(opponent-team)" team
	@rm -f ascii-soccer/last-linked*
	@cp ascii-soccer/teams/$(opponent-team)/libcommon.a ascii-soccer/libwestcommon.a
	@cp ascii-soccer/teams/$(opponent-team)/libwest.a ascii-soccer/libwest.a
	@echo foo > $(copy-opponent-team)

$(compile-opponent-team): ascii-soccer/teams/$(opponent-team)/*.c
	@echo compiling the "$(opponent-team)" team
	@make -s -C ascii-soccer/teams/$(opponent-team) libwest.a
	@make -s -C ascii-soccer/teams/$(opponent-team) libcommon.a

DOC:
	@make -s -C XabslExampleAgentTeam/xabsl-src DOC
clean:
	@make -s -C XabslExampleAgentTeam clean
	@make -s -C ascii-soccer clean
	@make -s -C XabslExampleAgentTeam/xabsl-src clean
	@make -s -C Xabsl/compiler clean
	@rm -f ascii-soccer/last-linked*
	@rm -f ascii-soccer/messages.log
	@rm -f ascii-soccer/soccer.exe
	@rm -f ascii-soccer/soccer
	@find ascii-soccer/teams/ | grep "\.o" | sed "s%\([.]*\)%rm -f \1%" | bash
	@find ascii-soccer/teams/ | grep "\.a" | sed "s%\([.]*\)%rm -f \1%" | bash
