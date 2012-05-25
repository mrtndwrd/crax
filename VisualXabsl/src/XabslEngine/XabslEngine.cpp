// TestDLLapplication.cpp : Defines the exported functions for the DLL application.
//

#include "stdafx.h"

void __declspec(dllexport) TestFunc()
{
	std::cout << "Inside the DLL!";
}
