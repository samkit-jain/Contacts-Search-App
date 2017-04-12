package jain.samkit.contacts;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = "ContactsDatabase";

    private static final String COL_ID = "ID";
    private static final String COL_NAME = "NAME";
    private static final String COL_NUMBER = "NUMBER";
    private static final String COL_IM = "IM_NAME";
    private static final String COL_EMAIL = "EMAIL";
    private static final String COL_ORGANIZATION = "ORGANIZATION";
    private static final String COL_NOTE = "NOTE";
    private static final String COL_ADDRESS = "ADDRESS";

    private static final String DATABASE_NAME = "CONTACTS";
    private static final String FTS_VIRTUAL_TABLE = "FTS";
    private static final int DATABASE_VERSION = 1;

    private Context mHelperContext;

    private static final String FTS_TABLE_CREATE =
            "CREATE VIRTUAL TABLE " + FTS_VIRTUAL_TABLE +
                    " USING fts4 (" +
                    COL_ID + ", " +
                    COL_NAME + ", " +
                    COL_NUMBER + ", " +
                    COL_EMAIL + ", " +
                    COL_ORGANIZATION + ", " +
                    COL_NOTE + ", " +
                    COL_IM + ", " +
                    COL_ADDRESS + ")";

    public DatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mHelperContext = context;
    }

    public void readContacts() throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentResolver cr = mHelperContext.getContentResolver();

        final String[] projection = new String[] {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);

        try {
            int hits = 0;
            if (cur != null && cur.getCount() > 0) {
                while (cur.moveToNext()) {
                    ++hits;
                    String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
                    String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    //Log.v("taga", cur.getString(cur.getColumnIndex(ContactsContract.Contacts.Data.DATA1)));
                    String phone = "";
                    String email = "";
                    String note = "";
                    String addr = "";
                    String imName = "";
                    String org = "";

                    if(Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                        final String[] pProjection = new String[] {
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                        };

                        Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, pProjection, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);

                        while(pCur != null && pCur.moveToNext()) {
                            String ph = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            ph = ph.replace(" ", "");
                            phone += ph + " ; ";
                        }

                        if (pCur != null && !pCur.isClosed()) {
                            pCur.close();
                        }
                    }

                    final String[] eProjection = new String[] {
                            ContactsContract.CommonDataKinds.Email.DATA
                    };

                    Cursor emailCur = cr.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI, eProjection, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[]{id}, null);

                    while(emailCur != null && emailCur.moveToNext()) {
                        email += emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA)) + " ; ";
                    }

                    if(emailCur != null && !emailCur.isClosed()) {
                        emailCur.close();
                    }

                    final String[] nProjection = new String[] {
                            ContactsContract.CommonDataKinds.Note.NOTE
                    };

                    String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] noteWhereParams = new String[]{id, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE};
                    Cursor noteCur = cr.query(ContactsContract.Data.CONTENT_URI, nProjection, noteWhere, noteWhereParams, null);

                    if (noteCur != null && noteCur.moveToFirst()) {
                        if(noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)).length() != 0){
                            note += noteCur.getString(noteCur.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE)) + " ; ";
                        }
                    }

                    if(noteCur != null && !noteCur.isClosed()) {
                        noteCur.close();
                    }

                    final String[] aProjection = new String[] {
                            ContactsContract.CommonDataKinds.StructuredPostal.POBOX,
                            ContactsContract.CommonDataKinds.StructuredPostal.STREET,
                            ContactsContract.CommonDataKinds.StructuredPostal.CITY,
                            ContactsContract.CommonDataKinds.StructuredPostal.REGION,
                            ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE,
                            ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY,
                            ContactsContract.CommonDataKinds.StructuredPostal.TYPE
                    };

                    String addrWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] addrWhereParams = new String[]{id, ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_ITEM_TYPE};
                    Cursor addrCur = cr.query(ContactsContract.Data.CONTENT_URI, aProjection, addrWhere, addrWhereParams, null);

                    while(addrCur != null && addrCur.moveToNext()) {
                        String poBox = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POBOX));
                        String street = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.STREET));
                        String city = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.CITY));
                        String state = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.REGION));
                        String postalCode = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.POSTCODE));
                        String country = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.COUNTRY));
                        String type = addrCur.getString(addrCur.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

                        addr += poBox + " , " + street + " , " + city + " , " + state + " , " + postalCode + " , " + country + " , " + type + " ; ";
                    }

                    if(addrCur != null && !addrCur.isClosed()) {
                        addrCur.close();
                    }

                    final String[] iProjection = new String[] {
                            ContactsContract.CommonDataKinds.Im.DATA
                    };

                    String imWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] imWhereParams = new String[]{id, ContactsContract.CommonDataKinds.Im.CONTENT_ITEM_TYPE};
                    Cursor imCur = cr.query(ContactsContract.Data.CONTENT_URI, iProjection, imWhere, imWhereParams, null);

                    if(imCur != null && imCur.moveToFirst()) {
                        imName += imCur.getString(imCur.getColumnIndex(ContactsContract.CommonDataKinds.Im.DATA)) + " ; ";
                    }

                    if(imCur != null && !imCur.isClosed()) {
                        imCur.close();
                    }

                    final String[] oProjection = new String[] {
                            ContactsContract.CommonDataKinds.Organization.DATA,
                            ContactsContract.CommonDataKinds.Organization.TITLE
                    };

                    String orgWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                    String[] orgWhereParams = new String[]{id, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE};
                    Cursor orgCur = cr.query(ContactsContract.Data.CONTENT_URI, oProjection, orgWhere, orgWhereParams, null);

                    if (orgCur != null && orgCur.moveToFirst()) {
                        String orgName = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.DATA));
                        String title = orgCur.getString(orgCur.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TITLE));

                        org += title + " , " + orgName + " ; ";
                    }

                    if(orgCur != null && !orgCur.isClosed()) {
                        orgCur.close();
                    }

                    addContact(db, id, name, phone, email, org, note, addr, imName);

                }
            }

            if(cur != null && !cur.isClosed()){
                cur.close();
            }

            //Log.v("tagga", hits + "");
        } catch (Exception e) {
            //Log.v("tagga", e.toString());
        }

        db.close();
    }

    private long addContact(SQLiteDatabase mDatabase, String id, String name, String number, String email, String org, String note, String addr, String imName) {
        //Log.v("taga", "adding contact " + name);
        ContentValues initialValues = new ContentValues();
        initialValues.put(COL_ID, id);
        initialValues.put(COL_NAME, name);
        initialValues.put(COL_NUMBER, number);
        initialValues.put(COL_ORGANIZATION, org);
        initialValues.put(COL_ADDRESS, addr);
        initialValues.put(COL_IM, imName);
        initialValues.put(COL_EMAIL, email);
        initialValues.put(COL_NOTE, note);

        return mDatabase.insert(FTS_VIRTUAL_TABLE, null, initialValues);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FTS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Log.w(TAG, "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + FTS_VIRTUAL_TABLE);
        onCreate(db);
    }

    public void populateData() {
        SQLiteDatabase db = this.getReadableDatabase();

        long c = DatabaseUtils.queryNumEntries(db, FTS_VIRTUAL_TABLE);

        db.close();

        if(c == 0) {
            new MyTask().execute();
        }
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {
        long tStart;
        ProgressDialog asyncDialog;

        @Override
        protected void onPreExecute() {
            tStart = System.currentTimeMillis();

            asyncDialog = new ProgressDialog(mHelperContext);
            asyncDialog.setMessage("Loading contacts. This may take some time.");
            asyncDialog.setCancelable(false);
            asyncDialog.show();

            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            asyncDialog.dismiss();

            long tEnd = System.currentTimeMillis();
            long tDelta = tEnd - tStart;
            double elapsedSeconds = tDelta / 1000.0;
            //Log.v("tagga", "time - " + elapsedSeconds + "s");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                readContacts();
            } catch (Exception e) {
                //Log.v("tagga", e.toString());
            }

            return null;
        }
    }

    public List<ContactInfo> getContactMatches(String que, String[] columns) {
        //Log.v("tagga", "in matches");
        //String selection = FTS_VIRTUAL_TABLE + " MATCH ?";
        //String[] selectionArgs = new String[] {que + "*"};

        String selection = COL_NAME + " LIKE ? OR " + COL_NUMBER + " LIKE ? OR " + COL_EMAIL + " LIKE ? OR " + COL_ORGANIZATION + " LIKE ? OR " + COL_ADDRESS + " LIKE ? OR " + COL_NOTE + " LIKE ? OR " + COL_IM + " LIKE ?";
        String[] selectionArgs = new String[] {"%" + que + "%", "%" + que + "%", "%" + que + "%", "%" + que + "%", "%" + que + "%", "%" + que + "%", "%" + que + "%"};

        return query(selection, selectionArgs, columns);
    }

    private List<ContactInfo> query(String selection, String[] selectionArgs, String[] columns) {
        SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables(FTS_VIRTUAL_TABLE);

        SQLiteDatabase mDatabaseOpenHelper = this.getReadableDatabase();
        Cursor cursor = builder.query(mDatabaseOpenHelper, columns, selection, selectionArgs, null, null, null);
        List<ContactInfo> contactInfos = new ArrayList<ContactInfo>();

        if(cursor.moveToFirst()) {
            do{
                ContactInfo contactinfo = new ContactInfo(
                        cursor.getString(cursor.getColumnIndex(COL_ID)),
                        cursor.getString(cursor.getColumnIndex(COL_NAME)),
                        cursor.getString(cursor.getColumnIndex(COL_NUMBER)),
                        cursor.getString(cursor.getColumnIndex(COL_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COL_ORGANIZATION)),
                        cursor.getString(cursor.getColumnIndex(COL_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(COL_NOTE)),
                        cursor.getString(cursor.getColumnIndex(COL_IM)));
                contactInfos.add(contactinfo);
            } while (cursor.moveToNext());
        }

        //Log.v("tagga", "matched data");

        mDatabaseOpenHelper.close();
        return contactInfos;
    }

    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }
}
