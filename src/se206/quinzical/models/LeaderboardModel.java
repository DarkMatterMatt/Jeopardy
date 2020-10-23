package se206.quinzical.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;

public class LeaderboardModel {
	private final ObservableList<Entry> sortedScores = FXCollections.observableArrayList();

	public void addScore(int score, String name) {
		sortedScores.add(new Entry(score, name));
		Collections.sort(sortedScores);
	}

	public ObservableList<Entry> getSortedScores() {
		return sortedScores;
	}

	public static class Entry implements Comparable<Entry> {
		public final long createdAt;
		public final String name;
		public final int score;

		public Entry(int score, String name) {
			this.score = score;
			this.name = name;
			this.createdAt = System.currentTimeMillis();
		}

		@Override
		public int compareTo(Entry o) {
			if (score != o.score) {
				// prioritise higher score
				return o.score - score;
			}
			if (createdAt != o.createdAt) {
				// prioritise earlier creation time
				return (int) (createdAt - o.createdAt);
			}
			// sort alphabetically
			return name.compareTo(o.name);
		}

		/**
		 * Returns a string representation of the object
		 */
		@Override
		public String toString() {
			return String.format("LeaderboardModel.Entry[score=%d, name=%s, createdAt=%d]", score, name, createdAt);
		}
	}
}
