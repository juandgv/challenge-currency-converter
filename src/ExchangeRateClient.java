import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRateClient {
    public static void main(String[] args) {
        String apiKey = EnvLoader.getApiKey();
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Imprimir JSON crudo por ahora
            System.out.println("Respuesta JSON:");
            System.out.println(response.body());

        } catch (Exception e) {
            System.out.println("Error al hacer la solicitud: " + e.getMessage());
        }
    }
}
