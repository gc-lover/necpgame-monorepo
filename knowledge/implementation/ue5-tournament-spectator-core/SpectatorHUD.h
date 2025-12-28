// Spectator HUD
// Issue: #2213
// Advanced spectator interface for tournament viewing

#pragma once

#include "CoreMinimal.h"
#include "GameFramework/HUD.h"
#include "SpectatorHUD.generated.h"

class AActor;

/**
 * Spectator HUD
 * Displays tournament information, camera controls, and spectator interface
 */
UCLASS()
class NECPGAME_API ASpectatorHUD : public AHUD
{
    GENERATED_BODY()

public:
    ASpectatorHUD();

    //~ Begin AHUD Interface
    virtual void DrawHUD() override;
    virtual void BeginPlay() override;
    //~ End AHUD Interface

    // Tournament data management
    void InitializeTournamentData(const FString& TournamentId, const FString& MatchId);
    void UpdateTournamentData(const FString& TournamentData);
    void UpdateMatchData(const FString& MatchId, const FString& MatchData);

    // Tournament lifecycle
    void OnTournamentStarted();
    void OnTournamentEnded();

    // Camera mode display
    void UpdateCameraModeDisplay(const FString& CameraMode);
    void UpdateFollowTarget(AActor* TargetActor);

    // Player statistics
    void ShowPlayerStats(const FString& PlayerId);
    void HidePlayerStats();
    void UpdatePlayerStats(const FString& PlayerId, const FString& StatsData);
    void UpdatePlayerEliminated(const FString& PlayerData);

    // Chat system
    void AddChatMessage(const FString& Sender, const FString& Message);
    void ToggleChatWindow();

    // HUD controls
    void ShowHUD();
    void HideHUD();

protected:
    // Drawing functions
    void DrawTournamentInfo();
    void DrawCameraControls();
    void DrawPlayerStats();
    void DrawChatWindow();
    void DrawMinimap();
    void DrawSpectatorControls();

    // UI layout
    void UpdateLayout();
    void CalculateWidgetPositions();

private:
    // Tournament data
    FString CurrentTournamentId;
    FString CurrentMatchId;
    FString TournamentData;
    FString MatchData;

    // HUD state
    bool bHUDVisible;
    bool bChatWindowVisible;
    bool bPlayerStatsVisible;

    // Camera information
    FString CurrentCameraMode;
    FString CurrentFollowTargetName;

    // Player stats
    FString CurrentPlayerStatsId;
    FString CurrentPlayerStatsData;

    // Chat system
    TArray<FString> ChatMessages;
    int32 MaxChatMessages;

    // UI layout constants
    float HUDPadding;
    float WidgetSpacing;
    FVector2D ScreenCenter;
    FVector2D ScreenSize;

    // Font and colors
    UFont* HUDFont;
    FLinearColor TextColor;
    FLinearColor BackgroundColor;
    FLinearColor AccentColor;

    // Animation state
    float HUDOpacity;
    float ChatWindowOpacity;
    float PlayerStatsOpacity;
    float AnimationSpeed;

    // Utility functions
    void DrawTextWithBackground(const FString& Text, const FVector2D& Position,
                               const FLinearColor& TextColor, const FLinearColor& BackgroundColor);
    void DrawProgressBar(const FVector2D& Position, const FVector2D& Size,
                        float Progress, const FLinearColor& FillColor, const FLinearColor& BackgroundColor);
    void DrawButton(const FString& ButtonText, const FVector2D& Position,
                   bool bHighlighted, const FLinearColor& TextColor);

    // Data parsing
    void ParseTournamentData(const FString& Data);
    void ParseMatchData(const FString& Data);
    void ParsePlayerStats(const FString& Data);

    // Performance monitoring
    void UpdateHUDPerformance();
    float LastHUDUpdateTime;
};
