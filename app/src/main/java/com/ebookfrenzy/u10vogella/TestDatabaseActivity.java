package com.ebookfrenzy.u10vogella;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

public class TestDatabaseActivity extends ListActivity {
    private CommentsDataSource datasource;
    private int listPosition = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_database);

        datasource = new CommentsDataSource(this);
        datasource.open();

        List<Comment> values = datasource.getAllComments();

        // use the SimpleCursorAdapter to show the
        // elements in a ListView
        ArrayAdapter<Comment> adapter = new ArrayAdapter<Comment>(this,
                android.R.layout.simple_list_item_1, values);
        setListAdapter(adapter);

    // need to grab the list item click so we remember what item is selected
    ListView lv = (ListView) findViewById(android.R.id.list);
    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                long arg3) {
            listPosition = position;
            //Toast.makeText(SuggestionActivity.this, "" + position, Toast.LENGTH_SHORT).show();
        }
    });

}


    // Will be called via the onClick attribute
    // of the buttons in main.xml
    public void onClick(View view) {
        @SuppressWarnings("unchecked")
        ArrayAdapter<Comment> adapter = (ArrayAdapter<Comment>) getListAdapter();
        Comment comment = null;
        switch (view.getId()) {
            case R.id.add:
                //String[] comments = new String[] { "Cool", "Very nice", "Hate it" };
                //int nextInt = new Random().nextInt(3);
                // save the new comment to the database
                EditText textComment = (EditText) findViewById(R.id.editTextComment);
                EditText textRating = (EditText) findViewById(R.id.editTextRating);
                comment = datasource.createComment(textComment.getText().toString(), textRating.getText().toString());
                adapter.add(comment);
                break;
            case R.id.delete:
//                if (getListAdapter().getCount() > 0) {
//                    comment = (Comment) getListAdapter().getItem(0);
//                    datasource.deleteComment(comment);
//                    adapter.remove(comment);
//                }
                // The selected item position is stored in the variable listPosition by the onItemClick listener
                if (adapter.getCount() > listPosition) {
                    comment = (Comment) getListAdapter().getItem(listPosition);
                    datasource.deleteComment(comment);
                    adapter.remove(comment);
                break;
        }
        adapter.notifyDataSetChanged();
    }}

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