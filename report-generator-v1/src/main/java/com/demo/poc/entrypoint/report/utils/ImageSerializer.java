package com.demo.poc.entrypoint.report.utils;

import java.util.Base64;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.http.codec.multipart.FilePart;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ImageSerializer {

  public static Mono<String> serializeImageAndGetBase64(FilePart imageFile) {
    return imageFile.content()
        .reduce(new byte[0], (acc, dataBuffer) -> {
          byte[] newBytes = new byte[acc.length + dataBuffer.readableByteCount()];
          System.arraycopy(acc, 0, newBytes, 0, acc.length);
          dataBuffer.read(newBytes, acc.length, dataBuffer.readableByteCount());
          return newBytes;
        })
        .map(bytes -> Base64.getEncoder().encodeToString(bytes));
  }
}
