package telran.io;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import static java.nio.file.FileVisitResult.*;

public class MyFiles implements FileVisitor<Object> {
	public static void displayDir(String path, int maxDepth) throws IOException {
		// TODO
		// Throwing IllegalArgumentException in the case the given is not a directory
		// Printing out the directory content using appropriate offsets showing which
		// node belong to which directory
		// printing type of node, for example
		// input-output-network - dir
		// 						src - dir
		// 							telran - dir
		// 								io - dir
		// 									test - dir
		// 										FileSystemTests.java - file
		int offset = Path.of(path).toAbsolutePath().normalize().getNameCount();
		Files.walk(Path.of(path).toAbsolutePath().normalize(), maxDepth).forEach(p -> {
			int depth = p.getNameCount();
			System.out.println("   ".repeat(depth - offset) + p.getFileName() + " - " + getType(p));
		});

		System.out.println("============================================");
	}

	private static String getType(Path p) {
		return p.getFileName().toString().contains(".") ? "file" : "dir";

	}

	@Override
	public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
		Path p = (Path) file;
		int depth = p.getNameCount();

		if (attrs.isRegularFile()) {
			System.out.println("   ".repeat(depth) + p.getFileName() + " - File");
		}

		return CONTINUE;
	}

	@Override
	public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
		System.err.println(exc);
		return CONTINUE;
	}

	@Override
	public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
		Path p = (Path) dir;
		int depth = p.getNameCount();
		System.out.println("   ".repeat(depth) + p.getFileName() + " - Dir");
		return CONTINUE;
	}
}
