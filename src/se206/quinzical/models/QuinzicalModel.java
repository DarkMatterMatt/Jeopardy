package se206.quinzical.models;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.beans.property.*;
import org.hildan.fxgson.FxGson;
import se206.quinzical.models.util.FileHelper;
import se206.quinzical.models.util.GsonPostProcessable;
import se206.quinzical.models.util.GsonPostProcessingEnabler;
import se206.quinzical.models.util.TextToSpeech;
import se206.quinzical.views.AlertFactory;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Quinzical Model represents the model for the whole game.
 * Is responsible for keeping track of all the state (including state of the screens, questions, categories, all the other sub-models)
 */
public class QuinzicalModel implements GsonPostProcessable {
	public static final String INTERNATIONAL = "INTERNATIONAL";
	private static final String DEFAULT_CATEGORIES_LOCATION = "./categories/";
	private static final String DEFAULT_SAVE_LOCATION = "./.save";
	private static final Gson GSON = FxGson.coreBuilder()
			.registerTypeAdapterFactory(new GsonPostProcessingEnabler())
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
	private static final Integer MAXIMUM_LIVES = 3;
	private final List<Category> _categories = new ArrayList<>();
	private final IntegerProperty _currentInternationalScore = new SimpleIntegerProperty(0);
	private final IntegerProperty _internationalHighScore = new SimpleIntegerProperty(0);
	private final LeaderboardModel _leaderboardModel = new LeaderboardModel();
	private final IntegerProperty _lives = new SimpleIntegerProperty(MAXIMUM_LIVES);
	private final PracticeModel _practiceModel;
	private final PresetQuinzicalModel _presetModel;
	private transient final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.MENU);
	private final BooleanProperty _textEnabled = new SimpleBooleanProperty(true);
	private transient final TextToSpeech _textToSpeech = TextToSpeech.getInstance();
	private final ObjectProperty<Theme> _theme = new SimpleObjectProperty<>(Theme.BUMBLEBEE);
	private transient String _categoriesLocation = DEFAULT_CATEGORIES_LOCATION;
	private Category _internationalCategory = new Category(INTERNATIONAL);
	private transient String _saveFileLocation = DEFAULT_SAVE_LOCATION;

	public QuinzicalModel() {
		this(null);
	}

	public QuinzicalModel(String categoriesLocation) {
		if (categoriesLocation != null) {
			setCategoriesLocation(categoriesLocation);
		}
		loadCategories();

		_presetModel = new PresetQuinzicalModel(this);
		_practiceModel = new PracticeModel(this);
	}

	/**
	 * Load current state from disk
	 */
	public static QuinzicalModel load() {
		return load(DEFAULT_SAVE_LOCATION);
	}

	/**
	 * Load current state from disk
	 *
	 * @param saveFileLocation location to load state from
	 */
	public static QuinzicalModel load(String saveFileLocation) {
		try (Reader reader = new FileReader(saveFileLocation)) {
			Type type = new TypeToken<QuinzicalModel>() {
			}.getType();
			QuinzicalModel model = GSON.fromJson(reader, type);
			model.setSaveFileLocation(saveFileLocation);
			return model;
		}
		catch (FileNotFoundException err) {
			// save file does not exist
		}
		catch (IOException err) {
			System.err.println("Failed opening save file for reading");
			err.printStackTrace();
		}
		catch (JsonIOException err) {
			// invalid save file
			System.err.println("Failed reading save file");
		}
		catch (JsonSyntaxException err) {
			// invalid save file
			System.err.println("Invalid save file, ignoring it");
		}
		QuinzicalModel model = new QuinzicalModel();
		model.setSaveFileLocation(saveFileLocation);
		return model;
	}

	/**
	 * Set the screen state to MENU, and this state is enum defined in this class
	 */
	public void backToMainMenu() {
		setState(State.MENU);
	}

	/**
	 * Changes to the 'real' game state
	 */
	public void beginGame() {
		setState(State.GAME);
	}

	/**
	 * Switch to international game mode
	 */
	public void beginInternationalGame() {
		if (checkInternationalSectionCanStart()) {
			resetLives();
			resetInternationalScore();
			setState(State.INTERNATIONAL);
			getPracticeModel().setState(QuizModel.State.ANSWER_QUESTION);
		}
		else {
			AlertFactory.getCustomWarning(this, "This section is locked!",
					"You gotta complete at least two categories in Play mode.");
		}
	}

	/**
	 * Changes to the 'practice' game state
	 */
	public void beginPracticeGame() {
		setState(State.PRACTICE);
		getPracticeModel().setState(QuizModel.State.SELECT_CATEGORY);
	}

	/**
	 * Check if international section can start prerequisites - toBeInitialised
	 * state should not be true - PresetModel's categories size must be 5 - number
	 * of finished categories (in preset model) should be 2 or more
	 *
	 * @return true if international section can start
	 */
	public boolean checkInternationalSectionCanStart() {
		if (getPresetModel().getCategories().size() != 5 || getPresetModel().checkNeedToBeInitialised()) {
			return false;
		}
		int count = 0;
		for (Category c : getPresetModel().getCategories()) {
			if (c.getActiveQuestion() == null) {
				count++;
			}
		}
		return count >= 2;
	}

	/**
	 * Return the categories (ALL available categories)
	 */
	public List<Category> getCategories() {
		return Collections.unmodifiableList(_categories);
	}

	/**
	 * Get international category
	 */
	public Category getInternationalCategory() {
		return _internationalCategory;
	}

	public int getInternationalHighScore() {
		return _internationalHighScore.get();
	}

	public void setInternationalHighScore(int value) {
		_internationalHighScore.set(value);
	}

	public int getInternationalScore() {
		return _currentInternationalScore.get();
	}

	/**
	 * Return the leaderboard model (scores for main module)
	 */
	public LeaderboardModel getLeaderboardModel() {
		return _leaderboardModel;
	}

	/**
	 * Get lives property that is listenable
	 * lives are applicable for the international section
	 * can also be used to retrieve the current number of lives for the international section
	 */
	public IntegerProperty getLivesProperty() {
		return _lives;
	}

	/**
	 * Get the max number of lives
	 */
	public int getMaxLives() {
		return MAXIMUM_LIVES;
	}

	/**
	 * Return the practice model
	 */
	public PracticeModel getPracticeModel() {
		return _practiceModel;
	}

	/**
	 * Return the preset model (for main module)
	 */
	public PresetQuinzicalModel getPresetModel() {
		return _presetModel;
	}

	/**
	 * Return current save file location
	 */
	public String getSaveFileLocation() {
		return _saveFileLocation;
	}

	/**
	 * Set save file location
	 */
	private void setSaveFileLocation(String saveFileLocation) {
		_saveFileLocation = saveFileLocation;
	}

	public IntegerProperty getScoreProperty() {
		return _currentInternationalScore;
	}

	/**
	 * Return the state of the application, where this State definition is found as enum inn this class.
	 */
	public State getState() {
		return _state.get();
	}

	/**
	 * Set state to the given state (this State definition is found as enum defined in this class)
	 */
	private void setState(State state) {
		_state.set(state);
		save();
	}

	/**
	 * Return state property (e.g. so change listeners can be bound)
	 */
	public ObjectProperty<State> getStateProperty() {
		return _state;
	}

	/**
	 * Return the TextToSpeech manager
	 */
	public TextToSpeech getTextToSpeech() {
		return _textToSpeech;
	}

	/**
	 * Return whether text for clue is visible
	 */
	public BooleanProperty getTextVisibleProperty() {
		return _textEnabled;
	}

	/**
	 * Return current application theme.
	 */
	public Theme getTheme() {
		return _theme.get();
	}

	/**
	 * Change application theme
	 */
	public void setTheme(Theme theme) {
		_theme.set(theme);
		save();
	}

	/**
	 * Return theme property (e.g. so change listeners can be bound)
	 */
	public ObjectProperty<Theme> getThemeProperty() {
		return _theme;
	}

	/**
	 * After deserializing, we need to give each QuizModel a reference to its parent model (removed during serialization)
	 */
	@Override
	public void gsonPostProcess() {
		_presetModel.setQuinzicalModel(this);
		_practiceModel.setQuinzicalModel(this);
	}

	public void increaseInternationalScore() {
		_currentInternationalScore.set(_currentInternationalScore.get() + 1);
	}

	private void loadCategories() {
		loadCategories(_categoriesLocation);
	}

	private void loadCategories(String categoriesLocation) {
		_categories.clear();

		for (File categoryFile : FileHelper.filesInDirectory(categoriesLocation)) {
			String categoryName = categoryFile.getName();
			String[] iconTypes = {"jpg", "png"};

			boolean isImage = Arrays.stream(iconTypes).anyMatch(categoryName::endsWith);

			if (!isImage) {
				Category newCategory = new Category(categoryFile.getName());

				// list of questions
				for (String rawQuestion : FileHelper.readFileOutputString(categoryFile)) {
					try {
						newCategory.addQuestion(new Question(rawQuestion, newCategory));
					}
					catch (IllegalArgumentException e) {
						System.err.println("Invalid question data: " + rawQuestion);
					}
				}
				// make category out of that
				if (newCategory.getName().equals(INTERNATIONAL)) {
					_internationalCategory = newCategory;
				}
				else if (newCategory.getQuestions().size() > 0) {
					_categories.add(newCategory);
				}
			}
		}
	}

	/**
	 * Reduce the number of lives for international section
	 */
	public void reduceLives() {
		_lives.set((_lives.get() != 0) ? (_lives.get() - 1) : (0));
	}

	/**
	 * Called when reset is triggered by the user.
	 */
	public void reset() {
		if (getState() != State.GAME) {
			throw new IllegalStateException("Can only reset when in the GAME state");
		}
		loadCategories();
		_presetModel.reset();
	}

	public void resetInternationalScore() {
		_currentInternationalScore.set(0);
	}

	/**
	 * Reset lives
	 */
	public void resetLives() {
		_lives.set(MAXIMUM_LIVES);
	}

	/**
	 * Save current state to disk
	 */
	public void save() {
		save(_saveFileLocation);
	}

	/**
	 * Save current state to disk
	 *
	 * @param saveFileLocation location to load state from
	 */
	public void save(String saveFileLocation) {
		try (Writer writer = new FileWriter(saveFileLocation)) {
			GSON.toJson(this, writer);
		}
		catch (JsonIOException err) {
			System.err.println("Failed serializing game state");
			err.printStackTrace();
		}
		catch (IOException err) {
			System.err.println("Failed opening save file for writing");
			err.printStackTrace();
		}
	}

	/**
	 * Set category directory location
	 */
	private void setCategoriesLocation(String categoriesLocation) {
		_categoriesLocation = categoriesLocation;
	}

	/**
	 * Display the leaderboard containing 'real' game scores
	 */
	public void showLeaderboard() {
		setState(State.LEADERBOARD);
	}

	/**
	 * Display the leaderboard containing 'real' game scores
	 */
	public void showThemeSelection() {
		setState(State.THEME_SELECT);
	}

	/**
	 * Check whether text for clue is disabled or enabled
	 */
	public boolean textVisible() {
		return _textEnabled.get();
	}

	/**
	 * Set text visibility to what is not the current visibility.
	 * <p>
	 * This text visibility refers to the visibility of the question string.
	 * An alternative text is displayed (press T again to revert the visibility)
	 */
	public void toggleTextVisibility() {
		_textEnabled.set(!_textEnabled.get());
		save();
	}

	/**
	 * Main screen being shown
	 */
	public enum State {
		MENU,
		GAME,
		PRACTICE,
		INTERNATIONAL,
		LEADERBOARD,
		THEME_SELECT,
	}
}
