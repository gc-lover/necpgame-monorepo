// Tournament Spectator Player Controller Implementation
// Issue: #2213
// Advanced spectator controls and camera management

#include "NTournamentSpectatorPlayerController.h"
#include "SpectatorHUD.h"
#include "SpectatorCameraManager.h"
#include "GameFramework/Pawn.h"
#include "Camera/CameraComponent.h"
#include "Kismet/GameplayStatics.h"
#include "TimerManager.h"

ANTournamentSpectatorPlayerController::ANTournamentSpectatorPlayerController()
    : Super()
    , bSpectatorInitialized(false)
    , CurrentTargetIndex(0)
    , bHUDVisible(true)
    , CurrentLatency(0.0f)
    , BandwidthUsage(0)
    , MaxViewingAngle(160.0f)
    , MinCameraDistance(200.0f)
    , MaxCameraDistance(5000.0f)
{
    // Enable spectator-specific features
    bShowMouseCursor = true;
    bEnableClickEvents = true;
    bEnableMouseOverEvents = true;
}

void ANTournamentSpectatorPlayerController::SetupInputComponent()
{
    Super::SetupInputComponent();

    if (InputComponent)
    {
        SetupSpectatorInputBindings();
    }
}

void ANTournamentSpectatorPlayerController::BeginPlay()
{
    Super::BeginPlay();

    // Initialize spectator systems
    CameraManager = NewObject<USpectatorCameraManager>(this);
    if (CameraManager)
    {
        CameraManager->InitializeForPlayer(this);
    }

    // Create spectator HUD
    SpectatorHUD = CreateWidget<USpectatorHUD>(this, USpectatorHUD::StaticClass());
    if (SpectatorHUD)
    {
        SpectatorHUD->AddToViewport();
        SpectatorHUD->SetVisibility(bHUDVisible ? ESlateVisibility::Visible : ESlateVisibility::Hidden);
    }

    // Start performance monitoring
    GetWorldTimerManager().SetTimer(PerformanceTimerHandle, this,
        &ANTournamentSpectatorPlayerController::UpdatePerformanceMetrics, 1.0f, true);

    UE_LOG(LogTemp, Log, TEXT("Spectator Player Controller initialized"));
}

void ANTournamentSpectatorPlayerController::Tick(float DeltaSeconds)
{
    Super::Tick(DeltaSeconds);

    // Update camera movement
    HandleCameraMovement(DeltaSeconds);

    // Validate spectator position (anti-cheat)
    ValidateSpectatorPosition();
}

void ANTournamentSpectatorPlayerController::InitializeSpectatorSession(const FString& TournamentId, const FString& MatchId)
{
    CurrentTournamentId = TournamentId;
    CurrentMatchId = MatchId;
    bSpectatorInitialized = true;

    UE_LOG(LogTemp, Log, TEXT("Initializing spectator session: Tournament=%s, Match=%s"),
        *TournamentId, *MatchId);

    // Connect to tournament stream
    ConnectToTournamentStream();

    // Initialize camera system
    if (CameraManager)
    {
        CameraManager->InitializeTournamentSession(TournamentId, MatchId);
    }

    // Update HUD
    if (SpectatorHUD)
    {
        SpectatorHUD->InitializeTournamentData(TournamentId, MatchId);
    }
}

void ANTournamentSpectatorPlayerController::OnTournamentStarted()
{
    UE_LOG(LogTemp, Log, TEXT("Tournament started for spectator"));

    if (SpectatorHUD)
    {
        SpectatorHUD->OnTournamentStarted();
    }

    // Switch to default camera mode
    SwitchCameraMode(TEXT("follow"));
}

void ANTournamentSpectatorPlayerController::OnTournamentEnded()
{
    UE_LOG(LogTemp, Log, TEXT("Tournament ended for spectator"));

    if (SpectatorHUD)
    {
        SpectatorHUD->OnTournamentEnded();
    }

    // Cleanup resources
    CleanupSpectatorResources();
}

void ANTournamentSpectatorPlayerController::UpdateTournamentData(const FString& TournamentData)
{
    if (SpectatorHUD)
    {
        SpectatorHUD->UpdateTournamentData(TournamentData);
    }

    // Update available camera targets
    // Parse tournament data to extract player information
    UpdateAvailableTargets();
}

void ANTournamentSpectatorPlayerController::UpdateMatchData(const FString& MatchId, const FString& MatchData)
{
    CurrentMatchId = MatchId;

    if (SpectatorHUD)
    {
        SpectatorHUD->UpdateMatchData(MatchId, MatchData);
    }

    // Update camera targets for new match
    UpdateAvailableTargets();
}

void ANTournamentSpectatorPlayerController::SwitchCameraMode(const FString& CameraMode)
{
    CurrentCameraMode = CameraMode;

    if (CameraManager)
    {
        CameraManager->SwitchCameraMode(CameraMode);
    }

    if (SpectatorHUD)
    {
        SpectatorHUD->UpdateCameraModeDisplay(CameraMode);
    }

    UE_LOG(LogTemp, Log, TEXT("Switched to camera mode: %s"), *CameraMode);
}

void ANTournamentSpectatorPlayerController::OnCameraModeChanged(const FString& CameraMode)
{
    SwitchCameraMode(CameraMode);
}

void ANTournamentSpectatorPlayerController::SetFollowTarget(AActor* TargetActor)
{
    CurrentFollowTarget = TargetActor;

    if (CameraManager)
    {
        CameraManager->SetFollowTarget(TargetActor);
    }

    if (SpectatorHUD)
    {
        SpectatorHUD->UpdateFollowTarget(TargetActor);
    }

    UE_LOG(LogTemp, Log, TEXT("Set follow target: %s"), TargetActor ? *TargetActor->GetName() : TEXT("None"));
}

void ANTournamentSpectatorPlayerController::CycleCameraTarget()
{
    if (AvailableTargets.Num() == 0)
    {
        return;
    }

    CurrentTargetIndex = (CurrentTargetIndex + 1) % AvailableTargets.Num();
    AActor* NextTarget = AvailableTargets[CurrentTargetIndex];

    SetFollowTarget(NextTarget);
}

void ANTournamentSpectatorPlayerController::ToggleSpectatorHUD()
{
    bHUDVisible = !bHUDVisible;

    if (SpectatorHUD)
    {
        SpectatorHUD->SetVisibility(bHUDVisible ? ESlateVisibility::Visible : ESlateVisibility::Hidden);
    }
}

void ANTournamentSpectatorPlayerController::ShowPlayerStats(const FString& PlayerId)
{
    if (SpectatorHUD)
    {
        SpectatorHUD->ShowPlayerStats(PlayerId);
    }
}

void ANTournamentSpectatorPlayerController::HidePlayerStats()
{
    if (SpectatorHUD)
    {
        SpectatorHUD->HidePlayerStats();
    }
}

void ANTournamentSpectatorPlayerController::SendSpectatorMessage(const FString& Message)
{
    // Send message through chat system
    // This would integrate with chat service

    if (SpectatorHUD)
    {
        SpectatorHUD->AddChatMessage(TEXT("You"), Message);
    }

    UE_LOG(LogTemp, Log, TEXT("Spectator message sent: %s"), *Message);
}

void ANTournamentSpectatorPlayerController::ToggleChatWindow()
{
    if (SpectatorHUD)
    {
        SpectatorHUD->ToggleChatWindow();
    }
}

void ANTournamentSpectatorPlayerController::SetupSpectatorInputBindings()
{
    // Camera controls
    InputComponent->BindAction("CameraModeSwitch", IE_Pressed, this, &ANTournamentSpectatorPlayerController::OnCameraModeSwitchPressed);
    InputComponent->BindAction("TargetCycle", IE_Pressed, this, &ANTournamentSpectatorPlayerController::OnTargetCyclePressed);
    InputComponent->BindAction("FollowTarget", IE_Pressed, this, &ANTournamentSpectatorPlayerController::OnFollowTargetPressed);

    // UI controls
    InputComponent->BindAction("HUDToggle", IE_Pressed, this, &ANTournamentSpectatorPlayerController::OnHUDTogglePressed);
    InputComponent->BindAction("ChatToggle", IE_Pressed, this, &ANTournamentSpectatorPlayerController::OnChatTogglePressed);

    // Camera movement
    InputComponent->BindAction("ZoomIn", IE_Pressed, this, &ANTournamentSpectatorPlayerController::OnZoomInPressed);
    InputComponent->BindAction("ZoomOut", IE_Pressed, this, &ANTournamentSpectatorPlayerController::OnZoomOutPressed);

    // Mouse controls
    InputComponent->BindAxis("MoveForward", this, &ANTournamentSpectatorPlayerController::AddPitchInput);
    InputComponent->BindAxis("MoveRight", this, &ANTournamentSpectatorPlayerController::AddYawInput);
}

void ANTournamentSpectatorPlayerController::OnCameraModeSwitchPressed()
{
    // Cycle through camera modes
    if (CurrentCameraMode == TEXT("free"))
    {
        SwitchCameraMode(TEXT("follow"));
    }
    else if (CurrentCameraMode == TEXT("follow"))
    {
        SwitchCameraMode(TEXT("overview"));
    }
    else if (CurrentCameraMode == TEXT("overview"))
    {
        SwitchCameraMode(TEXT("cinematic"));
    }
    else
    {
        SwitchCameraMode(TEXT("free"));
    }
}

void ANTournamentSpectatorPlayerController::OnTargetCyclePressed()
{
    CycleCameraTarget();
}

void ANTournamentSpectatorPlayerController::OnFollowTargetPressed()
{
    // Toggle follow mode
    if (CurrentFollowTarget)
    {
        SetFollowTarget(nullptr);
    }
    else if (AvailableTargets.Num() > 0)
    {
        SetFollowTarget(AvailableTargets[0]);
    }
}

void ANTournamentSpectatorPlayerController::OnHUDTogglePressed()
{
    ToggleSpectatorHUD();
}

void ANTournamentSpectatorPlayerController::OnChatTogglePressed()
{
    ToggleChatWindow();
}

void ANTournamentSpectatorPlayerController::OnZoomInPressed()
{
    if (CameraManager)
    {
        CameraManager->ZoomIn();
    }
}

void ANTournamentSpectatorPlayerController::OnZoomOutPressed()
{
    if (CameraManager)
    {
        CameraManager->ZoomOut();
    }
}

void ANTournamentSpectatorPlayerController::HandleCameraMovement(float DeltaTime)
{
    if (!CameraManager || CurrentCameraMode != TEXT("free"))
    {
        return;
    }

    // Handle free camera movement
    FVector MovementInput = FVector::ZeroVector;

    if (InputComponent)
    {
        MovementInput.X = InputComponent->GetAxisValue(TEXT("MoveForward"));
        MovementInput.Y = InputComponent->GetAxisValue(TEXT("MoveRight"));
        MovementInput.Z = InputComponent->GetAxisValue(TEXT("MoveUp"));
    }

    if (!MovementInput.IsZero())
    {
        // Apply movement to camera
        FVector NewLocation = CameraManager->GetCameraLocation() + MovementInput * 500.0f * DeltaTime;

        // Validate position
        if (ValidateCameraPosition(NewLocation))
        {
            UpdateCameraPosition(NewLocation);
        }
    }
}

void ANTournamentSpectatorPlayerController::UpdateCameraPosition(const FVector& NewPosition)
{
    if (CameraManager)
    {
        CameraManager->SetCameraLocation(NewPosition);
    }
}

bool ANTournamentSpectatorPlayerController::ValidateCameraPosition(const FVector& Position)
{
    // Anti-cheat validation
    bool bValid = true;

    // Check distance from tournament area
    FVector TournamentCenter = FVector::ZeroVector; // Would be set from tournament data
    float DistanceFromCenter = FVector::Distance(Position, TournamentCenter);

    if (DistanceFromCenter > MaxCameraDistance)
    {
        bValid = false;
    }

    // Check viewing angle restrictions
    if (CurrentFollowTarget)
    {
        if (!IsValidViewingAngle(Position, CurrentFollowTarget->GetActorLocation()))
        {
            bValid = false;
        }
    }

    // Clamp to boundaries if invalid
    if (!bValid)
    {
        FVector ClampedPosition = Position;
        ClampCameraToBoundaries(ClampedPosition);
        UpdateCameraPosition(ClampedPosition);
    }

    LastValidatedPosition = bValid ? Position : LastValidatedPosition;
    return bValid;
}

void ANTournamentSpectatorPlayerController::ValidateSpectatorPosition()
{
    if (CameraManager)
    {
        FVector CurrentLocation = CameraManager->GetCameraLocation();
        ValidateCameraPosition(CurrentLocation);
    }
}

void ANTournamentSpectatorPlayerController::ApplyAntiSnipingMeasures()
{
    // Prevent stream sniping by limiting viewing angles
    if (CurrentFollowTarget)
    {
        FVector CameraLocation = CameraManager->GetCameraLocation();
        FVector TargetLocation = CurrentFollowTarget->GetActorLocation();

        if (!IsValidViewingAngle(CameraLocation, TargetLocation))
        {
            // Adjust camera to valid position
            FVector ValidLocation = CalculateValidCameraPosition(TargetLocation);
            UpdateCameraPosition(ValidLocation);
        }
    }
}

bool ANTournamentSpectatorPlayerController::IsValidViewingAngle(const FVector& CameraLocation, const FVector& TargetLocation)
{
    FVector ToTarget = (TargetLocation - CameraLocation).GetSafeNormal();
    FVector CameraForward = GetControlRotation().Vector();

    float Angle = FMath::Acos(FVector::DotProduct(ToTarget, CameraForward));
    Angle = FMath::RadiansToDegrees(Angle);

    return Angle <= MaxViewingAngle;
}

bool ANTournamentSpectatorPlayerController::IsValidCameraDistance(float Distance)
{
    return Distance >= MinCameraDistance && Distance <= MaxCameraDistance;
}

void ANTournamentSpectatorPlayerController::ClampCameraToBoundaries(FVector& CameraLocation)
{
    // Clamp camera position to tournament boundaries
    FVector TournamentCenter = FVector::ZeroVector; // Would be set from tournament data
    FVector Direction = (CameraLocation - TournamentCenter).GetSafeNormal();

    float Distance = FVector::Distance(CameraLocation, TournamentCenter);
    Distance = FMath::Clamp(Distance, MinCameraDistance, MaxCameraDistance);

    CameraLocation = TournamentCenter + Direction * Distance;
}

FVector ANTournamentSpectatorPlayerController::CalculateValidCameraPosition(const FVector& TargetLocation)
{
    // Calculate a valid camera position that doesn't violate anti-sniping rules
    FVector Direction = (TargetLocation - LastValidatedPosition).GetSafeNormal();
    float Distance = FMath::Clamp(FVector::Distance(TargetLocation, LastValidatedPosition),
                                  MinCameraDistance, MaxCameraDistance);

    return TargetLocation - Direction * Distance;
}

void ANTournamentSpectatorPlayerController::UpdatePerformanceMetrics()
{
    // Update network performance metrics
    // This would integrate with network profiling systems

    CurrentLatency = FMath::RandRange(50.0f, 200.0f); // Placeholder
    BandwidthUsage = FMath::RandRange(500000, 2000000); // Placeholder: 0.5-2 Mbps

    // Report performance data periodically
    static float TimeSinceLastReport = 0.0f;
    TimeSinceLastReport += 1.0f;

    if (TimeSinceLastReport >= 60.0f) // Every minute
    {
        ReportPerformanceData();
        TimeSinceLastReport = 0.0f;
    }
}

void ANTournamentSpectatorPlayerController::ReportPerformanceData()
{
    // Report performance metrics to monitoring system
    UE_LOG(LogTemp, Log, TEXT("Spectator Performance - Latency: %.2fms, Bandwidth: %d bps"),
        CurrentLatency, BandwidthUsage);

    // This would send data to analytics service
}

void ANTournamentSpectatorPlayerController::ConnectToTournamentStream()
{
    // Connect to tournament WebSocket stream
    // This would integrate with tournament service

    UE_LOG(LogTemp, Log, TEXT("Connecting to tournament stream: %s"), *CurrentTournamentId);
}

void ANTournamentSpectatorPlayerController::HandleTournamentEvent(const FString& EventType, const FString& EventData)
{
    // Handle real-time tournament events
    if (EventType == TEXT("match_start"))
    {
        // Update match data
        UpdateMatchData(CurrentMatchId, EventData);
    }
    else if (EventType == TEXT("player_eliminated"))
    {
        // Update player stats
        if (SpectatorHUD)
        {
            SpectatorHUD->UpdatePlayerEliminated(EventData);
        }
    }
    else if (EventType == TEXT("tournament_end"))
    {
        // Handle tournament end
        OnTournamentEnded();
    }
}

void ANTournamentSpectatorPlayerController::UpdateAvailableTargets()
{
    // Update list of available camera targets
    AvailableTargets.Empty();

    // This would query the game state for active players
    // Placeholder: Add some dummy targets
    TArray<AActor*> FoundActors;
    UGameplayStatics::GetAllActorsOfClass(GetWorld(), AActor::StaticClass(), FoundActors);

    for (AActor* Actor : FoundActors)
    {
        // Filter for player characters
        if (Actor->ActorHasTag(FName("Player")))
        {
            AvailableTargets.Add(Actor);
        }
    }

    UE_LOG(LogTemp, Log, TEXT("Updated available camera targets: %d"), AvailableTargets.Num());
}

void ANTournamentSpectatorPlayerController::CleanupSpectatorResources()
{
    // Cleanup spectator-specific resources
    if (PerformanceTimerHandle.IsValid())
    {
        GetWorldTimerManager().ClearTimer(PerformanceTimerHandle);
    }

    CurrentTournamentId.Empty();
    CurrentMatchId.Empty();
    bSpectatorInitialized = false;

    UE_LOG(LogTemp, Log, TEXT("Spectator resources cleaned up"));
}
