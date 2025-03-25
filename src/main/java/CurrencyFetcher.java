import java.io.IOException;
import java.net.http.*;
        import java.net.URI;
import org.json.JSONObject;

public class CurrencyFetcher {

    public static double getRate(String from, String to) throws IOException, InterruptedException {
        if (from.equals(to)) return 1;

        HttpClient client = HttpClient.newHttpClient();
        String url = String.format("https://api.frankfurter.app/latest?from=%s&to=%s", from, to);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());

        double rate = json.getJSONObject("rates").getDouble(to);
        return rate;
    }

}
