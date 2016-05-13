package com.tengos.sociot.api;


import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class SensorRegistryLambdaHandler implements RequestHandler<SensorRegistryEvent, SensorRegistryEventResponse> {

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
	
    @Override
    public SensorRegistryEventResponse handleRequest(SensorRegistryEvent input, Context context) {
        context.getLogger().log("Input: " + input.toString());
        //Get DynamoCLient and send SensorEvent 
        AmazonDynamoDBClient dc = new AmazonDynamoDBClient(new BasicAWSCredentials("AKIAID272RX7TAE6QLQQ", "hEDU6DdViYzpMfCNj9LQYa9NRia/qAS3rjg8C5jc")).withRegion(Regions.EU_WEST_1);
        //Fill the map to put into DynamoDB
        Map<String, AttributeValue> se = getAsMap(input);
        se.put(SensorConstant.sensorUserIdCN, new AttributeValue(context.getAwsRequestId()));
        dc.putItem(SensorConstant.sensorTableName, se);
        
        SensorRegistryEventResponse response = new SensorRegistryEventResponse(SensorRegistryEventResponseStatus.OK.toString(), "Request processed");
        return response;
        		
    }
    
    private Map<String, AttributeValue> getAsMap(SensorRegistryEvent event) {
    	HashMap<String, AttributeValue> map = new HashMap<String, AttributeValue>();
    	map.put(SensorConstant.sensorIdCN, new AttributeValue(event.getSensorId()));
    	map.put(SensorConstant.sensorTypeCN, new AttributeValue(event.getType()));    	
    	map.put(SensorConstant.sensorRegistryDateCN, new AttributeValue(sdf.format(event.getRegistryDate())));    	
    	map.put(SensorConstant.sensorDescriptionCN, new AttributeValue(event.getDescription()));
    	map.put(SensorConstant.sensorStatusCN, new AttributeValue(event.getStatus()));    	
    	map.put(SensorConstant.sensorLongitudeCN, new AttributeValue(String.valueOf(event.getLongitude())));
    	map.put(SensorConstant.sensorLatitudeCN, new AttributeValue(String.valueOf(event.getLatitude())));    	
    	return map;

    }
    
    

}
