package se206.quinzical.models.util;

/**
 * Implement to enable automatic post-deserialization processing
 *
 * @see GsonPostProcessingEnabler must registered as a gson type adapter factory
 */
public interface GsonPostProcessable {
	void gsonPostProcess();
}
