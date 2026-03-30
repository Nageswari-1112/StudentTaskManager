package backend;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.*;
import java.util.*;

public class TaskServer {

    static String FILE = "../data/tasks.txt";

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // GET + POST tasks
        server.createContext("/tasks", exchange -> {
            if ("GET".equals(exchange.getRequestMethod())) {
                String response = readTasks();
                sendResponse(exchange, response);
            }

            if ("POST".equals(exchange.getRequestMethod())) {
                String body = new String(exchange.getRequestBody().readAllBytes());
                addTask(body);
                sendResponse(exchange, "Task Added");
            }
        });

        // MARK COMPLETE
        server.createContext("/complete", exchange -> {
            String query = exchange.getRequestURI().getQuery();
            String id = query.split("=")[1];

            markComplete(id);
            sendResponse(exchange, "Updated");
        });

        server.start();
        System.out.println("Server running at http://localhost:8080");
    }

    // READ tasks
    static String readTasks() {
        try {
            File file = new File(FILE);
            if (!file.exists()) return "";
            return Files.readString(Paths.get(FILE));
        } catch (Exception e) {
            return "";
        }
    }

    // ADD task
    static void addTask(String task) {
        try {
            FileWriter fw = new FileWriter(FILE, true);
            fw.write(task + "\n");
            fw.close();
        } catch (Exception e) {
            System.out.println("Error writing task");
        }
    }

    // MARK COMPLETE
    static void markComplete(String id) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(FILE));
            List<String> updated = new ArrayList<>();

            for (String line : lines) {
                String[] parts = line.split(",");

                if (parts[0].equals(id)) {
                    parts[3] = "true";
                    line = String.join(",", parts);
                }

                updated.add(line);
            }

            Files.write(Paths.get(FILE), updated);
        } catch (Exception e) {
            System.out.println("Error updating task");
        }
    }

    static void sendResponse(HttpExchange exchange, String response) throws IOException {
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.sendResponseHeaders(200, response.getBytes().length);

        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }
}