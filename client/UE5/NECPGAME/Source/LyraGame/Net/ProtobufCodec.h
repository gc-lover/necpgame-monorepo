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
    int32 MoveX = 0;
    int32 MoveY = 0;
    bool Shoot = false;
    int32 AimX = 0;
    int32 AimY = 0;
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
    int32 X = 0;
    int32 Y = 0;
    int32 Z = 0;
    int32 VX = 0;
    int32 VY = 0;
    int32 VZ = 0;
    int32 Yaw = 0;
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

  static int32 QuantizeCoordinate(float Value);
  static float DequantizeCoordinate(int32 Value);

private:
  static void WriteVarInt(TArray<uint8> &Buffer, uint64 Value);
  static void WriteString(TArray<uint8> &Buffer, const FString &Value);
  static void WriteFloat(TArray<uint8> &Buffer, float Value);
  static void WriteBool(TArray<uint8> &Buffer, bool Value);
  static void WriteInt64(TArray<uint8> &Buffer, int64 Value);
  static void WriteInt32ZigZag(TArray<uint8> &Buffer, int32 Value);

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
  static bool ReadInt32ZigZag(const TArray<uint8> &Data, int32 &Offset,
                               int32 &OutValue);
  static bool ReadBytes(const TArray<uint8> &Data, int32 &Offset,
                        TArray<uint8> &OutValue);
};
