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

    ' unit conversion constants (copied from OA)
    Private ReadOnly _RAD2DEG As Double = 180.0 / PI
    Private ReadOnly _DEG2RAD As Double = PI / 180.0

#Region "Sensor Logic"
    '   Protected Overridable Sub ProcessLaserRangeData(ByVal sensorName As String, ByVal laser As LaserRangeData)
    '   End Sub
    '   Protected Overridable Sub ProcessSonarData(ByVal sonar As SonarData)
    '   End Sub
    Protected Overrides Sub ProcessInsData(ByVal ins As InsData)
        ' Since I assumed using degrees in stead of radians (which is easier to use in hard-coded variables), convert to 
        ' degrees.
        Console.WriteLine("Sending 'GROUNDTRUTH:{0}' to the robot", (ins.Yaw * (180 / PI)).ToString)
        Me.xa.SendMessage(String.Format("GROUNDTRUTH:{0},{1},{2}", ins.X.ToString, ins.Y.ToString, (ins.Yaw * (180 / PI)).ToString))
    End Sub


    ' Isn't used right now:
    'Protected Overrides Sub ProcessGroundTruthData(ByVal current_data As GroundTruthData)
    '' It seems the current angle of the robot is the Yaw, so change in the angle should 
    '' be enough to make it turn 360 degrees (my first goal)
    '    Console.WriteLine("Sending 'GROUNDTRUTH:{0}' to the robot", current_data.Yaw)
    '    Me.xa.SendMessage("GROUNDTRUTH:" + (current_data.Yaw * (180 / PI)).ToString)
    'End Sub
    '   Protected Overridable Sub ProcessVictimData(ByVal current_data As VictimData)
    '  End Sub

    Protected Overridable Sub ProcessLaserRangeData(ByVal sensorName As String, ByVal laser As LaserRangeData)
        'It is possible that a sensor is mounted on a tilt.  Not interested in that here.
        If (sensorName = "TiltedScanner") Then
            Exit Sub
        End If

        Dim fov As Double = 22.5 / Me._RAD2DEG

        Dim minWestNorthWest As Double = Double.MaxValue    '  -78.75 till -56.25 deg
        Dim minNorthWest As Double = Double.MaxValue        '  -56.25 till -33.75 deg
        Dim minNorthNorthWest As Double = Double.MaxValue   '  -33.75 till -11.25 deg
        Dim minNorth As Double = Double.MaxValue            '  -11.25 till +11.25 deg
        Dim minNorthNorthEast As Double = Double.MaxValue
        Dim minNorthEast As Double = Double.MaxValue
        Dim minEastNorthEast As Double = Double.MaxValue

        'Corridors typical dimensions
        Dim minDistance As Double = 1.0
        Dim maxDistance As Double = 0.1

        'helper vars
        Dim length As Integer = laser.Range.Length - 1      ' index of last laser beam (180)
        Dim start As Double
        Dim until As Double



        'determine min distance in minWestNorthWest direction
        start = fov / 2
        until = start + fov

        For i As Integer = Max(0, CInt(start / laser.Resolution)) To Min(CInt(until / laser.Resolution), length)
            minWestNorthWest = Min(minWestNorthWest, laser.Range(i))
        Next
        If minWestNorthWest < minDistance Then
            minDistance = minWestNorthWest
        End If
        If minWestNorthWest > maxDistance Then
            maxDistance = minWestNorthWest
        End If

        'determine min distance in minNorthWest direction
        start = until
        until = start + fov
        For i As Integer = Max(0, CInt(start / laser.Resolution)) To Min(CInt(until / laser.Resolution), length)
            minNorthWest = Min(minNorthWest, laser.Range(i))
        Next
        If minNorthWest < minDistance Then
            minDistance = minNorthWest
        End If
        If minNorthWest > maxDistance Then
            maxDistance = minNorthWest
        End If

        'determine min distance in minNorthNorthWest direction
        start = until
        until = start + fov
        For i As Integer = Max(0, CInt(start / laser.Resolution)) To Min(CInt(until / laser.Resolution), length)
            minNorthNorthWest = Min(minNorthNorthWest, laser.Range(i))
        Next
        If minNorthNorthWest < minDistance Then
            minDistance = minNorthNorthWest
        End If
        If minNorthNorthWest > maxDistance Then
            maxDistance = minNorthNorthWest
        End If

        'determine min distance in minNorth direction
        start = until
        until = start + fov
        For i As Integer = Max(0, CInt(start / laser.Resolution)) To Min(CInt(until / laser.Resolution), length)
            minNorth = Min(minNorth, laser.Range(i))
        Next
        If minNorth < minDistance Then
            minDistance = minNorth
        End If
        If minNorth > maxDistance Then
            maxDistance = minNorth
        End If

        'determine min distance in minNorthNorthEast direction
        start = until
        until = start + fov
        For i As Integer = Max(0, CInt(start / laser.Resolution)) To Min(CInt(until / laser.Resolution), length)
            minNorthNorthEast = Min(minNorthNorthEast, laser.Range(i))
        Next
        If minNorthNorthEast < minDistance Then
            minDistance = minNorthNorthEast
        End If
        If minNorthNorthEast > maxDistance Then
            maxDistance = minNorthNorthEast
        End If

        'determine min distance in minNorthEast direction
        start = until
        until = start + fov
        For i As Integer = Max(0, CInt(start / laser.Resolution)) To Min(CInt(until / laser.Resolution), length)
            minNorthEast = Min(minNorthEast, laser.Range(i))
        Next
        If minNorthEast < minDistance Then
            minDistance = minNorthNorthEast
        End If
        If minNorthEast > maxDistance Then
            maxDistance = minNorthEast
        End If

        'determine min distance in minEastNorthEast direction
        start = until
        until = start + fov
        For i As Integer = Max(0, CInt(start / laser.Resolution)) To Min(CInt(until / laser.Resolution), length)
            minEastNorthEast = Min(minEastNorthEast, laser.Range(i))
        Next
        If minEastNorthEast < minDistance Then
            minDistance = minEastNorthEast
        End If
        If minEastNorthEast > maxDistance Then
            maxDistance = minEastNorthEast
        End If
    End Sub


#End Region


End Class