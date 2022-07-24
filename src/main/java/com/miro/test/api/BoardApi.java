package com.miro.test.api;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.miro.test.configs.ConfigManager.getDefaultAccessToken;
import static com.miro.test.configs.ConfigManager.getDefaultTeamId;
import static io.restassured.RestAssured.given;

public class BoardApi {
    protected Response post(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .when()
                .post();
    }

    protected Response post(RequestSpecification requestSpecification, String path) {
        return given()
                .spec(requestSpecification)
                .when()
                .post(path);
    }

    protected Response get(RequestSpecification requestSpecification, String path) {
        return given()
                .spec(requestSpecification)
                .when()
                .get(path);
    }

    protected Response get(RequestSpecification requestSpecification) {
        return given()
                .spec(requestSpecification)
                .when()
                .get();
    }

    private RequestSpecification baseRequest() {
        return new RequestSpecBuilder()
                .setBaseUri("https://api.miro.com")
                .setBasePath("/v2/boards")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .addHeader("Authorization", "Bearer " + getDefaultAccessToken())
                .build();
    }

    public String createBoard(String boardName) {
        String policy = "{\n" +
                "                \"permissionsPolicy\": {\n" +
                "                    \"collaborationToolsStartAccess\": \"all_editors\",\n" +
                "                    \"copyAccess\": \"anyone\",\n" +
                "                    \"copyAccessLevel\": \"anyone\",\n" +
                "                    \"sharingAccess\": \"team_members_with_editing_rights\"\n" +
                "                },\n" +
                "                \"sharingPolicy\": {\n" +
                "                    \"access\": \"private\",\n" +
                "                    \"inviteToAccountAndBoardLinkAccess\": \"editor\",\n" +
                "                    \"organizationAccess\": \"private\",\n" +
                "                    \"teamAccess\": \"edit\"\n" +
                "                }\n" +
                "            }";

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", boardName);
        requestBody.put("policy", new JSONObject(policy));
        requestBody.put("teamId", getDefaultTeamId());

        RequestSpecification requestSpecification = baseRequest().contentType("application/json")
                .body(requestBody.toString());
        return post(requestSpecification)
                .then().assertThat().statusCode(HttpStatus.SC_CREATED)
                .extract()
                .body()
                .jsonPath().getString("id");

    }

    public boolean isBoardPresent(String boardName) {
        Response response = get(baseRequest());
        JSONObject jsonObject = new JSONObject(response.then().assertThat().statusCode(HttpStatus.SC_OK)
                .extract()
                .body().asString());
        JSONArray data = jsonObject.getJSONArray("data");
        for (int i=0; i< data.length(); i++) {
            JSONObject dataJsonObject = data.getJSONObject(i);
            if (dataJsonObject.getString("name").equals(boardName)) {
                return true;
            }
        }
        return false;

    }
}
