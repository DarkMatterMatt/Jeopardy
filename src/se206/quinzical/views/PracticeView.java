package se206.quinzical.views;

import javafx.scene.layout.HBox;
import se206.quinzical.models.QuinzicalModel;

public class PracticeView extends View {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;

	public PracticeView(QuinzicalModel model) {
		_model = model;
		
		CategoriesListView list = new CategoriesListView(model);
		SwitcherView content = new PracticeSwitcher(model);
		
		_container.getChildren().addAll(list.getView(), content.getView());
		
	}

	@Override
	public HBox getView() {
		return _container;
	}
}

class PracticeSwitcher extends SwitcherView{
	
	private final QuinzicalModel _model;
	
	// incorrect, correct, question asking, 
	private final AnswerView _answerQuestion;
	private final CorrectView _correctPane;
	private final IncorrectView _incorrectPane;
	private final HBox _nothingChosen;
	
	public PracticeSwitcher(QuinzicalModel model) {
		_model = model;

		//initialise possible views
		_answerQuestion = new AnswerView(_model.getPracticeModel());
		_correctPane = new CorrectView(_model.getPracticeModel());
		_incorrectPane = new IncorrectView(_model.getPracticeModel());
		_nothingChosen = new HBox();
		
		
		getView().getChildren().addAll(_answerQuestion.getView(), _correctPane.getView(), _incorrectPane.getView(), _nothingChosen);
		
		//start with nothing chosen.
		switchToView(_nothingChosen);
		_model.getPracticeModel().getStateProperty().addListener((obs, old, val) -> onModelStateChange());
		
	}

	private void onModelStateChange() {
		switch(_model.getPracticeModel().getState()) {
			case SELECT_CATEGORY:
				
				switchToView(_nothingChosen);
				break;
			case INCORRECT_ANSWER:
				switchToView(_incorrectPane.getView());
				break;
			case CORRECT_ANSWER:
				switchToView(_correctPane.getView());
				break;
			case ANSWER_QUESTION:
				switchToView(_answerQuestion.getView());
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
	
	
}