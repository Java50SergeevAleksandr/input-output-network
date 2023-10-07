package telran.io.performance;

import java.nio.file.Files;
import java.nio.file.Path;

import telran.io.CopyFile;
import telran.performance.PerformanceTest;

public class CopyFilesPerformanceTest extends PerformanceTest {
	private String sourceFile;
	private String destinationFile;
	private CopyFile copyFile;

	public CopyFilesPerformanceTest(String testName, int nRuns, String sourceFile, String destinationFile, CopyFile copyFile) {
		super(testName, nRuns);
		this.sourceFile = sourceFile;
		this.destinationFile = destinationFile;
		this.copyFile = copyFile;
	}

	@Override
	protected void runTest() {
		try {
			checkAttributes(sourceFile, destinationFile);
			copyFile.copyFiles(sourceFile, destinationFile);
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

	}

	private void checkAttributes(String source, String destination) throws Exception {
		if (source == null || destination == null) {
			throw new Exception("Too few arguments");
		}

		Path sourceFile = Path.of(source).toAbsolutePath().normalize();
		Path destinationFile = Path.of(destination).toAbsolutePath().normalize();
		var c = destinationFile.getNameCount();
		var p = destinationFile.getName(c - 2);

		if (!Files.isRegularFile(sourceFile)) {
			throw new Exception("source file " + source + " must exist");
		}

		if (Files.isRegularFile(p)) {
			throw new Exception("The directory " + p + " must exist");
		}

	}

}
