import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsultaMonedaApi {

    private static final String API_KEY = "6ad121631456bd3caf0589fb";

    public ApiRespuesta consultar(String monedaOrigen, String monedaDestino) {
        String url = String.format("https://v6.exchangerate-api.com/v6/%s/pair/%s/%s", API_KEY, monedaOrigen, monedaDestino);
        URI direccion = URI.create(url);

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(direccion)
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new RuntimeException("Error al obtener datos de la API. Código: " + response.statusCode());
            }

            ApiRespuesta resultado = new Gson().fromJson(response.body(), ApiRespuesta.class);

            if (!"success".equalsIgnoreCase(resultado.result)) {
                throw new RuntimeException("No se pudo realizar la conversión. Verifique las monedas ingresadas.");
            }

            return resultado;

        } catch (Exception e) {
            throw new RuntimeException("No se pudo consultar la moneda: " + e.getMessage());
        }
    }
}