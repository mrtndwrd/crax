Public Class XabslCorridorWalk
    Inherits Motion

    Public Sub New(ByVal control As MotionControl)
        MyBase.New(control)
        Console.WriteLine("[RIKNOTE] Agent\Behavior\Motions\XabslCorridorWalk.vb::New() called")
    End Sub

    Protected Overrides Sub OnActivated()
        Console.WriteLine("[RIKNOTE] XabslCorridorWalk Activated")
    End Sub

    Protected Overrides Sub OnDeActivated()
        Console.WriteLine("[RIKNOTE] XabslCorridorWalk Deactivated")
    End Sub

End Class