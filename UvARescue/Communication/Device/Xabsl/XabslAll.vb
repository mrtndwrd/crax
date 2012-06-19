Imports UvARescue.Tools
Imports System.Threading


Public Class XabslAll
    Inherits RegularThread

#Region "Constructor"
    Public Sub New(ByVal xabslMotion As XabslOwner, ByVal xabslHost As String, ByVal xabslPort As Integer)
        MyBase.New()
        If String.IsNullOrEmpty(xabslHost) Then Throw New ArgumentNullException("xabslHost")
        Me._XabslHost = xabslHost
        Me._XabslPort = xabslPort
        Me._XabslMotion = xabslMotion
    End Sub
#End Region

#Region " Properties "
    Private _XabslHost As String
    Public ReadOnly Property Host() As String
        Get
            Return Me._XabslHost
        End Get
    End Property

    Private _XabslPort As Integer
    Public ReadOnly Property Port() As Integer
        Get
            Return Me._XabslPort
        End Get
    End Property

    Private _XabslConnection As New TcpConnection
    Public ReadOnly Property XabslConnection() As TcpConnection
        Get
            Return Me._XabslConnection
        End Get
    End Property

    Private _XabslMotion As XabslOwner
#End Region

#Region " TcpConnection to Send Control Messages to XABSL "

    Public ReadOnly Property ConnectedToXabsl() As Boolean
        Get
            Return Me._XabslConnection.IsConnected
        End Get
    End Property

    Private Sub ConnectToXabsl()
        Thread.Sleep(TimeSpan.FromSeconds(3)) 'was 1 second, seemed to short for UDK
        Me._XabslConnection.Connect(Me._XabslHost, Me._XabslPort)
        Console.WriteLine("[XABSLALL] connected to Xabsl")
    End Sub

    Private Sub DisconnectFromXabsl()
        Console.WriteLine("[XabslDevice]: DisconnectFromXABSL")
        If (Me._XabslConnection.IsConnected()) Then
            Me._XabslConnection.Disconnect()
        End If
    End Sub

    Public Sub startRunning()
        Me.ConnectToXabsl()
        Me.Start()
        Console.WriteLine("[XABSLALL] Started listening thread")
    End Sub

    Public Sub stopRunning()
        Me.Stop()
        Me.DisconnectFromXabsl()
        Console.WriteLine("[XABSLALL] Stopped")
    End Sub

#End Region

#Region "send and receive"

    Protected Overrides Sub Run()
        While (Me.ConnectedToXabsl)
            Dim msg As String
            Try
                msg = Me._XabslConnection.Receive(1000)
            Catch ex As Exception
                Exit Sub
            End Try
            If (Not String.IsNullOrEmpty(msg)) Then
                Console.WriteLine("[XABSLALL] Message received:" + msg)
                'Thread.Sleep(1000)
                Console.WriteLine("[XABSLALL] Parsing message")
                'Me._XabslConnection.Send("Message received")
                Me._XabslMotion.RunBehavior(msg)
            End If
        End While
    End Sub


    Public Sub SendMessage(ByVal message As String)
        'message += Chr(13) + Chr(10)
        'message += vbNewLine
        Me._XabslConnection.Send(message)
    End Sub
#End Region
End Class