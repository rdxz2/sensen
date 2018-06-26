package chandra.sensen;

import android.os.AsyncTask;

import com.opencsv.CSVWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;

public class OpenCSVWriter extends AsyncTask{
    private static String direktori = "";

    public static void OpenCSVWriter (String nama_file, List<String>) throws IOException {
        direktori = "./" + nama_file + ".csv";
        try (
            Writer writer = new BufferedWriter(new FileWriter(direktori));
            CSVWriter csvWriter = new CSVWriter(writer, CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER, CSVWriter.DEFAULT_ESCAPE_CHARACTER, CSVWriter.DEFAULT_LINE_END);
        ) {
            String[] headerRecord = {"IDAbsen", "IDUmat", "Nama"};
            csvWriter.writeNext(headerRecord);
            for(int a=0; a<)
            csvWriter.writeNext(new String[]{"Sundar Pichai ♥", "sundar.pichai@gmail.com", "+1-1111111111", "India"});
            csvWriter.writeNext(new String[]{"Satya Nadella", "satya.nadella@outlook.com", "+1-1111111112", "India"});
        }
    }
}
