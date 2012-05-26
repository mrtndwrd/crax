/* The implementation of the world state. This will store all the relevant information for good exploration */

#include "stdafx.h"
#include "WorldState.h"

WorldState* WorldState::theInstance = NULL;

WorldState::WorldState()
{
	theInstance = this;
}

void WorldState::update(double _x, double _y )
{
	x = _x;
	y = _y;
}

void WorldState::printWorld( xabsl::ErrorHandler& errorHandler )
{
	std::cout << "x position: " << x << " y position: " << y;
}

double WorldState::getX()
{
	return theInstance->x;
}

double WorldState::getY()
{
	return theInstance->y;
}
