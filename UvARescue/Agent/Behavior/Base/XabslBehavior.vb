Imports UvARescue.Tools
Imports UvARescue.Communication
Imports System.Net.Sockets



Public Class XabslBehavior
    Inherits Behavior

    Protected port As Integer
    Protected host As String
    Protected sock As Socket
    Protected xa As XabslAll



    ' Constructor 
    Public Sub New(ByVal control As BehaviorControl)
        MyBase.New(control)
        ' Setting my host and string, should be dynamic, but for now it's not.
        Me.host = "127.0.0.1"
        Me.port = 7001
        Me.xa = New XabslAll(Me.host, Me.port)
        Console.WriteLine("[RIKNOTE] Agent\Behavior\Behaviors\XabslBehavior.vb::New() called")
    End Sub

    ' OnActivated: Seems to be the overridable method that can be ran when the Behavior's "Activate" is called.
    Protected Overrides Sub OnActivated()
        MyBase.OnActivated()
        Me.xa.ConnectToXabsl()
        Console.WriteLine("[XABSLBEHAVIOR] Activating connection")
        ' Activate listen socket for JavaXabslEngine

    End Sub

    ' Same story as onActivated
    Protected Overrides Sub OnDeActivated()
        MyBase.OnDeActivated()
        ' Close listen socket for JavaXabslEngine
        Me.xa.DisconnectFromXabsl()
        Me.Control.DeActivateMotion(MotionType.XabslCorridorWalk)
    End Sub

End Class
