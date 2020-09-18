package se206.quinzical.models;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se206.quinzical.models.util.FileBrowser;
import se206.quinzical.models.util.MyScanner;

public class QuinzicalModel {
	List<Category> _categories;
	PresetQuinzicalModel _presetModel;
	
	public QuinzicalModel() {
		//read files from directory
		this(FileBrowser.filesInDirectory("./categories"));
		
	}
	
	
	public QuinzicalModel(File[] categories) {
		_categories = new ArrayList<Category>();
		for(File category: categories) {
			//list of questions
			List<String> questions = MyScanner.readFileOutputString(category);
			//make category out of that
			Category newCategory = new Category(questions, category.getName());
			_categories.add(newCategory);
		}

		_presetModel = new PresetQuinzicalModel(this);
	}
	
	
}
