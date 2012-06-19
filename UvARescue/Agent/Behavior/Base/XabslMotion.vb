Imports UvARescue.Communication
Imports System.Net.Sockets
Imports UvARescue.Tools


Public Class XabslMotion
    Inherits Motion
    Implements XabslOwner

    Protected port As Integer
    Protected host As String
    Protected sock As Socket
    Public xa As XabslAll
    Public desiredBehavior As String

    Public Sub New(ByVal control As MotionControl)
        MyBase.New(control)
        ' Setting my host and string, should be dynamic, but for now it's not.
        ' Now handled by readXabslConfig
        'Me.host = "192.168.2.11"
        'Me.port = 7001
        readXabslConfig()
        Me.xa = New XabslAll(Me, Me.host, Me.port)
        Console.WriteLine("[RIKNOTE] Agent\Behavior\Behaviors\XabslBehavior.vb::New() called")
    End Sub

    ' OnActivated: Seems to be the overridable method that can be ran when the Behavior's "Activate" is called.
    Protected Overrides Sub OnActivated()
        MyBase.OnActivated()
        Me.xa.startRunning()
        Me.xa.SendMessage("BEHAVIOR:" + Me.desiredBehavior)
        Console.WriteLine("[XABSLBEHAVIOR] Activating connection")
    End Sub

    Protected Sub readXabslConfig()
        Dim fileReader As System.IO.StreamReader
        Try
            fileReader = _
            My.Computer.FileSystem.OpenTextFileReader("../../../configs/xabslConfig.cfg")
        Catch e As IO.FileNotFoundException
            Console.WriteLine("couldn't find xabsl config file. Are you sure it is in configs/?")
            Me.host = "127.0.0.1"
            Me.port = 7001
            Me.desiredBehavior = "drive_circle"
            Exit Sub
        End Try

        Dim stringReader As String
        Dim sides() As String
        While (Not fileReader.EndOfStream)
            stringReader = fileReader.ReadLine()
            sides = Split(stringReader, " = ")
            Select Case sides(0)
                Case "agentNumber"
                    ' Don't handle yet
                Case "xabslhost"
                    Console.WriteLine("[XABSLMOTION] setting xabslHost to {0}", sides(1))
                    Me.host = sides(1)
                Case "xabslport"
                    Console.WriteLine("[XABSLMOTION] setting xabslport to {0}", sides(1))
                    Me.port = Integer.Parse(sides(1))
                Case "xabslBehavior"
                    Console.WriteLine("[XABSLMOTION] setting desiredBehavior to {0}", sides(1))
                    Me.desiredBehavior = sides(1)
                Case Else
                    Console.WriteLine("Couldn't parse rule {0}", stringReader)
            End Select
        End While

    End Sub

    ' Same story as onActivated
    Protected Overrides Sub OnDeActivated()
        MyBase.OnDeActivated()
        ' Close listen socket for JavaXabslEngine
        Me.xa.stopRunning()
        Me.Control.DeActivateMotion(MotionType.XabslCorridorWalk)
    End Sub

    Public Sub RunBehavior(behaviorString As String) Implements XabslOwner.RunBehavior
        Console.WriteLine("Runnin RunBehavior with command " + behaviorString)
        Dim splittedString() As String = Split(behaviorString, ":")
        If (splittedString.Length < 2) Then
            ' It seems empty messages are sent (or read?) all the time!
            'Console.WriteLine("[XABSLBEHAVIOR] received wrong string, message: {0}", behaviorString)
            Exit Sub
        Else
            Dim behavior As String = splittedString(0)
            Dim args() As String = Split(splittedString(1), ",")
            Select Case behavior
                Case "DRIVE"
                    ' Call a command to the robot to drive forward args(0) capibara
                    Dim speed As Single = Single.Parse(args(0))
                    Console.WriteLine("Will drive north with {0} speed", speed)
                    Me.Control.Agent.Drive(speed)
                Case "TURN"
                    ' Actually angle is turning_speed, but that will be solved later.
                    ' I use differential drive, because turnLeft or turnRight need another if statement.
                    Dim angle As Single = Single.Parse(args(0))
                    Console.WriteLine("Will turn {0} degrees", angle)
                    Me.Control.Agent.DifferentialDrive(0, angle)
                Case "DIFFERENTIALDRIVE"
                    Dim speed As Single = Single.Parse(args(0))
                    Dim turningSpeed As Single = Single.Parse(args(1))
                    Me.Control.Agent.DifferentialDrive(speed, turningSpeed)
                Case Else
                    Console.WriteLine("no drive command, couldn't parse {0}", behaviorString)
            End Select
        End If
    End Sub
#Region "Sensor Logic"
    Protected Overrides Sub ProcessSensorUpdate(ByVal sensor As Sensor)
        Console.WriteLine("Processing sensor: " + sensor.SensorName)
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

    Protected Overridable Sub ProcessLaserRangeData(ByVal sensorName As String, ByVal laser As LaserRangeData)
    End Sub
    Protected Overridable Sub ProcessSonarData(ByVal sonar As SonarData)
    End Sub
    Protected Overridable Sub ProcessInsData(ByVal ins As InsData)
    End Sub
    Protected Overridable Sub ProcessGroundTruthData(ByVal current_data As GroundTruthData)
    End Sub
    Protected Overridable Sub ProcessVictimData(ByVal current_data As VictimData)
    End Sub
#End Region
End Class
