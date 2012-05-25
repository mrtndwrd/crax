/*===========================================================================

  MyBasicBehaviors.h
  
    Implementation of all basic behaviors.
    
      author: Martin Lötzsch
===========================================================================*/

#include "MyBasicBehaviors.h"

void BasicBehaviorGetBehindBall::execute() 
{
  int ballLocalDirection = worldState.getBallLocalDirection();

  switch(ballLocalDirection)
  {
  case -1: action=worldState.ball_direction[worldState.activePlayerNumber]; break;
  case E: 
    if (worldState.y[worldState.activePlayerNumber] < 11) action=SE;
    else action=NE;
    break;
  case SE: action=E; break;  
  case NE: action=E; break;  
  case N:
    if (worldState.local_area[NE][worldState.activePlayerNumber] == EMPTY) action=NE; 
    else action = E;
    break;  
  case S: 
    if (worldState.local_area[SE][worldState.activePlayerNumber] == EMPTY) action=SE; 
    else action = E;
    break;  
action=SE; break;  
  case SW: action=S; break; 
  case NW: action=N; break;
  case W: action=W; break;
  case PLAYER: action=E; break;
  }
} 

void BasicBehaviorGoTo::execute()
{
  int _x=(int)x, _y=(int)y;

  if (_x<1) _x=1; if (_x>78) _x=78; if (_y>21) _y=21; if (_y<1) _y=1;

  if (_x<worldState.ball_x && _y==worldState.ball_y) if (_y <11) _y++; else _y--;

  int dx = _x - worldState.x[worldState.activePlayerNumber], dy = _y - worldState.y[worldState.activePlayerNumber];

  if (dy < 0)
  {
    if (dx >= 0 || abs(dx) < abs(dy)) action=NE;
    else action=NW;
  }
  else if (dy > 0)
  {
    if (dx >= 0 || abs(dx) < abs(dy)) action=SE;
    else action=SW;
  }
  else // dy == 0
  {
    if (dx>0) action = E;
    else if (dx<0) action = W;
    else action = DO_NOTHING;
  }

  if (worldState.local_area[action][worldState.activePlayerNumber] != EMPTY)
  {
    switch (action)
    {
    case N: action=NE; break; 
    case NE: action=E; break; 
    case E: action=(worldState.y[worldState.activePlayerNumber]<11?SE:NE); break; 
    case SE: action=E; break; 
    case S: action=SE; break; 
    case SW: action=S; break; 
    case W: action=W; break; 
    case NW: action=N; break; 
    }
  }
  if (worldState.local_area[action][worldState.activePlayerNumber] != EMPTY) action=DO_NOTHING;
}

