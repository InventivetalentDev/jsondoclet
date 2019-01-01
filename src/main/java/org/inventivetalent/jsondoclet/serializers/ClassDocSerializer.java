package org.inventivetalent.jsondoclet.serializers;

import com.google.gson.*;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.FieldDoc;

import java.lang.reflect.Type;

import static org.inventivetalent.jsondoclet.serializers.SerializerUtils.*;

public class ClassDocSerializer implements JsonSerializer<ClassDoc> {

	public JsonElement serialize(ClassDoc classDoc, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject json = new JsonObject();

		serializeDocInto(classDoc, json, jsonSerializationContext);
		serializeTypeInto(classDoc, json, jsonSerializationContext);

		json.addProperty("package", classDoc.containingPackage().name());
		json.addProperty("superclass", classDoc.superclass() != null ? classDoc.superclass().qualifiedTypeName() : null);
		{
			JsonArray interfaces = new JsonArray();
			for (ClassDoc iDoc : classDoc.interfaces()) {
				interfaces.add(iDoc.qualifiedTypeName());
			}
			json.add("interfaces", interfaces);
		}
		json.addProperty("isAbstract", classDoc.isAbstract());
		json.addProperty("isStatic", classDoc.isStatic());
		json.addProperty("since", getTagTextOrNull(classDoc.tags("since"), 0));

		JsonArray innerClassArray = new JsonArray();
		for (ClassDoc innerDoc : classDoc.innerClasses()) {
			innerClassArray.add(innerDoc.qualifiedName());
		}
		json.add("innerClasses", innerClassArray);
		json.addProperty("isInnerClass", classDoc.containingClass() != null);

		json.add("constructors", jsonSerializationContext.serialize(classDoc.constructors()));
		json.add("fields", jsonSerializationContext.serialize(classDoc.fields()));
		json.add("methods", jsonSerializationContext.serialize(classDoc.methods()));
		json.add("typeParameters", jsonSerializationContext.serialize(classDoc.typeParameters()));

		JsonArray enumArray = new JsonArray();
		for (FieldDoc e : classDoc.enumConstants()) {
			enumArray.add(e.name());
		}
		json.add("enumConstants", enumArray);

		return json;
	}

}
