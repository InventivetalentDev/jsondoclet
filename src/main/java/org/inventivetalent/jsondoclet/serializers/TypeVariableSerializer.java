package org.inventivetalent.jsondoclet.serializers;

import com.google.gson.*;
import com.sun.javadoc.TypeVariable;

import java.lang.reflect.Type;

public class TypeVariableSerializer implements JsonSerializer<TypeVariable> {

	@Override
	public JsonElement serialize(TypeVariable variable, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject json = new JsonObject();

		json.addProperty("name", variable.qualifiedTypeName());

		JsonArray boundsArray = new JsonArray();
		for (com.sun.javadoc.Type typ : variable.bounds()) {
			boundsArray.add(typ.qualifiedTypeName());
		}
		json.add("bounds", boundsArray);

		return json;
	}

}
