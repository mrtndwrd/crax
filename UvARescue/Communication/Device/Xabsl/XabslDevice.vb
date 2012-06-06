Imports UvARescue.Tools

Imports System.Net
Imports System.Net.Sockets
Imports System.Threading

''' <summary>
''' The central object that provides Xabsl capabilities to the Agent, adjusted from XabslDevice
''' 
''' The XabslDevice:
''' - holds a single TcpConnection to the XABSL server to send 
''' information
''' - holds a single XabslListener on which it accepts incoming messages
''' - holds a single XabslConversation for every currently active connection
''' with other agents through the XABSL. Note: multiple conversations can
''' be active simultaneously.
''' </summary>
''' <remarks></remarks>
Public Class XabslDevice
    Inherits RegularThread
    Implements ICommDevice

#Region " Constructor "

    Public Sub New( _
            ByVal owner As ICommOwner, _
            ByVal xabslHost As String, _
            ByVal xabslPort As Integer, _
            ByVal operatorName As String, _
            ByVal teamMembers As String, _
            ByVal agentName As String, _
            ByVal agentNumber As Integer, _
            ByVal agentHost As String)

        MyBase.New()


        'Thread.Sleep(TimeSpan.FromSeconds(10))

        If IsNothing(owner) Then Throw New ArgumentNullException("owner")
        If String.IsNullOrEmpty(xabslHost) Then Throw New ArgumentNullException("xabslHost")
        If String.IsNullOrEmpty(operatorName) Then Throw New ArgumentNullException("operatorName")
        If String.IsNullOrEmpty(teamMembers) Then Throw New ArgumentNullException("teamMembers")
        If String.IsNullOrEmpty(agentName) Then Throw New ArgumentNullException("agentName")
        If Not IPAddress.TryParse(agentHost, Me._AgentHost) Then Throw New ArgumentException("host should be an IP address", "agentHost")

        'Console.WriteLine(String.Format("[XABSLDevice] Creating device for {0} (SpawnNumber {1}) towards host {2} for owner {3}", agentName, agentNumber, agentHost, owner.ToString))

        If Not System.Globalization.CultureInfo.CurrentCulture.NumberFormat.NumberDecimalSeparator = System.Globalization.NumberFormatInfo.InvariantInfo.NumberDecimalSeparator Then
            'This prevents reading wrong dBm for European cultures
            Thread.CurrentThread.CurrentCulture = New System.Globalization.CultureInfo("en-US", False)
            Console.WriteLine("[XabslDriver:Run]: CurrentCulture is now {0}for thread '{1}'.", System.Globalization.CultureInfo.CurrentCulture.Name, Thread.CurrentThread.Name)
        End If

        'save args in local vars
        Me._Owner = owner
        Me._XabslHost = xabslHost
        Me._XabslPort = xabslPort

        Me._OperatorName = operatorName
        Me._TeamMembers = New Dictionary(Of String, Integer)

        Dim members() As String = Strings.Split(teamMembers, ",")
        For Each member As String In members
            Dim parts() As String = Strings.Split(member, "-")
            Dim number As Integer = CInt(Double.Parse(parts(0)))
            Dim name As String = parts(1)
            Dim port As Integer = 8000 + number
            Me._TeamMembers.Add(name, port)
        Next

        Me._AgentName = agentName
        Me._ListenerPort = 8000 + agentNumber
        Me._Listener = New XabslListener(Me, Me._AgentName, Me._AgentHost, Me._ListenerPort)
        Me._Conversations = New Dictionary(Of Integer, ICommConversation)

    End Sub

#End Region

#Region " Properties "

    Private _Owner As ICommOwner
    Public ReadOnly Property Owner() As ICommOwner
        Get
            Return Me._Owner
        End Get
    End Property

    Private _XabslHost As String
    Public ReadOnly Property Host() As String Implements ICommDevice.Host
        Get
            Return Me._XabslHost
        End Get
    End Property

    Private _XabslPort As Integer
    Public ReadOnly Property Port() As Integer Implements ICommDevice.Port
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

#End Region

#Region " TcpConnection to Send Control Messages to XABSL "


    Public ReadOnly Property ConnectedToXabsl() As Boolean
        Get
            Return Me._XabslConnection.IsConnected
        End Get
    End Property

    Protected Sub ConnectToXabsl()
        Thread.Sleep(TimeSpan.FromSeconds(3)) 'was 1 second, seemed to short for UDK
        Console.WriteLine("[XabslDevice]: ConnectToXABSL")
        Me._XabslConnection.Connect(Me._XabslHost, Me._XabslPort)
    End Sub

    Protected Sub DisconnectFromXabsl()
        Console.WriteLine("[XabslDevice]: DisconnectToXABSL")

        Me._XabslConnection.Disconnect()
    End Sub

#End Region

#Region " Xabsl Commands "

    ' Sends a command, if waitForReply is set, a reply can be received. When the reply starts with ERROR, success will be set to 'false'.
    Protected Function SendXabslCommand(ByVal command As String, ByVal waitForReply As Boolean, Optional ByRef reply As String = "") As Boolean

        'default to true
        Dim success As Boolean = True

        SyncLock Me._XabslConnection

            Try
                ' Append \r\n (or the other way around, I dont know)
                If Not command.Trim.EndsWith(Chr(13) + Chr(10)) Then
                    command += Chr(13) + Chr(10)
                End If

                Me._XabslConnection.Send(command)

                If waitForReply Then
                    'block this thread until acknowledgement or failure message is received
                    Dim done As Boolean = False
                    While Not done

                        If Me._XabslConnection.DataAvailable() Then
                            Dim msg As String = Me._XabslConnection.Receive(1024)
                            If msg.TrimStart.ToUpper.StartsWith("ERROR") Then
                                reply = msg
                                success = False
                                done = True
                            Else
                                reply = msg
                                success = True
                                done = True
                            End If
                        End If

                        If Not done Then
                            Thread.Sleep(TimeSpan.FromMilliseconds(500))
                        End If

                    End While
                End If

            Catch ex As Exception
                Console.WriteLine(ex)
                success = False
            End Try

        End SyncLock

        If Not success Then
            Console.WriteLine(String.Format("[XABSL] - ERROR: command: '{0}' received reply '{1}'", command, reply))
        End If

        Return success

    End Function

  
#End Region

#Region " Device Lifetime "

    Private _AgentName As String
    Private _AgentHost As IPAddress
    Private _ListenerPort As Integer

    Private _OperatorName As String
    Private _TeamMembers As Dictionary(Of String, Integer)

    Private _Listener As ICommListener


    Public Sub StartDevice() Implements ICommDevice.StartDevice

        'boot up

        '2007 competition (commented as listenerport is registered simultaneously with agent)
        'If Not Me.RegisterListenerPort() Then
        '   Throw New Exception("Could not register Listner port")
        'End If

        While Not Me.RegisterAgent()
            'Throw New Exception("Registering Agent failed")
            MsgBox("Registering Agent failed. Perhaps start XABSL?", MsgBoxStyle.OkOnly, "Registration failure")
            Me.ConnectToXabsl()
        End While
        ' First start a listener for Xabsl to connect to
        Me._Listener.StartListening()
        ' Then connect to the Xabsl listener
        Me.ConnectToXabsl()

        'start the thread
        Me.Start()

    End Sub

    Public Sub StopDevice() Implements ICommDevice.StopDevice

        'first stop the thread
        Me.Stop()

        Me._Listener.StopListening()

        Me.CloseAllConversations()

    End Sub


    Protected Overrides Sub Run()

        With Thread.CurrentThread
            If String.IsNullOrEmpty(.Name) Then
                .Name = Me._AgentName & " [XabslDevice]"
            End If
            .IsBackground = True
        End With

        While Me.IsRunning

            'request signal strength to all team members
            For Each name As String In Me._TeamMembers.Keys
                If Not name = Me._AgentName Then
                    Me.RequestSignalStrength(name)
                End If
            Next

            'query signal strength every other 10 seconds
            Thread.Sleep(TimeSpan.FromSeconds(10))

        End While

    End Sub


#End Region

#Region " Conversations "

    Private _Conversations As Dictionary(Of Integer, ICommConversation)

    Protected Sub OpenConversation(ByVal client As TcpClient, ByVal toRobot As String, ByVal cam As Boolean) Implements ICommDevice.OpenConversation

        'Called after a DNS-request (client-side)

        SyncLock Me._Conversations

            Try
                Dim otherIpPoint As IPEndPoint

                otherIpPoint = CType(client.Client.RemoteEndPoint, IPEndPoint)
                Console.WriteLine(String.Format("[CommActor] Connected to robot {0} on port {1}", toRobot, otherIpPoint.Port))

                Dim uniqueID As Guid = Guid.NewGuid
                Dim portID As Integer = otherIpPoint.Port

                'NICK
                Dim convID As Integer = portID
                If cam Then
                    convID += 100
                End If

                'Console.WriteLine("Opening conversation with id")
                'Console.WriteLine(convID)

                Dim conversation As New XabslConversation(Me._Owner, Me, convID, client, Me._AgentName, False) 'You can use False if JPEG, but because already compressed. Yet, doesnot seem to slow down.

                'NICK
                conversation.ConversationCam = cam

                Me._Conversations.Add(convID, conversation)
                conversation.StartConversation()

                Me._Owner.NotifyConversationStarted(conversation, toRobot)


            Catch ex As Exception
                Console.WriteLine(ex)

            End Try


        End SyncLock

    End Sub

    Protected Sub OpenConversation(ByVal client As TcpClient) Implements ICommDevice.OpenConversation

        ' Called when accepting a new client (server-side)
        SyncLock Me._Conversations

            Try
                Dim otherIpPoint As IPEndPoint

                otherIpPoint = CType(client.Client.RemoteEndPoint, IPEndPoint)
                Console.WriteLine(String.Format("Connected to client on port {0}", otherIpPoint.Port))

                Dim uniqueID As Guid = Guid.NewGuid
                Dim conversation As New XabslConversation(Me._Owner, Me, otherIpPoint.Port, client, Me._AgentName, True) 'Lets try, compression seems to take much processing time
                'NICK
                conversation.incoming = True
                Me._Conversations.Add(otherIpPoint.Port, conversation)
                conversation.StartConversation()


                Dim reply As String = ""

                'If Me.SendXabslCommand(String.Format("REVERSEDNS {{Port {0}}}{1}", otherIpPoint.Port, Environment.NewLine), True, reply) Then
                If Me.SendXabslCommand(String.Format("REVERSEDNS {{Port {0}}}", otherIpPoint.Port), True, reply) Then
                    Console.WriteLine(reply)
                    'expected REVERSEDNSREPLY {Port PortNumber} {Robot RobotName}
                    'when not use REVERSEDNSREPLY {Port PortNumber} {Error UnknownOrIllegalPort}

                    Dim start As Integer = reply.IndexOf("{Robot ") + 7
                    Dim until As Integer = reply.IndexOf("}", start)

                    'If no Robot in reply, the Index = -1 and start = 6

                    If start > 6 AndAlso until > start Then
                        Dim robotName As String = reply.Substring(start, until - start)

                        Console.WriteLine(String.Format("Connected to robot {0} via port {1}", robotName, otherIpPoint.Port))

                        'Arnoud store here!

                        Me._Owner.NotifyConversationStarted(conversation, robotName)
                        'We know the name, we do not know the position.

                    End If
                End If





            Catch ex As Exception
                Console.WriteLine(ex)

            End Try


        End SyncLock

    End Sub


    'Protected Sub OpenConversation(ByVal connection As TcpConnection) Implements ICommDevice.OpenConversation

    '    SyncLock Me._Conversations

    '        Try
    '            Dim uniqueID As Guid = Guid.NewGuid
    '            Dim conversation As New XabslConversation(Me._Owner, Me, uniqueID, connection, Me._AgentName, True)
    '            Me._Conversations.Add(uniqueID, conversation)
    '            conversation.StartConversation()

    '        Catch ex As Exception
    '            Console.WriteLine(ex)

    '        End Try

    '    End SyncLock

    '    'XABSL will automatically remove the registration of the listener 
    '    'for the agent that did NOT request the conversation. Since we 
    '    'don't keep track of who requested the conversation, we simply
    '    're-register the listener on both endpoints. For one of these
    '    'this registration will return a 'Already Registered' failure,
    '    'but that's ok.
    '    '[TODO: Does the 2008 XABSL still automatically remove the registration of the listener?]
    '    'Me.RegisterListenerPort()

    'End Sub

    Protected Sub CloseAllConversations()
        SyncLock Me._Conversations
            For Each conversation As ICommConversation In Me._Conversations.Values
                conversation.StopConversation()
            Next
            Me._Conversations.Clear()
        End SyncLock
    End Sub

#End Region

End Class
