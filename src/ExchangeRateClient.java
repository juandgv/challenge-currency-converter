import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ExchangeRateClient {

    // Método que convierte el monto en USD a la moneda deseada
    public static double convertir(double montoUSD, double tasa) {
        return montoUSD * tasa;
    }

    public static void main(String[] args) {
        String apiKey = EnvLoader.getApiKey();
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

        String[] monedas = {"ARS", "BOB", "BRL", "CLP", "COP", "USD"};

        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject conversionRates = json.getAsJsonObject("conversion_rates");

            // Solicitando cantidad de dinero al usuario
            Scanner scanner = new Scanner(System.in);
            System.out.print("Ingresa un monto en USD: ");
            double montoUSD = scanner.nextDouble();

            System.out.println("\nConversiones desde USD:");
            for (String moneda : monedas) {
                if (conversionRates.has(moneda)) {
                    double tasa = conversionRates.get(moneda).getAsDouble();
                    double convertido = convertir(montoUSD, tasa);
                    System.out.printf("USD %.2f → %s %.2f%n", montoUSD, moneda, convertido);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al procesar la conversión: " + e.getMessage());
        }
    }
}

