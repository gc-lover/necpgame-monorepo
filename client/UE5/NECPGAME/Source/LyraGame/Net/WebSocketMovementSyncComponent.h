#pragma once

#include "Components/ActorComponent.h"
#include "CoreMinimal.h"
#include "Net/EntityStateHistoryManager.h"
#include "Net/MovementApplier.h"
#include "Net/MovementInterpolator.h"
#include "Net/PlayerIdResolver.h"
#include "Net/ProtobufCodec.h"
#include "Net/RotationFilter.h"
#include "WebSocketMovementSyncComponent.generated.h"

UCLASS(ClassGroup = (Custom), meta = (BlueprintSpawnableComponent))
class LYRAGAME_API UWebSocketMovementSyncComponent : public UActorComponent {
  GENERATED_BODY()

public:
  UWebSocketMovementSyncComponent(
      const FObjectInitializer &ObjectInitializer = FObjectInitializer::Get());

  virtual void BeginPlay() override;
  virtual void EndPlay(const EEndPlayReason::Type EndPlayReason) override;
  virtual void
  TickComponent(float DeltaTime, ELevelTick TickType,
                FActorComponentTickFunction *ThisTickFunction) override;

  void OnGameStateReceived(const TArray<uint8> &GameStateData);

private:
  void ProcessEntityUpdate(const FProtobufCodec::FEntityState &Entity,
                           int64 GameStateTick, const FString &LocalPlayerId,
                           UWorld *World, APlayerController *TargetController);
  void ProcessInterpolationForEntity(const FString &EntityId, UWorld *World);
  void ApplySingleSnapshot(APawn *TargetPawn,
                           const FEntityStateSnapshot &Snapshot, UWorld *World);
  void ApplyInterpolatedSnapshot(APawn *TargetPawn,
                                 const FEntityStateSnapshot &OldState,
                                 const FEntityStateSnapshot &NewState,
                                 float Alpha, UWorld *World);
  void ApplyExtrapolatedSnapshot(APawn *TargetPawn,
                                 const FEntityStateSnapshot &Snapshot,
                                 float TimeSinceLastUpdate, UWorld *World);

  UPROPERTY()
  TObjectPtr<UPlayerIdResolver> PlayerIdResolver;

  UPROPERTY()
  TObjectPtr<UEntityStateHistoryManager> HistoryManager;

  UPROPERTY()
  TObjectPtr<UObject> MovementInterpolatorObject;

  UPROPERTY()
  TObjectPtr<UObject> RotationFilterObject;

  UPROPERTY()
  TObjectPtr<UCharacterMovementApplier> CharacterMovementApplier;

  UPROPERTY()
  TObjectPtr<UBasicPawnMovementApplier> BasicPawnMovementApplier;

  IMovementInterpolator *GetMovementInterpolator() const;
  IRotationFilter *GetRotationFilter() const;
  IMovementApplier *GetMovementApplier(APawn *Pawn) const;

  static constexpr float InterpolationDelay = 0.1f;
  static constexpr float MaxExtrapolationTime = 0.1f;
};
