// main.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"
#include "main.h"

// Constructor
XabslEngineRunner::XabslEngineRunner()
{
    // TODO: Initialize values
}

XabslEngineRunner::TestFunc()
{
	std::cout << "Inside the DLL!";
}

XabslEngineRunner::Initialize()
{
		// create the engines (now only one, but can be expanded to several)
		for (int i=0; i<1;i++)
		{
			// create the engine
			pEngine[i] = new xabsl::Engine(myErrorHandler, &getCurrentSystemTime);

			// register basic behaviors
			pEngine[i]->registerBasicBehavior(testBehavior);

			// register the symbols
			/*
			pEngine[i]->registerEnumElement("role", "role.striker", WorldState::striker);
			pEngine[i]->registerEnumElement("role", "role.defender", WorldState::defender);
			pEngine[i]->registerEnumElement("role", "role.midfielder", WorldState::midfielder);
			pEngine[i]->registerEnumElement("next_action", "next_action.undefined", -1);
			pEngine[i]->registerEnumElement("next_action", "next_action.NW", NW);
			pEngine[i]->registerEnumElement("next_action", "next_action.N", N);
			pEngine[i]->registerEnumElement("next_action", "next_action.NE", NE);
			pEngine[i]->registerEnumElement("next_action", "next_action.E", E);
			pEngine[i]->registerEnumElement("next_action", "next_action.SE", SE);
			pEngine[i]->registerEnumElement("next_action", "next_action.S", S);
			pEngine[i]->registerEnumElement("next_action", "next_action.SW", SW);
			pEngine[i]->registerEnumElement("next_action", "next_action.W", W);
			pEngine[i]->registerEnumElement("next_action", "next_action.kick", KICK);
			pEngine[i]->registerEnumElement("next_action", "next_action.do_nothing", DO_NOTHING);
			pEngine[i]->registerEnumeratedOutputSymbol("next_action", "next_action", &nextAction);
			pEngine[i]->registerEnumeratedInputSymbol("role", "role", &WorldState::getPlayerRole);
			*/
			pEngine[i]->registerDecimalInputSymbol("x", &WorldState::getX);
			pEngine[i]->registerDecimalInputSymbol("y", &WorldState::getY);
			/*
			pEngine[i]->registerDecimalInputSymbol("ball.x", &WorldState::getBallX);
			pEngine[i]->registerDecimalInputSymbol("ball.y", &WorldState::getBallY);
			pEngine[i]->registerDecimalInputSymbol("ball.distance", &WorldState::getBallDistance);
			pEngine[i]->registerEnumeratedInputSymbol("ball.local.direction","next_action", &WorldState::getBallLocalDirection);
			pEngine[i]->registerDecimalInputSymbol("most_westerly_teammate.x", &WorldState::getMostWesterlyTeammateX);
			*/
			// parse the intermediate code
			MyFileInputSource input("intermediate-code.dat");
			pEngine[i]->createOptionGraph(input);
		}
	}
