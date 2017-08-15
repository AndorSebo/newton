package hu.newtonsapple.andor.Classes;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import hu.newtonsapple.andor.R;

/**
 * Created by Andor on 2017.08.15..
 */

public class UserAdapter extends ArrayAdapter<User> {

    private Activity context;
    private List<User> userList;

    public UserAdapter(Activity context, List<User> userList) {
        super(context,R.layout.toplist_item,userList);
        this.context = context;
        this.userList = userList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.toplist_item, null, true);

        TextView nameTV = listViewItem.findViewById(R.id.nameTV);
        TextView scoreTV = listViewItem.findViewById(R.id.scoreTV);

        User user = userList.get(position);
        nameTV.setText(String.valueOf(user.getName()));
        scoreTV.setText(String.valueOf(user.getPoint()));

        return listViewItem;
    }



}
