package com.example.shoppinglist.shoppinglist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class MainActivity extends ActionBarActivity {
    ListView shoppingList;
    WorkDataBase workDataBase;
    SimpleCursorAdapter cursorAdapter;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        shoppingList = (ListView) findViewById(R.id.listShoppingView);
        workDataBase = new WorkDataBase(this);
        cursor = workDataBase.fetchAllList();
        String[] columns = new String[]{ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_ENTRY_ID, ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_TITLE};
        int[] to = new int[]{R.id.productName, R.id.countName};
        cursorAdapter = new SimpleCursorAdapter(this, R.layout.list_cursor, cursor, columns, to, 0);
        shoppingList.setAdapter(cursorAdapter);
        shoppingList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final Cursor cursor = (Cursor) shoppingList.getItemAtPosition(position);
                final int idList = cursor.getInt(cursor.getColumnIndexOrThrow(ListShoppingDbHelper.ShoppingEntry._ID));
                cursor.getInt(cursor.getColumnIndexOrThrow(ListShoppingDbHelper.ShoppingEntry._ID));
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String[] arrayMenu = {"Delete", "Update"};
                builder.setTitle("Menu").setItems(arrayMenu, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                workDataBase.deteleIdRecord(idList);
                                refrechListView();
                                break;
                            case 1:
                                editDialog(idList);
                                break;
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                return false;
            }
        });
    }

    public void refrechListView() {
        cursor.requery();
        shoppingList.invalidateViews();
    }

    public void addDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_dialog, null);
        builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText productEdit = (EditText) view.findViewById(R.id.productEdit);
                EditText countEdit = (EditText) view.findViewById(R.id.countEdit);
                workDataBase.addRecord(productEdit.getText().toString(), countEdit.getText().toString());
                refrechListView();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void editDialog(final int id) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        LayoutInflater inflater = MainActivity.this.getLayoutInflater();
        final View view = inflater.inflate(R.layout.edit_dialog, null);
        final EditText productEdit = (EditText) view.findViewById(R.id.productEdit);
        final EditText countEdit = (EditText) view.findViewById(R.id.countEdit);
        Cursor cursor = workDataBase.fetchIdList(id);
        productEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow(ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_ENTRY_ID)));
        countEdit.setText(cursor.getString(cursor.getColumnIndexOrThrow(ListShoppingDbHelper.ShoppingEntry.COLUMN_NAME_TITLE)));
        builder.setView(view).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                workDataBase.updateIdDataBase(id, productEdit.getText().toString(), countEdit.getText().toString());
                refrechListView();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                Log.d("", "Action Add Menu");
                addDialog();
                break;
            case R.id.action_deleteall:
                workDataBase.deleteAll();
                refrechListView();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
