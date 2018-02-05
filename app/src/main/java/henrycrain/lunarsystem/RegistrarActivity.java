package henrycrain.lunarsystem;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class RegistrarActivity extends AppCompatActivity {

    private HashMap<String, Student> database;
    private TableLayout studentTL;

    private DialogInterface.OnClickListener dismiss =
      new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
          }
      };

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);

        database =
          (HashMap<String,Student>)getIntent().getSerializableExtra(
          Constants.KEY_DATABASE);
        studentTL = findViewById(R.id.table_students);

        findViewById(R.id.button_register).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  register();
              }
          });

        findViewById(R.id.button_deregister).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  deregister();
              }
          });

        ((EditText) findViewById(R.id.input_course)).addTextChangedListener(
          new TextWatcher() {
              @Override
              public void beforeTextChanged(CharSequence s, int start,
                int count, int after) {}

              @Override
              public void onTextChanged(CharSequence s, int start, int before,
                int count) {}

              @Override
              public void afterTextChanged(Editable s) {
                  enrolled(s);
              }
          }
        );

        findViewById(R.id.button_logout).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  logout();
              }
          });
    }

    @SuppressLint("InflateParams")
    private void register() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_register,
          null);
        final EditText webIdEt = view.findViewById(R.id.input_webid);

        new AlertDialog.Builder(this)
          .setTitle(R.string.title_register)
          .setView(view)
          .setPositiveButton(R.string.button_register,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String webID = webIdEt.getText().toString();
                    registerStudent(webID);
                    dialog.dismiss();
                }
            })
          .setNeutralButton(R.string.button_cancel, dismiss)
          .show();
    }

    private void registerStudent(String webID) {
        if (database.containsKey(webID.toLowerCase())) {
            String message = getString(R.string.message_studentAlreadyExists);
            new AlertDialog.Builder(this)
              .setMessage(String.format(Locale.US, message, webID))
              .setPositiveButton(R.string.button_ok, dismiss)
              .show();
            return;
        }

        database.put(webID.toLowerCase(), new Student(webID));
        String message = getString(R.string.message_register);
        new AlertDialog.Builder(this)
          .setMessage(String.format(Locale.US, message, webID))
          .setPositiveButton(R.string.button_ok, dismiss)
          .show();
    }

    @SuppressLint("InflateParams")
    private void deregister() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_register,
          null);
        final EditText webIdEt = view.findViewById(R.id.input_webid);

        new AlertDialog.Builder(this)
          .setTitle(R.string.title_deregister)
          .setView(view)
          .setPositiveButton(R.string.button_deregister,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String webID = webIdEt.getText().toString();
                    deregisterStudent(webID);
                    dialog.dismiss();
                }
            })
          .setNeutralButton(R.string.button_cancel, dismiss)
          .show();
    }

    private void deregisterStudent(String webID) {
        database.remove(webID.toLowerCase());

        String message = getString(R.string.message_deregister);
        new AlertDialog.Builder(this)
          .setMessage(String.format(Locale.US, message, webID))
          .setPositiveButton(R.string.button_ok, dismiss)
          .show();
    }

    private void enrolled(Editable s) {
        studentTL.removeAllViews();

        String text = s.toString();
        if (text.length() != 7 || text.charAt(3) != ' ') {
            return;
        }

        String department = text.substring(0, 3).toUpperCase();
        String numberStr = text.substring(4, 7);
        for (int i = 0; i <= 2; i++) {
            if (!Character.isAlphabetic(department.charAt(i)) ||
              !Character.isDigit(numberStr.charAt(i))) {
                return;
            }
        }
        int number = Integer.parseInt(numberStr);

        ArrayList<Student> students = new ArrayList<>();
        ArrayList<String> semesters = new ArrayList<>();
        for (Student student: database.values()) {
            for (Course c: student.getCourses()) {
                if (c.getDepartment().equals(department) &&
                  c.getNumber() == number) {
                    students.add(student);
                    semesters.add(c.getSemester());
                    break;
                }
            }
        }

        sortTable(students, semesters);
    }

    private void sortTable(ArrayList<Student> students,
      ArrayList<String> semesters) {
        for (int i = 0; i < students.size(); i++) {
            TableRow row = new TableRow(this);
            TableRow.LayoutParams params = new TableRow.LayoutParams(
              0, TableRow.LayoutParams.WRAP_CONTENT, 1);

            TextView studentTV = new TextView(this);
            studentTV.setLayoutParams(params);
            studentTV.setText(students.get(i).getWebID());
            studentTV.setTextSize(24);
            studentTV.setTextColor(ContextCompat.getColor(this,
              R.color.black));
            row.addView(studentTV);

            TextView semesterTV = new TextView(this);
            semesterTV.setLayoutParams(params);
            semesterTV.setText(semesters.get(i));
            semesterTV.setTextSize(24);
            semesterTV.setTextColor(ContextCompat.getColor(this,
              R.color.black));
            row.addView(semesterTV);

            studentTL.addView(row);
        }
    }

    private void logout() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
          Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(Constants.KEY_FROM_ACTIVITY, 1);
        intent.putExtra(Constants.KEY_DATABASE, database);
        startActivity(intent);
    }
}
