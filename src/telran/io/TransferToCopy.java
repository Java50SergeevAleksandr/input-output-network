package telran.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TransferToCopy implements CopyFile {

	@Override
	public void copyFiles(String sourceFile, String destinationFile) throws Exception {
		try (InputStream input = new FileInputStream(sourceFile);
				OutputStream output = new FileOutputStream(destinationFile)) {
			input.transferTo(output);
		}

	}

}
