package se206.quinzical.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardModel {
	private final List<Entry> sortedScores = new ArrayList<>();

	public void addScore(int score, String name) {
		sortedScores.add(new Entry(score, name));
		Collections.sort(sortedScores);
	}

	public List<Entry> getSortedScores() {
		return Collections.unmodifiableList(sortedScores);
	}

	private static class Entry implements Comparable<Entry> {
		private final long createdAt;
		private final String name;
		private final int score;

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
