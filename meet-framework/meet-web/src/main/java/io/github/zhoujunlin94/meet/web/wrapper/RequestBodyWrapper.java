package io.github.zhoujunlin94.meet.web.wrapper;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Slf4j
public class RequestBodyWrapper extends HttpServletRequestWrapper {

    private final byte[] body;

    public RequestBodyWrapper(HttpServletRequest request) throws IOException {
        super(request);
        this.body = getRequestBodyBytes(request);
    }

    private byte[] getRequestBodyBytes(HttpServletRequest request) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            ServletInputStream inputStream = request.getInputStream();
            byte[] chunk = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(chunk, 0, chunk.length)) != -1) {
                buffer.write(chunk, 0, bytesRead);
            }
            return buffer.toByteArray();
        }
    }

    @Override
    public ServletInputStream getInputStream() {
        return new CachedServletInputStream(body);
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
    }

    public byte[] getBodyBytes() {
        return body;
    }

    public String getBodyString() {
        return new String(body, StandardCharsets.UTF_8);
    }

    private static class CachedServletInputStream extends ServletInputStream {
        private final ByteArrayInputStream bais;

        public CachedServletInputStream(byte[] body) {
            this.bais = new ByteArrayInputStream(body);
        }

        @Override
        public int read() {
            return bais.read();
        }

        @Override
        public int available() {
            return bais.available();
        }

        @Override
        public boolean isFinished() {
            return bais.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener readListener) {
            // 不支持异步读取
        }
    }
}
