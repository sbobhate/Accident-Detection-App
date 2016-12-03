package shantanubobhate.datatocsv;

import android.graphics.DashPathEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.androidplot.util.PixelUtils;
import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.opencsv.CSVReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlotActivity extends AppCompatActivity {

    private XYPlot plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plot);

        File root = android.os.Environment.getExternalStorageDirectory();
        File dir = new File(root.getAbsolutePath() + "/DataToCSV");              // Get the path to the new directory to create

        String fileName = "";
        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            fileName= "data.csv";
        } else {
            fileName = extras.getString("FILENAME");
        }
        File f = new File(dir, fileName);

        CSVReader reader = null;

        List myEntries = null;
        try {
            reader = new CSVReader(new FileReader(f), ',');
            String[] nextLine = new String[3];
            myEntries = reader.readAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<Double> AccelerationX = new ArrayList<Double>();
        ArrayList<Double> AccelerationY = new ArrayList<Double>();
        ArrayList<Double> AccelerationZ = new ArrayList<Double>();
        
        for (int ii = 0; ii < myEntries.get(0))

        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries accelerationX = new SimpleXYSeries(AccelerationX, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Acceleration X");
        XYSeries accelerationY = new SimpleXYSeries(AccelerationY, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Acceleration Y");
        XYSeries accelerationZ = new SimpleXYSeries(AccelerationZ, SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Acceleration Z");

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.setPointLabelFormatter(new PointLabelFormatter());
        series1Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series2Format = new LineAndPointFormatter();
        series2Format.setPointLabelFormatter(new PointLabelFormatter());
        series2Format.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels_2);

        LineAndPointFormatter series3Format = new LineAndPointFormatter();
        series3Format.setPointLabelFormatter(new PointLabelFormatter());
        series3Format.configure(getApplication(),
                R.xml.line_point_formatter_with_labels_2);

        // add an "dash" effect to the series2 line:
        series2Format.getLinePaint().setPathEffect(
                new DashPathEffect(new float[] {

                        // always use DP when specifying pixel sizes, to keep things consistent across devices:
                        PixelUtils.dpToPix(20),
                        PixelUtils.dpToPix(15)}, 0));

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        series1Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series2Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        series3Format.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // add a new series' to the xyplot:
        plot.addSeries(accelerationX, series1Format);
        plot.addSeries(accelerationY, series2Format);
        plot.addSeries(accelerationZ, series3Format);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);

        // rotate domain labels 45 degrees to make them more compact horizontally:
        plot.getGraphWidget().setDomainLabelOrientation(-45);
    }
}

