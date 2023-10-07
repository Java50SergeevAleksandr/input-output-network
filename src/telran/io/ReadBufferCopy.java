package telran.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ReadBufferCopy implements CopyFile {
	private int bufferSize;

	public ReadBufferCopy(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	@Override
	public void copyFiles(String sourceFile, String destinationFile) throws Exception {
		float partLenght = 0;
		byte[] buffer = new byte[bufferSize];

		try (OutputStream output = new FileOutputStream(destinationFile);
				InputStream input = new FileInputStream(sourceFile)) {
			while ((partLenght = input.read(buffer)) > 0) {
				output.write(buffer, 0, (int) partLenght);
			}
		}
	}

}
