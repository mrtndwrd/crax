Imports UvARescue.Tools
Imports System.Net.Sockets

Public Class XabslBehavior
    Inherits Behavior

    Protected port As Integer
    Protected host As String
    Protected sock As Socket


    ' Constructor 
    Public Sub New(ByVal control As BehaviorControl)
        MyBase.New(control)
        ' Setting my host and string, should be dynamic, but for now it's not.
        Me.host = "127.0.0.1"
        Me.port = 5005
        Me.sock = New Socket()
        'Console.WriteLine("[RIKNOTE] Agent\Behavior\Behaviors\XabslFollowCorridor.vb::New() called")
    End Sub

    ' OnActivated: Seems to be the overridable method that can be ran when the Behavior's "Activate" is called.
    Protected Overrides Sub OnActivated()
        MyBase.OnActivated()
        Console.WriteLine("[XABSLBEHAVIOR] Activating connection")
        ' Activate listen socket for JavaXabslEngine
        Me.sock.Protocol = sckTCPProtocol ' choose the TCP protocol 
        Me.sock.LocalPort = 50505 ' an example port number 
        Me.sock.Listen() ' tell 
    End Sub

    ' Same story as onActivated
    Protected Overrides Sub OnDeActivated()
        MyBase.OnDeActivated()
        ' Close listen socket for JavaXabslEngine
        Me.xabslConnection.Disconnect()
        Me.Control.DeActivateMotion(MotionType.XabslCorridorWalk)
    End Sub

End Class
