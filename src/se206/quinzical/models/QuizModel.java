package se206.quinzical.models;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.image.Image;
import se206.quinzical.models.util.GsonPostProcessable;
import se206.quinzical.models.util.TextToSpeech;
import se206.quinzical.views.atom.Icon;

/**
 * Base class defining the behaviour of QuizModel.
 * Any class extending this class can track of the current question and trigger stuff in QuinzicalModel (protected field).
 */
public abstract class QuizModel implements GsonPostProcessable {
	private final ObjectProperty<Question> _currentQuestion = new SimpleObjectProperty<>();
	private final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.SELECT_CATEGORY);
	protected transient QuinzicalModel _model;

	/**
	 * Constructor that stores reference to the QuinzicalModel that stores QuizModel.
	 */
	public QuizModel(QuinzicalModel model) {
		_model = model;
	}

	/**
	 * Returns true if this model is being modified by the user
	 */
	public abstract boolean isActive();

	/**
	 * Submit an answer for the current question
	 */
	public abstract void answerQuestion(String answer);

	/**
	 * Select a new question
	 */
	public void beginQuestion(Question question) {
		_currentQuestion.set(question);
	}

	/**
	 * Return to category selection
	 */
	public void finishQuestion() {
		if (this.currentStateIsInternationalSection()) {
			setState(State.SELECT_CATEGORY);
			return;
		}

		if (getState() != State.CORRECT_ANSWER && getState() != State.INCORRECT_ANSWER && getState() != State.SKIP_ANSWER) {
			throw new IllegalStateException("Can only reset when in the CORRECT_ANSWER, INCORRECT_ANSWER or SKIP_ANSWER state");
		}
		setState(State.SELECT_CATEGORY);
	}

	/**
	 * Return list of categories of this QuizModel
	 */
	public abstract List<Category> getCategories();

	/**
	 * Return the current question of this model
	 */
	public Question getCurrentQuestion() {
		return _currentQuestion.get();
	}

	/**
	 * Return the current question property (e.g. so change listeners can be added)
	 */
	public ObjectProperty<Question> getCurrentQuestionProperty() {
		return _currentQuestion;
	}

	/**
	 * Return QuinzicalModel that encapsulates this QuizModel
	 */
	public QuinzicalModel getModel() {
		return _model;
	}

	/**
	 * Return state of this model (defined as enum in this class)
	 */
	public State getState() {
		return _state.get();
	}

	/**
	 * Return the state of the page (State is defined as enum in this class)
	 */
	protected void setState(State state) {
		_state.set(state);
		save();
	}

	/**
	 * Return the state property of the Quiz screen (State is defined in this class as enum)
	 */
	public ObjectProperty<State> getStateProperty() {
		return _state;
	}

	/**
	 * Return the TextToSpeech manager
	 */
	public TextToSpeech getTextToSpeech() {
		return _model.getTextToSpeech();
	}

	/**
	 * Return the visibility of the text for clue.
	 */
	public boolean getTextVisibility() {
		return _model.textVisible();
	}

	/**
	 * Return whether the clue text is visible
	 */
	public BooleanProperty getTextVisibleProperty() {
		return _model.getTextVisibleProperty();
	}

	/**
	 * After deserializing, we need to give currentQuestion a reference to its parent category (removed during serialization)
	 */
	@Override
	public void gsonPostProcess() {
		Question currentQuestion = getCurrentQuestion();
		if (currentQuestion != null) {
			Category activeCategory = getCategories()
					.stream()
					.filter(Category::isSelected)
					.findFirst()
					.orElse(null);
			currentQuestion.setCategory(activeCategory);
		}
	}

	/**
	 * Save current state to disk
	 */
	public void save() {
		_model.save();
	}

	public abstract void selectCategory(Category item);

	/**
	 * Sets the parent QuinzicalModel model
	 * Used by QuinzicalModel deserializer
	 */
	public void setQuinzicalModel(QuinzicalModel model) {
		_model = model;
	}

	/**
	 * Skin the category image to whatever category name is supplied
	 */
	public void skinCategoryImage(Icon icon, String categoryName) {
		if (categoryName.equals("icon-missing.png")) {
			icon.setImage("/se206/quinzical/assets/icon-missing.png");
			return;
		}

		if (categoryName.equals("toBeFilledIcon.png")) {
			icon.setImage("/se206/quinzical/assets/toBeFilledIcon.png");
			return;
		}

		if (categoryName.equals(QuinzicalModel.INTERNATIONAL)) {
			icon.setImage(new Image(getClass()
					.getResourceAsStream("/se206/quinzical/assets/" + QuinzicalModel.INTERNATIONAL + ".png")));
			return;
		}

		try {
			Image img = new Image(new FileInputStream("./categories/" + categoryName + ".png"));
			icon.setImage(img);
		}
		catch (NullPointerException | FileNotFoundException e) {
			icon.setImage("/se206/quinzical/assets/icon-missing.png");
		}
	}

	/**
	 * checks if the current state is international section
	 */
	public boolean currentStateIsInternationalSection() {
		return _model.getState() == QuinzicalModel.State.INTERNATIONAL;
	}

	/**
	 * gets international category from quinzical model
	 */
	public Category getInternationalCategoryFromQuinzicalModel() {
		return _model.getInternationalCategory();
	}

	/**
	 * The current screen being shown
	 */
	public enum State {
		RESET,
		SELECT_CATEGORY,
		CATEGORY_PREVIEW,
		ANSWER_QUESTION,
		CORRECT_ANSWER,
		INCORRECT_ANSWER,
		SKIP_ANSWER,
		RETRY_INCORRECT_ANSWER,
		GAME_OVER,
	}
}
