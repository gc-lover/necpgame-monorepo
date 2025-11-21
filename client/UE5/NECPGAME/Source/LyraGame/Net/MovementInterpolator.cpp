#include "Net/MovementInterpolator.h"
#include "Net/EntityStateHistoryManager.h"

FVector ULinearMovementInterpolator::InterpolateLocation(
    const FVector &OldLocation, const FVector &NewLocation, float Alpha) const {
  return FMath::Lerp(OldLocation, NewLocation, Alpha);
}

float ULinearMovementInterpolator::InterpolateYaw(float OldYaw, float NewYaw,
                                                  float Alpha) const {
  return FMath::Lerp(OldYaw, NewYaw, Alpha);
}

FVector ULinearMovementInterpolator::InterpolateVelocity(
    const FVector &OldVelocity, const FVector &NewVelocity, float Alpha) const {
  return FMath::Lerp(OldVelocity, NewVelocity, Alpha);
}

void ULinearMovementInterpolator::InterpolateSnapshot(
    const FEntityStateSnapshot &OldState, const FEntityStateSnapshot &NewState,
    float Alpha, FVector &OutLocation, float &OutYaw,
    FVector &OutVelocity) const {
  OutLocation =
      InterpolateLocation(OldState.Location, NewState.Location, Alpha);
  OutYaw = InterpolateYaw(OldState.Rotation.Yaw, NewState.Rotation.Yaw, Alpha);
  OutVelocity =
      InterpolateVelocity(OldState.Velocity, NewState.Velocity, Alpha);
}
