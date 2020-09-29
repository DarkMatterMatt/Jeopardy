package se206.quinzical.models;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hildan.fxgson.FxGson;
import se206.quinzical.models.util.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuinzicalModel implements GsonPostProcessable {
	private static final String DEFAULT_SAVE_LOCATION = "./.save";
	private static final Gson GSON = FxGson.coreBuilder()
			.registerTypeAdapterFactory(new GsonPostProcessingEnabler())
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
	private final List<Category> _categories;
	private final PracticeModel _practiceModel;
	private final PresetQuinzicalModel _presetModel;
	private final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.MENU);
	private final TextToSpeech _textToSpeech = new TextToSpeech();
	private transient String _saveFileLocation = DEFAULT_SAVE_LOCATION;

	public QuinzicalModel() {
		//read files from directory
		this(FileBrowser.filesInDirectory("./categories"));
	}

	public QuinzicalModel(File[] categories) {
		_categories = new ArrayList<Category>();
		for (File category : categories) {
			Category newCategory = new Category(category.getName());

			//list of questions
			for (String rawQuestion : MyScanner.readFileOutputString(category)) {
				try {
					Question question = new Question(rawQuestion, newCategory);
					newCategory.addQuestion(question);
				}
				catch (IllegalArgumentException e) {
					//
				}
			}
			//make category out of that
			_categories.add(newCategory);
		}

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

	public List<Category> getCategories() {
		return Collections.unmodifiableList(_categories);
	}

	public PracticeModel getPracticeModel() {
		return _practiceModel;
	}

	public PresetQuinzicalModel getPresetModel() {
		return _presetModel;
	}

	public String getSaveFileLocation() {
		return _saveFileLocation;
	}

	private void setSaveFileLocation(String saveFileLocation) {
		_saveFileLocation = saveFileLocation;
	}

	public State getState() {
		return _state.get();
	}

	private void setState(State state) {
		_state.set(state);
		save();
	}

	public ObjectProperty<State> getStateProperty() {
		return _state;
	}

	public TextToSpeech getTextToSpeech() {
		return _textToSpeech;
	}

	@Override
	public void gsonPostProcess() {
		_presetModel.setQuinzicalModel(this);
		_practiceModel.setQuinzicalModel(this);
	}

	public void reset() {
		if (getState() != State.GAME) {
			throw new IllegalStateException("Can only reset when in the GAME state");
		}
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
	
	public void backToMainMenu() {
		_state.set(State.MENU);
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
