package hu.newtonsapple.andor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.victor.loading.newton.NewtonCradleLoading;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import hu.newtonsapple.andor.Classes.User;
import hu.newtonsapple.andor.Classes.UserAdapter;

public class ToplistActivity extends AppCompatActivity {

    DatabaseReference userReference;
    List<User> userList;
    ListView userListView;
    NewtonCradleLoading loading;
    TextView loadingTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toplist);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.scale_from_corner, R.anim.scale_to_corner);
        userReference = FirebaseDatabase.getInstance().getReference("users");
        loadingTV = (TextView) findViewById(R.id.loadingTV);

        userListView = (ListView) findViewById(R.id.userListView);
        ViewGroup header = (ViewGroup)(getLayoutInflater()).inflate(R.layout.users_header, userListView, false);
        userListView.addHeaderView(header, null, false);

        userList = new ArrayList<>();
        loading = (NewtonCradleLoading) findViewById(R.id.loader);
        loading.setVisibility(View.VISIBLE);
        loading.start();
    }

    @Override
    protected void onStart() {
        super.onStart();
        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                userList.clear();


                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User u = userSnapshot.getValue(User.class);
                    userList.add(u);
                }
                //RENDEZÉS
                Collections.sort(userList, new Comparator<User>() {
                    @Override
                    public int compare(User lhs, User rhs) {
                        return ((Integer)lhs.getPoint()).compareTo(rhs.getPoint());
                    }
                });
                Collections.reverse(userList);

                UserAdapter adapter = new UserAdapter(ToplistActivity.this,userList);
                loadingTV.setVisibility(View.GONE);
                loading.stop();
                loading.setVisibility(View.GONE);
                userListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent menu = new Intent(ToplistActivity.this,MainActivity.class);
        startActivity(menu);
        finish();
    }
}
