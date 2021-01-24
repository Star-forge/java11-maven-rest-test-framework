package tests;

import config.PropertyLoader;
import io.qameta.allure.Feature;
import io.qameta.allure.Story;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import report.RestAssuredLogger;
import util.Container;

/**
 * @author starforge
 */
@Feature("Тесты на проверку GitHub api")
@DisplayName("Тесты на проверку GitHub api")
class SimpleTest extends BaseTest {

    @Test
    @Tag("github")
    @Tag("status_code")
    @Tag("example")
    @Story("Проверка передачи User Information - проверка http статус кода")
    @DisplayName("Проверка  передачи User Information - проверка http статус кода")
    void testGivenUserExists_whenUserInformationIsRetrieved_thenRetrievedResourceIsCorrect() {
        Container<Response> responseContainer = new Container<>();
        step("Получение ответа на запрос", () -> responseContainer.accept(doRestRequest()));
        step("Проверяем, что код ответа " + HttpStatus.SC_OK, () ->
                Assertions.assertEquals(HttpStatus.SC_OK, responseContainer.get().getStatusCode(), "Получен неверный код ответа!"));
    }

    @Test
    @Tag("github")
    @Tag("content_type")
    @Tag("example")
    @Story("Проверка передачи User Information - проверка, что Response Content Type содержит application/json")
    @DisplayName("Проверка передачи User Information - проверка, что Response Content Type содержит application/json")
    void testGivenRequestWithNoAcceptHeader_whenRequestIsExecuted_thenDefaultResponseContentTypeIsJson() {
        Container<Response> responseContainer = new Container<>();
        String jsonMimeType = "application/json";
        step("Получение ответа на запрос", () -> responseContainer.accept(doRestRequest()));
        step("Проверяем, что Content Type содержит " + jsonMimeType, () -> {
            String contentType = responseContainer.get().getContentType();
            Assertions.assertTrue(contentType.contains(jsonMimeType), "Получен неверный Response Content Type!");
        });
    }

    private Response doRestRequest() {
        return RestAssured.given()
                .filters(new AllureRestAssured(), new RestAssuredLogger())
                .auth().basic(PropertyLoader.REST_USERNAME, PropertyLoader.REST_PASSWORD)
                .when()
                .get(PropertyLoader.REST_SERVER_URL)
                .then()
                .extract().response();
    }
}
