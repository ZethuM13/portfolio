package com.serviceplus;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Main {
    private static final Path FRONTEND = Path.of("frontend");
    private static final List<ServiceRecord> SERVICES = new ArrayList<>();
    private static final AtomicLong IDS = new AtomicLong(1004);
    private static final Pattern JSON_FIELD = Pattern.compile("\\\"([^\\\"]+)\\\"\\s*:\\s*\\\"([^\\\"]*)\\\"");

    public static void main(String[] args) throws IOException {
        seedServices();
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/api/health", Main::health);
        server.createContext("/api/services", Main::services);
        server.createContext("/", Main::staticFiles);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("ServicePlus running at http://localhost:8080");
    }

    private static void seedServices() {
        SERVICES.add(new ServiceRecord(1001, "Customer Identity Gateway", "Thandi Ndlovu", "Customer Experience", "CRITICAL", "HEALTHY", Instant.now().toString()));
        SERVICES.add(new ServiceRecord(1002, "Payments Orchestration", "Michael Peters", "Finance", "CRITICAL", "DEGRADED", Instant.now().toString()));
        SERVICES.add(new ServiceRecord(1003, "Partner Reporting Hub", "Aisha Khan", "Data & Analytics", "HIGH", "HEALTHY", Instant.now().toString()));
        SERVICES.add(new ServiceRecord(1004, "Legacy Claims Connector", "Lerato Molefe", "Operations", "HIGH", "UNAVAILABLE", Instant.now().toString()));
    }

    private static void health(HttpExchange exchange) throws IOException {
        if (!exchange.getRequestMethod().equals("GET")) {
            json(exchange, 405, "{\"error\":\"Method not allowed\"}");
            return;
        }
        json(exchange, 200, "{\"status\":\"UP\",\"service\":\"serviceplus-api\"}");
    }

    private static void services(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET" -> listServices(exchange);
            case "POST" -> createService(exchange);
            default -> json(exchange, 405, "{\"error\":\"Method not allowed\"}");
        }
    }

    private static void listServices(HttpExchange exchange) throws IOException {
        String query = exchange.getRequestURI().getRawQuery();
        String search = query != null && query.startsWith("q=")
                ? java.net.URLDecoder.decode(query.substring(2), StandardCharsets.UTF_8).toLowerCase(Locale.ROOT)
                : "";
        String json = SERVICES.stream()
                .filter(service -> search.isBlank() || service.searchText().contains(search))
                .map(ServiceRecord::toJson)
                .reduce((left, right) -> left + "," + right)
                .map(value -> "[" + value + "]")
                .orElse("[]");
        json(exchange, 200, json);
    }

    private static void createService(HttpExchange exchange) throws IOException {
        String request = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        Map<String, String> fields = parseFields(request);
        List<String> required = List.of("name", "owner", "domain", "criticality", "status");
        List<String> missing = required.stream().filter(key -> fields.getOrDefault(key, "").isBlank()).toList();
        if (!missing.isEmpty()) {
            json(exchange, 400, "{\"error\":\"Missing required fields: " + String.join(", ", missing) + "\"}");
            return;
        }
        if (SERVICES.stream().anyMatch(service -> service.name.equalsIgnoreCase(fields.get("name")))) {
            json(exchange, 409, "{\"error\":\"A service with this name already exists\"}");
            return;
        }
        ServiceRecord service = new ServiceRecord(
                IDS.incrementAndGet(),
                fields.get("name").trim(),
                fields.get("owner").trim(),
                fields.get("domain").trim(),
                fields.get("criticality").toUpperCase(Locale.ROOT),
                fields.get("status").toUpperCase(Locale.ROOT),
                Instant.now().toString()
        );
        SERVICES.add(service);
        json(exchange, 201, service.toJson());
    }

    private static Map<String, String> parseFields(String json) {
        java.util.HashMap<String, String> fields = new java.util.HashMap<>();
        Matcher matcher = JSON_FIELD.matcher(json);
        while (matcher.find()) {
            fields.put(matcher.group(1), matcher.group(2));
        }
        return fields;
    }

    private static void staticFiles(HttpExchange exchange) throws IOException {
        String requestPath = exchange.getRequestURI().getPath();
        String relative = requestPath.equals("/") ? "index.html" : requestPath.substring(1);
        Path file = FRONTEND.resolve(relative).normalize();
        if (!file.startsWith(FRONTEND) || !Files.isRegularFile(file)) {
            text(exchange, 404, "Not found", "text/plain; charset=utf-8");
            return;
        }
        String contentType = relative.endsWith(".css") ? "text/css; charset=utf-8"
                : relative.endsWith(".js") ? "application/javascript; charset=utf-8"
                : "text/html; charset=utf-8";
        byte[] bytes = Files.readAllBytes(file);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }

    private static void json(HttpExchange exchange, int status, String body) throws IOException {
        text(exchange, status, body, "application/json; charset=utf-8");
    }

    private static void text(HttpExchange exchange, int status, String body, String contentType) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", contentType);
        exchange.getResponseHeaders().set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }

    private record ServiceRecord(long id, String name, String owner, String domain, String criticality, String status, String createdAt) {
        String searchText() {
            return (name + " " + owner + " " + domain + " " + criticality + " " + status).toLowerCase(Locale.ROOT);
        }

        String toJson() {
            return "{\"id\":" + id
                    + ",\"name\":\"" + escape(name)
                    + "\",\"owner\":\"" + escape(owner)
                    + "\",\"domain\":\"" + escape(domain)
                    + "\",\"criticality\":\"" + escape(criticality)
                    + "\",\"status\":\"" + escape(status)
                    + "\",\"createdAt\":\"" + escape(createdAt) + "\"}";
        }

        private static String escape(String value) {
            return value.replace("\\", "\\\\").replace("\"", "\\\"");
        }
    }
}
