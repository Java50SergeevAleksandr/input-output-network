package telran.performance;

import java.nio.file.*;
import java.util.*;

import telran.io.*;
import telran.io.performance.CopyFilesPerformanceTest;

public class CopyFilesPerformanceAppl {
	static final String pathoToSource = "";
	static final String pathoToDestination = "";

	public static void main(String[] args) {
		Integer[] bufferLengthValues = { 4_000, 1_000_000, 1_000_000_000 };
		try {
			long size = Files.size(Path.of(pathoToSource));
			List<CopyFilesPerformanceTest> tests = new ArrayList<>(
					List.of(getPerformanceTest(size, "FilesCopy", new FilesCopy()),
							getPerformanceTest(size, "TransferToCopy", new TransferToCopy())));
			tests.addAll(Arrays.stream(bufferLengthValues).map(bl -> getPerformanceBufferSizeTest(bl, size)).toList());
			tests.forEach(t -> t.run());
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	private static CopyFilesPerformanceTest getPerformanceTest(long size, String testName, CopyFile copyFile) {
		CopyFilesPerformanceTest test = new CopyFilesPerformanceTest(String.format("%s ; size:%d", testName, size), 1,
				pathoToSource, pathoToDestination, copyFile);
		return test;
	}

	private static CopyFilesPerformanceTest getPerformanceBufferSizeTest(Integer bl, long size) {
		CopyFilesPerformanceTest test = new CopyFilesPerformanceTest(
				String.format("%s implementation buffer length %d; size:%d", "CopyFileStreams", bl, size), 1,
				pathoToSource, pathoToDestination, new ReadBufferCopy(bl));
		return test;
	}

}
