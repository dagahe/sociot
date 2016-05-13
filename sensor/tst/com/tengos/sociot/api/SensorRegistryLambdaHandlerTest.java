package com.tengos.sociot.api;

import java.io.IOException;
import java.util.Date;

import org.junit.BeforeClass;
import org.junit.Test;

import com.amazonaws.services.lambda.runtime.Context;

/**
 * A simple test harness for locally invoking your Lambda function handler.
 */
public class SensorRegistryLambdaHandlerTest {
	
    private static SensorRegistryEvent input;

    @BeforeClass
    public static void createInput() throws IOException {
        input = new SensorRegistryEvent("0001", new Date(System.currentTimeMillis()),  SensorConstant.SensorType.TEMPERATURE.name(), "Test sensor");
    }

    private Context createContext() {
        TestContext ctx = new TestContext();
        ctx.setFunctionName("SensorRegistryLambdaHandler");
        return ctx;
    }

    @Test
    public void testSensorRegistryLambdaHandler() {
        SensorRegistryLambdaHandler handler = new SensorRegistryLambdaHandler();
        Context ctx = createContext();

        Object output = handler.handleRequest(input, ctx);

        // TODO: validate output here if needed.
        if (output != null) {
            System.out.println(output.toString());
        }
    }
}
