package lab;

import java.io.File;

public class TreePrinter {
    public static void main(String[] args) {
        File currentDirectory = new File(".");
        printDirectoryTree(currentDirectory, "");
    }

    private static void printDirectoryTree(File directory, String indent) {
        System.out.println(indent + directory.getName());

        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        printDirectoryTree(file, indent + "\t");
                    } else {
                        System.out.println(indent + "\t" + file.getName());
                    }
                }
            }
        }
    }
}