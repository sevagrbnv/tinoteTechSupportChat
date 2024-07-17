package utils

fun getMimeType(fileName: String): Pair<String, String> {
    return when (fileName.substringAfterLast('.', "").lowercase()) {
        "txt" -> "text/plain" to "EX"
        "jpg", "jpeg" -> "image/jpeg" to "IM"
        "png" -> "image/png" to "IM"
        "pdf" -> "application/pdf" to "EX"
        "zip" -> "application/zip" to "EX"
        "rar" -> "application/x-rar-compressed" to "EX"
        "mp4" -> "video/mp4" to "VD"
        else -> "application/octet-stream" to "EX"
    }
}