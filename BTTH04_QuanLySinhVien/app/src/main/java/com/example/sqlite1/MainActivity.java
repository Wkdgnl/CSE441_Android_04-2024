package com.example.sqlite1;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
//Khai báo các biến giao diện
public class MainActivity extends AppCompatActivity {
    EditText edtmalop, edttenlop, edtsiso;
    Button btninsert, btndelete, btnupdate;
    // khai báo ListView
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;
    SQLiteDatabase mydatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtmalop = findViewById(R.id.edtmalop);
        edttenlop = findViewById(R.id.edtTenLop);
        edtsiso = findViewById(R.id.edtSiso);
        btninsert = findViewById(R.id.btnInsert);
        btndelete = findViewById(R.id.btnDelete);
        btnupdate = findViewById(R.id.btnUpdate);

// Tạo ListView
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,mylist);
        lv.setAdapter(myadapter);
// Tạo và mở Cơ sở dữ liệu Sqlite
        mydatabase = openOrCreateDatabase("qlsinhvien.db",MODE_PRIVATE,null);
// Tạo Table để chứa dữ liệu
        try {
            String sql = "CREATE TABLE tbllop(malop TEXT primary key,tenlop TEXT, siso INTEGER)";
            mydatabase.execSQL(sql);
        }
        catch (Exception e)
        {
            Log.e("Error","Table đã tồn tại");
        }
        btninsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtmalop.getText().toString().isEmpty() || edttenlop.getText().toString().isEmpty() || edtsiso.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Vui lòng điền thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                String malop = edtmalop.getText().toString();
                String tenlop = edttenlop.getText().toString();
                int siso = Integer.parseInt(edtsiso.getText().toString());
                if(siso<=0){
                    Toast.makeText(getApplicationContext(),"Sĩ sô không hợp lệ",Toast.LENGTH_SHORT).show();
                }
                ContentValues myvalue = new ContentValues();
                myvalue.put("malop",malop);
                myvalue.put("tenlop",tenlop);
                myvalue.put("siso",siso);
                String msg = "";


                if (mydatabase.insert("tbllop",null,myvalue) == -1)
                {
                    msg = "Fail to Insert Record!";
                }
                else {
                    msg ="Insert record Sucessfully";
                    edtmalop.setText("");
                    edttenlop.setText("");
                    edtsiso.setText("");
                    loadData();
                }
                Toast.makeText(MainActivity.this, msg,
                        Toast.LENGTH_SHORT).show();

            }
        });
        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtmalop.getText().toString().isEmpty() )
                {
                    Toast.makeText(getApplicationContext(),"Vui lòng điền mã lớp muốn xóa",Toast.LENGTH_SHORT).show();
                    return;
                }
                String malop = edtmalop.getText().toString();
                int n = mydatabase.delete("tbllop","malop = ?",new
                        String[]{malop});
                String msg ="";
                if (n == 0)
                {
                    msg = "No record to Delete";
                }
                else {
                    msg = n +" record is deleted";
                }
                Toast.makeText(MainActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(edtmalop.getText().toString().isEmpty() || edttenlop.getText().toString().isEmpty() || edtsiso.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Vui lòng điền thông tin",Toast.LENGTH_SHORT).show();
                    return;
                }
                int siso = Integer.parseInt(edtsiso.getText().toString());
                if(siso<=0){
                    Toast.makeText(getApplicationContext(),"Sĩ sô không hợp lệ",Toast.LENGTH_SHORT).show();
                }
                String malop = edtmalop.getText().toString();
                ContentValues myvalue = new ContentValues();
                myvalue.put("siso",siso);
                int n = mydatabase.update("tbllop",myvalue,"malop = ?",new
                        String[]{malop});
                String msg = "";
                if (n == 0)
                {
                    msg = "Update không thành công";
                }
                else {
                    msg = n+ "Update thành công";
                }
                Toast.makeText(MainActivity.this, msg,
                        Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadData() {
        mylist.clear();
        Cursor c = mydatabase.query("tbllop", null, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            String malop = c.getString(0);
            String tenlop = c.getString(1);
            int siso = c.getInt(2);
            mylist.add(malop + " - " + tenlop + " - " + siso);
            c.moveToNext();
        }
        c.close();
        myadapter.notifyDataSetChanged();
    }
}