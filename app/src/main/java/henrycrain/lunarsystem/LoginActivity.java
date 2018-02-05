package henrycrain.lunarsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private HashMap<String, Student> database;

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
        setContentView(R.layout.activity_login);

        try {
            File file = new File(getFilesDir(), Constants.FILENAME);
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream input = new ObjectInputStream(fis);
            database = (HashMap<String, Student>) input.readObject();

            new AlertDialog.Builder(this)
              .setMessage(R.string.message_dataLoaded)
              .setPositiveButton(R.string.button_ok, dismiss)
              .show();
        } catch (IOException e) {
            new AlertDialog.Builder(this)
              .setMessage(R.string.message_dataNotFound)
              .setPositiveButton(R.string.button_ok, dismiss)
              .show();

            database = new HashMap<>();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        findViewById(R.id.button_login).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  login();
              }
          });
        findViewById(R.id.button_saveQuit).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  saveQuit();
              }
          });
        findViewById(R.id.button_quitNoSave).setOnClickListener(
          new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  quitNoSave();
              }
          });
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onNewIntent(Intent intent) {
        int fromActivity = intent.getIntExtra(Constants.KEY_FROM_ACTIVITY, -1);
        if (fromActivity == Constants.CODE_STUDENT) {
            Student student = (Student) intent.getSerializableExtra(
              Constants.KEY_STUDENT);
            database.put(student.getWebID().toLowerCase(), student);
        } else if (fromActivity == Constants.CODE_REGISTRAR) {
            database =
              (HashMap<String, Student>)intent.getSerializableExtra(
              Constants.KEY_DATABASE);
        }
    }

    private void login() {
        EditText webIdEt = findViewById(R.id.input_webid);
        String webID = webIdEt.getText().toString();

        if (webID.equals(Constants.ID_REGISTRAR)) {
            Intent intent = new Intent(this, RegistrarActivity.class);
            intent.putExtra(Constants.KEY_DATABASE, database);
            startActivity(intent);
        } else {
            Student student = database.get(webID.toLowerCase());

            if (student == null) {
                new AlertDialog.Builder(this)
                  .setMessage(R.string.message_studentNotFound)
                  .setPositiveButton(R.string.button_ok, dismiss)
                  .show();
                return;
            }

            Intent intent = new Intent(this, StudentActivity.class);
            intent.putExtra(Constants.KEY_STUDENT, student);
            startActivity(intent);
        }
    }

    private void saveQuit() {
        try {
            File file = new File(getFilesDir(), Constants.FILENAME);
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream output = new ObjectOutputStream(fos);
            output.writeObject(database);
            output.close();
            finish();
        } catch (IOException e) {
            new AlertDialog.Builder(this)
              .setMessage(R.string.message_saveFailed)
              .setPositiveButton(R.string.button_ok, dismiss)
              .show();
        }
    }

    private void quitNoSave() {
        new AlertDialog.Builder(this)
          .setMessage(R.string.message_quitNoSave)
          .setPositiveButton(R.string.button_continue,
            new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            })
          .setNeutralButton(R.string.button_cancel, dismiss)
          .show();
    }
}
