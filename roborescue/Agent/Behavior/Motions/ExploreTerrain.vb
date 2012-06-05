' Motion that explores terrain of unknown traversability
' Motion is part of behaviour ExploreTraversability
Public Class ExploreTerrain
    Inherits Motion

    Public Sub New(ByVal control As MotionControl)
        MyBase.New(control)
        'Console.WriteLine("[RIKNOTE] Agent\Behavior\Motions\ExploreTerrain.vb::New() called")
    End Sub

    Private _LastStopped As DateTime = DateTime.MinValue
    Private _IsStopped As Boolean = True

#Region " Activation / DeActivation "

    Protected Overrides Sub OnActivated()
        MyBase.OnActivated()

        'first stop the agent
        Me.Control.Agent.Halt()
        Me._IsStopped = True
        Me._LastStopped = Now

        'If Not Me.SelectNewTarget(Me.Control.Agent.CurrentPoseEstimate) Then
        'no target selected, nothing sensible to do, so just turn
        'Me.Control.Agent.TurnRight(0.2)
        'no target available, change motion
        'Me.Control.ActivateMotion(MotionType.CorridorWalk, True)
        'Exit Sub
        'End If

        'execute the current path plan
        'Me.FollowPath(Me.Control.Agent.CurrentPoseEstimate)

    End Sub

    Protected Overrides Sub OnDeActivated()
        MyBase.OnDeActivated()

        'first stop the agent
        Me.Control.Agent.Halt()
        Me._IsStopped = True
        Me._LastStopped = Now

    End Sub

#End Region
End Class
