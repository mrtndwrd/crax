Public Class XabslFollowCorridor
    Inherits Behavior

    ' Constructor 
    Public Sub New(ByVal control As BehaviorControl)
        MyBase.New(control)
        Console.WriteLine("[RIKNOTE] Agent\Behavior\Behaviors\XabslFollowCorridor.vb::New() called")
    End Sub

    ' OnActivated: Seems to be the overridable method that can be ran when the Behavior's "Activate" is called.
    Protected Overrides Sub OnActivated()
        MyBase.OnActivated()
        ' Activate listen socket for JavaXabslEngine
        Console.WriteLine("[XABSLFOLLOWCORRIDOR]: Activated")
        Me.Control.ActivateMotion(MotionType.XabslCorridorWalk, False)
    End Sub

    ' Same story as onActivated
    Protected Overrides Sub OnDeActivated()
        MyBase.OnDeActivated()
        Me.Control.DeActivateMotion(MotionType.XabslCorridorWalk)
    End Sub

End Class
