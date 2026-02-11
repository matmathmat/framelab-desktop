package fr.framelab.api;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.framelab.api.exceptions.HttpClientErrorException;
import fr.framelab.api.exceptions.HttpServerErrorException;
import fr.framelab.api.model.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FrameLabAPI {
    protected String domaineName;
    protected boolean useHTTPS;
    protected String token;
    protected HttpClient client;

    public FrameLabAPI(String domaineName, boolean useHTTPS) {
        if (domaineName == null) {
            throw new IllegalArgumentException("Null domaineName is not allowed");
        }
        if (domaineName.isBlank()) {
            throw new IllegalArgumentException("Empty domaineName is not allowed");
        }

        this.domaineName = domaineName;
        this.useHTTPS = useHTTPS;
        this.client = HttpClient.newHttpClient();
    }

    private String CreateURL() {
        if (useHTTPS) {
            return "https://" + domaineName;
        } else {
            return "http://" + domaineName;
        }
    }

    private String CreateURL(String endpoint) {
        return this.CreateURL() + "/" + endpoint;
    }

    public boolean login(String email, String password) throws IOException, InterruptedException {
        // Initialiser gson
        Gson gson = new Gson();

        // Créer le corps de la requête
        AuthRequest requestAuthRequest = new AuthRequest(email, password);
        String requestBody = gson.toJson(requestAuthRequest);

        // Créer la requête
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CreateURL("api/auth/login")))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        // Envoyer la requête
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Si la requête a réussis

            // On deserialize la réponse
            Type apiResponseType = new TypeToken<APIResponse<User>>(){}.getType();
            APIResponse<User> apiResponse = gson.fromJson(response.body(), apiResponseType);

            // Stocker le token récupéré
            this.token = apiResponse.getResult().getToken();

            // On retourne vrai car on a obtenu le token
            return true;
        } else {
            // Si la requête n'a pas réussis

            // On deserialize la réponse
            ErrorResponse errorResponse = gson.fromJson(response.body(), ErrorResponse.class);

            if (response.statusCode() > 399 && response.statusCode() < 500) {
                throw new HttpClientErrorException(errorResponse.getMessage());
            } else {
                throw new HttpServerErrorException(errorResponse.getMessage());
            }
        }
    }

    //public
}
