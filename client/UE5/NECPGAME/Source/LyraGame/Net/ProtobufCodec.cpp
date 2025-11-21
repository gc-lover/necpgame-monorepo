#include "Net/ProtobufCodec.h"
#include "Misc/ByteSwap.h"
#include "Math/UnrealMathUtility.h"

void FProtobufCodec::WriteVarInt(TArray<uint8>& Buffer, uint64 Value)
{
	while (Value >= 0x80)
	{
		Buffer.Add(static_cast<uint8>((Value & 0x7F) | 0x80));
		Value >>= 7;
	}
	Buffer.Add(static_cast<uint8>(Value & 0x7F));
}

void FProtobufCodec::WriteString(TArray<uint8>& Buffer, const FString& Value)
{
	FTCHARToUTF8 UTF8Value(*Value);
	int32 Length = UTF8Value.Length();
	WriteVarInt(Buffer, Length);
	Buffer.Append(reinterpret_cast<const uint8*>(UTF8Value.Get()), Length);
}

void FProtobufCodec::WriteFloat(TArray<uint8>& Buffer, float Value)
{
	uint32 IntValue = *reinterpret_cast<uint32*>(&Value);
	Buffer.Add(static_cast<uint8>(IntValue & 0xFF));
	Buffer.Add(static_cast<uint8>((IntValue >> 8) & 0xFF));
	Buffer.Add(static_cast<uint8>((IntValue >> 16) & 0xFF));
	Buffer.Add(static_cast<uint8>((IntValue >> 24) & 0xFF));
}

void FProtobufCodec::WriteBool(TArray<uint8>& Buffer, bool Value)
{
	WriteVarInt(Buffer, Value ? 1 : 0);
}

void FProtobufCodec::WriteInt64(TArray<uint8>& Buffer, int64 Value)
{
	uint64 UValue = static_cast<uint64>(Value);
	WriteVarInt(Buffer, UValue);
}

bool FProtobufCodec::ReadVarInt(const TArray<uint8>& Data, int32& Offset, uint64& OutValue)
{
	OutValue = 0;
	int32 Shift = 0;
	
	while (Offset < Data.Num())
	{
		uint8 Byte = Data[Offset++];
		OutValue |= static_cast<uint64>(Byte & 0x7F) << Shift;
		
		if ((Byte & 0x80) == 0)
		{
			return true;
		}
		
		Shift += 7;
		if (Shift >= 64)
		{
			return false;
		}
	}
	
	return false;
}

bool FProtobufCodec::ReadString(const TArray<uint8>& Data, int32& Offset, FString& OutValue)
{
	uint64 Length = 0;
	if (!ReadVarInt(Data, Offset, Length) || Offset + Length > Data.Num())
	{
		return false;
	}
	
	FUTF8ToTCHAR UTF8Converter(reinterpret_cast<const ANSICHAR*>(Data.GetData() + Offset), Length);
	OutValue = FString(UTF8Converter.Length(), UTF8Converter.Get());
	Offset += static_cast<int32>(Length);
	return true;
}

bool FProtobufCodec::ReadFloat(const TArray<uint8>& Data, int32& Offset, float& OutValue)
{
	if (Offset + 4 > Data.Num())
	{
		return false;
	}
	
	uint32 IntValue = static_cast<uint32>(Data[Offset]) |
		(static_cast<uint32>(Data[Offset + 1]) << 8) |
		(static_cast<uint32>(Data[Offset + 2]) << 16) |
		(static_cast<uint32>(Data[Offset + 3]) << 24);
	
	OutValue = *reinterpret_cast<float*>(&IntValue);
	Offset += 4;
	return true;
}

bool FProtobufCodec::ReadBool(const TArray<uint8>& Data, int32& Offset, bool& OutValue)
{
	uint64 Value = 0;
	if (!ReadVarInt(Data, Offset, Value))
	{
		return false;
	}
	OutValue = (Value != 0);
	return true;
}

bool FProtobufCodec::ReadInt64(const TArray<uint8>& Data, int32& Offset, int64& OutValue)
{
	uint64 UValue = 0;
	if (!ReadVarInt(Data, Offset, UValue))
	{
		return false;
	}
	OutValue = static_cast<int64>(UValue);
	return true;
}

bool FProtobufCodec::ReadBytes(const TArray<uint8>& Data, int32& Offset, TArray<uint8>& OutValue)
{
	uint64 Length = 0;
	if (!ReadVarInt(Data, Offset, Length) || Offset + Length > Data.Num())
	{
		return false;
	}
	
	OutValue.Empty(static_cast<int32>(Length));
	OutValue.Append(Data.GetData() + Offset, static_cast<int32>(Length));
	Offset += static_cast<int32>(Length);
	return true;
}

static uint32 EncodeZigZag(int32 Value)
{
	return static_cast<uint32>((Value << 1) ^ (Value >> 31));
}

static int32 DecodeZigZag(uint32 Value)
{
	return static_cast<int32>((Value >> 1) ^ -(static_cast<int32>(Value & 1)));
}

void FProtobufCodec::WriteInt32ZigZag(TArray<uint8>& Buffer, int32 Value)
{
	uint32 ZigZagValue = EncodeZigZag(Value);
	WriteVarInt(Buffer, ZigZagValue);
}

bool FProtobufCodec::ReadInt32ZigZag(const TArray<uint8>& Data, int32& Offset, int32& OutValue)
{
	uint64 ZigZagValue = 0;
	if (!ReadVarInt(Data, Offset, ZigZagValue))
	{
		return false;
	}
	OutValue = DecodeZigZag(static_cast<uint32>(ZigZagValue));
	return true;
}

constexpr float QuantizationScale = 10.0f;

int32 FProtobufCodec::QuantizeCoordinate(float Value)
{
	return FMath::RoundToInt(Value * QuantizationScale);
}

float FProtobufCodec::DequantizeCoordinate(int32 Value)
{
	return static_cast<float>(Value) / QuantizationScale;
}

TArray<uint8> FProtobufCodec::EncodeClientMessage(const FClientMessage& Message)
{
	TArray<uint8> Buffer;
	
	if (!Message.Token.IsEmpty())
	{
		WriteVarInt(Buffer, (1 << 3) | 2);
		WriteString(Buffer, Message.Token);
	}
	
	if (Message.Type == FClientMessage::EMessageType::Heartbeat)
	{
		TArray<uint8> HeartbeatBuffer;
		WriteVarInt(HeartbeatBuffer, (1 << 3) | 0);
		WriteInt64(HeartbeatBuffer, Message.Heartbeat.ClientTimeMs);
		
		WriteVarInt(Buffer, (10 << 3) | 2);
		WriteVarInt(Buffer, HeartbeatBuffer.Num());
		Buffer.Append(HeartbeatBuffer);
	}
	else if (Message.Type == FClientMessage::EMessageType::PlayerInput)
	{
		TArray<uint8> InputBuffer;
		
		if (!Message.PlayerInput.PlayerId.IsEmpty())
		{
			WriteVarInt(InputBuffer, (1 << 3) | 2);
			WriteString(InputBuffer, Message.PlayerInput.PlayerId);
		}
		else
		{
			UE_LOG(LogTemp, Warning, TEXT("EncodeClientMessage: PlayerId is empty, field will not be encoded"));
		}
		
		WriteVarInt(InputBuffer, (2 << 3) | 0);
		WriteInt64(InputBuffer, Message.PlayerInput.Tick);
		
		WriteVarInt(InputBuffer, (3 << 3) | 0);
		WriteInt32ZigZag(InputBuffer, Message.PlayerInput.MoveX);
		
		WriteVarInt(InputBuffer, (4 << 3) | 0);
		WriteInt32ZigZag(InputBuffer, Message.PlayerInput.MoveY);
		
		WriteVarInt(InputBuffer, (5 << 3) | 0);
		WriteBool(InputBuffer, Message.PlayerInput.Shoot);
		
		WriteVarInt(InputBuffer, (6 << 3) | 0);
		WriteInt32ZigZag(InputBuffer, Message.PlayerInput.AimX);
		
		WriteVarInt(InputBuffer, (7 << 3) | 0);
		WriteInt32ZigZag(InputBuffer, Message.PlayerInput.AimY);
		
		WriteVarInt(Buffer, (12 << 3) | 2);
		WriteVarInt(Buffer, InputBuffer.Num());
		Buffer.Append(InputBuffer);
	}
	
	return Buffer;
}

bool FProtobufCodec::DecodeClientMessage(const TArray<uint8>& Data, FClientMessage& OutMessage)
{
	OutMessage.Type = FClientMessage::EMessageType::None;
	
	int32 Offset = 0;
	
	while (Offset < Data.Num())
	{
		uint64 Tag = 0;
		if (!ReadVarInt(Data, Offset, Tag))
		{
			return false;
		}
		
		uint32 FieldNumber = static_cast<uint32>(Tag >> 3);
		uint32 WireType = static_cast<uint32>(Tag & 0x7);
		
		if (FieldNumber == 1 && WireType == 2)
		{
			// Token field
			if (!ReadString(Data, Offset, OutMessage.Token))
			{
				return false;
			}
		}
		else if (FieldNumber == 10 && WireType == 2)
		{
			// Heartbeat field
			uint64 Length = 0;
			if (!ReadVarInt(Data, Offset, Length) || Offset + Length > Data.Num())
			{
				return false;
			}
			
			int32 HeartbeatStart = Offset;
			while (Offset < HeartbeatStart + Length)
			{
				uint64 HeartbeatTag = 0;
				if (!ReadVarInt(Data, Offset, HeartbeatTag))
				{
					return false;
				}
				
				uint32 HeartbeatField = static_cast<uint32>(HeartbeatTag >> 3);
				uint32 HeartbeatWireType = static_cast<uint32>(HeartbeatTag & 0x7);
				
				if (HeartbeatField == 1 && HeartbeatWireType == 0)
				{
					if (!ReadInt64(Data, Offset, OutMessage.Heartbeat.ClientTimeMs))
					{
						return false;
					}
				}
				else
				{
					// Skip unknown fields
					if (HeartbeatWireType == 0)
					{
						uint64 SkipValue = 0;
						ReadVarInt(Data, Offset, SkipValue);
					}
					else if (HeartbeatWireType == 1)
					{
						Offset += 8;
					}
					else if (HeartbeatWireType == 2)
					{
						uint64 SkipLength = 0;
						if (ReadVarInt(Data, Offset, SkipLength))
						{
							Offset += static_cast<int32>(SkipLength);
						}
					}
					else if (HeartbeatWireType == 5)
					{
						Offset += 4;
					}
				}
			}
			
			OutMessage.Type = FClientMessage::EMessageType::Heartbeat;
		}
		else if (FieldNumber == 12 && WireType == 2)
		{
			// PlayerInput field
			uint64 Length = 0;
			if (!ReadVarInt(Data, Offset, Length) || Offset + Length > Data.Num())
			{
				return false;
			}
			
			int32 InputStart = Offset;
			while (Offset < InputStart + Length)
			{
				uint64 InputTag = 0;
				if (!ReadVarInt(Data, Offset, InputTag))
				{
					return false;
				}
				
				uint32 InputField = static_cast<uint32>(InputTag >> 3);
				uint32 InputWireType = static_cast<uint32>(InputTag & 0x7);
				
				if (InputField == 1 && InputWireType == 2)
				{
					if (!ReadString(Data, Offset, OutMessage.PlayerInput.PlayerId))
					{
						return false;
					}
				}
				else if (InputField == 2 && InputWireType == 0)
				{
					if (!ReadInt64(Data, Offset, OutMessage.PlayerInput.Tick))
					{
						return false;
					}
				}
				else if (InputField == 3 && InputWireType == 0)
				{
					if (!ReadInt32ZigZag(Data, Offset, OutMessage.PlayerInput.MoveX))
					{
						return false;
					}
				}
				else if (InputField == 4 && InputWireType == 0)
				{
					if (!ReadInt32ZigZag(Data, Offset, OutMessage.PlayerInput.MoveY))
					{
						return false;
					}
				}
				else if (InputField == 5 && InputWireType == 0)
				{
					if (!ReadBool(Data, Offset, OutMessage.PlayerInput.Shoot))
					{
						return false;
					}
				}
				else if (InputField == 6 && InputWireType == 0)
				{
					if (!ReadInt32ZigZag(Data, Offset, OutMessage.PlayerInput.AimX))
					{
						return false;
					}
				}
				else if (InputField == 7 && InputWireType == 0)
				{
					if (!ReadInt32ZigZag(Data, Offset, OutMessage.PlayerInput.AimY))
					{
						return false;
					}
				}
				else
				{
					// Skip unknown fields
					if (InputWireType == 0)
					{
						uint64 SkipValue = 0;
						ReadVarInt(Data, Offset, SkipValue);
					}
					else if (InputWireType == 1)
					{
						Offset += 8;
					}
					else if (InputWireType == 2)
					{
						uint64 SkipLength = 0;
						if (ReadVarInt(Data, Offset, SkipLength))
						{
							Offset += static_cast<int32>(SkipLength);
						}
					}
					else if (InputWireType == 5)
					{
						Offset += 4;
					}
				}
			}
			
			OutMessage.Type = FClientMessage::EMessageType::PlayerInput;
		}
		else
		{
			// Skip unknown fields
			if (WireType == 0)
			{
				uint64 SkipValue = 0;
				ReadVarInt(Data, Offset, SkipValue);
			}
			else if (WireType == 1)
			{
				Offset += 8;
			}
			else if (WireType == 2)
			{
				uint64 SkipLength = 0;
				if (ReadVarInt(Data, Offset, SkipLength))
				{
					Offset += static_cast<int32>(SkipLength);
				}
			}
			else if (WireType == 5)
			{
				Offset += 4;
			}
		}
	}
	
	return OutMessage.Type != FClientMessage::EMessageType::None;
}

bool FProtobufCodec::DecodeServerMessage(const TArray<uint8>& Data, FServerMessage& OutMessage)
{
	OutMessage.Type = FServerMessage::EMessageType::None;
	
	int32 Offset = 0;
	
	while (Offset < Data.Num())
	{
		uint64 Tag = 0;
		if (!ReadVarInt(Data, Offset, Tag))
		{
			return false;
		}
		
		uint32 FieldNumber = static_cast<uint32>(Tag >> 3);
		uint32 WireType = static_cast<uint32>(Tag & 0x7);
		
		if (FieldNumber == 10 && WireType == 2)
		{
			uint64 Length = 0;
			if (!ReadVarInt(Data, Offset, Length) || Offset + Length > Data.Num())
			{
				return false;
			}
			
			int32 HeartbeatStart = Offset;
			while (Offset < HeartbeatStart + Length)
			{
				uint64 HeartbeatTag = 0;
				if (!ReadVarInt(Data, Offset, HeartbeatTag))
				{
					return false;
				}
				
				uint32 HeartbeatField = static_cast<uint32>(HeartbeatTag >> 3);
				uint32 HeartbeatWireType = static_cast<uint32>(HeartbeatTag & 0x7);
				
				if (HeartbeatField == 1 && HeartbeatWireType == 0)
				{
					if (!ReadInt64(Data, Offset, OutMessage.HeartbeatAck.ServerTimeMs))
					{
						return false;
					}
				}
				else if (HeartbeatField == 2 && HeartbeatWireType == 0)
				{
					if (!ReadInt64(Data, Offset, OutMessage.HeartbeatAck.RTTEstimateMs))
					{
						return false;
					}
				}
				else
				{
					if (HeartbeatWireType == 0)
					{
						uint64 SkipValue = 0;
						ReadVarInt(Data, Offset, SkipValue);
					}
					else if (HeartbeatWireType == 1)
					{
						Offset += 8;
					}
					else if (HeartbeatWireType == 2)
					{
						uint64 SkipLength = 0;
						if (ReadVarInt(Data, Offset, SkipLength))
						{
							Offset += static_cast<int32>(SkipLength);
						}
					}
					else if (HeartbeatWireType == 5)
					{
						Offset += 4;
					}
				}
			}
			
			OutMessage.Type = FServerMessage::EMessageType::HeartbeatAck;
		}
		else if (FieldNumber == 12 && WireType == 2)
		{
			uint64 Length = 0;
			if (!ReadVarInt(Data, Offset, Length) || Offset + Length > Data.Num())
			{
				return false;
			}
			
			int32 GameStateStart = Offset;
			while (Offset < GameStateStart + Length)
			{
				uint64 GameStateTag = 0;
				if (!ReadVarInt(Data, Offset, GameStateTag))
				{
					return false;
				}
				
				uint32 GameStateField = static_cast<uint32>(GameStateTag >> 3);
				uint32 GameStateWireType = static_cast<uint32>(GameStateTag & 0x7);
				
				if (GameStateField == 1 && GameStateWireType == 2)
				{
					uint64 SnapshotLength = 0;
					if (!ReadVarInt(Data, Offset, SnapshotLength) || Offset + SnapshotLength > Data.Num())
					{
						return false;
					}
					
					int32 SnapshotStart = Offset;
					while (Offset < SnapshotStart + SnapshotLength)
					{
						uint64 SnapshotTag = 0;
						if (!ReadVarInt(Data, Offset, SnapshotTag))
						{
							return false;
						}
						
						uint32 SnapshotField = static_cast<uint32>(SnapshotTag >> 3);
						uint32 SnapshotWireType = static_cast<uint32>(SnapshotTag & 0x7);
						
						if (SnapshotField == 1 && SnapshotWireType == 0)
						{
							if (!ReadInt64(Data, Offset, OutMessage.GameState.Snapshot.Tick))
							{
								return false;
							}
						}
						else if (SnapshotField == 2 && SnapshotWireType == 2)
						{
							uint64 EntityLength = 0;
							if (!ReadVarInt(Data, Offset, EntityLength) || Offset + EntityLength > Data.Num())
							{
								return false;
							}
							
							FEntityState Entity;
							int32 EntityStart = Offset;
							while (Offset < EntityStart + EntityLength)
							{
								uint64 EntityTag = 0;
								if (!ReadVarInt(Data, Offset, EntityTag))
								{
									return false;
								}
								
								uint32 EntityField = static_cast<uint32>(EntityTag >> 3);
								uint32 EntityWireType = static_cast<uint32>(EntityTag & 0x7);
								
								if (EntityField == 1 && EntityWireType == 2)
								{
									if (!ReadString(Data, Offset, Entity.Id))
									{
										return false;
									}
								}
								else if (EntityField == 2 && EntityWireType == 0)
								{
									if (!ReadInt32ZigZag(Data, Offset, Entity.X))
									{
										return false;
									}
								}
								else if (EntityField == 3 && EntityWireType == 0)
								{
									if (!ReadInt32ZigZag(Data, Offset, Entity.Y))
									{
										return false;
									}
								}
								else if (EntityField == 4 && EntityWireType == 0)
								{
									if (!ReadInt32ZigZag(Data, Offset, Entity.Z))
									{
										return false;
									}
								}
								else if (EntityField == 5 && EntityWireType == 0)
								{
									if (!ReadInt32ZigZag(Data, Offset, Entity.VX))
									{
										return false;
									}
								}
								else if (EntityField == 6 && EntityWireType == 0)
								{
									if (!ReadInt32ZigZag(Data, Offset, Entity.VY))
									{
										return false;
									}
								}
								else if (EntityField == 7 && EntityWireType == 0)
								{
									if (!ReadInt32ZigZag(Data, Offset, Entity.VZ))
									{
										return false;
									}
								}
								else if (EntityField == 8 && EntityWireType == 0)
								{
									if (!ReadInt32ZigZag(Data, Offset, Entity.Yaw))
									{
										return false;
									}
								}
								else
								{
									if (EntityWireType == 0)
									{
										uint64 SkipValue = 0;
										ReadVarInt(Data, Offset, SkipValue);
									}
									else if (EntityWireType == 1)
									{
										Offset += 8;
									}
									else if (EntityWireType == 2)
									{
										uint64 SkipLength = 0;
										if (ReadVarInt(Data, Offset, SkipLength))
										{
											Offset += static_cast<int32>(SkipLength);
										}
									}
									else if (EntityWireType == 5)
									{
										Offset += 4;
									}
								}
							}
							
							OutMessage.GameState.Snapshot.Entities.Add(Entity);
						}
						else
						{
							if (SnapshotWireType == 0)
							{
								uint64 SkipValue = 0;
								ReadVarInt(Data, Offset, SkipValue);
							}
							else if (SnapshotWireType == 1)
							{
								Offset += 8;
							}
							else if (SnapshotWireType == 2)
							{
								uint64 SkipLength = 0;
								if (ReadVarInt(Data, Offset, SkipLength))
								{
									Offset += static_cast<int32>(SkipLength);
								}
							}
							else if (SnapshotWireType == 5)
							{
								Offset += 4;
							}
						}
					}
				}
				else
				{
					if (GameStateWireType == 0)
					{
						uint64 SkipValue = 0;
						ReadVarInt(Data, Offset, SkipValue);
					}
					else if (GameStateWireType == 1)
					{
						Offset += 8;
					}
					else if (GameStateWireType == 2)
					{
						uint64 SkipLength = 0;
						if (ReadVarInt(Data, Offset, SkipLength))
						{
							Offset += static_cast<int32>(SkipLength);
						}
					}
					else if (GameStateWireType == 5)
					{
						Offset += 4;
					}
				}
			}
			
			OutMessage.Type = FServerMessage::EMessageType::GameState;
		}
		else
		{
			if (WireType == 0)
			{
				uint64 SkipValue = 0;
				ReadVarInt(Data, Offset, SkipValue);
			}
			else if (WireType == 1)
			{
				Offset += 8;
			}
			else if (WireType == 2)
			{
				uint64 SkipLength = 0;
				if (ReadVarInt(Data, Offset, SkipLength))
				{
					Offset += static_cast<int32>(SkipLength);
				}
			}
			else if (WireType == 5)
			{
				Offset += 4;
			}
		}
	}
	
	return true;
}

TArray<uint8> FProtobufCodec::EncodeServerMessage(const FServerMessage& Message)
{
	TArray<uint8> Buffer;
	
	if (Message.Type == FServerMessage::EMessageType::HeartbeatAck)
	{
		TArray<uint8> HeartbeatBuffer;
		WriteVarInt(HeartbeatBuffer, (1 << 3) | 0);
		WriteInt64(HeartbeatBuffer, Message.HeartbeatAck.ServerTimeMs);
		WriteVarInt(HeartbeatBuffer, (2 << 3) | 0);
		WriteInt64(HeartbeatBuffer, Message.HeartbeatAck.RTTEstimateMs);
		
		WriteVarInt(Buffer, (10 << 3) | 2);
		WriteVarInt(Buffer, HeartbeatBuffer.Num());
		Buffer.Append(HeartbeatBuffer);
	}
	else if (Message.Type == FServerMessage::EMessageType::GameState)
	{
		TArray<uint8> GameStateBuffer;
		
		TArray<uint8> SnapshotBuffer;
		WriteVarInt(SnapshotBuffer, (1 << 3) | 0);
		WriteInt64(SnapshotBuffer, Message.GameState.Snapshot.Tick);
		
		for (const FEntityState& Entity : Message.GameState.Snapshot.Entities)
		{
			TArray<uint8> EntityBuffer;
			
			if (!Entity.Id.IsEmpty())
			{
				WriteVarInt(EntityBuffer, (1 << 3) | 2);
				WriteString(EntityBuffer, Entity.Id);
			}
			
			WriteVarInt(EntityBuffer, (2 << 3) | 0);
			WriteInt32ZigZag(EntityBuffer, Entity.X);
			
			WriteVarInt(EntityBuffer, (3 << 3) | 0);
			WriteInt32ZigZag(EntityBuffer, Entity.Y);
			
			WriteVarInt(EntityBuffer, (4 << 3) | 0);
			WriteInt32ZigZag(EntityBuffer, Entity.Z);
			
			WriteVarInt(EntityBuffer, (5 << 3) | 0);
			WriteInt32ZigZag(EntityBuffer, Entity.VX);
			
			WriteVarInt(EntityBuffer, (6 << 3) | 0);
			WriteInt32ZigZag(EntityBuffer, Entity.VY);
			
			WriteVarInt(EntityBuffer, (7 << 3) | 0);
			WriteInt32ZigZag(EntityBuffer, Entity.VZ);
			
			WriteVarInt(EntityBuffer, (8 << 3) | 0);
			WriteInt32ZigZag(EntityBuffer, Entity.Yaw);
			
			WriteVarInt(SnapshotBuffer, (2 << 3) | 2);
			WriteVarInt(SnapshotBuffer, EntityBuffer.Num());
			SnapshotBuffer.Append(EntityBuffer);
		}
		
		WriteVarInt(GameStateBuffer, (1 << 3) | 2);
		WriteVarInt(GameStateBuffer, SnapshotBuffer.Num());
		GameStateBuffer.Append(SnapshotBuffer);
		
		WriteVarInt(Buffer, (12 << 3) | 2);
		WriteVarInt(Buffer, GameStateBuffer.Num());
		Buffer.Append(GameStateBuffer);
	}
	
	return Buffer;
}

