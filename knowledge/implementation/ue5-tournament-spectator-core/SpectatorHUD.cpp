// Spectator HUD Implementation
// Issue: #2213
// Advanced spectator interface for tournament viewing

#include "SpectatorHUD.h"
#include "Engine/Canvas.h"
#include "Engine/Font.h"
#include "Kismet/GameplayStatics.h"

ASpectatorHUD::ASpectatorHUD()
    : bHUDVisible(true)
    , bChatWindowVisible(false)
    , bPlayerStatsVisible(false)
    , MaxChatMessages(50)
    , HUDPadding(10.0f)
    , WidgetSpacing(5.0f)
    , HUDOpacity(1.0f)
    , ChatWindowOpacity(0.0f)
    , PlayerStatsOpacity(0.0f)
    , AnimationSpeed(5.0f)
    , LastHUDUpdateTime(0.0f)
{
    // Initialize colors
    TextColor = FLinearColor::White;
    BackgroundColor = FLinearColor(0.0f, 0.0f, 0.0f, 0.7f);
    AccentColor = FLinearColor::Blue;

    // Load default font
    static ConstructorHelpers::FObjectFinder<UFont> FontAsset(TEXT("/Engine/EngineFonts/Roboto.Roboto"));
    if (FontAsset.Succeeded())
    {
        HUDFont = FontAsset.Object;
    }
}

void ASpectatorHUD::BeginPlay()
{
    Super::BeginPlay();

    // Initialize screen dimensions
    if (GEngine && GEngine->GameViewport)
    {
        GEngine->GameViewport->GetViewportSize(ScreenSize);
        ScreenCenter = ScreenSize * 0.5f;
    }

    UE_LOG(LogTemp, Log, TEXT("Spectator HUD initialized"));
}

void ASpectatorHUD::DrawHUD()
{
    Super::DrawHUD();

    if (!bHUDVisible && HUDOpacity <= 0.0f)
    {
        return;
    }

    // Update layout
    UpdateLayout();

    // Update animations
    UpdateHUDPerformance();

    // Draw HUD elements
    DrawTournamentInfo();
    DrawCameraControls();

    if (bPlayerStatsVisible)
    {
        DrawPlayerStats();
    }

    if (bChatWindowVisible)
    {
        DrawChatWindow();
    }

    DrawSpectatorControls();
    DrawMinimap();
}

void ASpectatorHUD::InitializeTournamentData(const FString& TournamentId, const FString& MatchId)
{
    CurrentTournamentId = TournamentId;
    CurrentMatchId = MatchId;

    // Parse initial data (would come from tournament service)
    ParseTournamentData("");
    ParseMatchData("");

    UE_LOG(LogTemp, Log, TEXT("HUD initialized for tournament: %s, match: %s"), *TournamentId, *MatchId);
}

void ASpectatorHUD::UpdateTournamentData(const FString& TournamentData)
{
    TournamentData = TournamentData;
    ParseTournamentData(TournamentData);
}

void ASpectatorHUD::UpdateMatchData(const FString& MatchId, const FString& MatchData)
{
    CurrentMatchId = MatchId;
    MatchData = MatchData;
    ParseMatchData(MatchData);
}

void ASpectatorHUD::OnTournamentStarted()
{
    // Show tournament start notification
    AddChatMessage(TEXT("SYSTEM"), TEXT("Tournament has started!"));

    UE_LOG(LogTemp, Log, TEXT("Tournament started notification"));
}

void ASpectatorHUD::OnTournamentEnded()
{
    // Show tournament end notification
    AddChatMessage(TEXT("SYSTEM"), TEXT("Tournament has ended!"));

    // Reset HUD state
    bPlayerStatsVisible = false;
    CurrentPlayerStatsData.Empty();

    UE_LOG(LogTemp, Log, TEXT("Tournament ended notification"));
}

void ASpectatorHUD::UpdateCameraModeDisplay(const FString& CameraMode)
{
    CurrentCameraMode = CameraMode;

    // Show camera mode change notification
    FString Notification = FString::Printf(TEXT("Camera mode: %s"), *CameraMode);
    AddChatMessage(TEXT("SYSTEM"), Notification);
}

void ASpectatorHUD::UpdateFollowTarget(AActor* TargetActor)
{
    if (TargetActor)
    {
        CurrentFollowTargetName = TargetActor->GetName();

        FString Notification = FString::Printf(TEXT("Following: %s"), *CurrentFollowTargetName);
        AddChatMessage(TEXT("SYSTEM"), Notification);
    }
    else
    {
        CurrentFollowTargetName.Empty();
        AddChatMessage(TEXT("SYSTEM"), TEXT("Stopped following"));
    }
}

void ASpectatorHUD::ShowPlayerStats(const FString& PlayerId)
{
    CurrentPlayerStatsId = PlayerId;
    bPlayerStatsVisible = true;
    PlayerStatsOpacity = 0.0f; // Start fade in

    // Request player stats (would call tournament service)
    UpdatePlayerStats(PlayerId, "");
}

void ASpectatorHUD::HidePlayerStats()
{
    bPlayerStatsVisible = false;
    CurrentPlayerStatsId.Empty();
    CurrentPlayerStatsData.Empty();
}

void ASpectatorHUD::UpdatePlayerStats(const FString& PlayerId, const FString& StatsData)
{
    if (CurrentPlayerStatsId == PlayerId)
    {
        CurrentPlayerStatsData = StatsData;
        ParsePlayerStats(StatsData);
    }
}

void ASpectatorHUD::UpdatePlayerEliminated(const FString& PlayerData)
{
    // Parse elimination data and show notification
    FString PlayerName = TEXT("Unknown Player"); // Would parse from PlayerData
    FString Notification = FString::Printf(TEXT("Player eliminated: %s"), *PlayerName);
    AddChatMessage(TEXT("SYSTEM"), Notification);
}

void ASpectatorHUD::AddChatMessage(const FString& Sender, const FString& Message)
{
    FString ChatLine = FString::Printf(TEXT("[%s] %s"), *Sender, *Message);
    ChatMessages.Add(ChatLine);

    // Keep only recent messages
    if (ChatMessages.Num() > MaxChatMessages)
    {
        ChatMessages.RemoveAt(0);
    }

    UE_LOG(LogTemp, Log, TEXT("Chat message added: %s"), *ChatLine);
}

void ASpectatorHUD::ToggleChatWindow()
{
    bChatWindowVisible = !bChatWindowVisible;

    if (bChatWindowVisible)
    {
        ChatWindowOpacity = 0.0f; // Start fade in
    }
}

void ASpectatorHUD::ShowHUD()
{
    bHUDVisible = true;
    HUDOpacity = 0.0f; // Start fade in
}

void ASpectatorHUD::HideHUD()
{
    bHUDVisible = false;
}

void ASpectatorHUD::DrawTournamentInfo()
{
    if (!Canvas || HUDOpacity <= 0.0f)
    {
        return;
    }

    FVector2D Position(HUDPadding, HUDPadding);
    FLinearColor BGColor = BackgroundColor;
    BGColor.A *= HUDOpacity;

    // Tournament title
    FString TournamentTitle = FString::Printf(TEXT("Tournament: %s"), *CurrentTournamentId);
    DrawTextWithBackground(TournamentTitle, Position, TextColor, BGColor);
    Position.Y += 30.0f;

    // Match info
    FString MatchInfo = FString::Printf(TEXT("Match: %s"), *CurrentMatchId);
    DrawTextWithBackground(MatchInfo, Position, TextColor, BGColor);
    Position.Y += 25.0f;

    // Camera mode
    FString CameraInfo = FString::Printf(TEXT("Camera: %s"), *CurrentCameraMode);
    DrawTextWithBackground(CameraInfo, Position, TextColor, BGColor);
    Position.Y += 25.0f;

    // Follow target
    if (!CurrentFollowTargetName.IsEmpty())
    {
        FString FollowInfo = FString::Printf(TEXT("Following: %s"), *CurrentFollowTargetName);
        DrawTextWithBackground(FollowInfo, Position, TextColor, BGColor);
    }
}

void ASpectatorHUD::DrawCameraControls()
{
    if (!Canvas || HUDOpacity <= 0.0f)
    {
        return;
    }

    FVector2D Position(ScreenSize.X - 200.0f, HUDPadding);
    FLinearColor BGColor = BackgroundColor;
    BGColor.A *= HUDOpacity;

    // Camera controls
    DrawTextWithBackground(TEXT("Camera Controls:"), Position, AccentColor, BGColor);
    Position.Y += 25.0f;

    DrawTextWithBackground(TEXT("Tab - Switch Mode"), Position, TextColor, BGColor);
    Position.Y += 20.0f;

    DrawTextWithBackground(TEXT("C - Cycle Target"), Position, TextColor, BGColor);
    Position.Y += 20.0f;

    DrawTextWithBackground(TEXT("F - Toggle Follow"), Position, TextColor, BGColor);
    Position.Y += 20.0f;

    DrawTextWithBackground(TEXT("[/] - Zoom"), Position, TextColor, BGColor);
}

void ASpectatorHUD::DrawPlayerStats()
{
    if (!Canvas || PlayerStatsOpacity <= 0.0f || !bPlayerStatsVisible)
    {
        return;
    }

    FVector2D Position(ScreenCenter.X - 150.0f, ScreenCenter.Y - 100.0f);
    FLinearColor BGColor = BackgroundColor;
    BGColor.A *= PlayerStatsOpacity;

    // Player stats panel
    DrawTextWithBackground(TEXT("Player Statistics"), Position, AccentColor, BGColor);
    Position.Y += 30.0f;

    if (!CurrentPlayerStatsData.IsEmpty())
    {
        // Parse and display stats (simplified)
        TArray<FString> StatsLines;
        CurrentPlayerStatsData.ParseIntoArray(StatsLines, TEXT("\n"));

        for (const FString& StatLine : StatsLines)
        {
            if (!StatLine.IsEmpty())
            {
                DrawTextWithBackground(StatLine, Position, TextColor, BGColor);
                Position.Y += 20.0f;
            }
        }
    }
    else
    {
        DrawTextWithBackground(TEXT("Loading stats..."), Position, TextColor, BGColor);
    }
}

void ASpectatorHUD::DrawChatWindow()
{
    if (!Canvas || ChatWindowOpacity <= 0.0f || !bChatWindowVisible)
    {
        return;
    }

    FVector2D Position(HUDPadding, ScreenSize.Y - 250.0f);
    FVector2D ChatSize(400.0f, 200.0f);

    FLinearColor BGColor = BackgroundColor;
    BGColor.A *= ChatWindowOpacity;

    // Draw chat background
    FCanvasTileItem TileItem(Position, FVector2D(ChatSize.X, ChatSize.Y), BGColor);
    TileItem.BlendMode = SE_BLEND_Translucent;
    Canvas->DrawItem(TileItem);

    // Draw chat border
    FCanvasLineItem BorderLine(Position, Position + FVector2D(ChatSize.X, 0.0f));
    BorderLine.SetColor(AccentColor);
    BorderLine.LineThickness = 2.0f;
    Canvas->DrawItem(BorderLine);

    // Draw chat messages
    FVector2D MessagePosition = Position + FVector2D(HUDPadding, HUDPadding);
    int32 MessagesToShow = FMath::Min(10, ChatMessages.Num());

    for (int32 i = ChatMessages.Num() - MessagesToShow; i < ChatMessages.Num(); ++i)
    {
        if (ChatMessages.IsValidIndex(i))
        {
            DrawTextWithBackground(ChatMessages[i], MessagePosition, TextColor, FLinearColor::Transparent);
            MessagePosition.Y += 18.0f;
        }
    }
}

void ASpectatorHUD::DrawMinimap()
{
    if (!Canvas || HUDOpacity <= 0.0f)
    {
        return;
    }

    FVector2D Position(ScreenSize.X - 200.0f, ScreenSize.Y - 200.0f);
    FVector2D MinimapSize(180.0f, 180.0f);

    FLinearColor BGColor = BackgroundColor;
    BGColor.A *= HUDOpacity * 0.8f;

    // Draw minimap background
    FCanvasTileItem TileItem(Position, MinimapSize, BGColor);
    TileItem.BlendMode = SE_BLEND_Translucent;
    Canvas->DrawItem(TileItem);

    // Draw minimap border
    FCanvasLineItem BorderLine(Position, Position + FVector2D(MinimapSize.X, 0.0f));
    BorderLine.SetColor(AccentColor);
    BorderLine.LineThickness = 1.0f;
    Canvas->DrawItem(BorderLine);

    // Draw minimap title
    FVector2D TitlePosition = Position + FVector2D(5.0f, 5.0f);
    DrawTextWithBackground(TEXT("Tournament Map"), TitlePosition, AccentColor, FLinearColor::Transparent);
}

void ASpectatorHUD::DrawSpectatorControls()
{
    if (!Canvas || HUDOpacity <= 0.0f)
    {
        return;
    }

    FVector2D Position(ScreenSize.X - 200.0f, ScreenSize.Y - 50.0f);
    FLinearColor BGColor = BackgroundColor;
    BGColor.A *= HUDOpacity;

    // Spectator controls
    DrawTextWithBackground(TEXT("H - Toggle HUD"), Position, TextColor, BGColor);
    Position.Y += 20.0f;

    DrawTextWithBackground(TEXT("T - Toggle Chat"), Position, TextColor, BGColor);
}

void ASpectatorHUD::UpdateLayout()
{
    if (GEngine && GEngine->GameViewport)
    {
        GEngine->GameViewport->GetViewportSize(ScreenSize);
        ScreenCenter = ScreenSize * 0.5f;
    }
}

void ASpectatorHUD::DrawTextWithBackground(const FString& Text, const FVector2D& Position,
                                          const FLinearColor& TextColor, const FLinearColor& BackgroundColor)
{
    if (!Canvas)
    {
        return;
    }

    // Draw background if specified
    if (BackgroundColor.A > 0.0f)
    {
        FVector2D TextSize = Canvas->TextSize(HUDFont, Text, 1.0f);
        FCanvasTileItem BackgroundTile(Position, TextSize + FVector2D(4.0f, 4.0f), BackgroundColor);
        BackgroundTile.BlendMode = SE_BLEND_Translucent;
        Canvas->DrawItem(BackgroundTile);
    }

    // Draw text
    FCanvasTextItem TextItem(Position + FVector2D(2.0f, 2.0f), FText::FromString(Text), HUDFont, TextColor);
    Canvas->DrawItem(TextItem);
}

void ASpectatorHUD::DrawProgressBar(const FVector2D& Position, const FVector2D& Size,
                                   float Progress, const FLinearColor& FillColor, const FLinearColor& BackgroundColor)
{
    if (!Canvas)
    {
        return;
    }

    // Draw background
    FCanvasTileItem BackgroundTile(Position, Size, BackgroundColor);
    BackgroundTile.BlendMode = SE_BLEND_Translucent;
    Canvas->DrawItem(BackgroundTile);

    // Draw fill
    FVector2D FillSize(Size.X * Progress, Size.Y);
    FCanvasTileItem FillTile(Position, FillSize, FillColor);
    FillTile.BlendMode = SE_BLEND_Translucent;
    Canvas->DrawItem(FillTile);

    // Draw border
    FCanvasLineItem BorderLine(Position, Position + FVector2D(Size.X, 0.0f));
    BorderLine.SetColor(FLinearColor::White);
    BorderLine.LineThickness = 1.0f;
    Canvas->DrawItem(BorderLine);
}

void ASpectatorHUD::ParseTournamentData(const FString& Data)
{
    // Parse tournament data from JSON/service response
    // This would deserialize tournament information
}

void ASpectatorHUD::ParseMatchData(const FString& Data)
{
    // Parse match data from JSON/service response
    // This would deserialize match information
}

void ASpectatorHUD::ParsePlayerStats(const FString& Data)
{
    // Parse player statistics from JSON/service response
    // This would deserialize player stats
}

void ASpectatorHUD::UpdateHUDPerformance()
{
    float CurrentTime = GetWorld()->GetTimeSeconds();
    float DeltaTime = CurrentTime - LastHUDUpdateTime;

    // Update opacity animations
    if (bHUDVisible && HUDOpacity < 1.0f)
    {
        HUDOpacity = FMath::Min(1.0f, HUDOpacity + DeltaTime * AnimationSpeed);
    }
    else if (!bHUDVisible && HUDOpacity > 0.0f)
    {
        HUDOpacity = FMath::Max(0.0f, HUDOpacity - DeltaTime * AnimationSpeed);
    }

    if (bChatWindowVisible && ChatWindowOpacity < 1.0f)
    {
        ChatWindowOpacity = FMath::Min(1.0f, ChatWindowOpacity + DeltaTime * AnimationSpeed);
    }
    else if (!bChatWindowVisible && ChatWindowOpacity > 0.0f)
    {
        ChatWindowOpacity = FMath::Max(0.0f, ChatWindowOpacity - DeltaTime * AnimationSpeed);
    }

    if (bPlayerStatsVisible && PlayerStatsOpacity < 1.0f)
    {
        PlayerStatsOpacity = FMath::Min(1.0f, PlayerStatsOpacity + DeltaTime * AnimationSpeed);
    }
    else if (!bPlayerStatsVisible && PlayerStatsOpacity > 0.0f)
    {
        PlayerStatsOpacity = FMath::Max(0.0f, PlayerStatsOpacity - DeltaTime * AnimationSpeed);
    }

    LastHUDUpdateTime = CurrentTime;
}
