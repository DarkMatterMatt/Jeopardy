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
		
		// count number of attempts 
		Question q = _model.getPracticeModel().getCurrentQuestion();
		// increase number of attempt for that question
		if(!correct) q.setNumAttempted(q.getNumAttempted()+1);
		
		// refresh (needed for hint to appear at third time)
		setState(State.SELECT_CATEGORY);
		setState(State.ANSWER_QUESTION);
		
		// if that question has been answered 3 times, reset that question
		// and change the active question to different random question
		if(correct || q.getNumAttempted()>=3) {
			q.setNumAttempted(0);
			q.getCategory().setActiveQUestionInPracticeModule(q.getCategory().getRandomQuestion());			
			setState(correct ? State.CORRECT_ANSWER : State.INCORRECT_ANSWER);
		}
		

		
	}

	@Override
	public List<Category> getCategories() {
		return _model.getCategories();
	}

	@Override
	public void selectCategory(Category item) {
		beginQuestion(item.getActiveQuestionInPracticeModule());
		setState(QuizModel.State.SELECT_CATEGORY);
		setState(QuizModel.State.ANSWER_QUESTION);
	}
}
