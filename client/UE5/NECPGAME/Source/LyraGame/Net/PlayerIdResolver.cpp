#include "Net/PlayerIdResolver.h"
#include "GameFramework/PlayerController.h"
#include "GameFramework/PlayerState.h"
#include "Player/LyraPlayerController.h"
#include "Player/LyraPlayerState.h"

FString UPlayerIdResolver::GetPlayerIdFromController(
    APlayerController *Controller) const {
  if (!Controller || !IsValid(Controller)) {
    return FString();
  }

  ALyraPlayerController *LyraPC = Cast<ALyraPlayerController>(Controller);
  if (!LyraPC || !IsValid(LyraPC)) {
    return FString();
  }

  if (ALyraPlayerState *LyraPS = LyraPC->GetLyraPlayerState()) {
    if (IsValid(LyraPS)) {
      FString OriginalId = LyraPS->GetUniqueId().ToString();
      return ALyraPlayerController::GenerateShortPlayerId(OriginalId);
    }
  }

  if (APlayerState *PS = Controller->GetPlayerState<APlayerState>()) {
    if (IsValid(PS)) {
      FString OriginalId = PS->GetUniqueId().ToString();
      return ALyraPlayerController::GenerateShortPlayerId(OriginalId);
    }
  }

  return FString();
}

APlayerController *
UPlayerIdResolver::FindControllerByPlayerId(const FString &PlayerId,
                                            UWorld *World) const {
  if (!World || PlayerId.IsEmpty()) {
    return nullptr;
  }

  TArray<APlayerController *> Controllers;
  for (FConstPlayerControllerIterator It = World->GetPlayerControllerIterator();
       It; ++It) {
    if (APlayerController *PC = It->Get()) {
      Controllers.Add(PC);
    }
  }

  for (APlayerController *PC : Controllers) {
    if (!IsValid(PC)) {
      continue;
    }

    FString PCPlayerId = GetPlayerIdFromController(PC);
    if (!PCPlayerId.IsEmpty() && PCPlayerId == PlayerId) {
      return PC;
    }
  }

  return nullptr;
}

TMap<FString, APlayerController *>
UPlayerIdResolver::BuildControllerMap(UWorld *World) const {
  TMap<FString, APlayerController *> ControllerMap;

  if (!World) {
    return ControllerMap;
  }

  for (FConstPlayerControllerIterator It = World->GetPlayerControllerIterator();
       It; ++It) {
    if (APlayerController *PC = It->Get()) {
      FString PCPlayerId = GetPlayerIdFromController(PC);
      if (!PCPlayerId.IsEmpty()) {
        ControllerMap.Add(PCPlayerId, PC);
      }
    }
  }

  return ControllerMap;
}
