package se206.quinzical.views;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import se206.quinzical.models.PracticeModel;
import se206.quinzical.models.QuinzicalModel;
/**
 * PracticeView is the structure of the GUI excluding the
 * HeaderView. The main composition is CategoriesListView on the left
 * and a switchable view on the right (refer to SwitcherView class).
 * It is visible when the user enters the practice mode. Depending on
 * the actions of the user, the contents will switch.
 *
 * @author hajinkim
 *
 */
public class PracticeView extends View {
	private final HBox _container = new HBox();
	private final QuinzicalModel _model;

	public PracticeView(QuinzicalModel model) {
		_model = model;

		CategoriesListView list = new CategoriesListView(_model.getPracticeModel());
		SwitcherView content = new PracticeSwitcher(_model.getPracticeModel());
		//center the answer view
		VBox listContainer = new VBox(list.getView());
		HBox.setHgrow(content.getView(), Priority.ALWAYS);
		listContainer.getStyleClass().add("practice");

		addStylesheet("practice.css");
		_container.getChildren().addAll(listContainer, content.getView());
	}
	@Override
	public HBox getView() {
		return _container;
	}
}

class PracticeSwitcher extends SwitcherView{
	private final PracticeModel _practiceModel;

	// incorrect, correct, question asking,
	private final AnswerView answerView;
	private final CorrectView _correctPane;
	private final IncorrectView _incorrectPane;
	private final HBox _nothingChosen;

	public PracticeSwitcher(PracticeModel practiceModel) {
		_practiceModel = practiceModel;

		//initialise possible views
		answerView = new AnswerView(_practiceModel);
		_correctPane = new CorrectView(_practiceModel);
		_incorrectPane = new IncorrectView(_practiceModel);
		_nothingChosen = new HBox();

//		//center the answer view
//		_answerQuestion = new VBox(answerView.getView());
//		HBox.setHgrow(_answerQuestion, Priority.ALWAYS);
//		_answerQuestion.getStyleClass().add("practice-view-container");
//		getView().getStyleClass().add("practice");


		getView().getChildren().addAll(answerView.getView(), _correctPane.getView(), _incorrectPane.getView(), _nothingChosen);
		
		//start with nothing chosen.
		switchToView(_nothingChosen);
		_practiceModel.getStateProperty().addListener((obs, old, val) -> onModelStateChange());
	}

	private void onModelStateChange() {
		switch(_practiceModel.getState()) {
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
				switchToView(answerView.getView());
				break;
			default:
				throw new UnsupportedOperationException("Unexpected model state");
		}
	}
}
