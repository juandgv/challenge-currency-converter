import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ExchangeRateClient {

    public static double convertir(double monto, double tasa) {
        return monto * tasa;
    }

    public static void exibirMenu() {
        System.out.println("=========================================");
        System.out.println("   🌍 Bienvenido/a al Conversor de Moneda 🌍");
        System.out.println("=========================================");
        System.out.println("1. USD => Peso Argentino (ARS)");
        System.out.println("2. Peso Argentino (ARS) => USD");
        System.out.println("3. USD => Real Brasileño (BRL)");
        System.out.println("4. Real Brasileño (BRL) => USD");
        System.out.println("5. USD => Peso Colombiano (COP)");
        System.out.println("6. Peso Colombiano (COP) => USD");
        System.out.println("7. ❌ Salir");
        System.out.print("👉 Elija una opción válida (1-7): ");
    }

    public static void main(String[] args) {
        String apiKey = EnvLoader.getApiKey();
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/USD";

        try {

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            JsonObject json = JsonParser.parseString(response.body()).getAsJsonObject();
            JsonObject rates = json.getAsJsonObject("conversion_rates");

            double tasaARS = rates.get("ARS").getAsDouble();
            double tasaBRL = rates.get("BRL").getAsDouble();
            double tasaCOP = rates.get("COP").getAsDouble();

            Scanner scanner = new Scanner(System.in);
            int opcion;

            do {
                exibirMenu();
                while (!scanner.hasNextInt()) {
                    System.out.print("⚠️ Entrada inválida. Por favor, ingrese un número del 1 al 7: ");
                    scanner.next(); // Limpia entrada inválida
                }
                opcion = scanner.nextInt();

                if (opcion >= 1 && opcion <= 6) {
                    System.out.print("💰 Ingrese el monto a convertir: ");
                    while (!scanner.hasNextDouble()) {
                        System.out.print("⚠️ Monto inválido. Ingrese un número válido: ");
                        scanner.next();
                    }
                    double monto = scanner.nextDouble();

                    double resultado = 0;
                    switch (opcion) {
                        case 1 -> resultado = convertir(monto, tasaARS);
                        case 2 -> resultado = convertir(monto, 1 / tasaARS);
                        case 3 -> resultado = convertir(monto, tasaBRL);
                        case 4 -> resultado = convertir(monto, 1 / tasaBRL);
                        case 5 -> resultado = convertir(monto, tasaCOP);
                        case 6 -> resultado = convertir(monto, 1 / tasaCOP);
                    }

                    String monedaDestino = switch (opcion) {
                        case 1, 2 -> "ARS";
                        case 3, 4 -> "BRL";
                        case 5, 6 -> "COP";
                        default -> "";
                    };

                    String desde = (opcion % 2 == 1) ? "USD" : monedaDestino;
                    String hacia = (opcion % 2 == 1) ? monedaDestino : "USD";

                    System.out.printf("💱 Resultado: %.2f %s = %.2f %s%n%n", monto, desde, resultado, hacia);

                } else if (opcion != 7) {
                    System.out.println("⚠️ Opción no válida. Intente nuevamente.\n");
                }

            } while (opcion != 7);

            System.out.println("👋 Gracias por usar el conversor de monedas. ¡Hasta pronto!");

        } catch (Exception e) {
            System.out.println("❌ Error al procesar la conversión: " + e.getMessage());
        }
    }
}
