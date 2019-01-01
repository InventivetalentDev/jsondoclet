package org.inventivetalent.jsondoclet.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sun.javadoc.Parameter;

import java.lang.reflect.Type;

import static org.inventivetalent.jsondoclet.serializers.SerializerUtils.serializeTypeInto;

public class ParameterSerializer implements JsonSerializer<Parameter> {

	@Override
	public JsonElement serialize(Parameter parameter, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject json = new JsonObject();

		json.addProperty("name", parameter.name());
		json.add("type", serializeTypeInto(parameter.type(), new JsonObject(), jsonSerializationContext));

		return json;
	}

}
