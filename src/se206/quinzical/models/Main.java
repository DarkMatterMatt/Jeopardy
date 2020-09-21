package se206.quinzical.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import se206.quinzical.models.util.FileBrowser;

import java.io.File;

public class Main {
	public static void main(String[] args) {
		File[] categories = FileBrowser.filesInDirectory("./categories");
		QuinzicalModel _model = new QuinzicalModel(categories);
		PresetQuinzicalModel _presetModel = new PresetQuinzicalModel(_model);

		System.out.println(makeJson(_model));
		System.out.println(makeJson(_presetModel));

//		Type t = new TypeToken<QuestionDatabaseModel>() {}.getType();
//		QuestionDatabaseModel _model2 = gson.fromJson(output, t);
	}

	public static String makeJson(Object obj) {
		GsonBuilder g = new GsonBuilder();
		g.setPrettyPrinting();
		g.disableHtmlEscaping();
		Gson gson = g.create();
//		Gson gson = new Gson();

		return gson.toJson(obj);
	}
}
