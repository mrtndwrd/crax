/* A basic test behavior based on Marten Loetschers example */
#include "stdafx.h"
#ifndef __MyBasicBehaviors_h_
#define __MyBasicBehaviors_h_

#include "Tools/xabsl/Xabsl/XabslEngine/XabslBasicBehavior.h"
#include "WorldState.h"

class TestBehavior : public xabsl::BasicBehavior
{
public:	  
  TestBehavior(xabsl::ErrorHandler& errorHandler, WorldState& worldState, int& action)
    : xabsl::BasicBehavior("testBehavior",errorHandler),  worldState(worldState), action(action)
  {
  }
  virtual void execute();
private:
  WorldState& worldState; // the world state on that the action is based
  int& action; // the action to be generated
};
#endif