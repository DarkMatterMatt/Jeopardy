package se206.quinzical.models;

import java.util.Collections;
import java.util.List;

public class PracticeModel extends QuizModel {
	private final List<Category> _categories;

	public PracticeModel(QuinzicalModel model) {
		super(model);
		_categories = _model.getCategories();
	}

	@Override
	public void answerQuestion(String answer) {
		if (getState() != State.ANSWER_QUESTION && getState() != State.RETRY_INCORRECT_ANSWER) {
			throw new IllegalStateException("Previous state should be ANSWER_QUESTION or RETRY_INCORRECT_ANSWER, found " + getState());
		}
		boolean correct = getCurrentQuestion().checkAnswer(answer);

		// count number of attempts
		Question q = getCurrentQuestion();
		// increase number of attempt for that question
		if(!correct) q.setNumAttempted(q.getNumAttempted()+1);

		// if that question has been answered 3 times, reset that question
		// and change the active question to different random question
		if(correct || q.getNumAttempted()>=3) {
			q.setNumAttempted(0);
			q.getCategory().setActiveQUestionInPracticeModule(q.getCategory().getRandomQuestion());
			setState(correct ? State.CORRECT_ANSWER : State.INCORRECT_ANSWER);
			return;
		}
		// incorrect, answered less than 3 times, let the user retry
		setState(State.RETRY_INCORRECT_ANSWER);
		setState(State.ANSWER_QUESTION);
	}

	@Override
	public List<Category> getCategories() {
		return Collections.unmodifiableList(_categories);
	}

	@Override
	public void selectCategory(Category item) {
		beginQuestion(item.getActiveQuestionInPracticeModule());
		setState(QuizModel.State.SELECT_CATEGORY);
		setState(QuizModel.State.ANSWER_QUESTION);
	}
}
