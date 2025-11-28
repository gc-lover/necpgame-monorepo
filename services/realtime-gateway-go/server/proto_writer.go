package server

import (
	"encoding/binary"
	"math"
)

func writeVarInt(buf []byte, v uint64) []byte {
	for v >= 0x80 {
		buf = append(buf, byte(v)|0x80)
		v >>= 7
	}
	buf = append(buf, byte(v))
	return buf
}

func writeString(buf []byte, s string) []byte {
	buf = writeVarInt(buf, uint64(len(s)))
	buf = append(buf, []byte(s)...)
	return buf
}

func writeFloat32(buf []byte, f float32) []byte {
	bits := math.Float32bits(f)
	bytes := make([]byte, 4)
	binary.LittleEndian.PutUint32(bytes, bits)
	return append(buf, bytes...)
}

func writeInt64(buf []byte, v int64) []byte {
	return writeVarInt(buf, uint64(v))
}

const QuantizationScale = 10.0

func QuantizeCoordinate(value float32) int32 {
	return int32(math.Round(float64(value) * QuantizationScale))
}

func DequantizeCoordinate(value int32) float32 {
	return float32(value) / QuantizationScale
}

func writeVarIntZigZag(buf []byte, v int32) []byte {
	return writeVarInt(buf, uint64(encodeZigZag(v)))
}

