package com.financeflow.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
public class HealthController {

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> rootLandingPage() {
        String html = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>FinanceFlow API — Online</title>
                <style>
                    body {
                        font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif;
                        background: #08090d;
                        color: #f4f4f5;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        min-height: 100vh;
                        margin: 0;
                        padding: 20px;
                        box-sizing: border-box;
                    }
                    .card {
                        background: rgba(255, 255, 255, 0.03);
                        border: 1px solid rgba(255, 255, 255, 0.1);
                        border-radius: 12px;
                        max-width: 580px;
                        width: 100%;
                        padding: 32px;
                        box-shadow: 0 20px 40px rgba(0,0,0,0.5);
                    }
                    .status {
                        display: inline-flex;
                        align-items: center;
                        gap: 8px;
                        color: #10b981;
                        background: rgba(16, 185, 129, 0.1);
                        border: 1px solid rgba(16, 185, 129, 0.25);
                        padding: 4px 12px;
                        border-radius: 20px;
                        font-size: 13px;
                        font-family: monospace;
                        font-weight: 600;
                        margin-bottom: 20px;
                    }
                    .dot {
                        width: 8px;
                        height: 8px;
                        background: #10b981;
                        border-radius: 50%;
                        box-shadow: 0 0 8px #10b981;
                    }
                    h1 {
                        font-size: 26px;
                        font-weight: 800;
                        margin: 0 0 10px 0;
                        letter-spacing: -0.5px;
                    }
                    p {
                        color: #a1a1aa;
                        font-size: 14.5px;
                        line-height: 1.6;
                        margin: 0 0 24px 0;
                    }
                    .endpoints {
                        background: #040508;
                        border: 1px solid rgba(255, 255, 255, 0.06);
                        border-radius: 8px;
                        padding: 16px;
                        font-family: monospace;
                        font-size: 13px;
                        line-height: 1.8;
                        color: #d4d4d8;
                        margin-bottom: 24px;
                    }
                    .method { color: #38bdf8; font-weight: 700; }
                    .btn-swagger {
                        display: block;
                        text-align: center;
                        background: linear-gradient(135deg, #0284c7 0%, #0891b2 100%);
                        color: #ffffff !important;
                        font-weight: 700;
                        font-size: 14px;
                        padding: 12px 20px;
                        border-radius: 8px;
                        text-decoration: none;
                        margin-bottom: 24px;
                        box-shadow: 0 4px 14px rgba(8, 145, 178, 0.35);
                        transition: transform 0.15s ease, box-shadow 0.15s ease;
                    }
                    .btn-swagger:hover {
                        transform: translateY(-1px);
                        box-shadow: 0 6px 20px rgba(8, 145, 178, 0.5);
                        text-decoration: none !important;
                    }
                    .footer {
                        border-top: 1px solid rgba(255, 255, 255, 0.08);
                        padding-top: 18px;
                        display: flex;
                        justify-content: space-between;
                        align-items: center;
                        font-size: 13px;
                        color: #71717a;
                    }
                    a { color: #38bdf8; text-decoration: none; }
                    a:hover { text-decoration: underline; }
                </style>
            </head>
            <body>
                <div class="card">
                    <div class="status">
                        <span class="dot"></span>
                        <span>API ONLINE &bull; v1.0.0</span>
                    </div>
                    <h1>FinanceFlow Core REST API</h1>
                    <p>Production enterprise banking and financial aggregation platform built on Spring Boot 3.3.5 and PostgreSQL 16.</p>
                    
                    <div class="endpoints">
                        <div><span class="method">GET</span>  <a href="/api/health">/api/health</a> &bull; Service Health Check</div>
                        <div><span class="method">POST</span> /api/auth/register &bull; User Registration</div>
                        <div><span class="method">POST</span> /api/auth/login &bull; Stateless JWT Authentication</div>
                        <div><span class="method">GET</span>  /api/categories &bull; Expense Categories (JWT Required)</div>
                        <div><span class="method">GET</span>  /api/transactions &bull; Transaction History (JWT Required)</div>
                        <div><span class="method">POST</span> /api/v1/mesh/simulate &bull; Offline BLE Mesh Simulation</div>
                    </div>

                    <a href="/swagger-ui/index.html" class="btn-swagger">⚡ Open Interactive Swagger UI &rarr;</a>

                    <div class="footer">
                        <span>Engineered by <strong>Ronit Gupta</strong></span>
                        <a href="https://github.com/ronitgupta138/finflow-core" target="_blank">GitHub Repository &rarr;</a>
                    </div>
                </div>
            </body>
            </html>
            """;
        return ResponseEntity.ok(html);
    }

    @GetMapping(value = "/api/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "FinanceFlow-API",
                "version", "1.0.0",
                "timestamp", Instant.now().toString()
        ));
    }
}
