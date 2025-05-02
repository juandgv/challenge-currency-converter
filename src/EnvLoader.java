import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class EnvLoader {
    public static String getApiKey() {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream(".env"));
            return props.getProperty("API_KEY");
        } catch (IOException e) {
            System.out.println("No se pudo leer la API KEY: " + e.getMessage());
            return null;
        }
    }
}
