package henrycrain.lunarsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.Arrays;
import java.util.Locale;

public class StudentActivity extends AppCompatActivity {

    private Student student;
    private Button nameBtn;
    private Button semesterBtn;
    private TableLayout courseTL;
    private boolean nameMode = true;
    //true if sorted by name, false if sorted by semester

    private DialogInterface.OnClickListener dismiss =
      new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
          }
      };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        student = (Student) getIntent().getSerializableExtra(
          Constants.KEY_STUDENT);
        ((TextView) findViewById(R.id.view_webid)).setText(student.getWebID());

        nameBtn = findViewById(R.id.button_name);
        semesterBtn = findViewById(R.id.button_semester);
        courseTL = findViewById(R.id.table_courses);

        nameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name();
            }
        });
        semesterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                semester();
            }
        });
        findViewById(R.id.button_add).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  add();
              }
          });
        findViewById(R.id.button_drop).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  drop();
              }
          });
        findViewById(R.id.button_logout).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  logout();
              }
          });

        name();
    }

    private void name() {
        nameMode = true;
        nameBtn.setBackgroundColor(ContextCompat.getColor(this,
          R.color.colorPrimary));
        semesterBtn.setBackgroundColor(ContextCompat.getColor(this,
          R.color.colorPrimaryDark));

        CourseNameComparator comparator = new CourseNameComparator();
        Object[] coursesObj = student.getCourses().toArray();
        Course[] courses = Arrays.copyOf(coursesObj, coursesObj.length,
          Course[].class);
        Arrays.sort(courses, comparator);
        sortTable(courses);
    }

    private void semester() {
        nameMode = false;
        nameBtn.setBackgroundColor(ContextCompat.getColor(this,
          R.color.colorPrimaryDark));
        semesterBtn.setBackgroundColor(ContextCompat.getColor(this,
          R.color.colorPrimary));

        SemesterComparator comparator = new SemesterComparator();
        Object[] coursesObj = student.getCourses().toArray();
        Course[] courses = Arrays.copyOf(coursesObj, coursesObj.length,
          Course[].class);
        Arrays.sort(courses, comparator);
        sortTable(courses);
    }

    private void sortTable(Course[] courses) {
        courseTL.removeAllViews();
        for (Course c: courses) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
              0, TableRow.LayoutParams.WRAP_CONTENT, 1);

            TextView departmentTV = new TextView(this);
            departmentTV.setLayoutParams(params);
            departmentTV.setText(c.getDepartment());
            departmentTV.setTextSize(24);
            departmentTV.setTextColor(ContextCompat.getColor(this,
              R.color.black));
            row.addView(departmentTV);

            TextView numberTV = new TextView(this);
            numberTV.setLayoutParams(params);
            numberTV.setText(String.format(Locale.US, "%d", c.getNumber()));
            numberTV.setTextSize(24);
            numberTV.setTextColor(ContextCompat.getColor(this, R.color.black));
            row.addView(numberTV);

            TextView semesterTV = new TextView(this);
            semesterTV.setLayoutParams(params);
            semesterTV.setText(c.getSemester());
            semesterTV.setTextSize(24);
            semesterTV.setTextColor(ContextCompat.getColor(this,R.color.black));
            row.addView(semesterTV);

            courseTL.addView(row);
        }
    }


    private void add() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add,null);
        final EditText departmentET = view.findViewById(R.id.input_department);
        final EditText numberET = view.findViewById(R.id.input_number);
        final RadioGroup semesterRG =view.findViewById(R.id.selection_semester);
        final EditText yearET = view.findViewById(R.id.input_year);

        new AlertDialog.Builder(this)
          .setTitle(R.string.title_add)
          .setView(view)
          .setPositiveButton(R.string.button_add,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String department = departmentET.getText().toString();
                    String numberStr = numberET.getText().toString();
                    int number;
                    try {
                        number = Integer.parseInt(numberStr);
                    } catch (NumberFormatException e) {
                        showInvalidNumberDialog();
                        return;
                    }

                    String semester;
                    int semesterID = semesterRG.getCheckedRadioButtonId();
                    switch (semesterID) {
                        case R.id.button_fall:
                            semester = "F";
                            break;
                        case R.id.button_spring:
                            semester = "S";
                            break;
                        default:
                            semester = "";
                            break;
                    }

                    String yearStr = yearET.getText().toString();
                    int year;
                    try {
                        year = Integer.parseInt(yearStr);
                    } catch (NumberFormatException e) {
                        showInvalidYearDialog();
                        return;
                    }

                    if (department.length() != 3) {
                        showInvalidDepartmentDialog();
                        dialog.dismiss();
                        return;
                    }
                    if (!(number >= 100 && number <= 999)) {
                        showInvalidNumberDialog();
                        dialog.dismiss();
                        return;
                    }
                    if (!(semester.equals("S") || semester.equals("F"))) {
                        showNoSemesterDialog();
                        dialog.dismiss();
                        return;
                    }
                    if (!(year >= 2010 && year <= 2025)) {
                        showInvalidYearDialog();
                        dialog.dismiss();
                        return;
                    }

                    addClass(department, number, semester, year);
                }
            })
          .setNeutralButton(R.string.button_cancel, dismiss)
          .show();
    }

    private void addClass(String department, int number, String semester,
      int year) {
        Course course = new Course(department, number, semester + year);
        student.addCourse(course);

        String fullSemester;
        if (semester.equals("F")) {
            fullSemester = "Fall";
        } else {
            fullSemester = "Spring";
        }
        String message = getString(R.string.message_add);

        new AlertDialog.Builder(this)
          .setMessage(String.format(message,
            department, number, fullSemester, year))
          .setPositiveButton(R.string.button_ok, dismiss)
          .show();

        if (nameMode) {
            name();
        } else {
            semester();
        }
    }

    private void showInvalidDepartmentDialog() {
        new AlertDialog.Builder(this)
          .setMessage(R.string.message_invalidDepartment)
          .setPositiveButton(R.string.button_ok, dismiss)
          .show();
    }

    private void showInvalidNumberDialog() {
        new AlertDialog.Builder(this)
          .setMessage(R.string.message_invalidNumber)
          .setPositiveButton(R.string.button_ok, dismiss)
          .show();
    }

    private void showNoSemesterDialog() {
        new AlertDialog.Builder(this)
          .setMessage(R.string.message_noSemester)
          .setPositiveButton(R.string.button_ok, dismiss)
          .show();
    }

    private void showInvalidYearDialog() {
        new AlertDialog.Builder(this)
          .setMessage(R.string.message_invalidYear)
          .setPositiveButton(R.string.button_ok, dismiss)
          .show();
    }

    private void drop() {
        View view=LayoutInflater.from(this).inflate(R.layout.dialog_drop, null);
        final EditText departmentET = view.findViewById(R.id.input_department);
        final EditText numberET = view.findViewById(R.id.input_number);

        new AlertDialog.Builder(this)
          .setTitle(R.string.title_drop)
          .setView(view)
          .setPositiveButton(R.string.button_drop,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String department = departmentET.getText().toString();
                    String numberStr = numberET.getText().toString();
                    int number = Integer.parseInt(numberStr);
                    dropClass(department, number);
                }
            })
          .setNeutralButton(R.string.button_cancel, dismiss)
          .show();
    }

    private void dropClass(String department, int number) {
        for (Course c: student.getCourses()) {
            if (c.getDepartment().equals(department)&&c.getNumber() == number) {
                student.removeCourse(c);

                String message = getString(R.string.message_drop);
                new AlertDialog.Builder(this)
                  .setMessage(String.format(message, department, number))
                  .setPositiveButton(R.string.button_ok, dismiss)
                  .show();

                if (nameMode) {
                    name();
                } else {
                    semester();
                }
                return;
            }
        }

        new AlertDialog.Builder(this)
          .setMessage(R.string.message_courseNotFound)
          .setPositiveButton(R.string.button_ok, dismiss)
          .show();
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constants.KEY_FROM_ACTIVITY, 0);
        intent.putExtra(Constants.KEY_STUDENT, student);
        startActivity(intent);
    }
}
