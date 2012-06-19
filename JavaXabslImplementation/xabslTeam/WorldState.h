/*===========================================================================

WorldState.h
  
The class WorldState represents the knowledge of all agents.
    
author: Martin Lötzsch
===========================================================================*/

#ifndef __WorldState_h_
#define __WorldState_h_

#include "../Xabsl/XabslEngine/XabslSymbols.h"
#include "../ascii-soccer/soccer.h"


class WorldState
{
public:
  WorldState();

  // the player indexing works like a ring buffer. 
  // E.g. x[0] is for the acticve player, x[1] for the previously actice player etc.

  /*
  	local_area[]	Reveals what's nearby.  Indexed by
			N,NE,E,SE,S, etc. so that, for example,
			local_area[S] tells you what is in the
			cell to the south of the robot.  Possible 
			values include: BOUNDARY, OPPONENT, TEAMMATE,
			BALL and EMPTY. */
  int local_area[9][4];

  /*	ball_direction	Compass heading to the ball: N, S, E, W, NE, etc. */
  int ball_direction[4];
  
  /*	x, y		The robot's location.  y varies from 1 to 22,
			x varies from 1 to 78. */
  int x[4];
  int y[4];
  

  // the position of the ball, estimated from observations by all four players.
  int ball_x, ball_y;

  // the number of the active player (0..3)
  int activePlayerNumber;

  /* updates the world state for a single player */
  void update(int playerNumber, int local_area[9], int ball_direction, int x, int y);

  /* computes the dynamic roles */
  void computeRoles();

  void reset(); // resets the positions

  static WorldState* theInstance;

  /* the dynamically assigned player role */
  enum PlayerRole { defender, midfielder, striker } playerRole[4];

  static double getX(); // a function for the symbol "x"
  static double getY(); // a function for the symbol "y"
  static double getBallX(); // a function for the symbol "ball.x"
  static double getBallY(); // a function for the symbol "ball.y"
  static double getBallDistance(); // a function for the symbol "ball.distance"
  static int getBallLocalDirection(); // a function for the symbol "ball.local.direction"
  static double getMostWesterlyTeammateX(); // a function for the symbol "most-westerly-teammate.x"
  static int getPlayerRole(); // a function for the symbol "player-role"

  char field[MAX_X][MAX_Y]; // for debug output
  void printField(xabsl::ErrorHandler& errorHandler); // prints the field containing debug informations
};



#endif //__WorldState_h_
