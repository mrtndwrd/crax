Imports UvARescue.Communication
Imports System.Math

Public Class XabslCorridorWalk
    Inherits XabslMotion

    Public Sub New(ByVal control As MotionControl)
        MyBase.New(control)
        Console.WriteLine("[XABSLCORRIDORWALK] Agent\Behavior\Motions\XabslCorridorWalk.vb::New() called")
    End Sub

    Protected Overrides Sub OnActivated()
        MyBase.OnActivated()
        Console.WriteLine("[XABSLCORRIDORWALK] XabslCorridorWalk Activated")
    End Sub

    Protected Overrides Sub OnDeActivated()
        MyBase.OnDeActivated()
        Console.WriteLine("[XABSLCORRIDORWALK] XabslCorridorWalk Deactivated")
    End Sub

#Region "Sensor Logic"
    '   Protected Overridable Sub ProcessLaserRangeData(ByVal sensorName As String, ByVal laser As LaserRangeData)
    '   End Sub
    '   Protected Overridable Sub ProcessSonarData(ByVal sonar As SonarData)
    '   End Sub
    Protected Overrides Sub ProcessInsData(ByVal ins As InsData)
        ' Since I assumed using degrees in stead of radians (which is easier to use in hard-coded variables), convert to 
        ' degrees.
        Console.WriteLine("Sending 'GROUNDTRUTH:{0}' to the robot", (ins.Yaw * (180 / PI)).ToString)
        Me.xa.SendMessage("GROUNDTRUTH:" + (ins.Yaw * (180 / PI)).ToString)
    End Sub
    Protected Overrides Sub ProcessGroundTruthData(ByVal current_data As GroundTruthData)
        ' It seems the current angle of the robot is the Yaw, so change in the angle should 
        ' be enough to make it turn 360 degrees (my first goal)
        Console.WriteLine("Sending 'GROUNDTRUTH:{0}' to the robot", current_data.Yaw)
        Me.xa.SendMessage("GROUNDTRUTH:" + (current_data.Yaw * (180 / PI)).ToString)
    End Sub
    '   Protected Overridable Sub ProcessVictimData(ByVal current_data As VictimData)
    '  End Sub



#End Region


End Class