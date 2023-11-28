package handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import layer.service.APIGatewayService;
import layer.service.APIGatewayServiceImpl;
import layer.service.DynamoDBService;
import layer.service.DynamoDBServiceImpl;

public class UpdateUserFunction implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private static final DynamoDBService dynamoDBService = new DynamoDBServiceImpl();
    private static final APIGatewayService apiGatewayService = new APIGatewayServiceImpl();

    public APIGatewayProxyResponseEvent handleRequest(final APIGatewayProxyRequestEvent input, final Context context) {
        try {
            String userId = getUserIdFromPathParameters(input);
            String requestBody = input.getBody();

            dynamoDBService.updateUser(userId, requestBody);

            return apiGatewayService.createResponse("Record updated successfully", 200);
        } catch (Exception e) {
            e.printStackTrace();

            return apiGatewayService.createResponse("Error updating record", 503);
        }
    }

    private String getUserIdFromPathParameters(APIGatewayProxyRequestEvent input) {
        return input.getPathParameters().get("userId");
    }
}

