package org.inventivetalent.jsondoclet.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sun.javadoc.ConstructorDoc;

import java.lang.reflect.Type;

import static org.inventivetalent.jsondoclet.serializers.SerializerUtils.serializeDocInto;

public class ConstructorDocSerializer implements JsonSerializer<ConstructorDoc> {

	@Override
	public JsonElement serialize(ConstructorDoc constructorDoc, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject json = new JsonObject();

		serializeDocInto(constructorDoc, json, jsonSerializationContext);

		json.addProperty("signature", constructorDoc.signature());
		json.addProperty("flatSignature", constructorDoc.flatSignature());
		json.add("parameters",jsonSerializationContext.serialize(constructorDoc.parameters()));
		json.add("typeParameters", jsonSerializationContext.serialize(constructorDoc.typeParameters()));

		return json;
	}

}
