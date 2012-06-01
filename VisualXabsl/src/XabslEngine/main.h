#include "Tools/xabsl/Xabsl/XabslEngine/XabslEngine.h"
#include "Tools.h"
#include "BasicBehaviorImplementation.h"
#include "WorldState.h"

class XabslEngineRunner {
    public:
XabslEngineRunner();
// an instance of the derived error handler 
MyErrorHandler myErrorHandler;
// the world state that is shared by all players
WorldState worldState;
// the next action to be generated by the active agent
int nextAction;
// an xabsl engine (currently only one, but in an array to have expansibility)
xabsl::Engine* pEngine[1];
// My basic TestBehavior
TestBehavior testBehavior(myErrorHandler);
void TestFunc();
void Initialize();
};