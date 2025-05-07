package kr.respectme.group.common.aspect

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.BufferedReader
import java.io.ByteArrayInputStream
import java.io.InputStreamReader

class RequestWrapper(request: HttpServletRequest): HttpServletRequestWrapper(request) {

    private val body: ByteArray

    init {
        val inputStream = request.inputStream
        body = inputStream.readAllBytes()
    }

    override fun getInputStream(): ServletInputStream {
        val byteInputStream = ByteArrayInputStream(body)
        return object: ServletInputStream() {
            override fun isFinished(): Boolean {
                return byteInputStream.available() == 0
            }

            override fun isReady() = true

            override fun setReadListener(listener: ReadListener?) {}

            override fun read() = byteInputStream.read()
        }
    }

    override fun getReader(): BufferedReader {
        val inputStreamReader = InputStreamReader(ByteArrayInputStream(body))
        return BufferedReader(inputStreamReader)
    }

    fun getBody(): String {
        return String(body)
    }
}