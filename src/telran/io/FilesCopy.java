package telran.io;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class FilesCopy implements CopyFile {

	@Override
	public void copyFiles(String sourceFile, String destinationFile) throws Exception {
		Files.copy(Path.of(sourceFile), Path.of(sourceFile), StandardCopyOption.REPLACE_EXISTING);
	}

}
