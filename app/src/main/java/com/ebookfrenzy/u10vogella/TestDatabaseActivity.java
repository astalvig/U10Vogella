package com.ebookfrenzy.u10vogella;

import java.util.List;

import android.app.ListActivity;
import android.media.Rating;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class TestDatabaseActivity extends ListActivity {
    private CommentsDataSource datasource;      // our link to the datasource for SQLite access
    private int listPosition = 0;                // currently selected item in the list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);

        datasource = new CommentsDataSource(this);
        datasource.open();

        List<Comment> values = datasource.getAllComments();

        // use the SimpleCursorAdapter to show the elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

        // need to grab the list item click so we remember what item is selected
        ListView lv = (ListView) findViewById(android.R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                listPosition = position;
            }
        });

    }

    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> myListAdapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;
        Rating rating = null;
        switch (view.getId()) {
            case R.id.add:
                // save the new comment to the database

                // Create variables for UI components
                EditText txComment = (EditText) findViewById(R.id.editTextComment);
                EditText txRating = (EditText) findViewById(R.id.editTextRating);

                comment = datasource.createComment(txComment.getText().toString(),
                        txRating.getText().toString());

                myListAdapter.add(comment);
                txComment.setText("");
                txRating.setText("");

                break;
            case R.id.delete:
                // The selected item position is stored in the variable listPosition by the onItemClick listener
                if (myListAdapter.getCount() > listPosition) {
                    comment = (Comment) getListAdapter().getItem(listPosition);
                    datasource.deleteComment(comment);
                    myListAdapter.remove(comment);
                }
                break;
        }
        myListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
