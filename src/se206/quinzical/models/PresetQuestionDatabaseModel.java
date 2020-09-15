package se206.quinzical.models;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se206.quinzical.models.util.RandomNumberGenerator;

public class PresetQuestionDatabaseModel {
	List<Category> _fiveCategoriesWithFiveQuestions;

	public PresetQuestionDatabaseModel(QuestionDatabaseModel qdb) {
		_fiveCategoriesWithFiveQuestions = new ArrayList<Category>();
		
		List<Category> categories = selectFiveCategories(qdb);
		for(Category category: categories) {
			Category newCategory = selectFiveQuestions(category);
			_fiveCategoriesWithFiveQuestions.add(newCategory);
		}
	}
	
	public List<Category> selectFiveCategories(QuestionDatabaseModel qdb) {
		List<Integer> fiveNumbers = RandomNumberGenerator.takeFive(qdb._categories.size());
		List<Category> result = new ArrayList<Category>();
		
		
		for(Integer n: fiveNumbers) {
			//get nth category from the categories set, and add to the preset categories
			result.add(qdb._categories.get(n));
		}
		
		return result;
		
	}
	
	public Category selectFiveQuestions(Category c) {
		List<Integer> fiveNumbers = RandomNumberGenerator.takeFive(c._questions.size());
		List<Question> fiveQuestions = new ArrayList<Question>();
		
		
		for(Integer n: fiveNumbers) {
			//get nth question from the questions of the category, and add to the list
			fiveQuestions.add(c._questions.get(n));
		}
		
		return new Category(fiveQuestions, c._name, true);		
	}
	
	

}
