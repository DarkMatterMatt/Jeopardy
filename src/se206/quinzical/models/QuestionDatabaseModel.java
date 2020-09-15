package se206.quinzical.models;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se206.quinzical.models.util.MyScanner;

public class QuestionDatabaseModel {
	List<Category> _categories;
	
	public QuestionDatabaseModel(File[] categories) {
		_categories = new ArrayList<Category>();
		for(File category: categories) {
			//list of questions
			List<String> questions = MyScanner.readFileOutputString(category);
			//make category out of that
			Category newCategory = new Category(questions, category.getName());
			_categories.add(newCategory);
		}
	}
	
	
}
