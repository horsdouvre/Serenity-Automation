package com.serenity.testautomation.commons.fileutils;

import org.apache.commons.io.FileUtils;

import java.io.*;

public class FileHandler {

    private static FileFilter directoryFileFilter = file -> file.isDirectory();

    public static File[] getDirectories(String path) {
        File directory = new File(path);
        return directory.listFiles(directoryFileFilter);
    }

    public static File[] getFiles(String path) {
        File filePath = new File(path);
        return filePath.listFiles();
    }

    public static String readFile(File file) throws IOException {
        return FileUtils.readFileToString(file, "UTF-8");
    }

    public static String readFileFromPath(String filePath) throws IOException {
        return FileUtils.readFileToString(new File(filePath), "UTF-8");
    }

    public static void writeFileToPath(String filePath, String content) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdir();
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(content);
        }
    }

    public static File createFileThruPath(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    public static String getFileName(String filePath) {
        File file = new File(filePath);
        return file.getName();
    }
}
