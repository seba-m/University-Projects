#AutoIt3Wrapper_Run_Au3Stripper=y
#Au3Stripper_Parameters=/so /rm /pe
#AutoIt3Wrapper_Res_HiDpi=y

#pragma compile(Out, "Mathematica Pointer.exe")
#pragma compile(x64, True)
#pragma compile(UPX, False)
#pragma compile(ProductName, "Mathematica Pointer")
#pragma compile(FileDescription, "Mathematica Pointer")
#pragma compile(LegalCopyright, "Copyright (C) Sebastian Morgado")
#pragma compile(FileVersion, 1.0.5)
#pragma compile(ProductVersion, 1.0.5.0)
#pragma compile(OriginalFilename, "Mathematica Pointer.exe")
#pragma compile(AutoItExecuteAllowed, True)
;#NoTrayIcon

#RequireAdmin
#include <MetroGUI_UDF.au3>
#include <_GUIDisable.au3>
#include <GUIConstants.au3>
#include <GUIConstantsEx.au3>
#include <WindowsConstants.au3>
#include <Misc.au3>
#include <Array.au3>

HotKeySet("{ESC}", "_salir")

;velocidad del mouse

Global $deltax = 100
Global $deltay = 100
Global $delayxd = 1

_Interfaz()

Func _Interfaz()

	_Metro_EnableHighDPIScaling()
	_SetTheme("DarkGreenV2")
	$Mathematica_Pointer = _Metro_CreateGUI("Mathematica Pointer ~ by Sebastian Morgado", 390, 180, -1, -1, False)
	$Control_Buttons = _Metro_AddControlButtons(True, True, True, False, False)
	$GUI_CLOSE_BUTTON = $Control_Buttons[0]
	$GUI_MAXIMIZE_BUTTON = $Control_Buttons[1]
	$GUI_RESTORE_BUTTON = $Control_Buttons[2]
	$GUI_MINIMIZE_BUTTON = $Control_Buttons[3]
	$GUI_FULLSCREEN_BUTTON = $Control_Buttons[4]
	$GUI_FSRestore_BUTTON = $Control_Buttons[5]
	$GUI_MENU_BUTTON = $Control_Buttons[6]
	$xdxd = GUICtrlCreateLabel("Mathematica Pointer (c) seba morgado", 10, 10, 220, 40)
	GUICtrlSetFont(-1, 8.5, 1000)
	GUICtrlSetColor(-1, 0xFF0000)
	DllCall("UxTheme.dll", "int", "SetWindowTheme", "hwnd", GUICtrlGetHandle($xdxd), "wstr", 0, "wstr", 0)

	;opciones

	$grupo1 = GUICtrlCreateGroup("lista de ips disponibles", 10, 30, 370, 140)
	GUICtrlSetFont(-1, 8.5, 1000)
	GUICtrlSetColor(-1, 0xFF0000)
	DllCall("UxTheme.dll", "int", "SetWindowTheme", "hwnd", GUICtrlGetHandle($grupo1), "wstr", 0, "wstr", 0)
	GUICtrlSetFont(-1, 8.5, 1000)
	GUICtrlSetColor(-1, 0xFF0000)
	$ip1 = _Metro_CreateRadioEx("1", "ip 1 = " & @IPAddress1, 15, 45, 180, 25)
	$ip2 = _Metro_CreateRadioEx("1", "ip 2 = " & @IPAddress2, 15, 75, 180, 25)
	$ip3 = _Metro_CreateRadioEx("1", "ip 3 = " & @IPAddress3, 15, 105, 180, 25)
	$ip4 = _Metro_CreateRadioEx("1", "ip 4 = " & @IPAddress4, 15, 135, 180, 25)
	_Metro_RadioCheck("1", $ip1)
	$version = GUICtrlCreateLabel("version 1.0.0", 280, 140, 90)
	GUICtrlSetFont(-1, 12, 400)
	GUICtrlSetColor(-1, 0xFFFFFF)
	$ip1xd = True
	$ip2xd = False
	$ip3xd = False
	$ip4xd = False
	$boton_iniciar = _Metro_CreateButtonEx2("iniciar", 270, 80, 100, 50)
	GUISetState(@SW_SHOW)

	While 1
		$nMsg = GUIGetMsg()
		Switch $nMsg
			Case $GUI_EVENT_CLOSE, $GUI_CLOSE_BUTTON
				_Metro_GUIDelete($Mathematica_Pointer)
				Exit
			Case $GUI_MINIMIZE_BUTTON
				GUISetState(@SW_MINIMIZE, $Mathematica_Pointer)
			Case $ip1
				_Metro_RadioCheck(1, $ip1)
				$ip1xd = True
				$ip2xd = False
				$ip3xd = False
				$ip4xd = False
			Case $ip2
				_Metro_RadioCheck(1, $ip2)
				$ip1xd = False
				$ip2xd = True
				$ip3xd = False
				$ip4xd = False
			Case $ip3
				_Metro_RadioCheck(1, $ip3)
				$ip1xd = False
				$ip2xd = False
				$ip3xd = True
				$ip4xd = False
			Case $ip4
				_Metro_RadioCheck(1, $ip4)
				$ip1xd = False
				$ip2xd = False
				$ip3xd = False
				$ip4xd = True
			Case $boton_iniciar
				If $ip1xd = True Then
					$dirIP = @IPAddress1
					_Metro_GUIDelete($Mathematica_Pointer)
					_conectar($dirIP)
				ElseIf $ip2xd = True Then
					$dirIP = @IPAddress2
					_Metro_GUIDelete($Mathematica_Pointer)
					_conectar($dirIP)
				ElseIf $ip3xd = True Then
					$dirIP = @IPAddress3
					_Metro_GUIDelete($Mathematica_Pointer)
					_conectar($dirIP)
				ElseIf $ip4xd = True Then
					$dirIP = @IPAddress4
					_Metro_GUIDelete($Mathematica_Pointer)
					_conectar($dirIP)
				Else
					_Metro_MsgBox(0, "ERROR!", "no se que ha sucedido, pero este error no deberia haber ocurrido, intenta iniciar de nuevo la aplicación o reiniciando el computador.)", 400, 11, $Mathematica_Pointer)
					_Metro_GUIDelete($Mathematica_Pointer)
					Exit
				EndIf
		EndSwitch
	WEnd
EndFunc   ;==>_Interfaz

Func _conectar($direccionip)

	Dim $sRootDir = @ScriptDir & "\www\"
	Dim $IP = $direccionip
	Dim $Port = 80
	Dim $Max_Users = 1
	Dim $Socket[$Max_Users]
	Dim $Buffer[$Max_Users]
	$Socket[0] = -1
	$mousexd = False
	TCPStartup()

	$MainSocket = TCPListen($IP, $Port)

	If @error Then
		MsgBox(0x20, "ERROR!", "No puedo crear un socket en el puerto " & $Port & " :c")
		Exit
	EndIf

	TrayTip("", "El servidor ha sido creado en http://" & $IP & "/", 10)

	While 1
		$NewSocket = TCPAccept($MainSocket)
		If $NewSocket >= 0 Then
			For $x = 0 To UBound($Socket) - 1
				If $Socket[$x] = -1 Then
					$Socket[$x] = $NewSocket
					ExitLoop
				EndIf
			Next
		EndIf

		For $x = 0 To UBound($Socket) - 1
			If $Socket[$x] = -1 Then ContinueLoop
			$NewData = TCPRecv($Socket[$x], 1024)
			If @error Then
				$Socket[$x] = -1
				ContinueLoop
			ElseIf $NewData Then
				$Buffer[$x] &= $NewData
				If StringInStr(StringStripCR($Buffer[$x]), @LF & @LF) Then
					$FirstLine = StringLeft($Buffer[$x], StringInStr($Buffer[$x], @LF))
					$RequestType = StringLeft($FirstLine, StringInStr($FirstLine, " ") - 1)
					If $RequestType = "GET" Then
						Global $Request = StringTrimRight(StringTrimLeft($FirstLine, 5), 11)
						;aqui estan las acciones
						If $Request = "arriba" Then 
							Send("{UP}")
						ElseIf $Request = "abajo" Then 
							Send("{DOWN}")
						ElseIf $Request = "izquierda" Then 
							Send("{LEFT}")
						ElseIf $Request = "derecha" Then 
							Send("{RIGHT}")
						ElseIf $Request = "salir" Then 
							If ProcessExists("Mathematica.exe") Then
								ProcessClose("Mathematica.exe")
							EndIf
						ElseIf $Request = "exit" Then 
							Exit
						ElseIf $Request = "diapanterior" Then
							Send("{PGUP}")
						ElseIf $Request = "sigdiapositiva" Then
							Send("{PGDN}")
						ElseIf $Request = "closecell" Then 
							Send("{CTRLDOWN}")
							Send("{SHIFTDOWN}")
							Send("{]}")
							Send("{SHIFTUP}")
							Send("{CTRLUP}")
						ElseIf $Request = "opencell" Then 
							Send("{CTRLDOWN}")
							Send("{SHIFTDOWN}")
							Send("{[}")
							Send("{SHIFTUP}")
							Send("{CTRLUP}")
						ElseIf $Request = "enter" Then 
							Send("{SHIFTDOWN}")
							Send("{ENTER}")
							Send("{SHIFTUP}")
						ElseIf $Request = "zoom1" Then 
							Send("{CTRLDOWN}")
							MouseWheel("up", 1)
							Send("{CTRLUP}")
						ElseIf $Request = "zoom2" Then 
							Send("{CTRLDOWN}")
							MouseWheel("down", 1)
							Send("{CTRLUP}")
						ElseIf $Request = "up" Then 
							MouseWheel("up", 12)
						ElseIf $Request = "down" Then 
							MouseWheel("down", 12)
						ElseIf $Request = "cd" Then 
							MouseClick($MOUSE_CLICK_RIGHT)

						ElseIf $Request = "ci" Then 
							MouseClick($MOUSE_CLICK_LEFT)

						ElseIf $Request = "mouse1" Then 
							If $mousexd = False Then
								$mousexd = True
							EndIf
						ElseIf $Request = "mouse2" Then 
							If $mousexd = True Then
								$mousexd = False
							EndIf

						ElseIf $Request = "" Then
						ElseIf $mousexd = True Then
							$posicion = StringSplit($Request, ",")

							Dim $posx = $posicion[1]
							Dim $posy = $posicion[2]

							If ($posx < 16) And ($posy < 15) And ($mousexd = True) Then
								$oldpos = MouseGetPos()
								MouseMove($oldpos[0] - ($posx * $delayxd), $oldpos[1] - ($posy * $delayxd))

							ElseIf ($posx = 16) And ($posy < 15) And ($mousexd = True) Then
								$oldpos = MouseGetPos()
								MouseMove($oldpos[0], $oldpos[1] - ($posy * $delayxd))

							ElseIf ($posx > 16) And ($posy < 15) And ($mousexd = True) Then
								$oldpos = MouseGetPos()
								MouseMove($oldpos[0] + ($posx * $delayxd), $oldpos[1] - ($posy * $delayxd))

							ElseIf ($posx < 16) And ($posy = 15) And ($mousexd = True) Then
								$oldpos = MouseGetPos()
								MouseMove($oldpos[0] - ($posx * $delayxd), $oldpos[1])

							ElseIf ($posx = 16) And ($posy = 15) And ($mousexd = True) Then
								;nada xd

							ElseIf ($posx > 16) And ($posy = 15) And ($mousexd = True) Then
								$oldpos = MouseGetPos()
								MouseMove($oldpos[0] + ($posx * $delayxd), $oldpos[1])

							ElseIf ($posx < 16) And ($posy > 15) And ($mousexd = True) Then
								$oldpos = MouseGetPos()
								MouseMove($oldpos[0] - ($posx * $delayxd), $oldpos[1] + ($posy * $delayxd))

							ElseIf ($posx = 16) And ($posy > 15) And ($mousexd = True) Then
								$oldpos = MouseGetPos()
								MouseMove($oldpos[0], $oldpos[1] + ($posy * $delayxd))

							ElseIf ($posx > 16) And ($posy > 15) And ($mousexd = True) Then
								$oldpos = MouseGetPos()
								MouseMove($oldpos[0] + ($posx * $delayxd), $oldpos[1] + ($posy * $delayxd))

							EndIf
						EndIf
						_Response($Socket[$x])
					EndIf
				EndIf
				If $Request = "/" Then
					$Request = "/index.html"
				EndIf
				$Request = StringReplace($Request, "/", "\")
				If FileExists($sRootDir & "\" & $Request) Then
					$sFileType = StringRight($Request, 4)
					Switch $sFileType
						Case "html"
							_SendFile($sRootDir & "\" & $Request, "text/html", $Socket[$x])
						Case ".htm"
							_SendFile($sRootDir & "\" & $Request, "text/html", $Socket[$x])
						Case ".jpg"
							_SendFile($sRootDir & "\" & $Request, "image/jpeg", $Socket[$x])
						Case "jpeg"
							_SendFile($sRootDir & "\" & $Request, "image/jpeg", $Socket[$x])
						Case ".png"
							_SendFile($sRootDir & "\" & $Request, "image/png", $Socket[$x])
					EndSwitch
				ElseIf $RequestType = "POST" Then
					$_POST = _Get_Post($Buffer[$x])
					$Name = StringReplace(DllStructGetData($_POST, 'Name'), '%', '')
					For $t = 0 To @extended
						$Find_Char = StringLeft(StringTrimLeft($Name, StringInStr($Name, '%')), 2)
						$Name = StringReplace($Name, '%' & $Find_Char, Chr(Dec($Find_Char)))
					Next
					$Comment = StringReplace(DllStructGetData($_POST, 'Comment'), '+', ' ')
					StringReplace($Comment, '%', '')
					For $t = 0 To @extended
						$Find_Char = StringLeft(StringTrimLeft($Comment, StringInStr($Comment, '%')), 2)
						$Comment = StringReplace($Comment, '%' & $Find_Char, Chr(Dec($Find_Char)))
					Next

					If $Name <> '' And $Comment <> '' Then
						$File_Data = StringReplace(FileRead($sRootDir & '\index.html'), '</TABLE>', '')
						FileDelete($sRootDir & '\index.html')
						FileWrite($sRootDir & '\index.html', $File_Data & '<td>' & $Name & ': <td>' & $Comment & '<tr>' & @CRLF & '</TABLE>')
					EndIf
					_SendFile($sRootDir & "\index.html", "text/html", $Socket[$x])
				EndIf
				$Buffer[$x] = ""
				TCPCloseSocket($Socket[$x])
				$Socket[$x] = -1
			EndIf
		Next

	WEnd
EndFunc   ;==>_conectar

Func _SendHTML($sHTML, $sSocket)
	$iLen = StringLen($sHTML)
	$sPacket = Binary("HTTP/1.1 200 OK" & @CRLF & _
			"Server: ManadarX/1.0 (" & @OSVersion & ") AutoIt " & @AutoItVersion & @CRLF & _
			"Connection: close" & @CRLF & _
			"Content-Lenght: " & $iLen & @CRLF & _
			"Content-Type: text/html/url; charset=WINDOWS- 1250" & @CRLF & @CRLF & $sHTML)
	$sSplit = StringSplit($sPacket, "")
	$sPacket = ""
	For $i = 1 To $sSplit[0]
		If Asc($sSplit[$i]) <> 0 Then
			$sPacket = $sPacket & $sSplit[$i]
		EndIf
	Next
	TCPSend($sSocket, $sPacket)
EndFunc   ;==>_SendHTML

Func _SendFile($sAddress, $sType, $sSocket)
	$File = FileOpen($sAddress, 16)
	$sImgBuffer = FileRead($File)
	FileClose($File)

	$Packet = Binary("HTTP/1.1 200 OK" & @CRLF & _
			"Server: ManadarX/1.3.26 (" & @OSVersion & ") AutoIt " & @AutoItVersion & @CRLF & _
			"Connection: close" & @CRLF & _
			"Content-Type: " & $sType & @CRLF & _
			@CRLF)
	TCPSend($sSocket, $Packet)

	While BinaryLen($sImgBuffer)
		$a = TCPSend($sSocket, $sImgBuffer)
		$sImgBuffer = BinaryMid($sImgBuffer, $a + 1, BinaryLen($sImgBuffer) - $a)
	WEnd

	$Packet = Binary(@CRLF & _
			@CRLF)
	TCPSend($sSocket, $Packet)
	TCPCloseSocket($sSocket)
EndFunc   ;==>_SendFile

Func _Response($sSocket)
	_SendHTML($Request, $sSocket)
EndFunc   ;==>_Response

Func _Get_Post($s_Buffer)
	Local $s_Temp_Post, $s_Post_Data
	Local $Temp, $s_Struct, $s_Len
	$s_Temp_Post = StringTrimLeft($s_Buffer, StringInStr($s_Buffer, 'Content-Length:'))
	$s_Len = StringTrimLeft($s_Temp_Post, StringInStr($s_Temp_Post, ': '))
	$s_Post_Data = StringSplit(StringRight($s_Buffer, $s_Len), '&')
	For $t = 1 To $s_Post_Data[0]
		$Temp = StringSplit($s_Post_Data[$t], '=')
		$s_Struct &= 'char ' & $Temp[1] & '[' & StringLen($Temp[2]) + 1 & '];'
	Next
	$s_Temp_Post = DllStructCreate($s_Struct)
	For $t = 1 To $s_Post_Data[0]
		$Temp = StringSplit($s_Post_Data[$t], '=')
		DllStructSetData($s_Temp_Post, $Temp[1], $Temp[2])
	Next
	Return $s_Temp_Post
EndFunc   ;==>_Get_Post

Func _salir()
	Exit
EndFunc   ;==>_salir
