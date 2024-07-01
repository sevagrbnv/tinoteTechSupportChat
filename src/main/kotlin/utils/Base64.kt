package utils

class Base64 {
    companion object {
        private const val BASE64_ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"

        fun encodeToBase64(inputString: String): String {
            val input = inputString.toByteArray()
            val output = StringBuilder()
            var padding = 0

            for (i in input.indices step 3) {
                val byte1 = input[i].toInt() and 0xff
                val byte2 = if (i + 1 < input.size) input[i + 1].toInt() and 0xff else {
                    padding++
                    0
                }
                val byte3 = if (i + 2 < input.size) input[i + 2].toInt() and 0xff else {
                    padding++
                    0
                }

                val triple = (byte1 shl 16) or (byte2 shl 8) or byte3

                output.append(BASE64_ALPHABET[(triple shr 18) and 0x3f])
                output.append(BASE64_ALPHABET[(triple shr 12) and 0x3f])
                output.append(BASE64_ALPHABET[(triple shr 6) and 0x3f])
                output.append(BASE64_ALPHABET[triple and 0x3f])
            }

            for (i in 0 until padding) {
                output.setCharAt(output.length - 1 - i, '=')
            }

            return output.toString()
        }

        fun decodeFromBase64(encoded: String): String {
            val cleaned = encoded.filter { it != '=' }
            val output = ByteArray(cleaned.length * 3 / 4)

            var outputIndex = 0
            for (i in cleaned.indices step 4) {
                val sextet1 = BASE64_ALPHABET.indexOf(cleaned[i])
                val sextet2 = BASE64_ALPHABET.indexOf(cleaned.getOrElse(i + 1) { 'A' })
                val sextet3 = BASE64_ALPHABET.indexOf(cleaned.getOrElse(i + 2) { 'A' })
                val sextet4 = BASE64_ALPHABET.indexOf(cleaned.getOrElse(i + 3) { 'A' })

                val triple = (sextet1 shl 18) or (sextet2 shl 12) or (sextet3 shl 6) or sextet4

                if (outputIndex < output.size) output[outputIndex++] = (triple shr 16).toByte()
                if (outputIndex < output.size) output[outputIndex++] = (triple shr 8).toByte()
                if (outputIndex < output.size) output[outputIndex++] = triple.toByte()
            }

            return output.toString()
        }
    }
}
