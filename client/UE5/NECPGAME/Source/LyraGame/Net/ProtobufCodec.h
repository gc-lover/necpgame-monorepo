#pragma once

#include "CoreMinimal.h"

class FProtobufCodec {
public:
  struct FHeartbeat {
    int64 ClientTimeMs = 0;
  };

  struct FHeartbeatAck {
    int64 ServerTimeMs = 0;
    int64 RTTEstimateMs = 0;
  };

  struct FPlayerInput {
    FString PlayerId;
    int64 Tick = 0;
    float MoveX = 0.0f;
    float MoveY = 0.0f;
    bool Shoot = false;
    float AimX = 0.0f;
    float AimY = 0.0f;
  };

  struct FClientMessage {
    FString Token;
    enum class EMessageType { None, Heartbeat, PlayerInput };
    EMessageType Type = EMessageType::None;
    FHeartbeat Heartbeat;
    FPlayerInput PlayerInput;
  };

  struct FEntityState {
    FString Id;
    float X = 0.0f;
    float Y = 0.0f;
    float Z = 0.0f;
    float VX = 0.0f;
    float VY = 0.0f;
    float VZ = 0.0f;
    float Yaw = 0.0f;
  };

  struct FGameSnapshot {
    int64 Tick = 0;
    TArray<FEntityState> Entities;
  };

  struct FGameState {
    FGameSnapshot Snapshot;
  };

  struct FServerMessage {
    enum class EMessageType { None, HeartbeatAck, GameState };
    EMessageType Type = EMessageType::None;
    FHeartbeatAck HeartbeatAck;
    FGameState GameState;
  };

  static TArray<uint8> EncodeClientMessage(const FClientMessage &Message);
  static bool DecodeClientMessage(const TArray<uint8> &Data,
                                  FClientMessage &OutMessage);
  static TArray<uint8> EncodeServerMessage(const FServerMessage &Message);
  static bool DecodeServerMessage(const TArray<uint8> &Data,
                                  FServerMessage &OutMessage);

private:
  static void WriteVarInt(TArray<uint8> &Buffer, uint64 Value);
  static void WriteString(TArray<uint8> &Buffer, const FString &Value);
  static void WriteFloat(TArray<uint8> &Buffer, float Value);
  static void WriteBool(TArray<uint8> &Buffer, bool Value);
  static void WriteInt64(TArray<uint8> &Buffer, int64 Value);

  static bool ReadVarInt(const TArray<uint8> &Data, int32 &Offset,
                         uint64 &OutValue);
  static bool ReadString(const TArray<uint8> &Data, int32 &Offset,
                         FString &OutValue);
  static bool ReadFloat(const TArray<uint8> &Data, int32 &Offset,
                        float &OutValue);
  static bool ReadBool(const TArray<uint8> &Data, int32 &Offset,
                       bool &OutValue);
  static bool ReadInt64(const TArray<uint8> &Data, int32 &Offset,
                        int64 &OutValue);
  static bool ReadBytes(const TArray<uint8> &Data, int32 &Offset,
                        TArray<uint8> &OutValue);
};
