package com.demo.poc.entrypoint.report.service;

import com.demo.poc.entrypoint.report.dto.request.ReportRequestDto;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface ReportGeneratorService {

  Mono<byte[]> generateReport(Map<String, String> headers, ReportRequestDto reportRequest);
}
