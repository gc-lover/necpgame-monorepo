param(
  [string]$HostName = "127.0.0.1",
  [int]$Port = 18080,
  [string]$Message = "echo"
)
$udp = New-Object System.Net.Sockets.UdpClient
$udp.Client.ReceiveTimeout = 2000
$remote = New-Object System.Net.IPEndPoint ([System.Net.IPAddress]::Parse($HostName)), $Port
$bytes = [System.Text.Encoding]::UTF8.GetBytes($Message)
[void]$udp.Send($bytes, $bytes.Length, $HostName, $Port)
try {
  $sender = New-Object System.Net.IPEndPoint([System.Net.IPAddress]::Any,0)
  $resp = $udp.Receive([ref]$sender)
  $text = [System.Text.Encoding]::UTF8.GetString($resp)
  Write-Output "UDP echo response: $text"
} catch {
  Write-Output "No UDP response (timeout)"
} finally {
  $udp.Close()
}


