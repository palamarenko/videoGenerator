package ua.palamarenko.videogenerator.io.video.pexel

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer

class ProgressResponseBody(
    private val responseBody: ResponseBody,
    private val progressListener: ((progress: Int) -> Unit)
) : ResponseBody() {

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return responseBody.contentType()
    }

    override fun contentLength(): Long {
        return responseBody.contentLength()
    }

    override fun source(): BufferedSource {
        if (bufferedSource == null) {
            bufferedSource = source(responseBody.source()).buffer()
        }
        return bufferedSource!!
    }

    private fun source(source: Source): Source {
        return object : ForwardingSource(source) {
            private var totalBytesRead = 0L

            override fun read(sink: Buffer, byteCount: Long): Long {
                val bytesRead = super.read(sink, byteCount)
                totalBytesRead += if (bytesRead != -1L) bytesRead else 0L
                val progress = ((totalBytesRead * 100) / responseBody.contentLength()).toInt()
                progressListener(progress)
                return bytesRead
            }
        }
    }

    fun updateProgress(bytesRead: Long, contentLength: Long) {
        val progress = ((bytesRead * 100) / contentLength).toInt()
        progressListener(progress)
    }
}