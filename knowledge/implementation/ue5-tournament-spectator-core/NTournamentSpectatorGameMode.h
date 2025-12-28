// Tournament Spectator Game Mode
// Issue: #2213
// Enterprise-grade tournament spectator mode for NECPGAME MMOFPS RPG

#pragma once

#include "CoreMinimal.h"
#include "GameFramework/GameModeBase.h"
#include "NTournamentSpectatorGameMode.generated.h"

class ANTournamentSpectatorPlayerController;
class USpectatorCameraManager;

/**
 * Tournament Spectator Game Mode
 * Manages spectator sessions for tournament viewing
 */
UCLASS()
class NECPGAME_API ANTournamentSpectatorGameMode : public AGameModeBase
{
    GENERATED_BODY()

public:
    ANTournamentSpectatorGameMode();

    //~ Begin AGameModeBase Interface
    virtual void InitGame(const FString& MapName, const FString& Options, FString& ErrorMessage) override;
    virtual void PostLogin(APlayerController* NewPlayer) override;
    virtual void Logout(AController* Exiting) override;
    //~ End AGameModeBase Interface

    // Spectator session management
    void InitializeSpectatorSession(const FString& TournamentId);
    void StartTournamentSpectatorMode();
    void EndTournamentSpectatorMode();

    // Tournament data management
    void UpdateTournamentData(const FString& TournamentData);
    void UpdateMatchData(const FString& MatchId, const FString& MatchData);
    void UpdatePlayerData(const TArray<FString>& PlayerIds);

    // Spectator management
    void RegisterSpectator(ANTournamentSpectatorPlayerController* Spectator);
    void UnregisterSpectator(ANTournamentSpectatorPlayerController* Spectator);
    int32 GetSpectatorCount() const { return ActiveSpectators.Num(); }

    // Camera management
    void SetGlobalCameraMode(const FString& CameraMode);
    void UpdateCameraTargets(const TArray<AActor*>& NewTargets);

protected:
    //~ Begin AActor Interface
    virtual void BeginPlay() override;
    virtual void Tick(float DeltaSeconds) override;
    virtual void EndPlay(const EEndPlayReason::Type EndPlayReason) override;
    //~ End AActor Interface

private:
    // Tournament session data
    FString CurrentTournamentId;
    FString CurrentMatchId;
    bool bTournamentActive;

    // Spectator management
    TArray<ANTournamentSpectatorPlayerController*> ActiveSpectators;
    TMap<FString, AActor*> CameraTargets;

    // Systems
    USpectatorCameraManager* CameraManager;

    // Network/WebSocket connection for real-time updates
    void InitializeWebSocketConnection();
    void HandleTournamentUpdate(const FString& UpdateData);
    void HandleMatchEvent(const FString& EventData);

    // Performance monitoring
    void UpdatePerformanceMetrics();
    float AverageSpectatorLatency;
    int32 TotalSpectatorBandwidth;

    // Anti-cheat measures
    void ValidateSpectatorPositions();
    void PreventStreamSniping();

    // Memory management
    void CleanupInactiveSpectators();
    FTimerHandle CleanupTimerHandle;
};
