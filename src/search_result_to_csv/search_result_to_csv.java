package search_result_to_csv;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class search_result_to_csv {

    public static void main(String[] args) {
        ArrayList<File> fileList = new ArrayList<>();
        searchFiles(new File("F:\\ИПР\\TE\\JAVA\\results\\mail.log.18.11.2014\\sep-files\\word_search"), fileList);
        for (File file : fileList) {
            to_csv(file, ",", "result");
        }
    }

    private static void searchFiles(File rootFile, List<File> fileList) {
        if (rootFile.isDirectory()) {
            File[] directoryFiles = rootFile.listFiles();
            if (directoryFiles != null) {
                for (File file : directoryFiles) {
                    if (file.isDirectory()) {
                        searchFiles(file, fileList);
                    } else {
                        fileList.add(file);
                    }
                }
            }
        }
    }

    public static void to_csv(File f, String lineSep, String file_name) {
        try (BufferedReader buff = new BufferedReader(new InputStreamReader(
                new FileInputStream(f), "Cp1251"))) {
            StringBuilder line = new StringBuilder();
            String buffLine;
            String sep = System.getProperty("line.separator");
            while ((buffLine = buff.readLine()) != null) {
                line.append(buffLine);
                replaceAll(line, "\t", lineSep);
                replaceAll(line, "; ", lineSep);
                replaceAll(line, ", ", lineSep);
                replaceAll(line, ": ", lineSep);
                int commaCounter = 0;
                for (int i = 0; i < line.length(); i++) {
                    if (line.charAt(i) == ' ') {
                        line.setCharAt(i, ',');
                        commaCounter++;
                        if (commaCounter == 2) {
                            break;
                        }
                    }
                }
                FileWriter writer = new FileWriter(f.getParent() + "\\" + file_name + ".csv", true);
                writer.write(line + sep);
                writer.flush();
                writer.close();
                line.setLength(0);
                }
            } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public static void replaceAll(StringBuilder sb, String find, String replace) {

        //compile pattern from find string
        Pattern p = Pattern.compile(find);

        //create new Matcher from StringBuilder object
        Matcher matcher = p.matcher(sb);

        //index of StringBuilder from where search should begin
        int startIndex = 0;

        while (matcher.find(startIndex)) {
            sb.replace(matcher.start(), matcher.end(), replace);

            //set next start index as start of the last match + length of replacement
            startIndex = matcher.start() + replace.length();
        }
    }
}