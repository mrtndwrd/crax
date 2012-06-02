/*===========================================================================

  MyBasicBehaviors.h
  
    Definition of all basic behaviors.
    
      author: Martin Lötzsch
===========================================================================*/

#ifndef __MyBasicBehaviors_h_
#define __MyBasicBehaviors_h_

#include "../Xabsl/XabslEngine/XabslBasicBehavior.h"
#include "WorldState.h"
#include "../ascii-soccer/soccer.h"

class BasicBehaviorGetBehindBall : public xabsl::BasicBehavior
{
public:
  BasicBehaviorGetBehindBall(xabsl::ErrorHandler& errorHandler, 
    WorldState& worldState, int& action)
    : xabsl::BasicBehavior("get_behind_ball",errorHandler), 
    worldState(worldState), 
    action(action)
  {
  }
  
  virtual void execute();
private:
  WorldState& worldState; // the world state on that the action is based
  int& action; // the action to be generated
};


class BasicBehaviorGoTo : public xabsl::BasicBehavior
{
public:
  double x,y; // the parameters of the basic behavior

  BasicBehaviorGoTo(xabsl::ErrorHandler& errorHandler, 
    WorldState& worldState, int& action)
    : xabsl::BasicBehavior("go_to",errorHandler), 
    worldState(worldState), 
    action(action)
  {
  }

  virtual void registerParameters()
  {
    // as basic behaviors are shared among engines for all players this is called more than once
    // register parameters only if they are not already registered
    if (!parameters->decimal.exists("go_to.x"))
    {
      parameters->registerDecimal("go_to.x",x);
      parameters->registerDecimal("go_to.y",y);
    }  
  }
  
  virtual void execute();
private:
  WorldState& worldState; // the world state on that the action is based
  int& action; // the action to be generated
};

#endif //__MyBasicBehaviors_h_
