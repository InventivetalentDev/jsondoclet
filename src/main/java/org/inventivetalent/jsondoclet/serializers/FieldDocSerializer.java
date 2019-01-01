package org.inventivetalent.jsondoclet.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sun.javadoc.FieldDoc;

import java.lang.reflect.Type;

import static org.inventivetalent.jsondoclet.serializers.SerializerUtils.serializeDocInto;

public class FieldDocSerializer implements JsonSerializer<FieldDoc> {
	@Override
	public JsonElement serialize(FieldDoc fieldDoc, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject json = new JsonObject();

		serializeDocInto(fieldDoc, json, jsonSerializationContext);


		json.addProperty("name", fieldDoc.name());
		json.addProperty("type", fieldDoc.type().qualifiedTypeName());
		json.addProperty("typeDimensions", fieldDoc.type().dimension());
		json.addProperty("isStatic", fieldDoc.isStatic());

		return json;
	}
}
