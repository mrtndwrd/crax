#!/bin/bash

if test -d ascii-soccer/teams/$1 && test $1; then 
  if (make -s opponent-team=$1); then
    echo Starting the game
    cd ascii-soccer
    ./soccer -p 7
  fi;
else
  echo usage: start-game.bash [teamname]
  echo teams: `ls ascii-soccer/teams`
  echo
fi
