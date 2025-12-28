// Spectator Camera Manager Implementation
// Issue: #2213
// Advanced multi-mode camera system for tournament spectators

#include "SpectatorCameraManager.h"
#include "NTournamentSpectatorPlayerController.h"
#include "Engine/World.h"
#include "TimerManager.h"
#include "Kismet/GameplayStatics.h"
#include "DrawDebugHelpers.h"

USpectatorCameraManager::USpectatorCameraManager()
    : OwningPlayerController(nullptr)
    , CurrentZoomLevel(1.0f)
    , MinZoomDistance(100.0f)
    , MaxZoomDistance(2000.0f)
    , MaxViewingAngle(160.0f)
    , MinCameraDistance(200.0f)
    , MaxCameraDistance(5000.0f)
    , bIsTransitioning(false)
    , TransitionProgress(0.0f)
    , TransitionDuration(1.0f)
    , CameraUpdateFrequency(0.016f) // 60fps
{
    // Initialize camera mode data
    FreeCameraData = {
        FVector::ZeroVector,
        500.0f, // Move speed
        90.0f   // Rotation speed
    };

    FollowCameraData = {
        FVector(0.0f, -300.0f, 150.0f), // Offset behind and above
        400.0f,  // Follow distance
        100.0f,  // Height offset
        0.1f,    // Smoothing factor
        true     // Maintain line of sight
    };

    OverviewCameraData = {
        FVector::ZeroVector, // Will be set to tournament center
        1000.0f, // Overview height
        1500.0f  // Overview distance
    };

    CinematicCameraData = {
        TArray<FVector>(), // Will be populated with cinematic path
        0,      // Current path index
        200.0f  // Path speed
    };
}

void USpectatorCameraManager::InitializeCameraManager()
{
    CurrentCameraMode = TEXT("free");
    CurrentCameraLocation = FVector(0.0f, 0.0f, 500.0f);
    CurrentCameraRotation = FRotator(-15.0f, 0.0f, 0.0f);

    // Start camera update timer
    if (UWorld* World = GetWorld())
    {
        World->GetTimerManager().SetTimer(CameraUpdateTimer, this,
            &USpectatorCameraManager::UpdateCamera, CameraUpdateFrequency, true);
    }

    UE_LOG(LogTemp, Log, TEXT("Spectator Camera Manager initialized"));
}

void USpectatorCameraManager::InitializeForPlayer(ANTournamentSpectatorPlayerController* PlayerController)
{
    OwningPlayerController = PlayerController;

    // Set initial camera view
    if (OwningPlayerController && OwningPlayerController->PlayerCameraManager)
    {
        OwningPlayerController->PlayerCameraManager->SetViewTarget(nullptr);
        // Note: In a real implementation, we would create a custom camera actor
    }
}

void USpectatorCameraManager::InitializeTournamentSession(const FString& TournamentId, const FString& MatchId)
{
    CurrentTournamentId = TournamentId;
    CurrentMatchId = MatchId;

    // Calculate tournament center and bounds
    OverviewCameraData.OverviewCenter = GetTournamentCenter();

    // Initialize cinematic camera path
    InitializeCinematicPath();

    UE_LOG(LogTemp, Log, TEXT("Camera manager initialized for tournament: %s"), *TournamentId);
}

void USpectatorCameraManager::StartTournamentMode()
{
    // Switch to follow mode by default
    SwitchCameraMode(TEXT("follow"));

    UE_LOG(LogTemp, Log, TEXT("Tournament camera mode started"));
}

void USpectatorCameraManager::EndTournamentMode()
{
    // Reset to free camera
    SwitchCameraMode(TEXT("free"));

    // Clear targets
    CurrentFollowTarget = nullptr;
    AvailableTargets.Empty();

    UE_LOG(LogTemp, Log, TEXT("Tournament camera mode ended"));
}

void USpectatorCameraManager::SwitchCameraMode(const FString& CameraMode)
{
    FString PreviousMode = CurrentCameraMode;
    CurrentCameraMode = CameraMode;

    // Initialize new camera mode
    if (CameraMode == TEXT("free"))
    {
        InitializeFreeCamera();
    }
    else if (CameraMode == TEXT("follow"))
    {
        InitializeFollowCamera();
    }
    else if (CameraMode == TEXT("overview"))
    {
        InitializeOverviewCamera();
    }
    else if (CameraMode == TEXT("cinematic"))
    {
        InitializeCinematicCamera();
    }

    // Apply anti-sniping restrictions when switching modes
    ApplyAntiSnipingRestrictions();

    UE_LOG(LogTemp, Log, TEXT("Switched camera mode from %s to %s"), *PreviousMode, *CameraMode);
}

void USpectatorCameraManager::SetGlobalCameraMode(const FString& CameraMode)
{
    SwitchCameraMode(CameraMode);

    // Notify other spectators (would broadcast via tournament service)
}

void USpectatorCameraManager::SetFollowTarget(AActor* TargetActor)
{
    CurrentFollowTarget = TargetActor;

    if (CurrentCameraMode == TEXT("follow") && TargetActor)
    {
        // Smooth transition to follow the new target
        FVector TargetLocation = CalculateFollowCameraPosition(TargetActor);
        FRotator TargetRotation = CalculateFollowCameraRotation(TargetActor);

        SmoothCameraTransition(TargetLocation, TargetRotation, 0.5f);
    }

    UE_LOG(LogTemp, Log, TEXT("Set follow target: %s"), TargetActor ? *TargetActor->GetName() : TEXT("None"));
}

void USpectatorCameraManager::UpdateCameraTargets(const TArray<AActor*>& NewTargets)
{
    AvailableTargets = NewTargets;

    // Update follow target if current target is no longer available
    if (CurrentFollowTarget && !AvailableTargets.Contains(CurrentFollowTarget))
    {
        if (AvailableTargets.Num() > 0)
        {
            SetFollowTarget(AvailableTargets[0]);
        }
        else
        {
            SetFollowTarget(nullptr);
        }
    }
}

void USpectatorCameraManager::ZoomIn()
{
    CurrentZoomLevel = FMath::Max(MinZoomDistance / MaxZoomDistance, CurrentZoomLevel - 0.1f);
    ApplyAntiSnipingRestrictions();
}

void USpectatorCameraManager::ZoomOut()
{
    CurrentZoomLevel = FMath::Min(1.0f, CurrentZoomLevel + 0.1f);
    ApplyAntiSnipingRestrictions();
}

FVector USpectatorCameraManager::GetCameraLocation() const
{
    return CurrentCameraLocation;
}

void USpectatorCameraManager::SetCameraLocation(const FVector& NewLocation)
{
    if (ValidateCameraPosition(NewLocation))
    {
        CurrentCameraLocation = NewLocation;

        // Update player camera
        if (OwningPlayerController && OwningPlayerController->PlayerCameraManager)
        {
            // In a real implementation, this would update the spectator camera actor
        }
    }
}

FRotator USpectatorCameraManager::GetCameraRotation() const
{
    return CurrentCameraRotation;
}

void USpectatorCameraManager::SetCameraRotation(const FRotator& NewRotation)
{
    CurrentCameraRotation = NewRotation;

    // Update player camera
    if (OwningPlayerController && OwningPlayerController->PlayerCameraManager)
    {
        // In a real implementation, this would update the spectator camera actor
    }
}

bool USpectatorCameraManager::ValidateCameraPosition(const FVector& Position)
{
    // Check distance from tournament area
    FVector TournamentCenter = GetTournamentCenter();
    float DistanceFromCenter = FVector::Distance(Position, TournamentCenter);

    if (DistanceFromCenter > MaxCameraDistance || DistanceFromCenter < MinCameraDistance)
    {
        return false;
    }

    // Check viewing angle restrictions
    if (CurrentFollowTarget)
    {
        if (!IsValidViewingAngle(Position, CurrentFollowTarget->GetActorLocation()))
        {
            return false;
        }
    }

    // Check line of sight for follow camera
    if (CurrentCameraMode == TEXT("follow") && FollowCameraData.bMaintainLineOfSight)
    {
        if (!IsLineOfSightClear(Position, CurrentFollowTarget->GetActorLocation()))
        {
            return false;
        }
    }

    return true;
}

bool USpectatorCameraManager::IsValidViewingAngle(const FVector& CameraLocation, const FVector& TargetLocation)
{
    FVector ToTarget = (TargetLocation - CameraLocation).GetSafeNormal();
    FVector CameraForward = CurrentCameraRotation.Vector();

    float Angle = FMath::Acos(FVector::DotProduct(ToTarget, CameraForward));
    Angle = FMath::RadiansToDegrees(Angle);

    return Angle <= MaxViewingAngle;
}

void USpectatorCameraManager::UpdateCamera(float DeltaTime)
{
    if (bIsTransitioning)
    {
        // Update smooth camera transition
        TransitionProgress += DeltaTime / TransitionDuration;
        TransitionProgress = FMath::Clamp(TransitionProgress, 0.0f, 1.0f);

        // Smooth interpolation
        float Alpha = FMath::InterpEaseInOut(0.0f, 1.0f, TransitionProgress, 2.0f);

        FVector NewLocation = FMath::Lerp(TransitionStartLocation, TransitionTargetLocation, Alpha);
        FRotator NewRotation = FMath::Lerp(TransitionStartRotation, TransitionTargetRotation, Alpha);

        SetCameraLocation(NewLocation);
        SetCameraRotation(NewRotation);

        if (TransitionProgress >= 1.0f)
        {
            bIsTransitioning = false;
        }
    }
    else
    {
        // Update camera based on current mode
        if (CurrentCameraMode == TEXT("free"))
        {
            UpdateFreeCamera(DeltaTime);
        }
        else if (CurrentCameraMode == TEXT("follow"))
        {
            UpdateFollowCamera(DeltaTime);
        }
        else if (CurrentCameraMode == TEXT("overview"))
        {
            UpdateOverviewCamera(DeltaTime);
        }
        else if (CurrentCameraMode == TEXT("cinematic"))
        {
            UpdateCinematicCamera(DeltaTime);
        }
    }
}

void USpectatorCameraManager::SmoothCameraTransition(const FVector& TargetLocation, const FRotator& TargetRotation, float TransitionTime)
{
    bIsTransitioning = true;
    TransitionProgress = 0.0f;
    TransitionDuration = TransitionTime;
    TransitionStartLocation = CurrentCameraLocation;
    TransitionStartRotation = CurrentCameraRotation;
    TransitionTargetLocation = TargetLocation;
    TransitionTargetRotation = TargetRotation;
}

void USpectatorCameraManager::InitializeFreeCamera()
{
    // Free camera starts at current position with no special constraints
    UE_LOG(LogTemp, Log, TEXT("Initialized free camera mode"));
}

void USpectatorCameraManager::InitializeFollowCamera()
{
    if (CurrentFollowTarget)
    {
        FVector TargetLocation = CalculateFollowCameraPosition(CurrentFollowTarget);
        FRotator TargetRotation = CalculateFollowCameraRotation(CurrentFollowTarget);

        SmoothCameraTransition(TargetLocation, TargetRotation, 1.0f);
    }

    UE_LOG(LogTemp, Log, TEXT("Initialized follow camera mode"));
}

void USpectatorCameraManager::InitializeOverviewCamera()
{
    FVector TargetLocation = CalculateOverviewCameraPosition();
    FRotator TargetRotation = FRotator(-45.0f, 0.0f, 0.0f); // Look down at tournament

    SmoothCameraTransition(TargetLocation, TargetRotation, 2.0f);

    UE_LOG(LogTemp, Log, TEXT("Initialized overview camera mode"));
}

void USpectatorCameraManager::InitializeCinematicCamera()
{
    if (CinematicCameraData.CinematicPath.Num() > 0)
    {
        CinematicCameraData.CurrentPathIndex = 0;
        FVector StartLocation = CinematicCameraData.CinematicPath[0];

        SmoothCameraTransition(StartLocation, FRotator(-15.0f, 0.0f, 0.0f), 3.0f);
    }

    UE_LOG(LogTemp, Log, TEXT("Initialized cinematic camera mode"));
}

void USpectatorCameraManager::UpdateFreeCamera(float DeltaTime)
{
    // Free camera movement is handled by player input in the PlayerController
    // This method can be used for additional free camera logic
}

void USpectatorCameraManager::UpdateFollowCamera(float DeltaTime)
{
    if (!CurrentFollowTarget)
    {
        return;
    }

    FVector TargetLocation = CalculateFollowCameraPosition(CurrentFollowTarget);
    FRotator TargetRotation = CalculateFollowCameraRotation(CurrentFollowTarget);

    // Smooth follow with interpolation
    float Alpha = FMath::Clamp(DeltaTime / FollowCameraData.SmoothingFactor, 0.0f, 1.0f);

    FVector NewLocation = FMath::Lerp(CurrentCameraLocation, TargetLocation, Alpha);
    FRotator NewRotation = FMath::Lerp(CurrentCameraRotation, TargetRotation, Alpha);

    // Validate position before applying
    if (ValidateCameraPosition(NewLocation))
    {
        SetCameraLocation(NewLocation);
        SetCameraRotation(NewRotation);
    }
}

void USpectatorCameraManager::UpdateOverviewCamera(float DeltaTime)
{
    // Overview camera stays relatively static, may have slight orbiting
    static float OrbitAngle = 0.0f;
    OrbitAngle += DeltaTime * 10.0f; // Slow orbit

    FVector OrbitLocation = OverviewCameraData.OverviewCenter;
    OrbitLocation.X += FMath::Cos(FMath::DegreesToRadians(OrbitAngle)) * 200.0f;
    OrbitLocation.Y += FMath::Sin(FMath::DegreesToRadians(OrbitAngle)) * 200.0f;
    OrbitLocation.Z = OverviewCameraData.OverviewHeight;

    if (ValidateCameraPosition(OrbitLocation))
    {
        SetCameraLocation(OrbitLocation);
    }
}

void USpectatorCameraManager::UpdateCinematicCamera(float DeltaTime)
{
    if (CinematicCameraData.CinematicPath.Num() == 0)
    {
        return;
    }

    int32 PathLength = CinematicCameraData.CinematicPath.Num();
    int32 NextIndex = (CinematicCameraData.CurrentPathIndex + 1) % PathLength;

    FVector CurrentPathPoint = CinematicCameraData.CinematicPath[CinematicCameraData.CurrentPathIndex];
    FVector NextPathPoint = CinematicCameraData.CinematicPath[NextIndex];

    FVector Direction = (NextPathPoint - CurrentPathPoint).GetSafeNormal();
    float DistanceToNext = FVector::Distance(CurrentCameraLocation, NextPathPoint);

    if (DistanceToNext < 50.0f) // Close to next point
    {
        CinematicCameraData.CurrentPathIndex = NextIndex;
    }
    else
    {
        // Move towards next point
        FVector NewLocation = CurrentCameraLocation + Direction * CinematicCameraData.PathSpeed * DeltaTime;

        if (ValidateCameraPosition(NewLocation))
        {
            SetCameraLocation(NewLocation);

            // Look at tournament center
            FVector TournamentCenter = GetTournamentCenter();
            FRotator LookAtRotation = (TournamentCenter - NewLocation).Rotation();
            SetCameraRotation(LookAtRotation);
        }
    }
}

FVector USpectatorCameraManager::CalculateFollowCameraPosition(AActor* TargetActor)
{
    if (!TargetActor)
    {
        return CurrentCameraLocation;
    }

    FVector TargetLocation = TargetActor->GetActorLocation();
    FVector CameraOffset = FollowCameraData.Offset * CurrentZoomLevel;

    // Apply zoom to follow distance
    float ZoomedDistance = FollowCameraData.FollowDistance * CurrentZoomLevel;
    FVector CameraDirection = FVector(0.0f, -1.0f, 0.2f).GetSafeNormal(); // Behind and slightly above

    FVector CameraPosition = TargetLocation + CameraDirection * ZoomedDistance;
    CameraPosition.Z += FollowCameraData.HeightOffset;

    return CameraPosition;
}

FRotator USpectatorCameraManager::CalculateFollowCameraRotation(AActor* TargetActor)
{
    if (!TargetActor)
    {
        return CurrentCameraRotation;
    }

    FVector TargetLocation = TargetActor->GetActorLocation();
    FVector CameraLocation = CalculateFollowCameraPosition(TargetActor);

    return (TargetLocation - CameraLocation).Rotation();
}

FVector USpectatorCameraManager::CalculateOverviewCameraPosition()
{
    return OverviewCameraData.OverviewCenter + FVector(0.0f, 0.0f, OverviewCameraData.OverviewHeight);
}

FVector USpectatorCameraManager::GetTournamentCenter() const
{
    // In a real implementation, this would be calculated from tournament bounds
    return FVector::ZeroVector;
}

FBox USpectatorCameraManager::GetTournamentBounds() const
{
    // In a real implementation, this would return the tournament play area bounds
    return FBox(FVector(-2000.0f, -2000.0f, 0.0f), FVector(2000.0f, 2000.0f, 1000.0f));
}

bool USpectatorCameraManager::IsLineOfSightClear(const FVector& From, const FVector& To) const
{
    UWorld* World = GetWorld();
    if (!World)
    {
        return true;
    }

    FHitResult HitResult;
    FCollisionQueryParams QueryParams;
    QueryParams.AddIgnoredActor(OwningPlayerController->GetPawn());

    return !World->LineTraceSingleByChannel(HitResult, From, To, ECC_Visibility, QueryParams);
}

FVector USpectatorCameraManager::FindValidCameraPosition(const FVector& DesiredPosition) const
{
    FVector ValidPosition = DesiredPosition;

    // Clamp to tournament boundaries
    ClampCameraToBoundaries(ValidPosition);

    return ValidPosition;
}

void USpectatorCameraManager::ApplyAntiSnipingRestrictions()
{
    // Apply restrictions to prevent stream sniping
    if (CurrentFollowTarget)
    {
        FVector TargetLocation = CurrentFollowTarget->GetActorLocation();
        FVector CameraLocation = CurrentCameraLocation;

        if (!IsValidViewingAngle(CameraLocation, TargetLocation))
        {
            // Adjust camera position to valid angle
            FVector ValidPosition = FindValidCameraPosition(CameraLocation);
            SetCameraLocation(ValidPosition);
        }
    }
}

void USpectatorCameraManager::ClampCameraToBoundaries(FVector& CameraLocation)
{
    FVector TournamentCenter = GetTournamentCenter();
    FVector Direction = (CameraLocation - TournamentCenter).GetSafeNormal();

    float Distance = FVector::Distance(CameraLocation, TournamentCenter);
    Distance = FMath::Clamp(Distance, MinCameraDistance, MaxCameraDistance);

    CameraLocation = TournamentCenter + Direction * Distance;
}

void USpectatorCameraManager::InitializeCinematicPath()
{
    // Create a cinematic camera path around the tournament area
    FVector TournamentCenter = GetTournamentCenter();
    float Radius = 800.0f;
    int32 NumPoints = 8;

    CinematicCameraData.CinematicPath.Empty();

    for (int32 i = 0; i < NumPoints; ++i)
    {
        float Angle = (float)i / (float)NumPoints * 2.0f * PI;
        FVector PathPoint = TournamentCenter;
        PathPoint.X += FMath::Cos(Angle) * Radius;
        PathPoint.Y += FMath::Sin(Angle) * Radius;
        PathPoint.Z = 400.0f; // Height above tournament

        CinematicCameraData.CinematicPath.Add(PathPoint);
    }
}
