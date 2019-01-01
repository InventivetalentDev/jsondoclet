package org.inventivetalent.jsondoclet.serializers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.sun.javadoc.MethodDoc;

import java.lang.reflect.Type;

import static org.inventivetalent.jsondoclet.serializers.SerializerUtils.serializeDocInto;
import static org.inventivetalent.jsondoclet.serializers.SerializerUtils.serializeTypeInto;

public class MethodDocSerializer implements JsonSerializer<MethodDoc> {
	@Override
	public JsonElement serialize(MethodDoc methodDoc, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject json = new JsonObject();

		serializeDocInto(methodDoc, json, jsonSerializationContext);

		json.addProperty("name", methodDoc.name());
		json.addProperty("signature", methodDoc.signature());
		json.add("returnType", serializeTypeInto(methodDoc.returnType(), new JsonObject(), jsonSerializationContext));
		json.addProperty("isAbstract", methodDoc.isAbstract());
		json.addProperty("isDefault", methodDoc.isDefault());
		json.addProperty("isStatic", methodDoc.isStatic());

		json.add("parameters", jsonSerializationContext.serialize(methodDoc.parameters()));
		json.add("typeParameters", jsonSerializationContext.serialize(methodDoc.typeParameters()));

		return json;
	}
}
