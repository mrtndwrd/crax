/* WorldState based on Martin Lötzsch' ASCII-soccer example */

#ifndef __WorldState_h_
#define __WorldState_h_

//#include "../Xabsl/XabslEngine/XabslSymbols.h"


class WorldState
{
public:
  WorldState();

  // the player indexing works like a ring buffer. 
  // E.g. x[0] is for the acticve player, x[1] for the previously actice player etc.

  /*	x, y		The robot's location.  */
  double x;
  double y;
  
  /* updates the world state for a single player */
  void update(double x, double y);
  
  // TODO: I don't think I'll need this:
  //void reset(); // resets the positions

  static WorldState* theInstance;

  // TODO: Don't think this is needed either:
  /* the dynamically assigned player role */
  //enum PlayerRole { defender, midfielder, striker } playerRole[4];

  static double getX(); // a function for the symbol "x"
  static double getY(); // a function for the symbol "y"
  
  void printWorld(xabsl::ErrorHandler& errorHandler); // prints the field containing debug informations
};



#endif //__WorldState_h_