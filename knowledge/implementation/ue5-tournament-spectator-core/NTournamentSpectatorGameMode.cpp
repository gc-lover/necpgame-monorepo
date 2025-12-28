// Tournament Spectator Game Mode Implementation
// Issue: #2213
// Enterprise-grade tournament spectator mode for NECPGAME MMOFPS RPG

#include "NTournamentSpectatorGameMode.h"
#include "NTournamentSpectatorPlayerController.h"
#include "SpectatorCameraManager.h"
#include "GameFramework/PlayerController.h"
#include "TimerManager.h"
#include "Engine/World.h"
#include "Kismet/GameplayStatics.h"

ANTournamentSpectatorGameMode::ANTournamentSpectatorGameMode()
    : Super()
    , bTournamentActive(false)
    , AverageSpectatorLatency(0.0f)
    , TotalSpectatorBandwidth(0)
{
    // Set default player controller class
    PlayerControllerClass = ANTournamentSpectatorPlayerController::StaticClass();

    // Configure tick for performance monitoring
    PrimaryActorTick.bCanEverTick = true;
    PrimaryActorTick.bStartWithTickEnabled = true;
    PrimaryActorTick.TickInterval = 1.0f; // Update every second
}

void ANTournamentSpectatorGameMode::InitGame(const FString& MapName, const FString& Options, FString& ErrorMessage)
{
    Super::InitGame(MapName, Options, ErrorMessage);

    // Parse tournament options from URL
    FString TournamentId;
    if (UGameplayStatics::ParseOption(Options, TEXT("TournamentId")) != "")
    {
        TournamentId = UGameplayStatics::ParseOption(Options, TEXT("TournamentId"));
        InitializeSpectatorSession(TournamentId);
    }

    // Initialize performance monitoring
    GetWorldTimerManager().SetTimer(CleanupTimerHandle, this,
        &ANTournamentSpectatorGameMode::CleanupInactiveSpectators, 30.0f, true);
}

void ANTournamentSpectatorGameMode::PostLogin(APlayerController* NewPlayer)
{
    Super::PostLogin(NewPlayer);

    if (ANTournamentSpectatorPlayerController* SpectatorPC = Cast<ANTournamentSpectatorPlayerController>(NewPlayer))
    {
        RegisterSpectator(SpectatorPC);

        // Initialize spectator with tournament data
        if (bTournamentActive)
        {
            SpectatorPC->InitializeSpectatorSession(CurrentTournamentId, CurrentMatchId);
        }
    }
}

void ANTournamentSpectatorGameMode::Logout(AController* Exiting)
{
    if (ANTournamentSpectatorPlayerController* SpectatorPC = Cast<ANTournamentSpectatorPlayerController>(Exiting))
    {
        UnregisterSpectator(SpectatorPC);
    }

    Super::Logout(Exiting);
}

void ANTournamentSpectatorGameMode::BeginPlay()
{
    Super::BeginPlay();

    // Initialize camera manager
    CameraManager = NewObject<USpectatorCameraManager>(this);
    if (CameraManager)
    {
        CameraManager->InitializeCameraManager();
    }

    // Initialize WebSocket connection for real-time updates
    InitializeWebSocketConnection();

    UE_LOG(LogTemp, Log, TEXT("Tournament Spectator Game Mode started"));
}

void ANTournamentSpectatorGameMode::Tick(float DeltaSeconds)
{
    Super::Tick(DeltaSeconds);

    // Update performance metrics
    UpdatePerformanceMetrics();

    // Validate spectator positions (anti-cheat)
    ValidateSpectatorPositions();
}

void ANTournamentSpectatorGameMode::EndPlay(const EEndPlayReason::Type EndPlayReason)
{
    // Cleanup resources
    if (CleanupTimerHandle.IsValid())
    {
        GetWorldTimerManager().ClearTimer(CleanupTimerHandle);
    }

    // End tournament session
    if (bTournamentActive)
    {
        EndTournamentSpectatorMode();
    }

    Super::EndPlay(EndPlayReason);
}

void ANTournamentSpectatorGameMode::InitializeSpectatorSession(const FString& TournamentId)
{
    CurrentTournamentId = TournamentId;
    bTournamentActive = true;

    // Initialize tournament data (would connect to backend service)
    UE_LOG(LogTemp, Log, TEXT("Initializing spectator session for tournament: %s"), *TournamentId);

    // Update all existing spectators
    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            Spectator->InitializeSpectatorSession(TournamentId, CurrentMatchId);
        }
    }
}

void ANTournamentSpectatorGameMode::StartTournamentSpectatorMode()
{
    if (!bTournamentActive)
    {
        UE_LOG(LogTemp, Warning, TEXT("Cannot start tournament spectator mode - no active tournament"));
        return;
    }

    UE_LOG(LogTemp, Log, TEXT("Starting tournament spectator mode"));

    // Initialize camera system
    if (CameraManager)
    {
        CameraManager->StartTournamentMode();
    }

    // Notify all spectators
    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            Spectator->OnTournamentStarted();
        }
    }
}

void ANTournamentSpectatorGameMode::EndTournamentSpectatorMode()
{
    bTournamentActive = false;

    UE_LOG(LogTemp, Log, TEXT("Ending tournament spectator mode"));

    // Cleanup camera system
    if (CameraManager)
    {
        CameraManager->EndTournamentMode();
    }

    // Notify all spectators
    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            Spectator->OnTournamentEnded();
        }
    }

    // Clear tournament data
    CurrentTournamentId.Empty();
    CurrentMatchId.Empty();
}

void ANTournamentSpectatorGameMode::UpdateTournamentData(const FString& TournamentData)
{
    // Parse and update tournament data
    // This would integrate with tournament service API

    UE_LOG(LogTemp, Log, TEXT("Updating tournament data"));

    // Notify spectators of tournament updates
    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            Spectator->UpdateTournamentData(TournamentData);
        }
    }
}

void ANTournamentSpectatorGameMode::UpdateMatchData(const FString& MatchId, const FString& MatchData)
{
    CurrentMatchId = MatchId;

    UE_LOG(LogTemp, Log, TEXT("Updating match data for: %s"), *MatchId);

    // Update camera targets based on match data
    TArray<AActor*> NewTargets;
    // Parse match data to extract player actors
    UpdateCameraTargets(NewTargets);

    // Notify spectators of match updates
    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            Spectator->UpdateMatchData(MatchId, MatchData);
        }
    }
}

void ANTournamentSpectatorGameMode::UpdatePlayerData(const TArray<FString>& PlayerIds)
{
    // Update camera targets for new players
    TArray<AActor*> NewTargets;
    for (const FString& PlayerId : PlayerIds)
    {
        // Find player actor by ID (would integrate with player service)
        AActor* PlayerActor = nullptr; // UGameplayStatics::GetPlayerPawn(GetWorld(), 0); // Placeholder
        if (PlayerActor)
        {
            NewTargets.Add(PlayerActor);
            CameraTargets.Add(PlayerId, PlayerActor);
        }
    }

    UpdateCameraTargets(NewTargets);
}

void ANTournamentSpectatorGameMode::RegisterSpectator(ANTournamentSpectatorPlayerController* Spectator)
{
    if (Spectator && !ActiveSpectators.Contains(Spectator))
    {
        ActiveSpectators.Add(Spectator);
        UE_LOG(LogTemp, Log, TEXT("Spectator registered. Total spectators: %d"), ActiveSpectators.Num());

        // Initialize spectator if tournament is active
        if (bTournamentActive)
        {
            Spectator->InitializeSpectatorSession(CurrentTournamentId, CurrentMatchId);
        }
    }
}

void ANTournamentSpectatorGameMode::UnregisterSpectator(ANTournamentSpectatorPlayerController* Spectator)
{
    if (Spectator)
    {
        ActiveSpectators.Remove(Spectator);
        UE_LOG(LogTemp, Log, TEXT("Spectator unregistered. Total spectators: %d"), ActiveSpectators.Num());
    }
}

void ANTournamentSpectatorGameMode::SetGlobalCameraMode(const FString& CameraMode)
{
    if (CameraManager)
    {
        CameraManager->SetGlobalCameraMode(CameraMode);
    }

    // Notify all spectators
    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            Spectator->OnCameraModeChanged(CameraMode);
        }
    }
}

void ANTournamentSpectatorGameMode::UpdateCameraTargets(const TArray<AActor*>& NewTargets)
{
    if (CameraManager)
    {
        CameraManager->UpdateCameraTargets(NewTargets);
    }
}

void ANTournamentSpectatorGameMode::InitializeWebSocketConnection()
{
    // Initialize WebSocket connection for real-time tournament updates
    // This would connect to the tournament service WebSocket endpoint

    UE_LOG(LogTemp, Log, TEXT("Initializing WebSocket connection for tournament updates"));
    // Implementation would use WebSockets module
}

void ANTournamentSpectatorGameMode::HandleTournamentUpdate(const FString& UpdateData)
{
    // Process real-time tournament updates
    UpdateTournamentData(UpdateData);
}

void ANTournamentSpectatorGameMode::HandleMatchEvent(const FString& EventData)
{
    // Process real-time match events
    // Parse event type and update accordingly

    UE_LOG(LogTemp, Log, TEXT("Processing match event: %s"), *EventData);
}

void ANTournamentSpectatorGameMode::UpdatePerformanceMetrics()
{
    // Update spectator performance metrics
    AverageSpectatorLatency = 0.0f;
    TotalSpectatorBandwidth = 0;

    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            AverageSpectatorLatency += Spectator->GetLatency();
            TotalSpectatorBandwidth += Spectator->GetBandwidthUsage();
        }
    }

    if (ActiveSpectators.Num() > 0)
    {
        AverageSpectatorLatency /= ActiveSpectators.Num();
    }

    // Log performance warnings
    if (AverageSpectatorLatency > 500.0f)
    {
        UE_LOG(LogTemp, Warning, TEXT("High spectator latency detected: %.2f ms"), AverageSpectatorLatency);
    }
}

void ANTournamentSpectatorGameMode::ValidateSpectatorPositions()
{
    // Anti-cheat: Validate spectator camera positions
    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            Spectator->ValidateSpectatorPosition();
        }
    }
}

void ANTournamentSpectatorGameMode::PreventStreamSniping()
{
    // Prevent spectators from getting unfair viewing angles
    for (ANTournamentSpectatorPlayerController* Spectator : ActiveSpectators)
    {
        if (Spectator)
        {
            Spectator->ApplyAntiSnipingMeasures();
        }
    }
}

void ANTournamentSpectatorGameMode::CleanupInactiveSpectators()
{
    // Remove disconnected spectators
    for (int32 i = ActiveSpectators.Num() - 1; i >= 0; --i)
    {
        ANTournamentSpectatorPlayerController* Spectator = ActiveSpectators[i];
        if (!Spectator || !Spectator->IsValidLowLevel() || Spectator->IsPendingKill())
        {
            ActiveSpectators.RemoveAt(i);
            UE_LOG(LogTemp, Log, TEXT("Cleaned up inactive spectator. Total spectators: %d"), ActiveSpectators.Num());
        }
    }
}
