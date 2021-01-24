package report;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author starforge
 */
@NoArgsConstructor
public class RestAssuredLogger implements OrderedFilter {
    private static final int MAX_LEN_FOR_LOG = 1_000;
    protected static Logger mainLog = LoggerFactory.getLogger("main");

    private static String trimLongMessage(String message) {
        return message.length() > MAX_LEN_FOR_LOG ? message.substring(0, MAX_LEN_FOR_LOG) + "<trimmed>" : message;
    }

    @SneakyThrows
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext filterContext) {
        mainLog.debug("Request method: {}", requestSpec.getMethod());
        mainLog.debug("Request URI: {}", requestSpec.getURI());
        mainLog.debug("Request Headers: {}", requestSpec.getHeaders());
        String requestBody = trimLongMessage(new ObjectMapper().writerWithDefaultPrettyPrinter()
                .writeValueAsString(requestSpec.getBody()));
        mainLog.debug("Request Body: {}", requestBody);


        Response response = filterContext.next(requestSpec, responseSpec);
        mainLog.debug("----------------------");
        mainLog.debug("Response Code: {}", response.getStatusCode());
        mainLog.debug("Response Headers: {}", response.getHeaders());
        String responseBody = trimLongMessage(response.getBody().prettyPrint());
        mainLog.debug("Response Body: {}", responseBody);

        return response;
    }

    @Override
    public int getOrder() {
        return OrderedFilter.LOWEST_PRECEDENCE;
    }
}
