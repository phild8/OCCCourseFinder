package edu.orangecoastcollege.cs273.occcoursefinder;

import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.id;

public class CourseSearchActivity extends AppCompatActivity {

    private DBHelper db;
    private static final String TAG = "OCC Course Finder";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_search);

        deleteDatabase(DBHelper.DATABASE_NAME);
        db = new DBHelper(this);
        db.importCoursesFromCSV("courses.csv");
        db.importInstructorsFromCSV("instructors.csv");
        db.importOfferingsFromCSV("offerings.csv");
    }

        //TODO: Create the method importOfferingsFromCSV, then use it in this activity.
        public boolean importOfferingsFromCSV(String csvFileName) {
            AssetManager manager = mContext.getAssets();
            InputStream inStream;
            try {
                inStream = manager.open(csvFileName);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            BufferedReader buffer = new BufferedReader(new InputStreamReader(inStream));
            String line;
            try {
                while ((line = buffer.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields.length != 4) {
                        Log.d("OCC Course Finder", "Skipping Bad CSV Row: " + Arrays.toString(fields));
                        continue;
                    }
                    int crn = Integer.parseInt(fields[0].trim());
                    int semesterCode = Integer.parseInt(fields[1].trim());
                    int courseId = Integer.parseInt(fields[2].trim());
                    int semestereId = Integer.parseInt(fields[3].trim());
                    addOffering(new Course(crn, semesterCode, courseId, semestereId));
                }
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }


        List<Course> allCourses = db.getAllCourses();
        for (Course course : allCourses)
            Log.i(TAG, course.toString());

        List<Instructor> allInstructors = db.getAllInstructors();
        for (Instructor instructor : allInstructors)
            Log.i(TAG, instructor.toString());

        //TODO: Get all the offerings from the database, then print them out to the Log


    }
}
