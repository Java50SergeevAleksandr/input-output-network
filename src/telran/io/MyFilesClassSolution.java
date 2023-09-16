package telran.io;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class MyFilesClassSolution {
	private static final int SPACES_PER_LEVEL = 2;

	public static void displayDir(String path, int maxDepth) throws IOException {
		// TODO
		// Throwing IllegalArgumentException in the case the given is not a directory
		// Printing out the directory content using appropriate offsets showing which
		// node belong to which directory
		// printing type of node, for example
		// input-output-network - dir
		// src - dir
		// telran - dir
		// io - dir
		// test - dir
		// FileSystemTests.java - file
		Path normalizedPath = Path.of(path).toAbsolutePath().normalize();
		
		if (!Files.isDirectory(normalizedPath)) {
			throw new IllegalArgumentException();
		}
		
		int count = normalizedPath.getNameCount();
		
		
//		Files.walk(normalizedPath, maxDepth)
//		.forEach(p -> printNode(p, count, null));
		
		
		printNode(normalizedPath, count, null);
		Files.walkFileTree(normalizedPath, EnumSet.noneOf(FileVisitOption.class), maxDepth,
				new SimpleFileVisitor<Path>() {
					@Override
					public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
						printNode(file, count, null);
						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
						printNode(dir, count, exc);

						return FileVisitResult.CONTINUE;
					}

					@Override
					public FileVisitResult visitFileFailed(Path file, IOException exc) {
						printNode(file, count, exc);
						return FileVisitResult.CONTINUE;
					}
				});

	};

		static void printNode(Path path, int count, IOException exc) {
			System.out.printf("%s%s - %s %s\n", " ".repeat(SPACES_PER_LEVEL * (path.getNameCount() - count)),
					path.getFileName(), Files.isDirectory(path) ? "dir" : "file", exc != null ? exc : "");
		}
}
