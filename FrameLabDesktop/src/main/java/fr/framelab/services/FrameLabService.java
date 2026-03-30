package fr.framelab.services;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import fr.framelab.dto.APIResponseDTO;
import fr.framelab.dto.AuthRequestDTO;
import fr.framelab.dto.ErrorResponseDTO;
import fr.framelab.exceptions.http.*;
import fr.framelab.models.Challenge;
import fr.framelab.models.User;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FrameLabService {
    protected String domaineName;
    protected boolean useHTTPS;
    protected String token;
    protected HttpClient client;
    public User currentUser;

    public FrameLabService(String domaineName, boolean useHTTPS) {
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

    public void setToken(String token) {
        this.token = token;
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

    private void ManageFailedResponse(HttpResponse<String> response) {
        // On deserialize la réponse
        ErrorResponseDTO errorResponseDTO = new Gson().fromJson(response.body(), ErrorResponseDTO.class);

        switch(response.statusCode()) {
            case 400:
                throw new HttpBadRequestException(errorResponseDTO.getMessage());
            case 401:
                throw new HttpUnauthorizedException(errorResponseDTO.getMessage());
            case 402:
                throw new HttpPaymentRequiredException(errorResponseDTO.getMessage());
            case 403:
                throw new HttpForbiddenException(errorResponseDTO.getMessage());
            case 404:
                throw new HttpNotFoundException(errorResponseDTO.getMessage());
            case 500:
                throw new HttpInternalServerErrorException(errorResponseDTO.getMessage());
            default:
                throw new HttpUnclassifiedException(errorResponseDTO.getMessage());
        }
    }

    public boolean login(String email, String password) throws IOException, InterruptedException {
        // Créer le corps de la requête
        AuthRequestDTO requestAuthRequestDTO = new AuthRequestDTO(email, password);
        String requestBody = new Gson().toJson(requestAuthRequestDTO);

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
            Type apiResponseType = new TypeToken<APIResponseDTO<User>>(){}.getType();
            APIResponseDTO<User> apiResponseDTO = new Gson().fromJson(response.body(), apiResponseType);

            // Stocker le token récupéré
            this.token = apiResponseDTO.getResult().getToken();

            // On retourne vrai car on a obtenu le token
            return true;
        } else {
            // Si la requête n'a pas réussis, on laisse notre fonction lever la bonne erreur
            ManageFailedResponse(response);
            return false;
        }
    }

    public Challenge getActiveChallenge() throws IOException, InterruptedException {
        // Initialiser gson
        Gson gson = new Gson();

        // Créer la requête
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CreateURL("api/challenges/current")))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.token)
                .build();

        // Envoyer la requête
        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // Si la requête a réussis

            // On deserialize la réponse
            Type apiResponseType = new TypeToken<APIResponseDTO<Challenge>>(){}.getType();
            APIResponseDTO<Challenge> apiResponseDTO = gson.fromJson(response.body(), apiResponseType);

            // On retourne le challenge
            return apiResponseDTO.getResult();
        } else {
            // Si la requête n'a pas réussis, on laisse notre fonction lever la bonne erreur
            ManageFailedResponse(response);
            return null;
        }
    }

    public User getMe() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CreateURL("api/users/me")))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + this.token)
                .GET()
                .build();

        HttpResponse<String> response = this.client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            Type apiResponseType = new TypeToken<APIResponseDTO<User>>(){}.getType();
            APIResponseDTO<User> apiResponseDTO = new Gson().fromJson(response.body(), apiResponseType);
            this.currentUser = apiResponseDTO.getResult();
            return this.currentUser;
        } else {
            ManageFailedResponse(response);
            return null;
        }
    }
}
