package com.networknt.benchmark.helloworld;

import com.dslplatform.json.CompiledJson;
import com.dslplatform.json.JsonObject;
import com.dslplatform.json.JsonWriter;
import com.dslplatform.json.StringConverter;
import com.jsoniter.annotation.JsonProperty;
import com.jsoniter.output.JsonStream;

/**
 * Created by steve on 13/05/17.
 */
@CompiledJson
public class HelloWorld implements JsonObject {
    @JsonProperty(nullable = false)
    public String message;

    public static HelloWorld createHelloWorld() {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.message = "Hello World!";
        return helloWorld;
    }

    public static byte[] createTestJSON() {
        return JsonStream.serialize(createHelloWorld()).getBytes();
    }

    public void serialize(JsonWriter writer, boolean minimal) {
        writer.writeAscii("{\"message\":");
        StringConverter.serialize(message, writer);
        writer.writeAscii("}");
    }
}
