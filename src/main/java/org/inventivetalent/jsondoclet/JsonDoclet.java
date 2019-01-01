package org.inventivetalent.jsondoclet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sun.javadoc.*;
import org.inventivetalent.jsondoclet.serializers.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonDoclet extends Doclet {

	public static boolean start(RootDoc root) {
		System.out.println(root.name());

		File outFile = null;
		boolean pretty = false;
		boolean singleFile = false;
		for (String[] pair : root.options()) {
			if ("-outfile".equals(pair[0])) {
				outFile = new File(pair[1]);
			}
			if ("-pretty".equals(pair[0])) {
				pretty = true;
			}
			if ("-singlefile".equals(pair[0])) {
				singleFile = true;
			}
		}
		if (outFile == null) {
			throw new IllegalArgumentException("Missing -outfile argument");
		}

		GsonBuilder builder = new GsonBuilder()

				.registerTypeHierarchyAdapter(ClassDoc.class, new ClassDocSerializer())
				.registerTypeHierarchyAdapter(ConstructorDoc.class, new ConstructorDocSerializer())
				.registerTypeHierarchyAdapter(Parameter.class, new ParameterSerializer())
				.registerTypeHierarchyAdapter(TypeVariable.class, new TypeVariableSerializer())
				.registerTypeHierarchyAdapter(FieldDoc.class, new FieldDocSerializer())
				.registerTypeHierarchyAdapter(MethodDoc.class, new MethodDocSerializer());

		if (pretty) {
			System.out.println("Pretty-Printing JSON is enabled");
			builder.setPrettyPrinting();
		}

		Gson gson = builder.create();

		if (singleFile) {
			System.out.println("Generating single .json file with all classes");

			JsonObject json = new JsonObject();
			ClassDoc[] classes = root.classes();
			System.out.println("Generating JSON for " + classes.length + " classes...");
			json.addProperty("size", classes.length);
			json.add("classes", gson.toJsonTree(classes));

			try (FileWriter writer = new FileWriter(outFile)) {
				writer.write(gson.toJson(json));
				return true;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		} else {
			System.out.println("Generating JSON folder structure and index.json");

			outFile.mkdirs();

			JsonObject index = new JsonObject();

			for (ClassDoc classDoc : root.classes()) {
				JsonObject indexEntry = new JsonObject();

				JsonArray fieldArray = new JsonArray();
				for (FieldDoc fieldDoc : classDoc.fields()) {
					fieldArray.add(fieldDoc.name());
				}
				indexEntry.add("fields", fieldArray);

				JsonArray enumArray = new JsonArray();
				for (FieldDoc fieldDoc : classDoc.enumConstants()) {
					enumArray.add(fieldDoc.name());
				}
				indexEntry.add("enumConstants", enumArray);

				JsonArray constructorArray = new JsonArray();
				for (ConstructorDoc constructorDoc : classDoc.constructors()) {
					constructorArray.add(constructorDoc.name() + constructorDoc.signature());
				}
				indexEntry.add("constructors", constructorArray);

				JsonArray methodArray = new JsonArray();
				for (MethodDoc methodDoc : classDoc.methods()) {
					methodArray.add(methodDoc.name() + methodDoc.signature());
				}
				indexEntry.add("methods", methodArray);

				String filePath = classDoc.containingPackage().name().replaceAll("\\.", "/");
				indexEntry.addProperty("path", filePath + "/" + classDoc.name() + ".json");

				File classDirectory = new File(outFile, filePath);
				if (!classDirectory.exists()) {
					classDirectory.mkdirs();
				}

				try (FileWriter writer = new FileWriter(new File(classDirectory, classDoc.name() + ".json"))) {
					writer.write(gson.toJson(classDoc));
					index.add(classDoc.qualifiedName(), indexEntry);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			try (FileWriter writer = new FileWriter(new File(outFile, "index.json"))) {
				writer.write(gson.toJson(index));
				return true;
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public static int optionLength(String option) {
		if ("-outfile".equals(option)) {
			return 2;
		}
		if ("-pretty".equals(option)) {
			return 1;
		}
		if ("-singlefile".equals(option)) {
			return 1;
		}
		return 0;
	}

	// Required for everything to work properly, e.g. isEnum always returns false if this is missing
	// https://stackoverflow.com/questions/21220943/problems-to-handle-some-5-0-language-features-enums-and-annotations-in-a-c
	public static LanguageVersion languageVersion() {
		return LanguageVersion.JAVA_1_5;
	}
}
