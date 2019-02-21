package org.inventivetalent.jsondoclet.serializers;

import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.sun.javadoc.Doc;
import com.sun.javadoc.Tag;
import com.sun.javadoc.Type;
import com.sun.javadoc.TypeVariable;

public class SerializerUtils {

	public static <T> T getOrNull(T[] elements, int i) {
		if (i < 0) {
			throw new IllegalArgumentException();
		}

		if (i < elements.length) {
			return elements[i];
		} else {
			return null;
		}
	}

	public static String getTagTextOrNull(Tag[] elements, int i) {
		if (i < 0) {
			throw new IllegalArgumentException();
		}

		if (i < elements.length) {
			return elements[i].text();
		} else {
			return null;
		}
	}

	public static JsonObject serializeDocInto(Doc doc, JsonObject json,JsonSerializationContext context) {
		if (doc != null) {
			json.addProperty("name", doc.name());
			json.addProperty("comment", doc.commentText());
			json.addProperty("isClass", doc.isClass());
			json.addProperty("isInterface", doc.isInterface());
			json.addProperty("isEnum", doc.isEnum());
			json.addProperty("isEnumConstant", doc.isEnumConstant());
			json.addProperty("isConstructor", doc.isConstructor());
			json.addProperty("isField", doc.isField());
			json.addProperty("isMethod", doc.isMethod());
			json.addProperty("isOrdinaryClass", doc.isOrdinaryClass());
		}

		return json;
	}

	public static JsonObject serializeTypeInto(Type type, JsonObject json, JsonSerializationContext context) {
		if (type != null) {
			json.addProperty("isPrimitive", type.isPrimitive());
			json.addProperty("simpleName", type.simpleTypeName());
			json.addProperty("qualifiedName", type.qualifiedTypeName());
			json.addProperty("dimension", type.dimension());// [] if it's an array
			json.add("elementType", serializeTypeInto(type.getElementType(), new JsonObject(),context));// contains array element types

			TypeVariable typeVariable =type.asTypeVariable();
			if (typeVariable != null) {
				if (context != null) {
					json.add("typeVariable", context.serialize(typeVariable));
				}
			}
		}

		return json;
	}

}
