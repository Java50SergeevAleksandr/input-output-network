package telran.view;

import java.time.LocalDate;
import java.util.HashSet;

import java.util.function.Function;
import java.util.function.Predicate;

public interface InputOutput {
	String readString(String prompt);

	void writeString(String string);

	default void writeLine(String string) {
		writeString(string + "\n");
	}

	default void writeObject(Object object) {
		writeString(object.toString());
	}

	default void writeObjectLine(Object object) {
		writeLine(object.toString());
	}

	default <T> T readObject(String prompt, String errorPrompt, Function<String, T> mapper) {
		boolean running = false;
		T res = null;
		do {
			running = false;
			String string = readString(prompt);
			try {
				res = mapper.apply(string);

			} catch (RuntimeException e) {
				writeLine(errorPrompt + ": " + e.getMessage());
				running = true;
			}
		} while (running);
		return res;
	}

	default Integer readInt(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Integer::parseInt);
	}

	default Integer readInt(String prompt, String errorPrompt, int min, int max) {
		Integer res;
		boolean running;
		do {
			running = false;
			res = readInt(prompt, errorPrompt);
			if (res > max || res < min) {
				writeLine(errorPrompt + ": min value = " + min + ", max value = " + max);
				running = true;
			}
		} while (running);

		return res;
	}

	default Long readLong(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Long::parseLong);
	}

	default Long readLong(String prompt, String errorPrompt, long min, long max) {
		Long res;
		boolean running;
		do {
			running = false;
			res = readLong(prompt, errorPrompt);
			if (res > max || res < min) {
				writeLine(errorPrompt + ": min value = " + min + ", max value = " + max);
				running = true;
			}
		} while (running);

		return res;
	}

	default Double readDouble(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, Double::parseDouble);
	}

	default String readString(String prompt, String errorPrompt, Predicate<String> pattern) {
		String res;
		boolean running;
		do {
			running = false;
			res = readString(prompt);
			if (!pattern.test(res)) {
				writeLine(errorPrompt + ": String must must pass the condition");
				running = true;
			}
		} while (running);

		return res;
	}

	default String readString(String prompt, String errorPrompt, HashSet<String> options) {
		String res;
		boolean running;
		do {
			running = false;
			res = readString(prompt);
			if (!options.contains(res)) {
				writeLine(errorPrompt + ": String value must be one of:" + options);
				running = true;
			}
		} while (running);

		return res;
	}

	default LocalDate readIsoDate(String prompt, String errorPrompt) {
		return readObject(prompt, errorPrompt, LocalDate::parse);
	}

	default LocalDate readIsoDate(String prompt, String errorPrompt, LocalDate min, LocalDate max) {
		LocalDate res;
		boolean running;
		do {
			running = false;
			res = readIsoDate(prompt, errorPrompt);
			if (res.isAfter(max) || res.isBefore(min)) {
				writeLine(errorPrompt + ": min value = " + min + ", max value = " + max);
				running = true;
			}
		} while (running);

		return res;
	}

}