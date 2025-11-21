#include "Net/MovementApplier.h"
#include "Character/LyraCharacter.h"
#include "Engine/World.h"
#include "GameFramework/CharacterMovementComponent.h"
#include "GameFramework/Pawn.h"

void UCharacterMovementApplier::ApplyLocation(APawn *Pawn,
                                              const FVector &Location,
                                              bool bSweep) {
  if (!Pawn || !IsValid(Pawn)) {
    return;
  }

  ALyraCharacter *LyraChar = Cast<ALyraCharacter>(Pawn);
  if (!LyraChar) {
    Pawn->SetActorLocation(Location, bSweep);
    return;
  }

  UCharacterMovementComponent *MovementComp = LyraChar->GetCharacterMovement();
  if (!MovementComp) {
    Pawn->SetActorLocation(Location, bSweep);
    return;
  }

  FVector CurrentLocation = Pawn->GetActorLocation();
  FVector LocationDelta = Location - CurrentLocation;
  float LocationDistance = LocationDelta.Size();
  float HorizontalDistance =
      FVector(LocationDelta.X, LocationDelta.Y, 0.0f).Size();

  MovementComp->StopMovementImmediately();
  MovementComp->Velocity = FVector::ZeroVector;

  if (LocationDistance > MaxTeleportDistance) {
    Pawn->SetActorLocation(Location, false);
  } else if (HorizontalDistance > HorizontalThreshold) {
    FVector HorizontalLocation(Location.X, Location.Y, CurrentLocation.Z);
    Pawn->SetActorLocation(HorizontalLocation, bSweep);
  } else {
    Pawn->SetActorLocation(Location, bSweep);
  }
}

void UCharacterMovementApplier::ApplyRotation(APawn *Pawn,
                                              const FRotator &Rotation) {
  if (!Pawn || !IsValid(Pawn)) {
    return;
  }

  Pawn->SetActorRotation(Rotation);
}

void UCharacterMovementApplier::ApplyVelocity(APawn *Pawn,
                                              const FVector &Velocity) {
  if (!Pawn || !IsValid(Pawn)) {
    return;
  }

  if (ALyraCharacter *LyraChar = Cast<ALyraCharacter>(Pawn)) {
    if (UCharacterMovementComponent *MovementComp =
            LyraChar->GetCharacterMovement()) {
      MovementComp->Velocity = Velocity;
      MovementComp->UpdateComponentVelocity();
    }
  }
}

bool UCharacterMovementApplier::ShouldTeleport(
    const FVector &CurrentLocation, const FVector &NewLocation) const {
  float Distance = (NewLocation - CurrentLocation).Size();
  return Distance > MaxTeleportDistance;
}

void UCharacterMovementApplier::ApplyLocationToCharacter(
    APawn *Pawn, const FVector &NewLocation, const FVector &NewVelocity) {
  if (!Pawn || !IsValid(Pawn)) {
    return;
  }

  ALyraCharacter *LyraChar = Cast<ALyraCharacter>(Pawn);
  if (!LyraChar) {
    Pawn->SetActorLocation(NewLocation, true);
    return;
  }

  UCharacterMovementComponent *MovementComp = LyraChar->GetCharacterMovement();
  if (!MovementComp) {
    Pawn->SetActorLocation(NewLocation, true);
    return;
  }

  FVector CurrentLocation = Pawn->GetActorLocation();
  FVector LocationDelta = NewLocation - CurrentLocation;
  float LocationDistance = LocationDelta.Size();
  float HorizontalDistance =
      FVector(LocationDelta.X, LocationDelta.Y, 0.0f).Size();

  if (LocationDistance > MaxTeleportDistance) {
    MovementComp->Velocity = NewVelocity;
    Pawn->SetActorLocation(NewLocation, false);
  } else if (HorizontalDistance > HorizontalThreshold) {
    FVector HorizontalLocation(NewLocation.X, NewLocation.Y, CurrentLocation.Z);
    MovementComp->Velocity =
        FVector(NewVelocity.X, NewVelocity.Y, MovementComp->Velocity.Z);
    Pawn->SetActorLocation(HorizontalLocation, true);
  } else if (LocationDistance > LocationThreshold) {
    MovementComp->Velocity =
        FVector(NewVelocity.X, NewVelocity.Y, MovementComp->Velocity.Z);
    Pawn->SetActorLocation(NewLocation, true);
  } else if (LocationDistance > LocationInterpolationThreshold) {
    MovementComp->Velocity =
        FVector(NewVelocity.X, NewVelocity.Y, MovementComp->Velocity.Z);
    Pawn->SetActorLocation(NewLocation, true);
  } else if (NewVelocity.SizeSquared2D() > FMath::Square(VelocityThreshold)) {
    MovementComp->Velocity =
        FVector(NewVelocity.X, NewVelocity.Y, MovementComp->Velocity.Z);
  } else {
    MovementComp->Velocity = FVector(0.0f, 0.0f, MovementComp->Velocity.Z);
  }
}

void UBasicPawnMovementApplier::ApplyLocation(APawn *Pawn,
                                              const FVector &Location,
                                              bool bSweep) {
  if (!Pawn || !IsValid(Pawn)) {
    return;
  }

  Pawn->SetActorLocation(Location, bSweep);
}

void UBasicPawnMovementApplier::ApplyRotation(APawn *Pawn,
                                              const FRotator &Rotation) {
  if (!Pawn || !IsValid(Pawn)) {
    return;
  }

  Pawn->SetActorRotation(Rotation);
}

void UBasicPawnMovementApplier::ApplyVelocity(APawn *Pawn,
                                              const FVector &Velocity) {}

bool UBasicPawnMovementApplier::ShouldTeleport(
    const FVector &CurrentLocation, const FVector &NewLocation) const {
  return false;
}
