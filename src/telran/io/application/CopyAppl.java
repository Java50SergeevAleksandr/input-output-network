package telran.io.application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class CopyAppl {

	private static final int MB = 1_000_000;

	public static void main(String[] args) {
		// args[0] - source file
		// args[1] - destination file
		// args[2] - "overwrite"
		// TODO write application for copying from source file to destination file
		// Implementation Requirement: to use while cycle with read call
		// main must not contain throws declaration
		byte[] buffer = new byte[MB];

		try {
			copyAppl(args, buffer);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static void copyAppl(String[] args, byte[] buffer) throws Exception {
		checkAttributes(args);
		Result res = copyWithBuffer(args, buffer);
		printResult(res.bytesLenght(), args[0], args[1], res.time());
	}

	private static void printResult(float bytesLenght, String sourceFile, String destinationFile, float time) {
		System.out.printf(
				"Successful copying of %.0f bytes have been copying \nfrom the file %s \nto the file %s. \nTime %.0f ms\n",
				bytesLenght, sourceFile, destinationFile, time);

	}

	private static Result copyWithBuffer(String[] args, byte[] buffer) throws IOException, FileNotFoundException {
		long start = System.currentTimeMillis();
		float bytesLenght = 0;
		float partLenght = 0;		

		try (OutputStream output = new FileOutputStream(args[1])) {

			try (InputStream input = new FileInputStream(args[0])) {

				while ((partLenght = input.read(buffer)) > 0) {
					bytesLenght += partLenght;
					output.write(buffer, 0, (int) partLenght);
				}
			}
		}

		return new Result(bytesLenght, System.currentTimeMillis() - start);
	}

	private static void checkAttributes(String[] args) throws Exception {
		if (args.length < 2) {
			throw new Exception("Too few arguments");
		}

		Path sourceFile = Path.of(args[0]).toAbsolutePath().normalize();
		Path destinationFile = Path.of(args[1]).toAbsolutePath().normalize();
		var c = destinationFile.getNameCount();
		var p = destinationFile.getName(c - 2);

		if (!Files.isRegularFile(sourceFile)) {
			throw new Exception("source file " + args[0] + " must exist");
		}

		if (Files.isRegularFile(p)) {
			throw new Exception("The directory " + p + " must exist");
		}

		if (Files.isRegularFile(destinationFile) && args.length < 3 || !args[2].equalsIgnoreCase("overwrite")) {
			throw new Exception("File " + destinationFile.getFileName() + " cannot be overwritten");
		}
	}

}
