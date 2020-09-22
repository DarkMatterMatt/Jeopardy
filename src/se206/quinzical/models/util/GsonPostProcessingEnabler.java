package se206.quinzical.models.util;


import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

/**
 * Enables automatic post-deserialization processing of classes that implement GsonPostProcessable
 * Usage: GsonBuilder().registerTypeAdapterFactory(new GsonPostProcessingEnabler())
 *
 * @link https://medium.com/mobile-app-development-publication/post-processing-on-gson-deserialization-26ce5790137d
 */
public class GsonPostProcessingEnabler implements TypeAdapterFactory {
	public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
		TypeAdapter<T> delegate = gson.getDelegateAdapter(this, type);

		return new TypeAdapter<T>() {
			@Override
			public T read(JsonReader in) throws IOException {
				T obj = delegate.read(in);
				if (obj instanceof GsonPostProcessable) {
					((GsonPostProcessable) obj).gsonPostProcess();
				}
				return obj;
			}

			@Override
			public void write(JsonWriter out, T value) throws IOException {
				delegate.write(out, value);
			}
		};
	}
}
