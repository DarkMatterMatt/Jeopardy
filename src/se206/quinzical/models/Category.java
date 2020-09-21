package se206.quinzical.models;
import java.util.ArrayList;
import java.util.List;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Callback;

public class Category {
	String _name;
	List<Question> _questions = new ArrayList<Question>();
	boolean _isSelected = false;
	
	public String getName() {
		return _name;
	}
	
	public void setSelected() {
		_isSelected = true;
	}
	
	public void setUnselected() {
		_isSelected = false;
	}
	public boolean isSelected() {
		return _isSelected;
	}
	
	public Category(List<String> questions, String name) {
		_name = name;
		for (String question: questions) {
			try {
				Question q = new Question(question);
				_questions.add(q);
			}catch(IllegalArgumentException e) {
				//
			}

		}
	}
	
	
	public Category(List<Question> questions, String name, boolean placeHolder) {
		_questions = questions;
		_name = name;
	}
	
	
}
