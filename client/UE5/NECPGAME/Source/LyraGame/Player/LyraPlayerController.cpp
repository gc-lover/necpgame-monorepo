// Copyright Epic Games, Inc. All Rights Reserved.

#include "LyraPlayerController.h"
#include "AbilitySystem/LyraAbilitySystemComponent.h"
#include "AbilitySystemGlobals.h"
#include "Camera/LyraPlayerCameraManager.h"
#include "CommonInputSubsystem.h"
#include "CommonInputTypeEnum.h"
#include "Components/PrimitiveComponent.h"
#include "Engine/GameInstance.h"
#include "EngineUtils.h"
#include "GameFramework/Pawn.h"
#include "GameModes/LyraGameState.h"
#include "LyraCheatManager.h"
#include "LyraGameplayTags.h"
#include "LyraLocalPlayer.h"
#include "LyraLogChannels.h"
#include "LyraPlayerState.h"
#include "Net/UnrealNetwork.h"
#include "Settings/LyraSettingsLocal.h"
#include "Settings/LyraSettingsShared.h"
#include "UI/LyraHUD.h"

// Still disabled in UE 5.7 - GetObjectW API issue persists
// #include "Replays/LyraReplaySubsystem.h"
// #include "ReplaySubsystem.h"
#include "Character/LyraCharacter.h"
#include "Development/LyraDeveloperSettings.h"
#include "GameFramework/CharacterMovementComponent.h"
#include "GameFramework/Pawn.h"
#include "GameMapsSettings.h"
#include "Net/ProtobufCodec.h"
#include "Net/RealtimeWebSocketConnection.h"
#include "Tests/LyraGameplayRpcRegistrationComponent.h"

#if WITH_RPC_REGISTRY
#include "HttpServerModule.h"
#endif

#include UE_INLINE_GENERATED_CPP_BY_NAME(LyraPlayerController)

namespace Lyra {
namespace Input {
static int32 ShouldAlwaysPlayForceFeedback = 0;
static FAutoConsoleVariableRef CVarShouldAlwaysPlayForceFeedback(
    TEXT("LyraPC.ShouldAlwaysPlayForceFeedback"), ShouldAlwaysPlayForceFeedback,
    TEXT("Should force feedback effects be played, even if the last input "
         "device was not a gamepad?"));
} // namespace Input
} // namespace Lyra

ALyraPlayerController::ALyraPlayerController(
    const FObjectInitializer &ObjectInitializer)
    : Super(ObjectInitializer) {
  PlayerCameraManagerClass = ALyraPlayerCameraManager::StaticClass();

#if USING_CHEAT_MANAGER
  CheatClass = ULyraCheatManager::StaticClass();
#endif // #if USING_CHEAT_MANAGER

  // Create WebSocket Connection
  WebSocketConnection = CreateDefaultSubobject<URealtimeWebSocketConnection>(
      TEXT("WebSocketConnection"));
  UE_LOG(
      LogLyra, Log,
      TEXT("ALyraPlayerController::Constructor - WebSocketConnection created"));
}

void ALyraPlayerController::PreInitializeComponents() {
  Super::PreInitializeComponents();
}

void ALyraPlayerController::BeginPlay() {
  Super::BeginPlay();
#if WITH_RPC_REGISTRY
  FHttpServerModule::Get().StartAllListeners();
  int32 RpcPort = 0;
  if (FParse::Value(FCommandLine::Get(), TEXT("rpcport="), RpcPort)) {
    ULyraGameplayRpcRegistrationComponent *ObjectInstance =
        ULyraGameplayRpcRegistrationComponent::GetInstance();
    if (ObjectInstance && ObjectInstance->IsValidLowLevel()) {
      ObjectInstance->RegisterAlwaysOnHttpCallbacks();
      ObjectInstance->RegisterInMatchHttpCallbacks();
    }
  }
#endif
  SetActorHiddenInGame(false);

  UE_LOG(LogLyra, Log,
         TEXT("LyraPlayerController::BeginPlay - Starting WebSocket connection "
              "setup"));

  if (WebSocketConnection) {
    bool bIsLocal = IsLocalController();
    ENetMode NetMode = GetNetMode();
    UE_LOG(LogLyra, Log,
           TEXT("LyraPlayerController::BeginPlay - WebSocketConnection exists, "
                "IsLocalController: %d, NetMode: %d"),
           bIsLocal, (int32)NetMode);

    if (bIsLocal || NetMode == NM_Standalone || NetMode == NM_Client) {
      FString PlayerIdToSet;
      if (ALyraPlayerState *LyraPS = GetLyraPlayerState()) {
        FString OriginalId = LyraPS->GetUniqueId().ToString();
        PlayerIdToSet = GenerateShortPlayerId(OriginalId);
      } else if (APlayerState *PS = GetPlayerState<APlayerState>()) {
        FString OriginalId = PS->GetUniqueId().ToString();
        PlayerIdToSet = GenerateShortPlayerId(OriginalId);
      }
      
      if (!PlayerIdToSet.IsEmpty()) {
        WebSocketConnection->SetPlayerId(PlayerIdToSet);
      }
      
      FString ServerAddress = TEXT("127.0.0.1");
      int32 ServerPort = 18080;
      FString Token = TEXT("test-token");

      if (GConfig) {
        GConfig->GetString(TEXT("WebSocketConnection"), TEXT("ServerAddress"),
                           ServerAddress, GEngineIni);
        GConfig->GetInt(TEXT("WebSocketConnection"), TEXT("ServerPort"),
                        ServerPort, GEngineIni);
        UE_LOG(LogLyra, Log,
               TEXT("LyraPlayerController::BeginPlay - Config loaded: %s:%d"),
               *ServerAddress, ServerPort);
      } else {
        UE_LOG(LogLyra, Warning,
               TEXT("LyraPlayerController::BeginPlay - GConfig is NULL!"));
      }

      UE_LOG(LogLyra, Log,
             TEXT("LyraPlayerController::BeginPlay - Calling ConnectWithConfig "
                  "with token: %s, PlayerId: %s"),
             *Token, *PlayerIdToSet);
      WebSocketConnection->ConnectWithConfig(Token);

      WebSocketConnection->OnGameStateReceived.AddDynamic(
          this, &ALyraPlayerController::OnGameStateReceived);
      WebSocketConnection->OnConnected.AddDynamic(
          this, &ALyraPlayerController::OnWebSocketConnected);
      WebSocketConnection->OnDisconnected.AddDynamic(
          this, &ALyraPlayerController::OnWebSocketDisconnected);
    } else {
      UE_LOG(LogLyra, Log,
             TEXT("LyraPlayerController::BeginPlay - Skipping WebSocket "
                  "connection (IsLocal: %d, NetMode: %d)"),
             bIsLocal, (int32)NetMode);
    }
  } else {
    UE_LOG(
        LogLyra, Warning,
        TEXT("LyraPlayerController::BeginPlay - WebSocketConnection is NULL!"));
  }
}

void ALyraPlayerController::EndPlay(const EEndPlayReason::Type EndPlayReason) {
  Super::EndPlay(EndPlayReason);
}

void ALyraPlayerController::GetLifetimeReplicatedProps(
    TArray<FLifetimeProperty> &OutLifetimeProps) const {
  Super::GetLifetimeReplicatedProps(OutLifetimeProps);

  // Disable replicating the PC target view as it doesn't work well for replays
  // or client-side spectating. The engine TargetViewRotation is only set in
  // APlayerController::TickActor if the server knows ahead of time that a
  // specific pawn is being spectated and it only replicates down for
  // COND_OwnerOnly. In client-saved replays, COND_OwnerOnly is never true and
  // the target pawn is not always known at the time of recording. To support
  // client-saved replays, the replication of this was moved to
  // ReplicatedViewRotation and updated in PlayerTick.
  DISABLE_REPLICATED_PROPERTY(APlayerController, TargetViewRotation);
}

void ALyraPlayerController::ReceivedPlayer() {
  Super::ReceivedPlayer();
  UE_LOG(LogLyra, Log,
         TEXT("ALyraPlayerController::ReceivedPlayer - Player received, "
              "NetMode: %d"),
         (int32)GetNetMode());
}

void ALyraPlayerController::PlayerTick(float DeltaTime) {
  Super::PlayerTick(DeltaTime);

  // If we are auto running then add some player input
  if (GetIsAutoRunning()) {
    if (APawn *CurrentPawn = GetPawn()) {
      const FRotator MovementRotation(0.0f, GetControlRotation().Yaw, 0.0f);
      const FVector MovementDirection =
          MovementRotation.RotateVector(FVector::ForwardVector);
      CurrentPawn->AddMovementInput(MovementDirection, 1.0f);
    }
  }

  ALyraPlayerState *LyraPlayerState = GetLyraPlayerState();

  if (PlayerCameraManager && LyraPlayerState) {
    APawn *TargetPawn = PlayerCameraManager->GetViewTargetPawn();

    if (TargetPawn) {
      // Update view rotation on the server so it replicates
      if (HasAuthority() || TargetPawn->IsLocallyControlled()) {
        LyraPlayerState->SetReplicatedViewRotation(
            TargetPawn->GetViewRotation());
      }

      // Update the target view rotation if the pawn isn't locally controlled
      if (!TargetPawn->IsLocallyControlled()) {
        LyraPlayerState = TargetPawn->GetPlayerState<ALyraPlayerState>();
        if (LyraPlayerState) {
          // Get it from the spectated pawn's player state, which may not be the
          // same as the PC's playerstate
          TargetViewRotation = LyraPlayerState->GetReplicatedViewRotation();
        }
      }
    }
  }
}

ALyraPlayerState *ALyraPlayerController::GetLyraPlayerState() const {
  return CastChecked<ALyraPlayerState>(PlayerState,
                                       ECastCheckedType::NullAllowed);
}

ULyraAbilitySystemComponent *
ALyraPlayerController::GetLyraAbilitySystemComponent() const {
  const ALyraPlayerState *LyraPS = GetLyraPlayerState();
  return (LyraPS ? LyraPS->GetLyraAbilitySystemComponent() : nullptr);
}

ALyraHUD *ALyraPlayerController::GetLyraHUD() const {
  return CastChecked<ALyraHUD>(GetHUD(), ECastCheckedType::NullAllowed);
}

bool ALyraPlayerController::TryToRecordClientReplay() {
  // Still disabled in UE 5.7 - GetObjectW API issue persists
  return false;

  /*if (ShouldRecordClientReplay())
  {
          if (ULyraReplaySubsystem* ReplaySubsystem =
  GetGameInstance()->GetSubsystem<ULyraReplaySubsystem>())
          {
                  APlayerController* FirstLocalPlayerController =
  GetGameInstance()->GetFirstLocalPlayerController(); if
  (FirstLocalPlayerController == this)
                  {
                          if (ALyraGameState* GameState =
  Cast<ALyraGameState>(GetWorld()->GetGameState()))
                          {
                                  GameState->SetRecorderPlayerState(PlayerState);
                                  ReplaySubsystem->RecordClientReplay(this);
                                  return true;
                          }
                  }
          }
  }
  return false;*/
}

bool ALyraPlayerController::ShouldRecordClientReplay() {
  UWorld *World = GetWorld();
  UGameInstance *GameInstance = GetGameInstance();
  if (GameInstance != nullptr && World != nullptr &&
      !World->IsPlayingReplay() && !World->IsRecordingClientReplay() &&
      NM_DedicatedServer != GetNetMode() && IsLocalPlayerController()) {
    FString DefaultMap = UGameMapsSettings::GetGameDefaultMap();
    FString CurrentMap = World->URL.Map;

#if WITH_EDITOR
    CurrentMap = UWorld::StripPIEPrefixFromPackageName(
        CurrentMap, World->StreamingLevelsPrefix);
#endif
    if (CurrentMap == DefaultMap) {
      // Never record demos on the default frontend map, this could be replaced
      // with a better check for being in the main menu
      return false;
    }

    // Still disabled in UE 5.7 - GetObjectW API issue persists
    /*if (UReplaySubsystem* ReplaySubsystem =
    GameInstance->GetSubsystem<UReplaySubsystem>())
    {
            if (ReplaySubsystem->IsRecording() || ReplaySubsystem->IsPlaying())
            {
                    return false;
            }
    }*/

    // If this is possible, now check the settings
    if (const ULyraLocalPlayer *LyraLocalPlayer =
            Cast<ULyraLocalPlayer>(GetLocalPlayer())) {
      if (LyraLocalPlayer->GetLocalSettings()->ShouldAutoRecordReplays()) {
        return true;
      }
    }
  }
  return false;
}

void ALyraPlayerController::OnPlayerStateChangedTeam(UObject *TeamAgent,
                                                     int32 OldTeam,
                                                     int32 NewTeam) {
  ConditionalBroadcastTeamChanged(this, IntegerToGenericTeamId(OldTeam),
                                  IntegerToGenericTeamId(NewTeam));
}

void ALyraPlayerController::OnPlayerStateChanged() {
  // Empty, place for derived classes to implement without having to hook all
  // the other events
}

void ALyraPlayerController::BroadcastOnPlayerStateChanged() {
  OnPlayerStateChanged();

  // Unbind from the old player state, if any
  FGenericTeamId OldTeamID = FGenericTeamId::NoTeam;
  if (LastSeenPlayerState != nullptr) {
    if (ILyraTeamAgentInterface *PlayerStateTeamInterface =
            Cast<ILyraTeamAgentInterface>(LastSeenPlayerState)) {
      OldTeamID = PlayerStateTeamInterface->GetGenericTeamId();
      PlayerStateTeamInterface->GetTeamChangedDelegateChecked().RemoveAll(this);
    }
  }

  // Bind to the new player state, if any
  FGenericTeamId NewTeamID = FGenericTeamId::NoTeam;
  if (PlayerState != nullptr) {
    if (ILyraTeamAgentInterface *PlayerStateTeamInterface =
            Cast<ILyraTeamAgentInterface>(PlayerState)) {
      NewTeamID = PlayerStateTeamInterface->GetGenericTeamId();
      PlayerStateTeamInterface->GetTeamChangedDelegateChecked().AddDynamic(
          this, &ThisClass::OnPlayerStateChangedTeam);
    }
  }

  // Broadcast the team change (if it really has)
  ConditionalBroadcastTeamChanged(this, OldTeamID, NewTeamID);

  LastSeenPlayerState = PlayerState;
}

void ALyraPlayerController::InitPlayerState() {
  Super::InitPlayerState();
  BroadcastOnPlayerStateChanged();
}

void ALyraPlayerController::CleanupPlayerState() {
  Super::CleanupPlayerState();
  BroadcastOnPlayerStateChanged();
}

void ALyraPlayerController::OnRep_PlayerState() {
  Super::OnRep_PlayerState();
  BroadcastOnPlayerStateChanged();

  // When we're a client connected to a remote server, the player controller may
  // replicate later than the PlayerState and AbilitySystemComponent. However,
  // TryActivateAbilitiesOnSpawn depends on the player controller being
  // replicated in order to check whether on-spawn abilities should execute
  // locally. Therefore once the PlayerController exists and has resolved the
  // PlayerState, try once again to activate on-spawn abilities. On other net
  // modes the PlayerController will never replicate late, so LyraASC's own
  // TryActivateAbilitiesOnSpawn calls will succeed. The handling here is only
  // for when the PlayerState and ASC replicated before the PC and incorrectly
  // thought the abilities were not for the local player.
  if (GetWorld()->IsNetMode(NM_Client)) {
    if (ALyraPlayerState *LyraPS = GetPlayerState<ALyraPlayerState>()) {
      if (ULyraAbilitySystemComponent *LyraASC =
              LyraPS->GetLyraAbilitySystemComponent()) {
        LyraASC->RefreshAbilityActorInfo();
        LyraASC->TryActivateAbilitiesOnSpawn();
      }
    }
  }
}

void ALyraPlayerController::SetPlayer(UPlayer *InPlayer) {
  Super::SetPlayer(InPlayer);

  if (const ULyraLocalPlayer *LyraLocalPlayer =
          Cast<ULyraLocalPlayer>(InPlayer)) {
    ULyraSettingsShared *UserSettings = LyraLocalPlayer->GetSharedSettings();
    UserSettings->OnSettingChanged.AddUObject(this,
                                              &ThisClass::OnSettingsChanged);

    OnSettingsChanged(UserSettings);
  }
}

void ALyraPlayerController::OnSettingsChanged(ULyraSettingsShared *InSettings) {
  bForceFeedbackEnabled = InSettings->GetForceFeedbackEnabled();
}

void ALyraPlayerController::AddCheats(bool bForce) {
#if USING_CHEAT_MANAGER
  Super::AddCheats(true);
#else  // #if USING_CHEAT_MANAGER
  Super::AddCheats(bForce);
#endif // #else //#if USING_CHEAT_MANAGER
}

void ALyraPlayerController::ServerCheat_Implementation(const FString &Msg) {
#if USING_CHEAT_MANAGER
  if (CheatManager) {
    UE_LOG(LogLyra, Warning, TEXT("ServerCheat: %s"), *Msg);
    ClientMessage(ConsoleCommand(Msg));
  }
#endif // #if USING_CHEAT_MANAGER
}

bool ALyraPlayerController::ServerCheat_Validate(const FString &Msg) {
  return true;
}

void ALyraPlayerController::ServerCheatAll_Implementation(const FString &Msg) {
#if USING_CHEAT_MANAGER
  if (CheatManager) {
    UE_LOG(LogLyra, Warning, TEXT("ServerCheatAll: %s"), *Msg);
    for (TActorIterator<ALyraPlayerController> It(GetWorld()); It; ++It) {
      ALyraPlayerController *LyraPC = (*It);
      if (LyraPC) {
        LyraPC->ClientMessage(LyraPC->ConsoleCommand(Msg));
      }
    }
  }
#endif // #if USING_CHEAT_MANAGER
}

bool ALyraPlayerController::ServerCheatAll_Validate(const FString &Msg) {
  return true;
}

void ALyraPlayerController::PreProcessInput(const float DeltaTime,
                                            const bool bGamePaused) {
  Super::PreProcessInput(DeltaTime, bGamePaused);
}

void ALyraPlayerController::PostProcessInput(const float DeltaTime,
                                             const bool bGamePaused) {
  if (ULyraAbilitySystemComponent *LyraASC = GetLyraAbilitySystemComponent()) {
    LyraASC->ProcessAbilityInput(DeltaTime, bGamePaused);
  }

  Super::PostProcessInput(DeltaTime, bGamePaused);
}

void ALyraPlayerController::OnCameraPenetratingTarget() {
  bHideViewTargetPawnNextFrame = true;
}

void ALyraPlayerController::OnPossess(APawn *InPawn) {
  Super::OnPossess(InPawn);
  UE_LOG(LogLyra, Log,
         TEXT("ALyraPlayerController::OnPossess - Pawn possessed, "
              "IsLocalController: %d"),
         IsLocalController());

  if (WebSocketConnection && IsLocalController()) {
    FString PlayerIdToSet;
    if (ALyraPlayerState *LyraPS = GetLyraPlayerState()) {
      FString OriginalId = LyraPS->GetUniqueId().ToString();
      PlayerIdToSet = GenerateShortPlayerId(OriginalId);
    } else if (APlayerState *PS = GetPlayerState<APlayerState>()) {
      FString OriginalId = PS->GetUniqueId().ToString();
      PlayerIdToSet = GenerateShortPlayerId(OriginalId);
    }
    
    if (!PlayerIdToSet.IsEmpty()) {
      WebSocketConnection->SetPlayerId(PlayerIdToSet);
    }
    
    if (!WebSocketConnection->IsConnected()) {
      FString ServerAddress = TEXT("127.0.0.1");
      int32 ServerPort = 18080;
      FString Token = TEXT("test-token");

      if (GConfig) {
        GConfig->GetString(TEXT("WebSocketConnection"), TEXT("ServerAddress"),
                           ServerAddress, GEngineIni);
        GConfig->GetInt(TEXT("WebSocketConnection"), TEXT("ServerPort"),
                        ServerPort, GEngineIni);
      }

      UE_LOG(LogLyra, Log,
             TEXT("ALyraPlayerController::OnPossess - Attempting WebSocket "
                  "connection to %s:%d with PlayerId: %s"),
             *ServerAddress, ServerPort, *PlayerIdToSet);
      WebSocketConnection->ConnectWithConfig(Token);

      if (!WebSocketConnection->OnGameStateReceived.IsAlreadyBound(
              this, &ALyraPlayerController::OnGameStateReceived)) {
        WebSocketConnection->OnGameStateReceived.AddDynamic(
            this, &ALyraPlayerController::OnGameStateReceived);
      }
    } else {
      UE_LOG(LogLyra, Log,
             TEXT("ALyraPlayerController::OnPossess - WebSocket already "
                  "connected, skipping"));
      if (!WebSocketConnection->OnGameStateReceived.IsAlreadyBound(
              this, &ALyraPlayerController::OnGameStateReceived)) {
        WebSocketConnection->OnGameStateReceived.AddDynamic(
            this, &ALyraPlayerController::OnGameStateReceived);
      }
    }
  }

#if WITH_SERVER_CODE && WITH_EDITOR
  if (GIsEditor && (InPawn != nullptr) && (GetPawn() == InPawn)) {
    for (const FLyraCheatToRun &CheatRow :
         GetDefault<ULyraDeveloperSettings>()->CheatsToRun) {
      if (CheatRow.Phase == ECheatExecutionTime::OnPlayerPawnPossession) {
        ConsoleCommand(CheatRow.Cheat, /*bWriteToLog=*/true);
      }
    }
  }
#endif

  SetIsAutoRunning(false);
}

void ALyraPlayerController::SetIsAutoRunning(const bool bEnabled) {
  const bool bIsAutoRunning = GetIsAutoRunning();
  if (bEnabled != bIsAutoRunning) {
    if (!bEnabled) {
      OnEndAutoRun();
    } else {
      OnStartAutoRun();
    }
  }
}

bool ALyraPlayerController::GetIsAutoRunning() const {
  bool bIsAutoRunning = false;
  if (const ULyraAbilitySystemComponent *LyraASC =
          GetLyraAbilitySystemComponent()) {
    bIsAutoRunning =
        LyraASC->GetTagCount(LyraGameplayTags::Status_AutoRunning) > 0;
  }
  return bIsAutoRunning;
}

void ALyraPlayerController::OnStartAutoRun() {
  if (ULyraAbilitySystemComponent *LyraASC = GetLyraAbilitySystemComponent()) {
    LyraASC->SetLooseGameplayTagCount(LyraGameplayTags::Status_AutoRunning, 1);
    K2_OnStartAutoRun();
  }
}

void ALyraPlayerController::OnEndAutoRun() {
  if (ULyraAbilitySystemComponent *LyraASC = GetLyraAbilitySystemComponent()) {
    LyraASC->SetLooseGameplayTagCount(LyraGameplayTags::Status_AutoRunning, 0);
    K2_OnEndAutoRun();
  }
}

void ALyraPlayerController::ConnectToWebSocketServer(
    const FString &ServerAddress, int32 ServerPort, const FString &Token) {
  if (WebSocketConnection) {
    WebSocketConnection->Connect(ServerAddress, ServerPort, Token);
  }
}

void ALyraPlayerController::DisconnectFromWebSocketServer() {
  if (WebSocketConnection) {
    WebSocketConnection->Disconnect();
  }
}

void ALyraPlayerController::UpdateForceFeedback(IInputInterface *InputInterface,
                                                const int32 ControllerId) {
  if (bForceFeedbackEnabled) {
    if (const UCommonInputSubsystem *CommonInputSubsystem =
            UCommonInputSubsystem::Get(GetLocalPlayer())) {
      const ECommonInputType CurrentInputType =
          CommonInputSubsystem->GetCurrentInputType();
      if (Lyra::Input::ShouldAlwaysPlayForceFeedback ||
          CurrentInputType == ECommonInputType::Gamepad ||
          CurrentInputType == ECommonInputType::Touch) {
        InputInterface->SetForceFeedbackChannelValues(ControllerId,
                                                      ForceFeedbackValues);
        return;
      }
    }
  }

  InputInterface->SetForceFeedbackChannelValues(ControllerId,
                                                FForceFeedbackValues());
}

void ALyraPlayerController::UpdateHiddenComponents(
    const FVector &ViewLocation,
    TSet<FPrimitiveComponentId> &OutHiddenComponents) {
  Super::UpdateHiddenComponents(ViewLocation, OutHiddenComponents);

  if (bHideViewTargetPawnNextFrame) {
    AActor *const ViewTargetPawn =
        PlayerCameraManager ? Cast<AActor>(PlayerCameraManager->GetViewTarget())
                            : nullptr;
    if (ViewTargetPawn) {
      // internal helper func to hide all the components
      auto AddToHiddenComponents =
          [&OutHiddenComponents](
              const TInlineComponentArray<UPrimitiveComponent *>
                  &InComponents) {
            // add every component and all attached children
            for (UPrimitiveComponent *Comp : InComponents) {
              if (Comp->IsRegistered()) {
                OutHiddenComponents.Add(Comp->GetPrimitiveSceneId());

                for (USceneComponent *AttachedChild :
                     Comp->GetAttachChildren()) {
                  static FName NAME_NoParentAutoHide(TEXT("NoParentAutoHide"));
                  UPrimitiveComponent *AttachChildPC =
                      Cast<UPrimitiveComponent>(AttachedChild);
                  if (AttachChildPC && AttachChildPC->IsRegistered() &&
                      !AttachChildPC->ComponentTags.Contains(
                          NAME_NoParentAutoHide)) {
                    OutHiddenComponents.Add(
                        AttachChildPC->GetPrimitiveSceneId());
                  }
                }
              }
            }
          };

      // TODO Solve with an interface.  Gather hidden components or something.
      // TODO Hiding isn't awesome, sometimes you want the effect of a fade out
      // over a proximity, needs to bubble up to designers.

      // hide pawn's components
      TInlineComponentArray<UPrimitiveComponent *> PawnComponents;
      ViewTargetPawn->GetComponents(PawnComponents);
      AddToHiddenComponents(PawnComponents);

      //// hide weapon too
      // if (ViewTargetPawn->CurrentWeapon)
      //{
      //	TInlineComponentArray<UPrimitiveComponent*> WeaponComponents;
      //	ViewTargetPawn->CurrentWeapon->GetComponents(WeaponComponents);
      //	AddToHiddenComponents(WeaponComponents);
      // }
    }

    // we consumed it, reset for next frame
    bHideViewTargetPawnNextFrame = false;
  }
}

void ALyraPlayerController::SetGenericTeamId(const FGenericTeamId &NewTeamID) {
  UE_LOG(LogLyraTeams, Error,
         TEXT("You can't set the team ID on a player controller (%s); it's "
              "driven by the associated player state"),
         *GetPathNameSafe(this));
}

FGenericTeamId ALyraPlayerController::GetGenericTeamId() const {
  if (const ILyraTeamAgentInterface *PSWithTeamInterface =
          Cast<ILyraTeamAgentInterface>(PlayerState)) {
    return PSWithTeamInterface->GetGenericTeamId();
  }
  return FGenericTeamId::NoTeam;
}

FOnLyraTeamIndexChangedDelegate *
ALyraPlayerController::GetOnTeamIndexChangedDelegate() {
  return &OnTeamChangedDelegate;
}

void ALyraPlayerController::OnUnPossess() {
  // Make sure the pawn that is being unpossessed doesn't remain our ASC's
  // avatar actor
  if (APawn *PawnBeingUnpossessed = GetPawn()) {
    if (UAbilitySystemComponent *ASC =
            UAbilitySystemGlobals::GetAbilitySystemComponentFromActor(
                PlayerState)) {
      if (ASC->GetAvatarActor() == PawnBeingUnpossessed) {
        ASC->SetAvatarActor(nullptr);
      }
    }
  }

  Super::OnUnPossess();
}

void ALyraPlayerController::OnGameStateReceived(
    const TArray<uint8> &GameStateData) {
  UE_LOG(LogLyra, Warning,
         TEXT("ALyraPlayerController::OnGameStateReceived: ===== CALLED with "
              "%d bytes ====="),
         GameStateData.Num());

  if (GameStateData.Num() == 0) {
    UE_LOG(LogLyra, Warning,
           TEXT("ALyraPlayerController::OnGameStateReceived: Empty data, "
                "returning"));
    return;
  }

  if (!WebSocketConnection || !WebSocketConnection->IsConnected()) {
    UE_LOG(LogLyra, Warning,
           TEXT("ALyraPlayerController::OnGameStateReceived: WebSocket not "
                "connected (conn=%p, connected=%d), ignoring GameState update"),
           WebSocketConnection.Get(),
           WebSocketConnection ? WebSocketConnection->IsConnected() : 0);
    return;
  }

  UE_LOG(LogLyra, Warning,
         TEXT("ALyraPlayerController::OnGameStateReceived: WebSocket "
              "connected, processing GameState"));

  FProtobufCodec::FServerMessage ServerMsg;
  if (!FProtobufCodec::DecodeServerMessage(GameStateData, ServerMsg)) {
    UE_LOG(LogLyra, Warning,
           TEXT("ALyraPlayerController::OnGameStateReceived: Failed to decode "
                "GameState"));
    return;
  }

  if (ServerMsg.Type !=
      FProtobufCodec::FServerMessage::EMessageType::GameState) {
    return;
  }

  UWorld *World = GetWorld();
  if (!World) {
    return;
  }

  FString LocalPlayerId;
  if (ALyraPlayerState *LyraPS = GetLyraPlayerState()) {
    FString OriginalId = LyraPS->GetUniqueId().ToString();
    LocalPlayerId = GenerateShortPlayerId(OriginalId);
  } else if (APlayerState *PS = GetPlayerState<APlayerState>()) {
    FString OriginalId = PS->GetUniqueId().ToString();
    LocalPlayerId = GenerateShortPlayerId(OriginalId);
  }

  for (const FProtobufCodec::FEntityState &Entity :
       ServerMsg.GameState.Snapshot.Entities) {
    if (Entity.Id == LocalPlayerId) {
      UE_LOG(LogLyra, Verbose,
             TEXT("ALyraPlayerController::OnGameStateReceived: Skipping local player Entity.Id=%s, LocalPlayerId=%s"),
             *Entity.Id, *LocalPlayerId);
      continue;
    }

    bool bFoundController = false;
    for (FConstPlayerControllerIterator It =
             World->GetPlayerControllerIterator();
         It; ++It) {
      if (APlayerController *PC = It->Get()) {
        FString PCPlayerId;
        if (ALyraPlayerState *LyraPS = PC->GetPlayerState<ALyraPlayerState>()) {
          FString OriginalId = LyraPS->GetUniqueId().ToString();
          PCPlayerId = GenerateShortPlayerId(OriginalId);
        } else if (APlayerState *PS = PC->GetPlayerState<APlayerState>()) {
          FString OriginalId = PS->GetUniqueId().ToString();
          PCPlayerId = GenerateShortPlayerId(OriginalId);
        }

        if (PCPlayerId == Entity.Id) {
          bFoundController = true;
          if (APawn *TargetPawn = PC->GetPawn()) {
            FVector NewLocation(Entity.X, Entity.Y, Entity.Z);
            FRotator NewRotation(0.0f, Entity.Yaw, 0.0f);
            FVector NewVelocity(Entity.VX, Entity.VY, Entity.VZ);

            if (ALyraCharacter *LyraChar = Cast<ALyraCharacter>(TargetPawn)) {
              if (UCharacterMovementComponent *MovementComp =
                      LyraChar->GetCharacterMovement()) {
                const float VelocityThreshold = 1.0f;
                const float LocationThreshold = 5.0f;
                
                FVector CurrentLocation = TargetPawn->GetActorLocation();
                FVector LocationDelta = NewLocation - CurrentLocation;
                float LocationDistance = LocationDelta.Size();
                
                if (LocationDistance > LocationThreshold) {
                  MovementComp->Velocity = NewVelocity;
                  TargetPawn->SetActorLocation(NewLocation, true);
                } else if (NewVelocity.SizeSquared() > FMath::Square(VelocityThreshold)) {
                  MovementComp->Velocity = NewVelocity;
                }
              }
            } else {
              TargetPawn->SetActorLocation(NewLocation, true);
            }
            
            TargetPawn->SetActorRotation(NewRotation);
          }
          break;
        }
      }
    }
    
    if (!bFoundController) {
      UE_LOG(LogLyra, Warning,
             TEXT("ALyraPlayerController::OnGameStateReceived: No controller found for Entity.Id=%s, LocalPlayerId=%s"),
             *Entity.Id, *LocalPlayerId);
    }
  }

  UE_LOG(LogLyra, Warning,
         TEXT("ALyraPlayerController::OnGameStateReceived: ===== PROCESSED "
              "GameState with %d entities, Tick=%lld ====="),
         ServerMsg.GameState.Snapshot.Entities.Num(),
         ServerMsg.GameState.Snapshot.Tick);
}

void ALyraPlayerController::OnWebSocketConnected(bool bSuccess) {
  UE_LOG(LogLyra, Warning,
         TEXT("ALyraPlayerController::OnWebSocketConnected: WebSocket %s"),
         bSuccess ? TEXT("CONNECTED") : TEXT("DISCONNECTED"));

  UWorld *World = GetWorld();
  if (!World) {
    return;
  }

  for (TActorIterator<ALyraCharacter> It(World); It; ++It) {
    if (ALyraCharacter *LyraChar = *It) {
      LyraChar->UpdateMovementReplication();
    }
  }
}

void ALyraPlayerController::OnWebSocketDisconnected(const FString &Reason) {
  UE_LOG(LogLyra, Warning,
         TEXT("ALyraPlayerController::OnWebSocketDisconnected: %s"), *Reason);

  UWorld *World = GetWorld();
  if (!World) {
    return;
  }

  for (TActorIterator<ALyraCharacter> It(World); It; ++It) {
    if (ALyraCharacter *LyraChar = *It) {
      LyraChar->UpdateMovementReplication();
    }
  }
}

//////////////////////////////////////////////////////////////////////
// ALyraReplayPlayerController

void ALyraReplayPlayerController::Tick(float DeltaSeconds) {
  Super::Tick(DeltaSeconds);

  // The state may go invalid at any time due to scrubbing during a replay
  if (!IsValid(FollowedPlayerState)) {
    UWorld *World = GetWorld();

    // Listen for changes for both recording and playback
    if (ALyraGameState *GameState =
            Cast<ALyraGameState>(World->GetGameState())) {
      if (!GameState->OnRecorderPlayerStateChangedEvent.IsBoundToObject(this)) {
        GameState->OnRecorderPlayerStateChangedEvent.AddUObject(
            this, &ThisClass::RecorderPlayerStateUpdated);
      }
      if (APlayerState *RecorderState = GameState->GetRecorderPlayerState()) {
        RecorderPlayerStateUpdated(RecorderState);
      }
    }
  }
}

void ALyraReplayPlayerController::SmoothTargetViewRotation(APawn *TargetPawn,
                                                           float DeltaSeconds) {
  // Default behavior is to interpolate to TargetViewRotation which is set from
  // APlayerController::TickActor but it's not very smooth

  Super::SmoothTargetViewRotation(TargetPawn, DeltaSeconds);
}

bool ALyraReplayPlayerController::ShouldRecordClientReplay() { return false; }

void ALyraReplayPlayerController::RecorderPlayerStateUpdated(
    APlayerState *NewRecorderPlayerState) {
  if (NewRecorderPlayerState) {
    FollowedPlayerState = NewRecorderPlayerState;

    // Bind to when pawn changes and call now
    NewRecorderPlayerState->OnPawnSet.AddUniqueDynamic(
        this, &ALyraReplayPlayerController::OnPlayerStatePawnSet);
    OnPlayerStatePawnSet(NewRecorderPlayerState,
                         NewRecorderPlayerState->GetPawn(), nullptr);
  }
}

FString ALyraPlayerController::GenerateShortPlayerId(const FString& OriginalId)
{
  if (OriginalId.IsEmpty())
  {
    return TEXT("player1");
  }

  if (OriginalId.Len() > 50)
  {
    UE_LOG(LogLyra, Error, TEXT("GenerateShortPlayerId: LONG OriginalId detected! Length=%d, Value='%s' - TRACING SOURCE"), 
      OriginalId.Len(), *OriginalId);
    UE_LOG(LogLyra, Error, TEXT("GenerateShortPlayerId: Called from: %s"), *FString(__FUNCTION__));
  }

  uint32 Hash = GetTypeHash(OriginalId);
  
  FString ShortId = FString::Printf(TEXT("p%08x"), Hash);
  
  return ShortId;
}

void ALyraReplayPlayerController::OnPlayerStatePawnSet(
    APlayerState *ChangedPlayerState, APawn *NewPlayerPawn,
    APawn *OldPlayerPawn) {
  if (ChangedPlayerState == FollowedPlayerState) {
    SetViewTarget(NewPlayerPawn);
  }
}
