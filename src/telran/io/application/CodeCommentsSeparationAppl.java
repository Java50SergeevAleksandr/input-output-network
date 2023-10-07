package telran.io.application;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeCommentsSeparationAppl {

	public static void main(String[] args) {
		if (args.length < 3) {
			System.out.println("Too few arguments, usage: <file with code and comments> <file code> <file comments");
		} else {
			try {
				stringS(args);
				regexS(args);
				classSolution(args);

			} catch (RuntimeException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

	}

	private static void regexS(String[] args) throws FileNotFoundException, IOException {
		long start = System.currentTimeMillis();
		try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
				PrintWriter commentWriter = new PrintWriter(args[1]);
				PrintWriter textWriter = new PrintWriter(args[2]);) {

			String line;
			Pattern commentPatern = Pattern.compile("^\\s*//(.+)"); // count of groups and it's complexity increase
																	// execution time

			while ((line = reader.readLine()) != null) {
				Matcher m = commentPatern.matcher(line);

				if (!m.find()) {
					textWriter.println(line);
				} else {
					commentWriter.println(m.group(1));
				}

			}
		}

		System.out.printf("RegexSeparator test; Running time: %d Ms\n", System.currentTimeMillis() - start);

	}

	private static void stringS(String[] args) throws IOException, FileNotFoundException {
		long start = System.currentTimeMillis();
		try (BufferedReader reader = new BufferedReader(new FileReader(args[0]));
				PrintWriter commentWriter = new PrintWriter(args[1]);
				PrintWriter textWriter = new PrintWriter(args[2]);) {

			String line;

			while ((line = reader.readLine()) != null) {
				line = line.trim();

				if (!line.startsWith("//")) {
					textWriter.println(line);
				} else {
					commentWriter.println(line.substring(2));
				}

			}
		}

		System.out.printf("StringSeparator test; Running time: %d Ms\n", System.currentTimeMillis() - start);
	}

	private static void classSolution(String[] args) {
		long start = System.currentTimeMillis();

		try (BufferedReader sourceFile = new BufferedReader(new FileReader(args[0]));
				PrintWriter codeFile = new PrintWriter(args[1]);
				PrintWriter commentsFile = new PrintWriter(args[2])) {
			if (!Files.exists(Path.of(args[0]))) {
				throw new Exception(args[0] + " doesn't exist");
			}
			sourceFile.lines().forEach(l -> {
				if (l.trim().startsWith("//")) {
					commentsFile.println(l.replace("//", ""));
				} else {
					codeFile.println(l);
				}
			});

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.printf("StringSeparatorClassSolution test; Running time: %d Ms\n", System.currentTimeMillis() - start);
	}
}
