package pe.cibertec.contentprovider.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Android on 29/04/2017.
 */

public final class ContactContract {
    //Identificador en sí del content provider, también llamado authority.
    //Dado que este dato debe ser único es una buena práctica utilizar un authority de tipo “nombre de clase java invertido”
    public static final String CONTENT_AUTHORITY = "pe.cibertec.contentprovider";
   //En primer lugar el prefijo “content://” que indica que dicho recurso deberá ser tratado por un content provider.
    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    //Por último, se indica la entidad concreta a la que queremos acceder dentro de los datos que proporciona el content provider
    public static final String PATH_CONTACT = "contact";

    private ContactContract (){
    }

    public static class Contact implements BaseColumns {
        public static final String TABLE_NAME = "contacts";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_PHONE = "phone";
        public static final String COLUMN_NAME_DOB = "dob";

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CONTACT).build();

        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd." + CONTENT_AUTHORITY + "/" + PATH_CONTACT;

        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd." + CONTENT_AUTHORITY + "/" + PATH_CONTACT;
    }
}
