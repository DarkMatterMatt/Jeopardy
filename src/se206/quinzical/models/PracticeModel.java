package se206.quinzical.models;

import java.util.List;

public class PracticeModel extends QuizModel {
	public PracticeModel(QuinzicalModel model) {
		super(model);
	}

	@Override
	public void answerQuestion(String answer) {
		if (getState() != State.ANSWER_QUESTION) {
			throw new IllegalStateException("Previous state should be ANSWER_QUESTION, found " + getState());
		}
		boolean correct = getCurrentQuestion().checkAnswer(answer);
		// boolean correct = true;
		setState(correct ? State.CORRECT_ANSWER : State.INCORRECT_ANSWER);
	}

	@Override
	public List<Category> getCategories() {
		return _model.getCategories();
	}

	@Override
	public void selectCategory(Category item) {
		beginQuestion(item.getRandomQuestion());
		setState(QuizModel.State.SELECT_CATEGORY);
		setState(QuizModel.State.ANSWER_QUESTION);
	}
}
