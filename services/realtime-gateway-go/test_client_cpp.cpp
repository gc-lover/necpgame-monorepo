#include <iostream>
#include <string>
#include <thread>
#include <chrono>
#include <msquic.h>

const QUIC_API_TABLE* MsQuic = nullptr;
HQUIC Registration = nullptr;
HQUIC Configuration = nullptr;
HQUIC Connection = nullptr;
bool Connected = false;

QUIC_STATUS QUIC_API ConnectionCallback(HQUIC Connection, void* Context, QUIC_CONNECTION_EVENT* Event)
{
    switch (Event->Type)
    {
    case QUIC_CONNECTION_EVENT_CONNECTED:
        std::cout << "✓ Connection established!" << std::endl;
        Connected = true;
        return QUIC_STATUS_SUCCESS;

    case QUIC_CONNECTION_EVENT_SHUTDOWN_INITIATED_BY_TRANSPORT:
        std::cout << "✗ Connection shutdown by transport" << std::endl;
        Connected = false;
        return QUIC_STATUS_SUCCESS;

    case QUIC_CONNECTION_EVENT_SHUTDOWN_INITIATED_BY_PEER:
        std::cout << "✗ Connection shutdown by peer" << std::endl;
        Connected = false;
        return QUIC_STATUS_SUCCESS;

    case QUIC_CONNECTION_EVENT_SHUTDOWN_COMPLETE:
        std::cout << "Connection shutdown complete" << std::endl;
        Connected = false;
        return QUIC_STATUS_SUCCESS;

    default:
        return QUIC_STATUS_SUCCESS;
    }
}

int main()
{
    std::cout << "MsQuic Test Client" << std::endl;
    std::cout << "==================" << std::endl;

    // Open MsQuic
    QUIC_STATUS Status = MsQuicOpenVersion(2, (const void**)&MsQuic);
    if (QUIC_FAILED(Status) || !MsQuic)
    {
        std::cerr << "Failed to open MsQuic: 0x" << std::hex << Status << std::endl;
        return 1;
    }
    std::cout << "✓ MsQuic opened" << std::endl;

    // Create registration
    const QUIC_REGISTRATION_CONFIG RegConfig = { "test-client", QUIC_EXECUTION_PROFILE_LOW_LATENCY };
    Status = MsQuic->RegistrationOpen(&RegConfig, &Registration);
    if (QUIC_FAILED(Status))
    {
        std::cerr << "Failed to create registration: 0x" << std::hex << Status << std::endl;
        MsQuicClose(MsQuic);
        return 1;
    }
    std::cout << "✓ Registration created" << std::endl;

    // Create configuration
    const QUIC_BUFFER Alpn = { sizeof("h3") - 1, (uint8_t*)"h3" };
    QUIC_SETTINGS Settings = { 0 };
    Settings.IdleTimeoutMs = 30000;
    Settings.IsSet.IdleTimeoutMs = TRUE;
    Settings.HandshakeIdleTimeoutMs = 10000;
    Settings.IsSet.HandshakeIdleTimeoutMs = TRUE;

    Status = MsQuic->ConfigurationOpen(Registration, &Alpn, 1, &Settings, sizeof(Settings), nullptr, &Configuration);
    if (QUIC_FAILED(Status))
    {
        std::cerr << "Failed to create configuration: 0x" << std::hex << Status << std::endl;
        MsQuic->RegistrationClose(Registration);
        MsQuicClose(MsQuic);
        return 1;
    }
    std::cout << "✓ Configuration created" << std::endl;

    // Load credentials with NO_CERTIFICATE_VALIDATION flag
    QUIC_CREDENTIAL_CONFIG CredConfig = { 0 };
    CredConfig.Type = QUIC_CREDENTIAL_TYPE_NONE;
    CredConfig.Flags = QUIC_CREDENTIAL_FLAG_CLIENT | QUIC_CREDENTIAL_FLAG_NO_CERTIFICATE_VALIDATION;

    Status = MsQuic->ConfigurationLoadCredential(Configuration, &CredConfig);
    if (QUIC_FAILED(Status))
    {
        std::cerr << "Failed to load credentials: 0x" << std::hex << Status << std::endl;
        MsQuic->ConfigurationClose(Configuration);
        MsQuic->RegistrationClose(Registration);
        MsQuicClose(MsQuic);
        return 1;
    }
    std::cout << "✓ Credentials loaded" << std::endl;

    // Open connection
    Status = MsQuic->ConnectionOpen(Registration, ConnectionCallback, nullptr, &Connection);
    if (QUIC_FAILED(Status))
    {
        std::cerr << "Failed to open connection: 0x" << std::hex << Status << std::endl;
        MsQuic->ConfigurationClose(Configuration);
        MsQuic->RegistrationClose(Registration);
        MsQuicClose(MsQuic);
        return 1;
    }
    std::cout << "✓ Connection opened" << std::endl;

    // Start connection
    const char* ServerName = "127.0.0.1";
    uint16_t ServerPort = 18080;
    QUIC_ADDRESS_FAMILY AddressFamily = QUIC_ADDRESS_FAMILY_INET;

    std::cout << "Connecting to " << ServerName << ":" << ServerPort << "..." << std::endl;
    Status = MsQuic->ConnectionStart(Connection, Configuration, AddressFamily, ServerName, ServerPort);
    if (QUIC_FAILED(Status))
    {
        std::cerr << "✗ ConnectionStart failed: 0x" << std::hex << Status << std::endl;
        MsQuic->ConnectionClose(Connection);
        MsQuic->ConfigurationClose(Configuration);
        MsQuic->RegistrationClose(Registration);
        MsQuicClose(MsQuic);
        return 1;
    }
    std::cout << "✓ ConnectionStart called" << std::endl;

    // Wait for connection (up to 10 seconds)
    std::cout << "Waiting for connection..." << std::endl;
    for (int i = 0; i < 100; ++i)
    {
        std::this_thread::sleep_for(std::chrono::milliseconds(100));
        if (Connected)
        {
            std::cout << "✓ Successfully connected!" << std::endl;
            break;
        }
    }

    if (!Connected)
    {
        std::cout << "✗ Connection timeout" << std::endl;
    }

    // Cleanup
    if (Connection)
    {
        MsQuic->ConnectionClose(Connection);
    }
    if (Configuration)
    {
        MsQuic->ConfigurationClose(Configuration);
    }
    if (Registration)
    {
        MsQuic->RegistrationClose(Registration);
    }
    MsQuicClose(MsQuic);

    return Connected ? 0 : 1;
}

