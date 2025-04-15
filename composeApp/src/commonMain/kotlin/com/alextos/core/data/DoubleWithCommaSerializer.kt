package com.alextos.core.data

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object DoubleWithCommaSerializer : KSerializer<Double> {
    override val descriptor = PrimitiveSerialDescriptor("DoubleWithComma", PrimitiveKind.STRING)
    
    override fun deserialize(decoder: Decoder): Double {
        return decoder.decodeString().replace(",", ".").toDouble()
    }
    
    override fun serialize(encoder: Encoder, value: Double) {
        encoder.encodeString(value.toString().replace(".", ","))
    }
}