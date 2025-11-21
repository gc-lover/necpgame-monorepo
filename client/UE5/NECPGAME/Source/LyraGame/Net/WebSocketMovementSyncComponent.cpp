#include "Net/WebSocketMovementSyncComponent.h"
#include "Character/LyraCharacter.h"
#include "GameFramework/CharacterMovementComponent.h"
#include "GameFramework/Pawn.h"
#include "LyraLogChannels.h"
#include "Net/EntityStateHistoryManager.h"
#include "Net/MovementApplier.h"
#include "Net/MovementInterpolator.h"
#include "Net/PlayerIdResolver.h"
#include "Net/ProtobufCodec.h"
#include "Net/RotationFilter.h"
#include "Player/LyraPlayerController.h"
#include "Player/LyraPlayerState.h"

UWebSocketMovementSyncComponent::UWebSocketMovementSyncComponent(
    const FObjectInitializer &ObjectInitializer)
    : Super(ObjectInitializer) {
  PrimaryComponentTick.bCanEverTick = true;
  PrimaryComponentTick.TickGroup = TG_PrePhysics;
}

void UWebSocketMovementSyncComponent::BeginPlay() {
  Super::BeginPlay();

  PlayerIdResolver = NewObject<UPlayerIdResolver>(this);
  HistoryManager = NewObject<UEntityStateHistoryManager>(this);
  MovementInterpolatorObject = NewObject<ULinearMovementInterpolator>(this);
  RotationFilterObject = NewObject<UYawOnlyRotationFilter>(this);
  CharacterMovementApplier = NewObject<UCharacterMovementApplier>(this);
  BasicPawnMovementApplier = NewObject<UBasicPawnMovementApplier>(this);
}

void UWebSocketMovementSyncComponent::EndPlay(
    const EEndPlayReason::Type EndPlayReason) {
  Super::EndPlay(EndPlayReason);

  if (HistoryManager) {
    HistoryManager->Clear();
  }
}

void UWebSocketMovementSyncComponent::TickComponent(
    float DeltaTime, ELevelTick TickType,
    FActorComponentTickFunction *ThisTickFunction) {
  Super::TickComponent(DeltaTime, TickType, ThisTickFunction);

  UWorld *World = GetWorld();
  if (!World || !IsValid(World) || !HistoryManager) {
    return;
  }

  float CurrentTime = World->GetTimeSeconds();
  float InterpolationTime = CurrentTime - InterpolationDelay;

  TArray<FString> EntityIds = HistoryManager->GetAllEntityIds();

  for (const FString &EntityId : EntityIds) {
    ProcessInterpolationForEntity(EntityId, World);
  }
}

void UWebSocketMovementSyncComponent::ProcessInterpolationForEntity(
    const FString &EntityId, UWorld *World) {
  if (!World || !HistoryManager || !PlayerIdResolver) {
    return;
  }

  TArray<FEntityStateSnapshot> HistoryCopy =
      HistoryManager->GetHistory(EntityId);
  if (HistoryCopy.Num() == 0) {
    return;
  }

  APlayerController *TargetController =
      PlayerIdResolver->FindControllerByPlayerId(EntityId, World);
  if (!TargetController) {
    return;
  }

  APawn *TargetPawn = TargetController->GetPawn();
  if (!TargetPawn || !IsValid(TargetPawn) ||
      TargetPawn->IsLocallyControlled()) {
    return;
  }

  float CurrentTime = World->GetTimeSeconds();
  float InterpolationTime = CurrentTime - InterpolationDelay;

  if (HistoryCopy.Num() == 1) {
    ApplySingleSnapshot(TargetPawn, HistoryCopy[0], World);
    return;
  }

  bool bAppliedMovement = false;
  for (int32 i = HistoryCopy.Num() - 1; i > 0; --i) {
    const FEntityStateSnapshot &OldSnapshot = HistoryCopy[i - 1];
    const FEntityStateSnapshot &NewSnapshot = HistoryCopy[i];

    if (InterpolationTime >= OldSnapshot.Timestamp &&
        InterpolationTime <= NewSnapshot.Timestamp) {
      float TimeDelta = NewSnapshot.Timestamp - OldSnapshot.Timestamp;
      if (TimeDelta > 0.001f) {
        float Alpha = (InterpolationTime - OldSnapshot.Timestamp) / TimeDelta;
        ApplyInterpolatedSnapshot(TargetPawn, OldSnapshot, NewSnapshot, Alpha,
                                  World);
      } else {
        ApplyInterpolatedSnapshot(TargetPawn, OldSnapshot, NewSnapshot, 1.0f,
                                  World);
      }
      bAppliedMovement = true;
      break;
    } else if (InterpolationTime > NewSnapshot.Timestamp &&
               i == HistoryCopy.Num() - 1) {
      float TimeSinceLastUpdate = InterpolationTime - NewSnapshot.Timestamp;
      ApplyExtrapolatedSnapshot(TargetPawn, NewSnapshot, TimeSinceLastUpdate,
                                World);
      bAppliedMovement = true;
      break;
    }
  }

  if (!bAppliedMovement && HistoryCopy.Num() > 0) {
    const FEntityStateSnapshot &LastSnapshot =
        HistoryCopy[HistoryCopy.Num() - 1];
    ApplySingleSnapshot(TargetPawn, LastSnapshot, World);
  }

  HistoryManager->CleanupOldSnapshots(EntityId, InterpolationTime);
}

void UWebSocketMovementSyncComponent::ApplySingleSnapshot(
    APawn *TargetPawn, const FEntityStateSnapshot &Snapshot, UWorld *World) {
  if (!TargetPawn || !IsValid(TargetPawn) || !World) {
    return;
  }

  IMovementApplier *Applier = GetMovementApplier(TargetPawn);
  IRotationFilter *Filter = GetRotationFilter();

  if (!Applier || !Filter) {
    return;
  }

  FVector CurrentLocation = TargetPawn->GetActorLocation();
  FRotator CurrentRotation = TargetPawn->GetActorRotation();

  float LocationDistance = (Snapshot.Location - CurrentLocation).Size();
  if (LocationDistance > 0.1f) {
    Applier->ApplyLocation(TargetPawn, Snapshot.Location, true);
  }

  Applier->ApplyVelocity(TargetPawn, Snapshot.Velocity);

  float YawDelta = FMath::Abs(
      FRotator::NormalizeAxis(Snapshot.Rotation.Yaw - CurrentRotation.Yaw));
  if (Filter->ShouldUpdateRotation(YawDelta)) {
    float DeltaTime = World->GetDeltaSeconds();
    FRotator FilteredRotation = Filter->FilterRotation(
        CurrentRotation, Snapshot.Rotation.Yaw, DeltaTime);
    Applier->ApplyRotation(TargetPawn, FilteredRotation);
  }
}

void UWebSocketMovementSyncComponent::ApplyInterpolatedSnapshot(
    APawn *TargetPawn, const FEntityStateSnapshot &OldState,
    const FEntityStateSnapshot &NewState, float Alpha, UWorld *World) {
  if (!TargetPawn || !IsValid(TargetPawn) || !World) {
    return;
  }

  IMovementInterpolator *Interpolator = GetMovementInterpolator();
  IMovementApplier *Applier = GetMovementApplier(TargetPawn);
  IRotationFilter *Filter = GetRotationFilter();

  if (!Interpolator || !Applier || !Filter) {
    return;
  }

  FVector InterpolatedLocation;
  float InterpolatedYaw;
  FVector InterpolatedVelocity;
  Interpolator->InterpolateSnapshot(OldState, NewState, Alpha,
                                    InterpolatedLocation, InterpolatedYaw,
                                    InterpolatedVelocity);

  FVector CurrentLocation = TargetPawn->GetActorLocation();
  float LocationDistance = (InterpolatedLocation - CurrentLocation).Size();

  if (LocationDistance > 0.1f) {
    if (Applier->ShouldTeleport(CurrentLocation, InterpolatedLocation)) {
      Applier->ApplyLocation(TargetPawn, InterpolatedLocation, false);
    } else {
      Applier->ApplyLocation(TargetPawn, InterpolatedLocation, true);
    }
  }

  Applier->ApplyVelocity(TargetPawn, InterpolatedVelocity);

  FRotator CurrentRotation = TargetPawn->GetActorRotation();
  float YawDelta = FMath::Abs(
      FRotator::NormalizeAxis(InterpolatedYaw - CurrentRotation.Yaw));

  if (Filter->ShouldUpdateRotation(YawDelta)) {
    FRotator FilteredRotation = Filter->FilterRotation(
        CurrentRotation, InterpolatedYaw, World->GetDeltaSeconds());
    Applier->ApplyRotation(TargetPawn, FilteredRotation);
  }
}

void UWebSocketMovementSyncComponent::ApplyExtrapolatedSnapshot(
    APawn *TargetPawn, const FEntityStateSnapshot &Snapshot,
    float TimeSinceLastUpdate, UWorld *World) {
  if (!TargetPawn || !IsValid(TargetPawn) || !World) {
    return;
  }

  if (TimeSinceLastUpdate >= MaxExtrapolationTime ||
      Snapshot.Velocity.SizeSquared() <= 1.0f) {
    ApplySingleSnapshot(TargetPawn, Snapshot, World);
    return;
  }

  FEntityStateSnapshot ExtrapolatedState = Snapshot;
  ExtrapolatedState.Location =
      Snapshot.Location + (Snapshot.Velocity * TimeSinceLastUpdate);

  ApplyInterpolatedSnapshot(TargetPawn, Snapshot, ExtrapolatedState, 1.0f,
                            World);
}

void UWebSocketMovementSyncComponent::OnGameStateReceived(
    const TArray<uint8> &GameStateData) {
  if (!IsValid(this) || GameStateData.Num() == 0) {
    return;
  }

  UWorld *World = GetWorld();
  if (!World || !IsValid(World) || World->bIsTearingDown || !HistoryManager ||
      !PlayerIdResolver) {
    return;
  }

  FProtobufCodec::FServerMessage ServerMsg;
  if (!FProtobufCodec::DecodeServerMessage(GameStateData, ServerMsg)) {
    return;
  }

  if (ServerMsg.Type !=
      FProtobufCodec::FServerMessage::EMessageType::GameState) {
    return;
  }

  ALyraPlayerController *OwnerPC = Cast<ALyraPlayerController>(GetOwner());
  if (!OwnerPC) {
    return;
  }

  FString LocalPlayerId = PlayerIdResolver->GetPlayerIdFromController(OwnerPC);
  if (LocalPlayerId.IsEmpty()) {
    return;
  }

  TMap<FString, APlayerController *> ControllerMap =
      PlayerIdResolver->BuildControllerMap(World);

  int64 GameStateTick = ServerMsg.GameState.Snapshot.Tick;
  for (const FProtobufCodec::FEntityState &Entity :
       ServerMsg.GameState.Snapshot.Entities) {
    const FString EntityId = Entity.Id;

    if (EntityId.IsEmpty() || EntityId == LocalPlayerId) {
      continue;
    }

    APlayerController *TargetController = ControllerMap.FindRef(EntityId);
    if (TargetController && IsValid(TargetController)) {
      ProcessEntityUpdate(Entity, GameStateTick, LocalPlayerId, World,
                          TargetController);
    }
  }
}

void UWebSocketMovementSyncComponent::ProcessEntityUpdate(
    const FProtobufCodec::FEntityState &Entity, int64 GameStateTick,
    const FString &LocalPlayerId, UWorld *World,
    APlayerController *TargetController) {
  if (!TargetController || !HistoryManager || !World) {
    return;
  }

  APawn *TargetPawn = TargetController->GetPawn();
  if (!TargetPawn || !IsValid(TargetPawn) ||
      TargetPawn->IsLocallyControlled()) {
    return;
  }

  FVector NewLocation(FProtobufCodec::DequantizeCoordinate(Entity.X),
                      FProtobufCodec::DequantizeCoordinate(Entity.Y),
                      FProtobufCodec::DequantizeCoordinate(Entity.Z));

  FRotator CurrentRotation = TargetPawn->GetActorRotation();
  float NewYaw = FProtobufCodec::DequantizeCoordinate(Entity.Yaw);
  FRotator NewRotation(CurrentRotation.Pitch, NewYaw, CurrentRotation.Roll);

  FVector NewVelocity(FProtobufCodec::DequantizeCoordinate(Entity.VX),
                      FProtobufCodec::DequantizeCoordinate(Entity.VY),
                      FProtobufCodec::DequantizeCoordinate(Entity.VZ));

  float CurrentTime = World->GetTimeSeconds();
  FEntityStateSnapshot NewSnapshot;
  NewSnapshot.Location = NewLocation;
  NewSnapshot.Rotation = NewRotation;
  NewSnapshot.Velocity = NewVelocity;
  NewSnapshot.Timestamp = CurrentTime;
  NewSnapshot.Tick = GameStateTick;

  HistoryManager->AddSnapshot(Entity.Id, NewSnapshot);
}

IMovementInterpolator *
UWebSocketMovementSyncComponent::GetMovementInterpolator() const {
  return Cast<IMovementInterpolator>(MovementInterpolatorObject);
}

IRotationFilter *UWebSocketMovementSyncComponent::GetRotationFilter() const {
  return Cast<IRotationFilter>(RotationFilterObject);
}

IMovementApplier *
UWebSocketMovementSyncComponent::GetMovementApplier(APawn *Pawn) const {
  if (!Pawn) {
    return nullptr;
  }

  if (Cast<ALyraCharacter>(Pawn)) {
    return CharacterMovementApplier.Get();
  }

  return BasicPawnMovementApplier.Get();
}
