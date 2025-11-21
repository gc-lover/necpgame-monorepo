// Copyright Epic Games, Inc. All Rights Reserved.

#include "LyraGameMode.h"
#include "AssetRegistry/AssetData.h"
#include "Character/LyraCharacter.h"
#include "Character/LyraPawnData.h"
#include "Character/LyraPawnExtensionComponent.h"
#include "CommonSessionSubsystem.h"
#include "CommonUserSubsystem.h"
#include "Development/LyraDeveloperSettings.h"
#include "Engine/GameInstance.h"
#include "Engine/World.h"
#include "EngineUtils.h"
#include "GameFramework/CharacterMovementComponent.h"
#include "GameMapsSettings.h"
#include "GameModes/LyraExperienceDefinition.h"
#include "GameModes/LyraExperienceManagerComponent.h"
#include "GameModes/LyraUserFacingExperienceDefinition.h"
#include "GameModes/LyraWorldSettings.h"
#include "Kismet/GameplayStatics.h"
#include "LyraGameState.h"
#include "LyraLogChannels.h"
#include "Misc/CommandLine.h"
#include "Misc/ConfigCacheIni.h"
#include "Net/ProtobufCodec.h"
#include "Player/LyraPlayerBotController.h"
#include "Player/LyraPlayerController.h"
#include "HAL/CriticalSection.h"
#include "Player/LyraPlayerSpawningManagerComponent.h"
#include "Player/LyraPlayerState.h"
#include "System/LyraAssetManager.h"
#include "System/LyraGameSession.h"
#include "TimerManager.h"
#include "UI/LyraHUD.h"
#include "AbilitySystem/LyraAbilitySystemComponent.h"
#include "Equipment/LyraEquipmentManagerComponent.h"
#include "Weapons/LyraRangedWeaponInstance.h"
#include "Weapons/LyraGameplayAbility_RangedWeapon.h"

#include UE_INLINE_GENERATED_CPP_BY_NAME(LyraGameMode)

ALyraGameMode::ALyraGameMode(const FObjectInitializer &ObjectInitializer)
    : Super(ObjectInitializer) {
  GameStateClass = ALyraGameState::StaticClass();
  GameSessionClass = ALyraGameSession::StaticClass();
  PlayerControllerClass = ALyraPlayerController::StaticClass();
  ReplaySpectatorPlayerControllerClass =
      ALyraReplayPlayerController::StaticClass();
  PlayerStateClass = ALyraPlayerState::StaticClass();
  DefaultPawnClass = ALyraCharacter::StaticClass();
  HUDClass = ALyraHUD::StaticClass();
}

const ULyraPawnData *
ALyraGameMode::GetPawnDataForController(const AController *InController) const {
  // See if pawn data is already set on the player state
  if (InController != nullptr) {
    if (const ALyraPlayerState *LyraPS =
            InController->GetPlayerState<ALyraPlayerState>()) {
      if (const ULyraPawnData *PawnData =
              LyraPS->GetPawnData<ULyraPawnData>()) {
        return PawnData;
      }
    }
  }

  // If not, fall back to the the default for the current experience
  check(GameState);
  ULyraExperienceManagerComponent *ExperienceComponent =
      GameState->FindComponentByClass<ULyraExperienceManagerComponent>();
  check(ExperienceComponent);

  if (ExperienceComponent->IsExperienceLoaded()) {
    const ULyraExperienceDefinition *Experience =
        ExperienceComponent->GetCurrentExperienceChecked();
    if (Experience->DefaultPawnData != nullptr) {
      return Experience->DefaultPawnData;
    }

    // Experience is loaded and there's still no pawn data, fall back to the
    // default for now
    return ULyraAssetManager::Get().GetDefaultPawnData();
  }

  // Experience not loaded yet, so there is no pawn data to be had
  return nullptr;
}

void ALyraGameMode::InitGame(const FString &MapName, const FString &Options,
                             FString &ErrorMessage) {
  Super::InitGame(MapName, Options, ErrorMessage);

  ENetMode NetMode = GetNetMode();
  UE_LOG(LogLyra, Log, TEXT("ALyraGameMode::InitGame: NetMode=%d (0=Standalone, 1=DedicatedServer, 2=ListenServer, 3=Client)"), NetMode);

  if (NetMode == NM_DedicatedServer || NetMode == NM_ListenServer) {
    UE_LOG(LogLyra, Log, TEXT("ALyraGameMode::InitGame: Initializing GatewayConnection for server"));
    InitializeGatewayConnection();

    // Start periodic GameState updates (60 Hz) after a short delay to ensure
    // GatewayConnection is ready
    if (GEngine && !GIsGarbageCollecting) {
      UWorld *World = GetWorld();
      if (World && IsValid(World) && !World->bIsTearingDown) {
        FTimerManager &TimerManager = World->GetTimerManager();
        TimerManager.SetTimer(GameStateUpdateTimerHandle, this,
                              &ALyraGameMode::UpdateAndSendGameState,
                              1.0f / 60.0f, true, 0.5f);
      }
    }
  }

  // Wait for the next frame to give time to initialize startup settings
  GetWorld()->GetTimerManager().SetTimerForNextTick(
      this, &ThisClass::HandleMatchAssignmentIfNotExpectingOne);
}

void ALyraGameMode::HandleMatchAssignmentIfNotExpectingOne() {
  FPrimaryAssetId ExperienceId;
  FString ExperienceIdSource;

  // Precedence order (highest wins)
  //  - Matchmaking assignment (if present)
  //  - URL Options override
  //  - Developer Settings (PIE only)
  //  - Command Line override
  //  - World Settings
  //  - Dedicated server
  //  - Default experience

  UWorld *World = GetWorld();

  if (!ExperienceId.IsValid() &&
      UGameplayStatics::HasOption(OptionsString, TEXT("Experience"))) {
    const FString ExperienceFromOptions =
        UGameplayStatics::ParseOption(OptionsString, TEXT("Experience"));
    ExperienceId = FPrimaryAssetId(
        FPrimaryAssetType(ULyraExperienceDefinition::StaticClass()->GetFName()),
        FName(*ExperienceFromOptions));
    ExperienceIdSource = TEXT("OptionsString");
  }

  if (!ExperienceId.IsValid() && World->IsPlayInEditor()) {
    ExperienceId = GetDefault<ULyraDeveloperSettings>()->ExperienceOverride;
    ExperienceIdSource = TEXT("DeveloperSettings");
  }

  // see if the command line wants to set the experience
  if (!ExperienceId.IsValid()) {
    FString ExperienceFromCommandLine;
    if (FParse::Value(FCommandLine::Get(), TEXT("Experience="),
                      ExperienceFromCommandLine)) {
      ExperienceId =
          FPrimaryAssetId::ParseTypeAndName(ExperienceFromCommandLine);
      if (!ExperienceId.PrimaryAssetType.IsValid()) {
        ExperienceId = FPrimaryAssetId(
            FPrimaryAssetType(
                ULyraExperienceDefinition::StaticClass()->GetFName()),
            FName(*ExperienceFromCommandLine));
      }
      ExperienceIdSource = TEXT("CommandLine");
    }
  }

  // see if the world settings has a default experience
  if (!ExperienceId.IsValid()) {
    if (ALyraWorldSettings *TypedWorldSettings =
            Cast<ALyraWorldSettings>(GetWorldSettings())) {
      ExperienceId = TypedWorldSettings->GetDefaultGameplayExperience();
      ExperienceIdSource = TEXT("WorldSettings");
    }
  }

  ULyraAssetManager &AssetManager = ULyraAssetManager::Get();
  FAssetData Dummy;
  if (ExperienceId.IsValid() &&
      !AssetManager.GetPrimaryAssetData(ExperienceId, /*out*/ Dummy)) {
    UE_LOG(LogLyraExperience, Error,
           TEXT("EXPERIENCE: Wanted to use %s but couldn't find it, falling "
                "back to the default)"),
           *ExperienceId.ToString());
    ExperienceId = FPrimaryAssetId();
  }

  // Final fallback to the default experience
  if (!ExperienceId.IsValid()) {
    if (TryDedicatedServerLogin()) {
      // This will start to host as a dedicated server
      return;
    }

    //@TODO: Pull this from a config setting or something
    ExperienceId =
        FPrimaryAssetId(FPrimaryAssetType("LyraExperienceDefinition"),
                        FName("B_LyraDefaultExperience"));
    ExperienceIdSource = TEXT("Default");
  }

  OnMatchAssignmentGiven(ExperienceId, ExperienceIdSource);
}

bool ALyraGameMode::TryDedicatedServerLogin() {
  // Some basic code to register as an active dedicated server, this would be
  // heavily modified by the game
  FString DefaultMap = UGameMapsSettings::GetGameDefaultMap();
  UWorld *World = GetWorld();
  UGameInstance *GameInstance = GetGameInstance();
  if (GameInstance && World && World->GetNetMode() == NM_DedicatedServer &&
      World->URL.Map == DefaultMap) {
    // Only register if this is the default map on a dedicated server
    UCommonUserSubsystem *UserSubsystem =
        GameInstance->GetSubsystem<UCommonUserSubsystem>();

    // Dedicated servers may need to do an online login
    UserSubsystem->OnUserInitializeComplete.AddDynamic(
        this, &ALyraGameMode::OnUserInitializedForDedicatedServer);

    // There are no local users on dedicated server, but index 0 means the
    // default platform user which is handled by the online login code
    if (!UserSubsystem->TryToLoginForOnlinePlay(0)) {
      OnUserInitializedForDedicatedServer(nullptr, false, FText(),
                                          ECommonUserPrivilege::CanPlayOnline,
                                          ECommonUserOnlineContext::Default);
    }

    return true;
  }

  return false;
}

void ALyraGameMode::HostDedicatedServerMatch(
    ECommonSessionOnlineMode OnlineMode) {
  FPrimaryAssetType UserExperienceType =
      ULyraUserFacingExperienceDefinition::StaticClass()->GetFName();

  // Figure out what UserFacingExperience to load
  FPrimaryAssetId UserExperienceId;
  FString UserExperienceFromCommandLine;
  if (FParse::Value(FCommandLine::Get(), TEXT("UserExperience="),
                    UserExperienceFromCommandLine) ||
      FParse::Value(FCommandLine::Get(), TEXT("Playlist="),
                    UserExperienceFromCommandLine)) {
    UserExperienceId =
        FPrimaryAssetId::ParseTypeAndName(UserExperienceFromCommandLine);
    if (!UserExperienceId.PrimaryAssetType.IsValid()) {
      UserExperienceId = FPrimaryAssetId(FPrimaryAssetType(UserExperienceType),
                                         FName(*UserExperienceFromCommandLine));
    }
  }

  // Search for the matching experience, it's fine to force load them because
  // we're in dedicated server startup
  ULyraAssetManager &AssetManager = ULyraAssetManager::Get();
  TSharedPtr<FStreamableHandle> Handle =
      AssetManager.LoadPrimaryAssetsWithType(UserExperienceType);
  if (ensure(Handle.IsValid())) {
    Handle->WaitUntilComplete();
  }

  TArray<UObject *> UserExperiences;
  AssetManager.GetPrimaryAssetObjectList(UserExperienceType, UserExperiences);
  ULyraUserFacingExperienceDefinition *FoundExperience = nullptr;
  ULyraUserFacingExperienceDefinition *DefaultExperience = nullptr;

  for (UObject *Object : UserExperiences) {
    ULyraUserFacingExperienceDefinition *UserExperience =
        Cast<ULyraUserFacingExperienceDefinition>(Object);
    if (ensure(UserExperience)) {
      if (UserExperience->GetPrimaryAssetId() == UserExperienceId) {
        FoundExperience = UserExperience;
        break;
      }

      if (UserExperience->bIsDefaultExperience &&
          DefaultExperience == nullptr) {
        DefaultExperience = UserExperience;
      }
    }
  }

  if (FoundExperience == nullptr) {
    FoundExperience = DefaultExperience;
  }

  UGameInstance *GameInstance = GetGameInstance();
  if (ensure(FoundExperience && GameInstance)) {
    // Actually host the game
    UCommonSession_HostSessionRequest *HostRequest =
        FoundExperience->CreateHostingRequest(this);
    if (ensure(HostRequest)) {
      HostRequest->OnlineMode = OnlineMode;

      // TODO override other parameters?

      UCommonSessionSubsystem *SessionSubsystem =
          GameInstance->GetSubsystem<UCommonSessionSubsystem>();
      SessionSubsystem->HostSession(nullptr, HostRequest);

      // This will handle the map travel
    }
  }
}

void ALyraGameMode::OnUserInitializedForDedicatedServer(
    const UCommonUserInfo *UserInfo, bool bSuccess, FText Error,
    ECommonUserPrivilege RequestedPrivilege,
    ECommonUserOnlineContext OnlineContext) {
  UGameInstance *GameInstance = GetGameInstance();
  if (GameInstance) {
    // Unbind
    UCommonUserSubsystem *UserSubsystem =
        GameInstance->GetSubsystem<UCommonUserSubsystem>();
    UserSubsystem->OnUserInitializeComplete.RemoveDynamic(
        this, &ALyraGameMode::OnUserInitializedForDedicatedServer);

    // Dedicated servers do not require user login, but some online subsystems
    // may expect it
    if (bSuccess && ensure(UserInfo)) {
      UE_LOG(LogLyraExperience, Log,
             TEXT("Dedicated server user login succeeded for id %s, starting "
                  "online server"),
             *UserInfo->GetNetId().ToString());
    } else {
      UE_LOG(LogLyraExperience, Log,
             TEXT("Dedicated server user login unsuccessful, starting online "
                  "server as login is not required"));
    }

    HostDedicatedServerMatch(ECommonSessionOnlineMode::Online);
  }
}

void ALyraGameMode::OnMatchAssignmentGiven(FPrimaryAssetId ExperienceId,
                                           const FString &ExperienceIdSource) {
  if (ExperienceId.IsValid()) {
    UE_LOG(LogLyraExperience, Log,
           TEXT("Identified experience %s (Source: %s)"),
           *ExperienceId.ToString(), *ExperienceIdSource);

    ULyraExperienceManagerComponent *ExperienceComponent =
        GameState->FindComponentByClass<ULyraExperienceManagerComponent>();
    check(ExperienceComponent);
    ExperienceComponent->SetCurrentExperience(ExperienceId);
  } else {
    UE_LOG(LogLyraExperience, Error,
           TEXT("Failed to identify experience, loading screen will stay up "
                "forever"));
  }
}

void ALyraGameMode::OnExperienceLoaded(
    const ULyraExperienceDefinition *CurrentExperience) {
  // Spawn any players that are already attached
  //@TODO: Here we're handling only *player* controllers, but in
  //GetDefaultPawnClassForController_Implementation we skipped all controllers
  // GetDefaultPawnClassForController_Implementation might only be getting
  // called for players anyways
  for (FConstPlayerControllerIterator Iterator =
           GetWorld()->GetPlayerControllerIterator();
       Iterator; ++Iterator) {
    APlayerController *PC = Cast<APlayerController>(*Iterator);
    if ((PC != nullptr) && (PC->GetPawn() == nullptr)) {
      if (PlayerCanRestart(PC)) {
        RestartPlayer(PC);
      }
    }
  }
}

bool ALyraGameMode::IsExperienceLoaded() const {
  check(GameState);
  ULyraExperienceManagerComponent *ExperienceComponent =
      GameState->FindComponentByClass<ULyraExperienceManagerComponent>();
  check(ExperienceComponent);

  return ExperienceComponent->IsExperienceLoaded();
}

UClass *ALyraGameMode::GetDefaultPawnClassForController_Implementation(
    AController *InController) {
  if (const ULyraPawnData *PawnData = GetPawnDataForController(InController)) {
    if (PawnData->PawnClass) {
      return PawnData->PawnClass;
    }
  }

  return Super::GetDefaultPawnClassForController_Implementation(InController);
}

APawn *ALyraGameMode::SpawnDefaultPawnAtTransform_Implementation(
    AController *NewPlayer, const FTransform &SpawnTransform) {
  FActorSpawnParameters SpawnInfo;
  SpawnInfo.Instigator = GetInstigator();
  SpawnInfo.ObjectFlags |=
      RF_Transient; // Never save the default player pawns into a map.
  SpawnInfo.bDeferConstruction = true;

  if (UClass *PawnClass = GetDefaultPawnClassForController(NewPlayer)) {
    if (APawn *SpawnedPawn = GetWorld()->SpawnActor<APawn>(
            PawnClass, SpawnTransform, SpawnInfo)) {
      if (ULyraPawnExtensionComponent *PawnExtComp =
              ULyraPawnExtensionComponent::FindPawnExtensionComponent(
                  SpawnedPawn)) {
        if (const ULyraPawnData *PawnData =
                GetPawnDataForController(NewPlayer)) {
          PawnExtComp->SetPawnData(PawnData);
        } else {
          UE_LOG(LogLyra, Error,
                 TEXT("Game mode was unable to set PawnData on the spawned "
                      "pawn [%s]."),
                 *GetNameSafe(SpawnedPawn));
        }
      }

      SpawnedPawn->FinishSpawning(SpawnTransform);

      return SpawnedPawn;
    } else {
      UE_LOG(LogLyra, Error,
             TEXT("Game mode was unable to spawn Pawn of class [%s] at [%s]."),
             *GetNameSafe(PawnClass), *SpawnTransform.ToHumanReadableString());
    }
  } else {
    UE_LOG(LogLyra, Error,
           TEXT("Game mode was unable to spawn Pawn due to NULL pawn class."));
  }

  return nullptr;
}

bool ALyraGameMode::ShouldSpawnAtStartSpot(AController *Player) {
  // We never want to use the start spot, always use the spawn management
  // component.
  return false;
}

void ALyraGameMode::HandleStartingNewPlayer_Implementation(
    APlayerController *NewPlayer) {
  // Delay starting new players until the experience has been loaded
  // (players who log in prior to that will be started by OnExperienceLoaded)
  if (IsExperienceLoaded()) {
    Super::HandleStartingNewPlayer_Implementation(NewPlayer);
  }
}

AActor *ALyraGameMode::ChoosePlayerStart_Implementation(AController *Player) {
  if (ULyraPlayerSpawningManagerComponent *PlayerSpawningComponent =
          GameState
              ->FindComponentByClass<ULyraPlayerSpawningManagerComponent>()) {
    return PlayerSpawningComponent->ChoosePlayerStart(Player);
  }

  return Super::ChoosePlayerStart_Implementation(Player);
}

void ALyraGameMode::FinishRestartPlayer(AController *NewPlayer,
                                        const FRotator &StartRotation) {
  if (ULyraPlayerSpawningManagerComponent *PlayerSpawningComponent =
          GameState
              ->FindComponentByClass<ULyraPlayerSpawningManagerComponent>()) {
    PlayerSpawningComponent->FinishRestartPlayer(NewPlayer, StartRotation);
  }

  Super::FinishRestartPlayer(NewPlayer, StartRotation);
}

bool ALyraGameMode::PlayerCanRestart_Implementation(APlayerController *Player) {
  return ControllerCanRestart(Player);
}

bool ALyraGameMode::ControllerCanRestart(AController *Controller) {
  if (APlayerController *PC = Cast<APlayerController>(Controller)) {
    if (!Super::PlayerCanRestart_Implementation(PC)) {
      return false;
    }
  } else {
    // Bot version of Super::PlayerCanRestart_Implementation
    if ((Controller == nullptr) || Controller->IsPendingKillPending()) {
      return false;
    }
  }

  if (ULyraPlayerSpawningManagerComponent *PlayerSpawningComponent =
          GameState
              ->FindComponentByClass<ULyraPlayerSpawningManagerComponent>()) {
    return PlayerSpawningComponent->ControllerCanRestart(Controller);
  }

  return true;
}

void ALyraGameMode::InitGameState() {
  Super::InitGameState();

  // Listen for the experience load to complete
  ULyraExperienceManagerComponent *ExperienceComponent =
      GameState->FindComponentByClass<ULyraExperienceManagerComponent>();
  check(ExperienceComponent);
  ExperienceComponent->CallOrRegister_OnExperienceLoaded(
      FOnLyraExperienceLoaded::FDelegate::CreateUObject(
          this, &ThisClass::OnExperienceLoaded));
}

void ALyraGameMode::GenericPlayerInitialization(AController *NewPlayer) {
  Super::GenericPlayerInitialization(NewPlayer);

  OnGameModePlayerInitialized.Broadcast(this, NewPlayer);
}

void ALyraGameMode::RequestPlayerRestartNextFrame(AController *Controller,
                                                  bool bForceReset) {
  if (bForceReset && (Controller != nullptr)) {
    Controller->Reset();
  }

  if (APlayerController *PC = Cast<APlayerController>(Controller)) {
    GetWorldTimerManager().SetTimerForNextTick(
        PC, &APlayerController::ServerRestartPlayer_Implementation);
  } else if (ALyraPlayerBotController *BotController =
                 Cast<ALyraPlayerBotController>(Controller)) {
    GetWorldTimerManager().SetTimerForNextTick(
        BotController, &ALyraPlayerBotController::ServerRestartController);
  }
}

bool ALyraGameMode::UpdatePlayerStartSpot(AController *Player,
                                          const FString &Portal,
                                          FString &OutErrorMessage) {
  // Do nothing, we'll wait until PostLogin when we try to spawn the player for
  // real. Doing anything right now is no good, systems like team assignment
  // haven't even occurred yet.
  return true;
}

void ALyraGameMode::FailedToRestartPlayer(AController *NewPlayer) {
  Super::FailedToRestartPlayer(NewPlayer);

  // If we tried to spawn a pawn and it failed, lets try again *note* check if
  // there's actually a pawn class before we try this forever.
  if (UClass *PawnClass = GetDefaultPawnClassForController(NewPlayer)) {
    if (APlayerController *NewPC = Cast<APlayerController>(NewPlayer)) {
      // If it's a player don't loop forever, maybe something changed and they
      // can no longer restart if so stop trying.
      if (PlayerCanRestart(NewPC)) {
        RequestPlayerRestartNextFrame(NewPlayer, false);
      } else {
        UE_LOG(LogLyra, Verbose,
               TEXT("FailedToRestartPlayer(%s) and PlayerCanRestart returned "
                    "false, so we're not going to try again."),
               *GetPathNameSafe(NewPlayer));
      }
    } else {
      RequestPlayerRestartNextFrame(NewPlayer, false);
    }
  } else {
    UE_LOG(LogLyra, Verbose,
           TEXT("FailedToRestartPlayer(%s) but there's no pawn class so giving "
                "up."),
           *GetPathNameSafe(NewPlayer));
  }
}

void ALyraGameMode::InitializeGatewayConnection() {
  if (GatewayConnection) {
    GatewayConnection->Shutdown();
    GatewayConnection = nullptr;
  }

  GatewayConnection = NewObject<ULyraServerGatewayConnection>(this);
  if (!GatewayConnection) {
    UE_LOG(LogLyra, Error,
           TEXT("ALyraGameMode::InitializeGatewayConnection: Failed to create "
                "GatewayConnection"));
    return;
  }

  FString GatewayAddress = TEXT("127.0.0.1");
  int32 GatewayPort = 18080;

  // Try to get from command line
  FString CmdLine = FCommandLine::Get();
  FParse::Value(*CmdLine, TEXT("WebSocketGateway="), GatewayAddress);
  FParse::Value(*CmdLine, TEXT("WebSocketGatewayPort="), GatewayPort);

  // Fall back to config
  if (GatewayAddress == TEXT("127.0.0.1") && GConfig) {
    GConfig->GetString(TEXT("WebSocketGateway"), TEXT("GatewayAddress"),
                       GatewayAddress, GEngineIni);
    GConfig->GetInt(TEXT("WebSocketGateway"), TEXT("GatewayPort"), GatewayPort,
                    GEngineIni);
  }

  GatewayConnection->OnPlayerInputReceived.AddDynamic(
      this, &ALyraGameMode::OnPlayerInputReceived);
  GatewayConnection->OnGatewayConnected.AddDynamic(
      this, &ALyraGameMode::OnGatewayConnected);
  GatewayConnection->OnGatewayDisconnected.AddDynamic(
      this, &ALyraGameMode::OnGatewayDisconnected);
  GatewayConnection->Initialize(GatewayAddress, GatewayPort);

  UE_LOG(LogLyra, Log,
         TEXT("ALyraGameMode::InitializeGatewayConnection: Initialized "
              "connection to %s:%d"),
         *GatewayAddress, GatewayPort);
}

ALyraPlayerController *
ALyraGameMode::FindPlayerControllerByID(const FString &PlayerID) {
  if (PlayerID.IsEmpty()) {
    return nullptr;
  }

  for (FConstPlayerControllerIterator It =
           GetWorld()->GetPlayerControllerIterator();
       It; ++It) {
    if (ALyraPlayerController *LyraPC =
            Cast<ALyraPlayerController>(It->Get())) {
      if (ALyraPlayerState *LyraPS =
              LyraPC->GetPlayerState<ALyraPlayerState>()) {
        FString OriginalId = LyraPS->GetUniqueId().ToString();
        FString PCPlayerID = ALyraPlayerController::GenerateShortPlayerId(OriginalId);
        if (PCPlayerID == PlayerID) {
          return LyraPC;
        }
      }
    }
  }

  return nullptr;
}

void ALyraGameMode::OnPlayerInputReceived(const TArray<uint8> &InputData) {
  FProtobufCodec::FClientMessage ClientMsg;
  if (!FProtobufCodec::DecodeClientMessage(InputData, ClientMsg)) {
    UE_LOG(LogLyra, Warning,
           TEXT("ALyraGameMode::OnPlayerInputReceived: Failed to decode "
                "PlayerInput"));
    return;
  }

  if (ClientMsg.Type !=
      FProtobufCodec::FClientMessage::EMessageType::PlayerInput) {
    return;
  }

  const FProtobufCodec::FPlayerInput &PlayerInput = ClientMsg.PlayerInput;
  FString PlayerID = PlayerInput.PlayerId;

  UE_LOG(LogLyra, VeryVerbose,
         TEXT("ALyraGameMode::OnPlayerInputReceived: PlayerID=%s, Tick=%lld, "
              "MoveX=%.2f, MoveY=%.2f, AimX=%.2f, AimY=%.2f, Shoot=%d"),
         *PlayerID, PlayerInput.Tick,
         FProtobufCodec::DequantizeCoordinate(PlayerInput.MoveX),
         FProtobufCodec::DequantizeCoordinate(PlayerInput.MoveY),
         FProtobufCodec::DequantizeCoordinate(PlayerInput.AimX),
         FProtobufCodec::DequantizeCoordinate(PlayerInput.AimY),
         PlayerInput.Shoot ? 1 : 0);

  ALyraPlayerController *LyraPC = FindPlayerControllerByID(PlayerID);
  if (!LyraPC) {
    if (!PlayerID.IsEmpty() && PlayerID.Len() > 0 && PlayerID.Len() < 100) {
      static TSet<FString> LoggedMissingPlayerIDs;
      static FCriticalSection LoggedMissingPlayerIDsCS;
      FScopeLock Lock(&LoggedMissingPlayerIDsCS);
      
      if (!LoggedMissingPlayerIDs.Contains(PlayerID)) {
        LoggedMissingPlayerIDs.Add(PlayerID);
      UE_LOG(LogLyra, Warning,
             TEXT("ALyraGameMode::OnPlayerInputReceived: No player controller "
                  "found for PlayerID=%s. Available controllers:"),
             *PlayerID);
      
      for (FConstPlayerControllerIterator It = GetWorld()->GetPlayerControllerIterator(); It; ++It) {
        if (ALyraPlayerController *PC = Cast<ALyraPlayerController>(It->Get())) {
          FString PCPlayerID = TEXT("UNKNOWN");
          if (ALyraPlayerState *LyraPS = PC->GetPlayerState<ALyraPlayerState>()) {
            FString OriginalId = LyraPS->GetUniqueId().ToString();
            PCPlayerID = ALyraPlayerController::GenerateShortPlayerId(OriginalId);
          } else if (APlayerState *PS = PC->GetPlayerState<APlayerState>()) {
            FString OriginalId = PS->GetUniqueId().ToString();
            PCPlayerID = ALyraPlayerController::GenerateShortPlayerId(OriginalId);
          }
          UE_LOG(LogLyra, Warning,
                 TEXT("  - Controller: %s, PlayerID: %s"),
                 *PC->GetName(), *PCPlayerID);
        }
      }
      } else {
        UE_LOG(LogLyra, VeryVerbose,
               TEXT("ALyraGameMode::OnPlayerInputReceived: No player controller "
                    "found for PlayerID=%s (already logged)"),
               *PlayerID);
      }
    } else {
      UE_LOG(LogLyra, VeryVerbose,
             TEXT("ALyraGameMode::OnPlayerInputReceived: No player controller "
                  "found for PlayerID (invalid or empty)"));
    }
    
    return;
  }

  APawn *Pawn = LyraPC->GetPawn();
  if (!Pawn) {
    UE_LOG(
        LogLyra, Warning,
        TEXT("ALyraGameMode::OnPlayerInputReceived: No pawn for PlayerID=%s"),
        *PlayerID);
    return;
  }

  ALyraCharacter *Character = Cast<ALyraCharacter>(Pawn);
  if (!Character) {
    UE_LOG(LogLyra, Warning,
           TEXT("ALyraGameMode::OnPlayerInputReceived: Pawn is not "
                "ALyraCharacter for PlayerID=%s"),
           *PlayerID);
    return;
  }

  FRotator ControlRotation = LyraPC->GetControlRotation();

  if (FMath::Abs(PlayerInput.AimX) > 0.01f ||
      FMath::Abs(PlayerInput.AimY) > 0.01f) {
    ControlRotation.Yaw += PlayerInput.AimX * 2.0f;
    ControlRotation.Pitch = FMath::Clamp(
        ControlRotation.Pitch + PlayerInput.AimY * 2.0f, -89.0f, 89.0f);
    LyraPC->SetControlRotation(ControlRotation);
  }

  if (FMath::Abs(PlayerInput.MoveX) > 0.01f ||
      FMath::Abs(PlayerInput.MoveY) > 0.01f) {
    const FRotator MovementRotation(0.0f, ControlRotation.Yaw, 0.0f);
    const FVector ForwardVector =
        MovementRotation.RotateVector(FVector::ForwardVector);
    const FVector RightVector =
        MovementRotation.RotateVector(FVector::RightVector);

    FVector MovementDirection =
        (ForwardVector * PlayerInput.MoveY) + (RightVector * PlayerInput.MoveX);
    MovementDirection.Normalize();

    Character->AddMovementInput(MovementDirection, 1.0f);
  }

  if (PlayerInput.Shoot) {
    UE_LOG(LogLyra, Log,
           TEXT("ALyraGameMode::OnPlayerInputReceived: Shoot action for "
                "PlayerID=%s"),
           *PlayerID);
    
    if (ULyraAbilitySystemComponent* ASC = Character->GetLyraAbilitySystemComponent()) {
      if (ULyraEquipmentManagerComponent* EquipmentManager = Character->FindComponentByClass<ULyraEquipmentManagerComponent>()) {
        if (ULyraRangedWeaponInstance* WeaponInstance = EquipmentManager->GetFirstInstanceOfType<ULyraRangedWeaponInstance>()) {
          FGameplayAbilitySpec* WeaponAbilitySpec = nullptr;
          
          for (FGameplayAbilitySpec& Spec : ASC->GetActivatableAbilities()) {
            if (Spec.SourceObject == WeaponInstance) {
              if (ULyraGameplayAbility_RangedWeapon* WeaponAbility = Cast<ULyraGameplayAbility_RangedWeapon>(Spec.Ability)) {
                if (WeaponAbility->GetWeaponInstance() == WeaponInstance) {
                  WeaponAbilitySpec = &Spec;
                  break;
                }
              }
            }
          }
          
          if (WeaponAbilitySpec) {
            if (ASC->TryActivateAbility(WeaponAbilitySpec->Handle)) {
              UE_LOG(LogLyra, Log,
                     TEXT("ALyraGameMode::OnPlayerInputReceived: Successfully activated weapon ability for PlayerID=%s"),
                     *PlayerID);
            } else {
              UE_LOG(LogLyra, Warning,
                     TEXT("ALyraGameMode::OnPlayerInputReceived: Failed to activate weapon ability for PlayerID=%s"),
                     *PlayerID);
            }
          } else {
            UE_LOG(LogLyra, Warning,
                   TEXT("ALyraGameMode::OnPlayerInputReceived: No weapon ability found for PlayerID=%s"),
                   *PlayerID);
          }
        } else {
          UE_LOG(LogLyra, Warning,
                 TEXT("ALyraGameMode::OnPlayerInputReceived: No ranged weapon equipped for PlayerID=%s"),
                 *PlayerID);
        }
      } else {
        UE_LOG(LogLyra, Warning,
               TEXT("ALyraGameMode::OnPlayerInputReceived: No EquipmentManager found for PlayerID=%s"),
               *PlayerID);
      }
    } else {
      UE_LOG(LogLyra, Warning,
             TEXT("ALyraGameMode::OnPlayerInputReceived: No AbilitySystemComponent found for PlayerID=%s"),
             *PlayerID);
    }
  }
}

void ALyraGameMode::UpdateAndSendGameState() {
  if (!GatewayConnection) {
    UE_LOG(LogLyra, Warning,
           TEXT("ALyraGameMode::UpdateAndSendGameState: GatewayConnection is "
                "NULL"));
    return;
  }

  if (!GatewayConnection->IsConnected()) {
    UE_LOG(LogLyra, Warning,
           TEXT("ALyraGameMode::UpdateAndSendGameState: GatewayConnection not "
                "connected"));
    return;
  }

  FProtobufCodec::FServerMessage ServerMsg;
  ServerMsg.Type = FProtobufCodec::FServerMessage::EMessageType::GameState;

  static int64 ServerTick = 0;
  ServerTick++;
  ServerMsg.GameState.Snapshot.Tick = ServerTick;

  int32 EntityIndex = 0;
  for (FConstPlayerControllerIterator It =
           GetWorld()->GetPlayerControllerIterator();
       It; ++It) {
    if (APlayerController *PC = It->Get()) {
      if (APawn *Pawn = PC->GetPawn()) {
        FProtobufCodec::FEntityState Entity;

        if (ALyraPlayerState *LyraPS = PC->GetPlayerState<ALyraPlayerState>()) {
          FString OriginalId = LyraPS->GetUniqueId().ToString();
          Entity.Id = ALyraPlayerController::GenerateShortPlayerId(OriginalId);
        } else if (APlayerState *PS = PC->GetPlayerState<APlayerState>()) {
          FString OriginalId = PS->GetUniqueId().ToString();
          Entity.Id = ALyraPlayerController::GenerateShortPlayerId(OriginalId);
        } else {
          Entity.Id = FString::Printf(TEXT("Player_%d"), EntityIndex);
        }

        FVector Location = Pawn->GetActorLocation();
        Entity.X = FProtobufCodec::QuantizeCoordinate(Location.X);
        Entity.Y = FProtobufCodec::QuantizeCoordinate(Location.Y);
        Entity.Z = FProtobufCodec::QuantizeCoordinate(Location.Z);

        if (UCharacterMovementComponent *MovementComp =
                Pawn->FindComponentByClass<UCharacterMovementComponent>()) {
          FVector Velocity = MovementComp->Velocity;
          Entity.VX = FProtobufCodec::QuantizeCoordinate(Velocity.X);
          Entity.VY = FProtobufCodec::QuantizeCoordinate(Velocity.Y);
          Entity.VZ = FProtobufCodec::QuantizeCoordinate(Velocity.Z);
        }

        FRotator Rotation = Pawn->GetActorRotation();
        Entity.Yaw = FProtobufCodec::QuantizeCoordinate(Rotation.Yaw);

        ServerMsg.GameState.Snapshot.Entities.Add(Entity);
        EntityIndex++;
      }
    }
  }

  TArray<uint8> EncodedMessage = FProtobufCodec::EncodeServerMessage(ServerMsg);
  GatewayConnection->SendGameStateUpdate(EncodedMessage);
}

void ALyraGameMode::OnGatewayConnected(bool bConnected)
{
	UE_LOG(LogLyra, Log, TEXT("ALyraGameMode::OnGatewayConnected: Gateway %s"), bConnected ? TEXT("CONNECTED") : TEXT("DISCONNECTED"));
	
	UWorld* World = GetWorld();
	if (!World) {
		return;
	}

	TArray<AActor*> AllActors;
	UGameplayStatics::GetAllActorsOfClass(World, ALyraCharacter::StaticClass(), AllActors);
	
	for (AActor* Actor : AllActors) {
		if (ALyraCharacter* Character = Cast<ALyraCharacter>(Actor)) {
			if (IsValid(Character)) {
				Character->UpdateMovementReplication();
			}
		}
	}
}

void ALyraGameMode::OnGatewayDisconnected(const FString& Reason)
{
	const bool bNormalClose = Reason.Contains(TEXT("Successfully closed")) || Reason.Contains(TEXT("Disconnected"));
	if (bNormalClose) {
		UE_LOG(LogLyra, Log, TEXT("ALyraGameMode::OnGatewayDisconnected: %s"), *Reason);
	} else {
		UE_LOG(LogLyra, Warning, TEXT("ALyraGameMode::OnGatewayDisconnected: %s"), *Reason);
	}
	
	UWorld* World = GetWorld();
	if (!World) {
		return;
	}

	TArray<AActor*> AllActors;
	UGameplayStatics::GetAllActorsOfClass(World, ALyraCharacter::StaticClass(), AllActors);
	
	for (AActor* Actor : AllActors) {
		if (ALyraCharacter* Character = Cast<ALyraCharacter>(Actor)) {
			if (IsValid(Character)) {
				Character->UpdateMovementReplication();
			}
		}
	}
}
