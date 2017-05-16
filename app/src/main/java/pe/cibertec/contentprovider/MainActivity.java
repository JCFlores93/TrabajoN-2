package pe.cibertec.contentprovider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pe.cibertec.contentprovider.db.ContactContract;
import pe.cibertec.contentprovider.model.Contact;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private EditText edtName, edtPhone, edtDob;
    private ArrayAdapter<Contact> adapter;
    private List<Contact> contactList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtPhone = (EditText) findViewById(R.id.edt_phone);
        edtDob = (EditText) findViewById(R.id.edt_dob);

        ListView listView = (ListView) findViewById(R.id.list_view);
        adapter = new ArrayAdapter<Contact>(this,
                android.R.layout.simple_list_item_1, contactList);
        listView.setAdapter(adapter);


        Button btnInsert = (Button) findViewById(R.id.btn_insert);
        btnInsert.setOnClickListener(this);

        Button btnModificar= (Button) findViewById(R.id.btn_modificar);
        btnModificar.setOnClickListener(this);

        Button btnEliminar= (Button) findViewById(R.id.btn_eliminar);
        btnEliminar.setOnClickListener(this);

        showContactList();
    }

    private void showContactList() {
        contactList.clear();
        contactList.addAll(getContactList());
        adapter.notifyDataSetChanged();
    }
//Para nuevos registros
    private ContentValues getContentValues() {
        ContentValues values = new ContentValues();
        values.put(ContactContract.Contact.COLUMN_NAME_NAME, edtName.getText().toString());
        values.put(ContactContract.Contact.COLUMN_NAME_PHONE, edtPhone.getText().toString());
        values.put(ContactContract.Contact.COLUMN_NAME_DOB, edtDob.getText().toString());
        return values;
    }

    //content resolver y utilizaremos su método query() para obtener los resultados en forma de cursor.
    private Cursor query() {
        ContentResolver cr = getContentResolver();
        String[] projection = new String[]{
                //Columnas de la tabla a recuperar
                ContactContract.Contact._ID,
                ContactContract.Contact.COLUMN_NAME_NAME,
                ContactContract.Contact.COLUMN_NAME_PHONE,
                ContactContract.Contact.COLUMN_NAME_DOB,
        };
        return cr.query(ContactContract.Contact.CONTENT_URI,
                projection, null, null, null);
    }

    //Pasamos de cursor a Contacto
    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        contact.id = cursor.getLong(0);
        contact.name = cursor.getString(1);
        contact.phone = cursor.getString(2);
        contact.dob = cursor.getString(3);
        return contact;
    }
    //Llenamos la lista
    private List<Contact> getContactList() {
        Cursor cursor = query();
        List<Contact> contactList = new ArrayList<>();
        if (cursor != null) {
            while(cursor.moveToNext()) {
                Contact contact = cursorToContact(cursor);
                contactList.add(contact);
            }
            cursor.close();
        }
        return contactList;
    }

    private long busqueda(List<Contact> lista,String nombre) {
          long idEncontrado=0;
        for (Contact contact : lista) {
            if(contact.name.trim().equals(nombre)) {
               idEncontrado=contact.id;
                break;
            }else{
                Log.i(TAG, "ERROR NO SE ENCUENTRA EL CONTACTO");
            }
        }
        return idEncontrado;}


    private void insert(ContentValues values) {
        ContentResolver cr = getContentResolver();
        Uri uri = cr.insert(ContactContract.Contact.CONTENT_URI, values);
        if (uri != null) {
            Log.i(TAG, "Inserted row id: " + uri.getLastPathSegment());
        } else {
            Log.i(TAG, "Uri is null");
        }
    }

    private void eliminar(ContentValues values,String nombre){
        ContentResolver cr = getContentResolver();
        long id =  busqueda(getContactList(),nombre);
        int uri = cr.delete(ContactContract.Contact.CONTENT_URI,ContactContract.Contact._ID + " = "+ id +" ",null);
        if (uri != 0) {
            Log.i(TAG, "Deleted row id: " + id);
        } else {
            Log.i(TAG, "Name is null");
        }
    }
    private void modificar(ContentValues values,String nombre){
        ContentResolver cr = getContentResolver();
        long id =  busqueda(getContactList(),nombre);
        int uri = cr.update(ContactContract.Contact.CONTENT_URI,values,ContactContract.Contact._ID + " = "+ id +" ",null);
        if (uri != 0) {
            Log.i(TAG, "Updated row id: " + id);
        } else {
            Log.i(TAG, "Name is null");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_insert:
                insert(getContentValues());
                showContactList();
                break;

            case R.id.btn_modificar:
                if(edtName!=null){
                    modificar(getContentValues(),edtName.getText().toString());
                    showContactList();
                }else{
                    Snackbar mySnackBar = Snackbar.make(v,"Ingrese el texto a modificar",Snackbar.LENGTH_SHORT);
                    mySnackBar.setAction(R.string.verificar, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            edtName.requestFocus();
                        }
                    });
                }

                break;

            case R.id.btn_eliminar:
                eliminar(getContentValues(),edtName.getText().toString());
                showContactList();
                break;
        }
    }
}
