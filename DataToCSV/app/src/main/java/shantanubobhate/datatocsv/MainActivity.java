package shantanubobhate.datatocsv;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {


    /*
     *
     * This project uses opencsv
     * The binary files can be found here: https://sourceforge.net/projects/opencsv/files/latest/download
     *
     * Instructions to compile binary:
     *  - Download the library.jar file and copy it to your /libs/ folder inside your application project.
     *  - Press "Sync Project with Gradle Files" button. (Between AVD Manager and Project Structure icon in the tool bar.
     *
     * NOTE: When you install the app, make sure you go to Applications and allow the permissions.
     *
     */

    private FileWriter mFileWriter;
    private TextView textViewPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPath = (TextView) findViewById(R.id.textViewPath);

        // Get the root directory
        // This is the Device Storage
        File root = android.os.Environment.getExternalStorageDirectory();
        // Get the path to the new directory to create
        File dir = new File(root.getAbsolutePath() + "/DataToCSV");
        // Check if this directory exists
        if (!dir.exists()) {
            dir.mkdirs();   // Create a new directory called DataToCSV
        }
        String fileName = "AnalysisData.csv";   // Declaring the file name
        // Create new File object
        File f = new File(dir, fileName);

        String filePath = f.getAbsolutePath();  // Get the absolute path to the file
        textViewPath.setText(filePath);
        // Create the CSVWrite object which is from the opencsv library
        CSVWriter writer = null;
        if(f.exists() && !f.isDirectory()){
            try {
                mFileWriter = new FileWriter(filePath , true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            writer = new CSVWriter(mFileWriter);
        }
        else {
            try {
                writer = new CSVWriter(new FileWriter(filePath));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // THIS IS WHERE THE DATA IS WRITTEN //
        String[] data = {"Acceleration", "Latitude", "Longitude", "..."};

        writer.writeNext(data);

        String[] value = {"4", "10", "101", "..."};
        writer.writeNext(value);
        ///////////////////////////////////////
        // Find

        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
