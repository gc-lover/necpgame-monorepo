// Tournament Spectator Player Controller
// Issue: #2213
// Advanced spectator controls and camera management

#pragma once

#include "CoreMinimal.h"
#include "GameFramework/PlayerController.h"
#include "NTournamentSpectatorPlayerController.generated.h"

class USpectatorHUD;
class USpectatorCameraManager;

/**
 * Tournament Spectator Player Controller
 * Handles spectator input, camera controls, and UI management
 */
UCLASS()
class NECPGAME_API ANTournamentSpectatorPlayerController : public APlayerController
{
    GENERATED_BODY()

public:
    ANTournamentSpectatorPlayerController();

    //~ Begin APlayerController Interface
    virtual void SetupInputComponent() override;
    virtual void BeginPlay() override;
    virtual void Tick(float DeltaSeconds) override;
    //~ End APlayerController Interface

    // Spectator session management
    void InitializeSpectatorSession(const FString& TournamentId, const FString& MatchId);
    void OnTournamentStarted();
    void OnTournamentEnded();

    // Tournament data updates
    void UpdateTournamentData(const FString& TournamentData);
    void UpdateMatchData(const FString& MatchId, const FString& MatchData);

    // Camera controls
    void SwitchCameraMode(const FString& CameraMode);
    void OnCameraModeChanged(const FString& CameraMode);
    void SetFollowTarget(AActor* TargetActor);
    void CycleCameraTarget();

    // UI controls
    void ToggleSpectatorHUD();
    void ShowPlayerStats(const FString& PlayerId);
    void HidePlayerStats();

    // Chat system
    void SendSpectatorMessage(const FString& Message);
    void ToggleChatWindow();

    // Performance monitoring
    float GetLatency() const { return CurrentLatency; }
    int32 GetBandwidthUsage() const { return BandwidthUsage; }

    // Anti-cheat validation
    void ValidateSpectatorPosition();
    void ApplyAntiSnipingMeasures();

protected:
    // Input handlers
    void OnCameraModeSwitchPressed();
    void OnTargetCyclePressed();
    void OnHUDTogglePressed();
    void OnChatTogglePressed();
    void OnZoomInPressed();
    void OnZoomOutPressed();
    void OnFollowTargetPressed();

    // Camera movement
    void HandleCameraMovement(float DeltaTime);
    void UpdateCameraPosition(const FVector& NewPosition);
    bool ValidateCameraPosition(const FVector& Position);

private:
    // Spectator state
    FString CurrentTournamentId;
    FString CurrentMatchId;
    bool bSpectatorInitialized;

    // Camera system
    USpectatorCameraManager* CameraManager;
    FString CurrentCameraMode;
    AActor* CurrentFollowTarget;
    TArray<AActor*> AvailableTargets;
    int32 CurrentTargetIndex;

    // UI system
    USpectatorHUD* SpectatorHUD;
    bool bHUDVisible;

    // Network performance
    float CurrentLatency;
    int32 BandwidthUsage;
    FTimerHandle PerformanceTimerHandle;

    // Anti-cheat measures
    FVector LastValidatedPosition;
    float MaxViewingAngle;
    float MinCameraDistance;
    float MaxCameraDistance;

    // Input setup
    void SetupSpectatorInputBindings();

    // Performance monitoring
    void UpdatePerformanceMetrics();
    void ReportPerformanceData();

    // Camera validation
    bool IsValidViewingAngle(const FVector& CameraLocation, const FVector& TargetLocation);
    bool IsValidCameraDistance(float Distance);
    void ClampCameraToBoundaries(FVector& CameraLocation);

    // Tournament integration
    void ConnectToTournamentStream();
    void HandleTournamentEvent(const FString& EventType, const FString& EventData);

    // Memory management
    void CleanupSpectatorResources();
};
