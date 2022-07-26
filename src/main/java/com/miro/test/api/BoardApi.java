package com.miro.test.api;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.HttpStatus;
import org.json.JSONArray;
import org.json.JSONObject;

import static com.miro.test.configs.ConfigManager.getDefaultTeamId;

public class BoardApi extends BaseApi {
    private static String BASE_PATH = "/v2/boards";

    public BoardApi() {
        super(BASE_PATH);
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
