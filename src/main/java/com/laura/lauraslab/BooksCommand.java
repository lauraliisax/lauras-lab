package com.laura.lauraslab;

import java.io.PrintStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class BooksCommand {
    private static final String BASE_URL = "http://localhost:8080/books";
    private static final HttpClient httpClient = HttpClient.newHttpClient();

    public BooksCommand() {
    }

    @ShellMethod(
            key = {"books-api-client"}
    )
    public String apiClientMain() throws JSONException {
        Scanner scanner = new Scanner(System.in);

        int option;
        do {
            System.out.println("\nSelect an option:");
            System.out.println("1. Retrieve a list of all books");
            System.out.println("2. Retrieve a specific book by its id");
            System.out.println("3. Create a new book");
            System.out.println("4. Delete a specific book by its id");
            System.out.println("5. Exit");
            System.out.print("Option: ");
            option = scanner.nextInt();
            scanner.nextLine();
            switch (option) {
                case 1:
                    listBooks();
                    break;
                case 2:
                    System.out.print("Enter book ID: ");
                    String id = scanner.nextLine();
                    getBook(id);
                    break;
                case 3:
                    System.out.print("Enter book title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter book description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter published year: ");
                    String year = scanner.nextLine();
                    System.out.print("Enter author: ");
                    String author = scanner.nextLine();
                    System.out.print("Enter category: ");
                    String category = scanner.nextLine();
                    createBook(title, description, year, author, category);
                    break;
                case 4:
                    System.out.print("Enter book ID: ");
                    String idDelete = scanner.nextLine();
                    this.deleteBook(idDelete);
                    break;
                case 5:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid option, please try again.");
            }
        } while(option != 5);

        return "Done";
    }

    private void deleteBook(String id) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/books/" + id)).DELETE().build();

        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("Deleted the book with id " + id);
        } catch (Exception var4) {
            System.out.println("Error deleting a book: " + var4.getMessage());
        }

    }

    private static void listBooks() {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/books")).GET().build();

        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            JSONArray books = new JSONArray((String)response.body());

            for(int i = 0; i < books.length(); ++i) {
                JSONObject book = books.getJSONObject(i);
                PrintStream var10000 = System.out;
                int var10001 = book.getInt("id");
                var10000.println("ID: " + var10001 + ", Title: " + book.getString("title") + ", Author: " + book.getString("author"));
            }
        } catch (Exception var5) {
            System.out.println("Error retrieving books: " + var5.getMessage());
        }

    }

    private static void getBook(String id) {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/books/" + id)).GET().build();

        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            JSONObject book = new JSONObject((String)response.body());
            PrintStream var10000 = System.out;
            int var10001 = book.getInt("id");
            var10000.println("ID: " + var10001 + ", Title: " + book.getString("title") + ", Description: " + book.getString("description") + ", Published Year: " + book.getInt("yearPublished") + ", Author: " + book.getString("author") + ", Category: " + book.getString("category"));
        } catch (Exception var4) {
            System.out.println("Error retrieving book: " + var4.getMessage());
        }

    }

    private static void createBook(String title, String description, String year, String author, String category) throws JSONException {
        JSONObject newBook = new JSONObject();
        newBook.put("title", title);
        newBook.put("description", description);
        newBook.put("yearPublished", year);
        newBook.put("author", author);
        newBook.put("category", category);
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create("http://localhost:8080/books")).header("Content-Type", "application/json").POST(BodyPublishers.ofString(newBook.toString())).build();

        try {
            HttpResponse<String> response = httpClient.send(request, BodyHandlers.ofString());
            System.out.println("New book created: " + (String)response.body());
        } catch (Exception var8) {
            System.out.println("Error creating book: " + var8.getMessage());
        }

    }
}
