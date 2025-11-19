param(
  [string]$Url = "ws://127.0.0.1:18081/ws",
  [string]$Token = ""
)
Add-Type -AssemblyName System.Net.Http
Add-Type -AssemblyName System.Net.WebSockets

$uri = if ([string]::IsNullOrWhiteSpace($Token)) { $Url } else { "$Url?token=$Token" }
$ws = [System.Net.WebSockets.ClientWebSocket]::new()
$cts = [System.Threading.CancellationTokenSource]::new()

$ws.ConnectAsync([Uri]$uri, $cts.Token).GetAwaiter().GetResult()

$msg = "hello"
$bytes = [System.Text.Encoding]::UTF8.GetBytes($msg)
$seg = New-Object System.ArraySegment[byte] (,$bytes)
$ws.SendAsync($seg, [System.Net.WebSockets.WebSocketMessageType]::Text, $true, $cts.Token).GetAwaiter().GetResult()

$recv = New-Object byte[] 4096
$rseg = New-Object System.ArraySegment[byte] (,$recv)
$res = $ws.ReceiveAsync($rseg, $cts.Token).GetAwaiter().GetResult()
if ($res.Count -gt 0) {
  $text = [System.Text.Encoding]::UTF8.GetString($recv,0,$res.Count)
  Write-Output "WS response: $text"
} else {
  Write-Output "WS received empty frame"
}

$ws.CloseAsync([System.Net.WebSockets.WebSocketCloseStatus]::NormalClosure,"bye",$cts.Token).GetAwaiter().GetResult()


