import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.json.JSONObject;
import java.io.FileReader;
import org.json.JSONException;


public class TestAPI {
    public static void main(String[] args) {
        try {
            // URL de l'API
            URL url = new URL("https://reqres.in/api/users");

            // Ouverture de la connexion HTTP
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            // Lecture de la réponse de l'API
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String response = "";
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response += inputLine;
            }
            in.close();

            // Affichage de la réponse
            System.out.println("Réponse de l'API :");
            System.out.println(response);

            // Conversion de la réponse en objet JSON
            JSONObject jsonResponse = new JSONObject(response);

            // Lecture du fichier JSON de référence
            BufferedReader fileReader = new BufferedReader(new FileReader("src/main/resources/File.json"));
            String reference = "";
            while ((inputLine = fileReader.readLine()) != null) {
                reference += inputLine;
            }
            fileReader.close();

            // Conversion du fichier JSON de référence en objet JSON
            JSONObject jsonReference = new JSONObject(reference);

            // Comparaison des deux objets JSON
            if (jsonResponse.similar(jsonReference)) {
                System.out.println("Les deux objets JSON sont identiques.");
            } else {
                System.out.println("Les deux objets JSON sont différents.");
                System.out.println("Différence entre les deux objets JSON : ");
                System.out.println(jsonDiff(jsonReference, jsonResponse));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String jsonDiff(JSONObject json1, JSONObject json2) throws JSONException {
        JSONObject diff = new JSONObject();
        for (String key : json1.keySet()) {
            if (!json2.has(key)) {
                diff.put(key, json1.get(key));
            } else {
                Object value1 = json1.get(key);
                Object value2 = json2.get(key);
                if (value1 instanceof JSONObject) {
                    String subDiff = jsonDiff((JSONObject) value1, (JSONObject) value2);
                    if (!subDiff.equals("{}")) {
                        diff.put(key, new JSONObject(subDiff));
                    }
                } else if (!value1.equals(value2)) {
                    diff.put(key, json1.get(key));
                }
            }
        }
        return diff.toString();
    }
}
