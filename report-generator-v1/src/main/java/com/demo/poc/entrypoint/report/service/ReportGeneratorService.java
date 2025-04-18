package com.demo.poc.entrypoint.report.service;

import org.springframework.http.codec.multipart.Part;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ReportGeneratorService {

  Mono<byte[]> generateReport(Map<String, String> headers, Map<String, Part> images);
}
