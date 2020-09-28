package se206.quinzical.models;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import org.hildan.fxgson.FxGson;
import se206.quinzical.models.util.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuinzicalModel implements GsonPostProcessable {
	private final List<Category> _categories;
	private final transient Gson _gson = FxGson.coreBuilder()
			.registerTypeAdapterFactory(new GsonPostProcessingEnabler())
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create();
	private final PracticeModel _practiceModel;
	private final PresetQuinzicalModel _presetModel;
	private final ObjectProperty<State> _state = new SimpleObjectProperty<>(State.MENU);
	private final TextToSpeech _textToSpeech = new TextToSpeech();
	private transient String _saveFileLocation = "./.save";

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
		try (Writer writer = new FileWriter(_saveFileLocation)) {
			_gson.toJson(this, writer);
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
	 * Main screen being shown
	 */
	public enum State {
		MENU,
		GAME,
		PRACTICE,
	}
}
