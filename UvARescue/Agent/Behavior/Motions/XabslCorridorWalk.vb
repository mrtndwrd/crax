Imports UvARescue.Communication

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
    Protected Overrides Sub ProcessSensorUpdate(ByVal sensor As Sensor)
        Console.WriteLine("[XABSLCORRIDORWALK] ProcessSensorUpdate called")
        ' RIKNOTE: pass the update to our Motion parent as well (This actually does nothing, but hey)
        MyBase.ProcessSensorUpdate(sensor)
        ' RIKNOTE: this Motion type uses only the laser range data for now (and
        ' the groundtruth data if our roll or pitch angle becomes too great?)
        ' but should probably just rely on the sonar updates
        If sensor.SensorType = LaserRangeSensor.SENSORTYPE_RANGESCANNER Then
            Me.ProcessLaserRangeData(sensor.SensorName, DirectCast(sensor, LaserRangeSensor).PeekData)
        ElseIf sensor.SensorType = SonarSensor.SENSORTYPE_SONAR Then
            Me.ProcessSonarData(DirectCast(sensor, SonarSensor).CurrentData)
        ElseIf sensor.SensorType = InsSensor.SENSORTYPE_INS Then
            Me.ProcessInsData(DirectCast(sensor, InsSensor).CurrentData)
        ElseIf sensor.SensorType = GroundTruthSensor.SENSORTYPE_GROUNDTRUTH Then
            Me.ProcessGroundTruthData(DirectCast(sensor, GroundTruthSensor).CurrentData)
        ElseIf sensor.SensorType = VictimSensor.SENSORTYPE_VICTIM Then
            Me.ProcessVictimData(DirectCast(sensor, VictimSensor).CurrentData)
        End If
    End Sub

    '   Protected Overridable Sub ProcessLaserRangeData(ByVal sensorName As String, ByVal laser As LaserRangeData)
    '   End Sub
    '   Protected Overridable Sub ProcessSonarData(ByVal sonar As SonarData)
    '   End Sub
    '   Protected Overridable Sub ProcessInsData(ByVal ins As InsData)
    '   End Sub
    Protected Overrides Sub ProcessGroundTruthData(ByVal current_data As GroundTruthData)
        ' It seems the current angle of the robot is the Yaw, so change in the angle should 
        ' be enough to make it turn 360 degrees (my first goal)
        Console.WriteLine("Sending 'GROUNDTRUTH:{0}' to the robot", current_data.Yaw)
        Me.xa.SendMessage("GROUNDTRUTH:" + current_data.Yaw.ToString)
    End Sub
    '   Protected Overridable Sub ProcessVictimData(ByVal current_data As VictimData)
    '  End Sub



#End Region


End Class