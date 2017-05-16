package pe.cibertec.contentprovider.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

/**
 * Created by Android on 29/04/2017.
 */

public class ContactProvider extends ContentProvider {

    private static final int CONTACTS = 1;
    private static final int CONTACT_ID = 2;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(ContactContract.CONTENT_AUTHORITY,
                ContactContract.PATH_CONTACT, CONTACTS);
        uriMatcher.addURI(ContactContract.CONTENT_AUTHORITY,
                ContactContract.PATH_CONTACT + "/#", CONTACT_ID);
    }

    private DatabaseHelper databaseHelper;

    @Override
    public boolean onCreate() {
        databaseHelper = new DatabaseHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        final SQLiteDatabase db = databaseHelper.getReadableDatabase();
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                if (TextUtils.isEmpty(sortOrder)){
                    sortOrder = ContactContract.Contact.COLUMN_NAME_NAME + " ASC";
                }
                break;
            case CONTACT_ID:
                //Esto es en el caso en que el ID sea diferente de un numero
                if (TextUtils.isEmpty(selection)) {
                    selection = ContactContract.Contact._ID + "=" + uri.getLastPathSegment();
                } else {
                    selection += " AND " + ContactContract.Contact._ID + "=" + uri.getLastPathSegment();
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Cursor cursor = db.query(ContactContract.Contact.TABLE_NAME,
                projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                return ContactContract.Contact.CONTENT_TYPE;
            case CONTACT_ID:
                return ContactContract.Contact.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = databaseHelper.getWritableDatabase();
        final long id;
        switch (uriMatcher.match(uri)) {
            case CONTACTS:
                id = db.insert(ContactContract.Contact.TABLE_NAME,null,values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        return ContentUris.withAppendedId(uri, id);
    }

    @Nullable
    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
      int contador=0;
      final SQLiteDatabase db= databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CONTACT_ID :
                contador = db.delete(ContactContract.Contact.TABLE_NAME,selection, selectionArgs);
                break;
            case CONTACTS:
                contador = db.delete(ContactContract.Contact.TABLE_NAME,selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return contador;
    }





    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int contador=0;
        final SQLiteDatabase db= databaseHelper.getWritableDatabase();
        switch (uriMatcher.match(uri)) {
            case CONTACT_ID :
                contador = db.update(ContactContract.Contact.TABLE_NAME,values,selection,selectionArgs);
                break;
            case CONTACTS:
                contador = db.update(ContactContract.Contact.TABLE_NAME,values,selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        return contador;
    }





}
