/* Implementation of the TestBehavior class */
#include "stdafx.h"
#include "MyBasicBehaviors.h"

void TestBehavior::execute()
{
	double x = worldState.getX();
	double y = worldState.getY();
	std::cout << "X: " << x << "y: " << y;
	action = (int)x+1;
}