''' <summary>
''' Will stop the robot upon activation.
''' </summary>
''' <remarks></remarks>
Public Class NoMotion
    Inherits Motion

    Public Sub New(ByVal control As MotionControl)
        MyBase.New(control)
    End Sub

    Protected Overrides Sub OnActivated()
        MyBase.OnActivated()
        Me.Control.Agent.Halt()
    End Sub

End Class
