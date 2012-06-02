/*===========================================================================

  WorldState.cpp
  
    The class WorldState represents the knowledge of an agent.
    
      author: Martin Lötzsch
===========================================================================*/

#include "WorldState.h"

WorldState* WorldState::theInstance = NULL;

WorldState::WorldState() : activePlayerNumber(0), ball_x(40), ball_y(10)
{
  theInstance = this;
}

void WorldState::update(int playerNumber, int _local_area[9], int _ball_direction, int _x, int _y)
{
  int i, j, k;
  
  // set the active player
  activePlayerNumber = playerNumber;

  // copy new values
  for (j=0; j<9; j++) local_area[j][activePlayerNumber] = _local_area[j]; 
  ball_direction[activePlayerNumber] = _ball_direction;
  x[activePlayerNumber] = _x; y[activePlayerNumber] = _y;
    
  // estimate the ball position: fill "field" with the number of players that see the ball for each field element
  for (i=0;i<MAX_Y;i++) for (j=0;j<MAX_X;j++) field[j][i] = 0;
  
  double temp_angle; int temp_dir;
  for (i=1;i<MAX_Y-1;i++) for (j=1;j<MAX_X-1;j++) for (k=3; k>=0; k--)
  {
    if ((j == x[k]) && (i == y[k])) temp_angle = 0;
    else temp_angle = atan2((j - x[k]), (i - y[k]));
    temp_angle += PI;
    temp_angle = 360.0*temp_angle/(2.0*PI);
    temp_dir = N;
    if (temp_angle > 22.5+0*45) temp_dir = NW;
    if (temp_angle > 22.5+1*45) temp_dir = W;
    if (temp_angle > 22.5+2*45) temp_dir = SW;
    if (temp_angle > 22.5+3*45) temp_dir = S;
    if (temp_angle > 22.5+4*45) temp_dir = SE;
    if (temp_angle > 22.5+5*45) temp_dir = E;
    if (temp_angle > 22.5+6*45) temp_dir = NE;
    if (temp_angle > 22.5+7*45) temp_dir = N;
    
    if (temp_dir == ball_direction[k]) field[j][i] += 1;
  } 
  
  // estimate the ball position: search for peaks:
  int sum_x=0, sum_y=0, num_fields=0;
  
  for (i=1;i<MAX_Y-1;i++) for (j=1;j<MAX_X-1;j++) if (field[j][i]==4) { sum_x += j; sum_y += i; num_fields++; }
  
  if (num_fields!=0) { ball_x=sum_x / num_fields; ball_y=sum_y / num_fields; }
  else 
  {
    for (i=1;i<MAX_Y-1;i++) for (j=1;j<MAX_X-1;j++) if (field[j][i]==3) { sum_x += j; sum_y += i; num_fields++;}
    if (num_fields!=0) { ball_x=sum_x / num_fields; ball_y=sum_y / num_fields; }
  }
  
  // check if the ball is in the local area of a player
  for (i=3; i>=0; i--)
  {
    if (local_area[N][i] == BALL) { ball_x=x[i]; ball_y=y[i]-1; }
    if (local_area[NE][i] == BALL) { ball_x=x[i]+1; ball_y=y[i]-1; }
    if (local_area[E][i] == BALL) { ball_x=x[i]+1; ball_y=y[i]; }
    if (local_area[SE][i] == BALL) { ball_x=x[i]+1; ball_y=y[i]+1; }
    if (local_area[S][i] == BALL) { ball_x=x[i]; ball_y=y[i]+1; }
    if (local_area[SW][i] == BALL) { ball_x=x[i]-1; ball_y=y[i]+1; }
    if (local_area[W][i] == BALL) { ball_x=x[i]-1; ball_y=y[i]; }
    if (local_area[NW][i] == BALL) { ball_x=x[i]-1; ball_y=y[i]-1; }
  }
}

void WorldState::computeRoles()
{
  int i,j; double ball_distances[4]; int rank[4]={0,1,2,3};

  for (i=0;i<4;i++) ball_distances[i]=sqrt(pow(x[i] - ball_x,2) + pow(y[i] - ball_y,2));
  
  for (i=0;i<3;i++)  for (j=0;j<3;j++)
  {
    if (ball_distances[rank[j]] > ball_distances[rank[j+1]])
    { int temp = rank[j+1]; rank[j+1] = rank[j]; rank[j] = temp;}
  }

  playerRole[rank[0]] = WorldState::midfielder;
  playerRole[rank[1]] = WorldState::midfielder;
  playerRole[rank[2]] = (x[rank[2]] >= x[rank[3]]?defender:striker); 
  playerRole[rank[3]] = (x[rank[2]] > x[rank[3]]?striker:defender); 

  for (i=0;i<4;i++) if (ball_distances[i] <3 || x[i] >73) playerRole[i]=WorldState::midfielder;
}

double WorldState::getMostWesterlyTeammateX()
{
  double minX=78;
  for (int player=1;player<4;player++) if (theInstance->x[player] < minX) minX = theInstance->x[player];
  return minX;
}

int WorldState::getBallLocalDirection()
{
  for (int i=0;i<10;i++) if (theInstance->local_area[i][theInstance->activePlayerNumber]==BALL) return i;
  return -1;
}

double WorldState::getBallDistance()
{
  if (getBallLocalDirection()!=-1)
    return 1;
  else 
    return sqrt(pow(theInstance->x[theInstance->activePlayerNumber] - theInstance->ball_x,2) + pow(theInstance->y[theInstance->activePlayerNumber] - theInstance->ball_y,2));
}

int WorldState::getPlayerRole()
{
  return theInstance->playerRole[theInstance->activePlayerNumber];
}

void WorldState::printField(xabsl::ErrorHandler& errorHandler)
{
  int i,j;
  field[(int)ball_x][(int)ball_y] = 5;
  for (i=0;i<4;i++) field[x[i]][y[i]] = playerRole[i] + ((i==activePlayerNumber) ? 6 : 9);
  errorHandler.message("===============================================================================");
  for (i=1;i<22;i++)
  {
    char line[80];
    for (j=1;j<=78;j++)
    {
      line[0]='|'; 
      char c;
      switch ((char)field[j][i])
      {
      case 0: c=' '; break;
      case 1: c='-'; break;
      case 2: c='='; break;
      case 3: c=':'; break;
      case 4: c='*'; break; 
      case 5: c='o'; break;
      case 6: c='D'; break;
      case 7: c='M'; break;
      case 8: c='S'; break;
      case 9: c='d'; break;
      case 10: c='m'; break;
      case 11: c='s'; break;
      default: c='.'; break;
      }
      line[j]=c;
    }
    line[79]=0;
    errorHandler.message("%s",line);
  }
  errorHandler.message("===============================================================================");
}

void WorldState::reset()
{
  for(int i = 0; i <4; i++)
	{
	  x[i] = 46;
	  y[i] = 4+3*i;
    for (int j=0; j<9; j++) local_area[j][i] = EMPTY;
    ball_direction[i]=W;
  }
}

double WorldState::getX()
{
  return (double)theInstance->x[theInstance->activePlayerNumber];
}

double WorldState::getY()
{
  return (double)theInstance->y[theInstance->activePlayerNumber];
}

double WorldState::getBallX()
{
  return (double)theInstance->ball_x;
}

double WorldState::getBallY()
{
  return (double)theInstance->ball_y;
}
