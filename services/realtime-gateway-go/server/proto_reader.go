package server

import (
	"encoding/binary"
	"fmt"
	"math"
)

func readVarInt(data []byte, offset *int) (uint64, error) {
	var result uint64
	var shift uint
	for *offset < len(data) {
		b := data[*offset]
		*offset++
		result |= uint64(b&0x7F) << shift
		if (b & 0x80) == 0 {
			return result, nil
		}
		shift += 7
		if shift >= 64 {
			return 0, fmt.Errorf("varint too long")
		}
	}
	return 0, fmt.Errorf("unexpected end of data")
}

func readString(data []byte, offset *int) (string, error) {
	length, err := readVarInt(data, offset)
	if err != nil {
		return "", err
	}
	if *offset+int(length) > len(data) {
		return "", fmt.Errorf("string length exceeds data")
	}
	result := string(data[*offset : *offset+int(length)])
	*offset += int(length)
	return result, nil
}

func readFloat32(data []byte, offset *int) (float32, error) {
	if *offset+4 > len(data) {
		return 0, fmt.Errorf("not enough data for float32")
	}
	bits := binary.LittleEndian.Uint32(data[*offset:])
	*offset += 4
	return math.Float32frombits(bits), nil
}

func readInt64(data []byte, offset *int) (int64, error) {
	val, err := readVarInt(data, offset)
	if err != nil {
		return 0, err
	}
	return int64(val), nil
}

func readBool(data []byte, offset *int) (bool, error) {
	val, err := readVarInt(data, offset)
	if err != nil {
		return false, err
	}
	return val != 0, nil
}

func encodeZigZag(n int32) uint32 {
	return uint32((n << 1) ^ (n >> 31))
}

func decodeZigZag(n uint32) int32 {
	return int32((n >> 1) ^ -(n & 1))
}

func readVarIntZigZag(data []byte, offset *int) (int32, error) {
	val, err := readVarInt(data, offset)
	if err != nil {
		return 0, err
	}
	return decodeZigZag(uint32(val)), nil
}

