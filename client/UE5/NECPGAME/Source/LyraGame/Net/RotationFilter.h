#pragma once

#include "CoreMinimal.h"
#include "UObject/Interface.h"
#include "RotationFilter.generated.h"

UINTERFACE(MinimalAPI)
class URotationFilter : public UInterface {
  GENERATED_BODY()
};

class LYRAGAME_API IRotationFilter {
  GENERATED_BODY()

public:
  virtual FRotator FilterRotation(const FRotator &CurrentRotation, float NewYaw,
                                  float DeltaTime) const = 0;
  virtual bool ShouldUpdateRotation(float YawDelta) const = 0;
};

UCLASS()
class LYRAGAME_API UYawOnlyRotationFilter : public UObject,
                                            public IRotationFilter {
  GENERATED_BODY()

public:
  static constexpr float MinYawDelta = 2.0f;
  static constexpr float LargeYawDelta = 180.0f;
  static constexpr float YawInterpolationSpeed = 25.0f;
  static constexpr float SmoothYawInterpolationSpeed = 30.0f;

  virtual FRotator FilterRotation(const FRotator &CurrentRotation, float NewYaw,
                                  float DeltaTime) const override;
  virtual bool ShouldUpdateRotation(float YawDelta) const override;
};
