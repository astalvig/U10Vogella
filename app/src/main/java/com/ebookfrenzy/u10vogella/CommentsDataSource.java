package com.ebookfrenzy.u10vogella;

/**
 * Created by ashley.stalvig on 4/26/2016.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class CommentsDataSource {

    // Database fields
    private SQLiteDatabase database;
    private MySQLiteHelper dbHelper;

    public CommentsDataSource(Context context) {
        dbHelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // create a comment from a string. Adds the comment to the database and converts it to a comment object
    public Comment createComment(String comment, String rating) {
        // use values to store a new record for the database
        ContentValues values = new ContentValues();
        // store the new comment in the comment field of the new record
        values.put(MySQLiteHelper.COLUMN_COMMENT, comment);
        values.put(MySQLiteHelper.COLUMN_RATING, rating);
        // insert new record into the table
        long insertId = database.insert(MySQLiteHelper.TABLE_COMMENTS, null, values);
        // reads the last record back from the database
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                null, MySQLiteHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        // convert the database record into a comment object
        Comment newComment = cursorToComment(cursor);
        cursor.close();
        return newComment;
    }

    // delete this comment from the database using the id field
    public void deleteComment(Comment comment) {
        long id = comment.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(MySQLiteHelper.TABLE_COMMENTS, MySQLiteHelper.COLUMN_ID + " = " + id, null);
    }

    // get all the comments from the database and convert them into a List
    public List<Comment> getAllComments() {
        List<Comment> comments = new ArrayList<Comment>();
        // grab all the comments from the database table
        String orderBy = MySQLiteHelper.COLUMN_RATING + " ASC";
        Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS,
                null, null, null, null, null, orderBy);

        // loop through all the record in the cursor
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            // create a comment from the current record and add it to the List
            Comment comment = cursorToComment(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return comments;
    }

    // converts database records stored in a cursor to a comment object
    private Comment cursorToComment(Cursor cursor) {
        Comment comment = new Comment();
        comment.setId( cursor.getLong( cursor.getColumnIndex( MySQLiteHelper.COLUMN_ID )) );
        comment.setComment(cursor.getString( cursor.getColumnIndex( MySQLiteHelper.COLUMN_COMMENT ) ));
        comment.setRating(cursor.getString( cursor.getColumnIndex( MySQLiteHelper.COLUMN_RATING ) ));

        return comment;
    }
}