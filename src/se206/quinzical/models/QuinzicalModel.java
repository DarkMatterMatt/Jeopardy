package se206.quinzical.models;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hildan.fxgson.FxGson;
import se206.quinzical.models.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Quinzical Model represents the model for the whole game.
 * Is responsible for keeping track of all the state (including state of the screens, questions, categories, all the other sub-models)
 */
public class QuinzicalModel implements GsonPostProcessable {
	private static final String DEFAULT_CATEGORIES_LOCATION = "./categories/";
	private static final String DEFAULT_SAVE_LOCATION = "./.save";
	private static final Gson GSON = FxGson.coreBuilder()
			.registerTypeAdapterFactory(new GsonPostProcessingEnabler())
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
	private final List<Category> _categories = new ArrayList<>();
	private final PracticeModel _practiceModel;
	private final PresetQuinzicalModel _presetModel;
	private final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.MENU);
	private final BooleanProperty _textEnabled = new SimpleBooleanProperty(true);
	private final TextToSpeech _textToSpeech = new TextToSpeech();
	private transient String _categoriesLocation = DEFAULT_CATEGORIES_LOCATION;
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
			QuinzicalModel model = GSON.fromJson(reader, QuinzicalModel.class);
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
		_state.set(State.MENU);
	}

	/**
	 * Changes to the 'real' game state
	 */
	public void beginGame() {
		setState(State.GAME);
	}

	/**
	 * Changes to the 'practice' game state
	 */
	public void beginPracticeGame() {
		setState(State.PRACTICE);
	}

	/**
	 * Return the categories (ALL available categories)
	 */
	public List<Category> getCategories() {
		return Collections.unmodifiableList(_categories);
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
	 * After deserializing, we need to give each QuizModel a reference to its parent model (removed during serialization)
	 */
	@Override
	public void gsonPostProcess() {
		_presetModel.setQuinzicalModel(this);
		_practiceModel.setQuinzicalModel(this);
	}

	private void loadCategories() {
		loadCategories(_categoriesLocation);
	}

	private void loadCategories(String categoriesLocation) {
		_categories.clear();

		for (File categoryFile : FileBrowser.filesInDirectory(categoriesLocation)) {
			String categoryName = categoryFile.getName();
			String[] iconTypes = {"jpg", "png"};

			boolean isImage = Arrays.stream(iconTypes).anyMatch(categoryName::endsWith);

			if (!isImage) {
				Category newCategory = new Category(categoryFile.getName());

				// list of questions
				for (String rawQuestion : MyScanner.readFileOutputString(categoryFile)) {
					try {
						newCategory.addQuestion(new Question(rawQuestion, newCategory));
					}
					catch (IllegalArgumentException e) {
						System.err.println("Invalid question data: " + rawQuestion);
					}
				}
				// make category out of that
				_categories.add(newCategory);
			}
		}
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
	}
}
