package se206.quinzical.models;

import java.io.File;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import se206.quinzical.models.util.*;

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
	
	
	public static void iterateEveryCategory(List<Category> categories) {
		// iterate over each category
		for(Category c: categories) {
			System.out.println("==============================");
			System.out.println("category: " + c._name);
			for(Question q: c._questions) {
				System.out.println("The question is "+ q._question);
				System.out.println("the answer is "+ q._answer);
			}
		}
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
