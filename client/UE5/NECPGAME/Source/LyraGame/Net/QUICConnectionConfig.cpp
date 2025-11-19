#include "Net/QUICConnectionConfig.h"

UQUICConnectionConfig::UQUICConnectionConfig()
{
	ServerAddress = TEXT("127.0.0.1");
	ServerPort = 18080;
	HeartbeatInterval = 1.0f;
	ConnectionTimeout = 5.0f;
}

