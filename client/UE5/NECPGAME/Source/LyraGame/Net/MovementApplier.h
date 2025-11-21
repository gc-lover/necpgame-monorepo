#pragma once

#include "CoreMinimal.h"
#include "UObject/Interface.h"
#include "MovementApplier.generated.h"

UINTERFACE(MinimalAPI)
class UMovementApplier : public UInterface {
  GENERATED_BODY()
};

class LYRAGAME_API IMovementApplier {
  GENERATED_BODY()

public:
  virtual void ApplyLocation(APawn *Pawn, const FVector &Location,
                             bool bSweep) = 0;
  virtual void ApplyRotation(APawn *Pawn, const FRotator &Rotation) = 0;
  virtual void ApplyVelocity(APawn *Pawn, const FVector &Velocity) = 0;
  virtual bool ShouldTeleport(const FVector &CurrentLocation,
                              const FVector &NewLocation) const = 0;
};

UCLASS()
class LYRAGAME_API UCharacterMovementApplier : public UObject,
                                               public IMovementApplier {
  GENERATED_BODY()

public:
  static constexpr float MaxTeleportDistance = 1000.0f;
  static constexpr float HorizontalThreshold = 50.0f;
  static constexpr float LocationThreshold = 5.0f;
  static constexpr float LocationInterpolationThreshold = 0.1f;
  static constexpr float VelocityThreshold = 1.0f;

  virtual void ApplyLocation(APawn *Pawn, const FVector &Location,
                             bool bSweep) override;
  virtual void ApplyRotation(APawn *Pawn, const FRotator &Rotation) override;
  virtual void ApplyVelocity(APawn *Pawn, const FVector &Velocity) override;
  virtual bool ShouldTeleport(const FVector &CurrentLocation,
                              const FVector &NewLocation) const override;

private:
  void ApplyLocationToCharacter(APawn *Pawn, const FVector &NewLocation,
                                const FVector &NewVelocity);
};

UCLASS()
class LYRAGAME_API UBasicPawnMovementApplier : public UObject,
                                               public IMovementApplier {
  GENERATED_BODY()

public:
  virtual void ApplyLocation(APawn *Pawn, const FVector &Location,
                             bool bSweep) override;
  virtual void ApplyRotation(APawn *Pawn, const FRotator &Rotation) override;
  virtual void ApplyVelocity(APawn *Pawn, const FVector &Velocity) override;
  virtual bool ShouldTeleport(const FVector &CurrentLocation,
                              const FVector &NewLocation) const override;
};
