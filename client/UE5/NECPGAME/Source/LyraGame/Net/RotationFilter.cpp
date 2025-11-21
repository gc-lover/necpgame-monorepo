#include "Net/RotationFilter.h"
#include "Engine/World.h"

FRotator UYawOnlyRotationFilter::FilterRotation(const FRotator &CurrentRotation,
                                                float NewYaw,
                                                float DeltaTime) const {
  float YawDelta =
      FMath::Abs(FRotator::NormalizeAxis(NewYaw - CurrentRotation.Yaw));

  if (YawDelta > LargeYawDelta) {
    return FRotator(CurrentRotation.Pitch, NewYaw, CurrentRotation.Roll);
  }

  if (DeltaTime > 0.0f) {
    float InterpolatedYaw = FMath::FInterpTo(CurrentRotation.Yaw, NewYaw,
                                             DeltaTime, YawInterpolationSpeed);
    return FRotator(CurrentRotation.Pitch, InterpolatedYaw,
                    CurrentRotation.Roll);
  }

  return FRotator(CurrentRotation.Pitch, NewYaw, CurrentRotation.Roll);
}

bool UYawOnlyRotationFilter::ShouldUpdateRotation(float YawDelta) const {
  return YawDelta > MinYawDelta;
}
