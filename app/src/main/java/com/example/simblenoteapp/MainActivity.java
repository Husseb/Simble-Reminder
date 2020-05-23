package com.example.simblenoteapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    public static String TAG = MainActivity.class.getSimpleName();
    private BottomSheetDialog bottomSheetDialog;

    private RecyclerView recyclerview;

    FirebaseFirestore db;
    EditText title, description;
    TextView dateTv;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        recyclerview = findViewById(R.id.recyclerview);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        createBoteSheeteDialog();
        progressDialog =new ProgressDialog(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            Log.e("tttt", "ffffffffff");
        } else {

            final Calendar cal33 = Calendar.getInstance(Locale.ENGLISH);
            cal33.setTimeInMillis(System.currentTimeMillis());

            Log.d("tttt", cal33.getTimeInMillis() + "");

            String year = DateFormat.format("yyyy", cal33).toString();
            String day = DateFormat.format("d", cal33).toString();
            String month = DateFormat.format("M", cal33).toString();

            final Calendar cal55 = Calendar.getInstance(Locale.ENGLISH);
            int i = Integer.parseInt(month);
            cal55.set(Integer.parseInt(year), i, Integer.parseInt(day), 0, 0);

            Query query = db.collection("note").orderBy("time")
                    .whereEqualTo("uid", FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .whereGreaterThan("time", cal55.getTimeInMillis());

            FirestoreRecyclerOptions<Note> options = null;

            options = new FirestoreRecyclerOptions.Builder<Note>()
                    .setQuery(query, Note.class).build();

            FirestoreRecyclerAdapter<Note, MyHolder> recyclerAdapter = new FirestoreRecyclerAdapter<Note, MyHolder>(options) {
                @NonNull
                @Override
                public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false));
                }

                @Override
                protected void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull final Note note) {
                    holder.name.setText(note.getTitle());
                    Log.d("tttt", "6: " + note.getTime());

//                holder.describtionNoteTv.setText(note.getDescription());
                    long abs = cal55.getTimeInMillis();

                    holder.describtionNoteTv.setText(note.getDescription());

                    Calendar cal1 = Calendar.getInstance(Locale.ENGLISH);
                    cal1.setTimeInMillis(System.currentTimeMillis());
                    String year1 = DateFormat.format("y", cal1).toString();
                    String day1 = DateFormat.format("d", cal1).toString();
                    String month1 = DateFormat.format("M", cal1).toString();

                    Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                    cal.setTimeInMillis(note.getTime());
                    String year = DateFormat.format("y", cal).toString();
                    String day = DateFormat.format("d", cal).toString();
                    String month = DateFormat.format("M", cal).toString();

                    cal.set(Integer.parseInt(year), (Integer.parseInt(month) - 2), Integer.parseInt(day));

                    String year3 = DateFormat.format("y", cal).toString();
                    String day3 = DateFormat.format("d", cal).toString();
                    String month3 = DateFormat.format("M", cal).toString();

                    long y = Long.parseLong(year) - Long.parseLong(year1);
                    long M = Long.parseLong(month) - Long.parseLong(month1) - 1;
                    long d = Long.parseLong(day) - Long.parseLong(day1);

                    holder.signLate.setBackgroundColor(0xffFB3A3A);
                    long reminedDay = 0, remiendMonth = 0, reminedYear = 0;
                    if (y > 0) {
                        holder.signLate.setBackgroundColor(0xff92E334);
                        reminedYear = y;
                    } else if (M > 0) {
                        holder.signLate.setBackgroundColor(0xff92E334);
                        remiendMonth = M;
                        if (M == 1) {
                            long ff = Long.parseLong(day) + (30 - Long.parseLong(day1));
                            if (ff >= 7) {
                                reminedDay = ff;
                                holder.signLate.setBackgroundColor(0xff92E334);
                            } else if (ff >= 3) {
                                reminedDay = ff;
                                holder.signLate.setBackgroundColor(0xffFFCD36);
                            } else if (ff == 0) {
                                holder.signLate.setBackgroundColor(0xffFB3A3A);
                                reminedDay = ff;
                            } else {
                                reminedDay = ff;

                                holder.signLate.setBackgroundColor(0xffFB3A3A);
                            }
                        }

                    } else if (d >= 0) {
                        if (d >= 7) {
                            reminedDay = d;
                            holder.signLate.setBackgroundColor(0xff92E334);
                        } else if (d >= 3) {
                            reminedDay = d;
                            holder.signLate.setBackgroundColor(0xffFFCD36);
                        } else if (d == 0) {
                            holder.signLate.setBackgroundColor(0xffFB3A3A);
                            reminedDay = d;
                        } else {
                            reminedDay = d;
                            holder.signLate.setBackgroundColor(0xffFB3A3A);
                        }
                    }
                    holder.time.setText(day3 + "/" + month3 + "/" + year3);

                    final long finalReminedYear = reminedYear;
                    final long finalRemiendMonth = remiendMonth;
                    final long finalReminedDay = reminedDay;
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (finalReminedYear > 0) {
                                Toast.makeText(MainActivity.this, "reminded " + finalReminedYear + " Year", Toast.LENGTH_SHORT).show();
                            } else if (finalReminedDay > 0) {
                                Toast.makeText(MainActivity.this, "reminded " + finalReminedDay + " Day", Toast.LENGTH_SHORT).show();

                            } else if (finalReminedDay == 0) {
                                Toast.makeText(MainActivity.this, "Thats Today", Toast.LENGTH_SHORT).show();

                            } else if (finalRemiendMonth > 0) {
                                Toast.makeText(MainActivity.this, "reminded " + finalRemiendMonth + " Month", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(MainActivity.this, "The Time Lasted ", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            showImagePicasoDialog(note.getId() + "");

                            return false;
                        }
                    });
                }
            };

            recyclerview.setAdapter(recyclerAdapter);
            recyclerAdapter.startListening();

        }
    }
    private void showImagePicasoDialog(final String primary) {
        String[] options = {"Delete"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    db.collection("note").document(primary).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(MainActivity.this, "Deleted!!", Toast.LENGTH_SHORT).show();
                            onStart();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, "Failed!!", Toast.LENGTH_SHORT).show();

                        }
                    });
                }
            }
        });

        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                // Action goes here
                bottomSheetDialog.show();
                return true;
            case R.id.logout:
                // Action goes here
                FirebaseFirestore.getInstance().collection("token")
                        .document(FirebaseAuth.getInstance().getCurrentUser().getUid()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            finish();
                        }
                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void createBoteSheeteDialog() {
        if (bottomSheetDialog == null) {
            View view = LayoutInflater.from(this).inflate(R.layout.bottom_add_project, null);
            bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(view);

            LinearLayout dateLinear = view.findViewById(R.id.dateLinear);
            title = view.findViewById(R.id.titleEt);
            description = view.findViewById(R.id.descriptionEt);
            dateTv = view.findViewById(R.id.dateTextView);

            Button createProject = view.findViewById(R.id.createProject);
            final int[] date = new int[3];

            final Calendar cal = Calendar.getInstance(Locale.ENGLISH);

            dateLinear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Calendar cldr = Calendar.getInstance();
                    int day = cldr.get(Calendar.DAY_OF_MONTH);
                    int month = cldr.get(Calendar.MONTH);
                    int year = cldr.get(Calendar.YEAR);
                    // date picker dialog
                    DatePickerDialog picker = new DatePickerDialog(MainActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                    dateTv.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);

                                    date[0] = year;
                                    date[1] = (monthOfYear + 1);
                                    date[2] = dayOfMonth;


                                }
                            }, year, month, day);
                    picker.setTitle("Select Date");
                    picker.getDatePicker().setMinDate(System.currentTimeMillis());
                    picker.show();
                }
            });

            createProject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String descriptionString = description.getText().toString();
                    final String titleString = title.getText().toString();
                    final String dateString = dateTv.getText().toString();

                    if (TextUtils.isEmpty(titleString) && titleString.length() >= 3) {
                        title.setFocusable(true);
//                        Toast.makeText(this, "The Title Must by more 3 Character", Toast.LENGTH_SHORT).show();
                    } else if (TextUtils.isEmpty(descriptionString)) {
                        description.setError("Description is Empty");
                        description.setFocusable(true);
                    } else if (TextUtils.isEmpty(dateString)) {
                        Toast.makeText(MainActivity.this, "Please Set Date", Toast.LENGTH_SHORT).show();
                    } else {

                        cal.set(date[0], date[1], date[2], 0, 2);
                        progressDialog.show();;
                        progressDialog.setTitle("Added");
                        progressDialog.setMessage("Loading...");

                        setProjectInFireStore(titleString, descriptionString, cal.getTimeInMillis());
                    }

                }
            });
        }
    }

    private void setProjectInFireStore(String titleString, String descriptionString, long time) {
// Create a new user with a first and last name
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String id = String.valueOf(System.currentTimeMillis());
        Note note = new Note(titleString, descriptionString, time, uid,id);
// Add a new document with a generated ID
        db.collection("note").document(id)
                .set(note)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        onStart();
                        progressDialog.hide();
                        bottomSheetDialog.hide();
                        title.setText("");
                        description.setText("");
                        dateTv.setText("");
                        Toast.makeText(MainActivity.this, "Added!!", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name, describtionNoteTv, time, signLate;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.nameNOTETv);
            describtionNoteTv = itemView.findViewById(R.id.describtionNoteTv);
            time = itemView.findViewById(R.id.showDateTV);
            signLate = itemView.findViewById(R.id.signLate);
        }
    }
}
