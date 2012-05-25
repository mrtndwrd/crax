Module LoadEngine

    Declare Sub TestFunc Lib "..\..\..\..\Release\XabslEngine.dll" ()
    Sub Main()
        TestFunc()
        MsgBox("Gezien?")
    End Sub

End Module
