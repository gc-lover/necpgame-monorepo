#pragma once

#include "CoreMinimal.h"
#include "Net/EntityStateHistoryManager.h"
#include "UObject/Interface.h"
#include "MovementInterpolator.generated.h"

UINTERFACE(MinimalAPI)
class UMovementInterpolator : public UInterface {
  GENERATED_BODY()
};

class LYRAGAME_API IMovementInterpolator {
  GENERATED_BODY()

public:
  virtual FVector InterpolateLocation(const FVector &OldLocation,
                                      const FVector &NewLocation,
                                      float Alpha) const = 0;
  virtual float InterpolateYaw(float OldYaw, float NewYaw,
                               float Alpha) const = 0;
  virtual FVector InterpolateVelocity(const FVector &OldVelocity,
                                      const FVector &NewVelocity,
                                      float Alpha) const = 0;
  virtual void InterpolateSnapshot(const FEntityStateSnapshot &OldState,
                                   const FEntityStateSnapshot &NewState,
                                   float Alpha, FVector &OutLocation,
                                   float &OutYaw,
                                   FVector &OutVelocity) const = 0;
};

UCLASS()
class LYRAGAME_API ULinearMovementInterpolator : public UObject,
                                                 public IMovementInterpolator {
  GENERATED_BODY()

public:
  virtual FVector InterpolateLocation(const FVector &OldLocation,
                                      const FVector &NewLocation,
                                      float Alpha) const override;
  virtual float InterpolateYaw(float OldYaw, float NewYaw,
                               float Alpha) const override;
  virtual FVector InterpolateVelocity(const FVector &OldVelocity,
                                      const FVector &NewVelocity,
                                      float Alpha) const override;
  virtual void InterpolateSnapshot(const FEntityStateSnapshot &OldState,
                                   const FEntityStateSnapshot &NewState,
                                   float Alpha, FVector &OutLocation,
                                   float &OutYaw,
                                   FVector &OutVelocity) const override;
};
