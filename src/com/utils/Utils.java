package com.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.logic.gates.Buffer;

public class Utils {

	@SuppressWarnings("unchecked")
	public static <T> T[] copyArray(T[] list, T[] newList, T... adds) {
		System.arraycopy(list, 0, newList, 0, list.length);
		for (int i = 0; i < adds.length; i++) {
			newList[list.length + i] = adds[i];
		}
		return newList;
	}

	public static <T> T[] removeObjectFromArray(T[] list, T[] newList, int index) {
		System.arraycopy(list, 0, newList, 0, index);
		System.arraycopy(list, index + 1, newList, index, newList.length
				- index);
		return newList;
	}

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

	private static int lineOfCode = 0;
	private static int numberOfFiles = 0;

	public static void main(String[] args) throws IOException {
		// Find the number of files and link of code
		String home = System.getProperty("user.dir") + "/src";
		countLines(new File(home));
		System.out.println(String.format(
				"Number of line of code is %d and the number of files is %d", lineOfCode,
				numberOfFiles));
	}

	public static void countLines(File f) throws IOException {
		if (f.isFile()) {
			numberOfFiles++;
			lineOfCode += getNumberOfLine(f);
			return;
		} else {
			for (File d : f.listFiles()) {
				countLines(d);
			}
		}
	}

	public static int getNumberOfLine(File f) throws IOException {
		int line = 0;
		BufferedReader in = new BufferedReader(new FileReader(f));
		if (in == null)
			return 0;
		while (in.readLine() != null)
			line++;
		return line;

	}

}
