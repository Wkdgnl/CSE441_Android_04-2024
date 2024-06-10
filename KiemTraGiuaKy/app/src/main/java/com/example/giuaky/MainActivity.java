package com.example.giuaky;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    TabHost tabHost;
    ImageButton btnThemDonVi, btnThemNhanVien;
    SearchView searchView, searchViewNhanVien;
    ListView lvDonVi;
    ListView lvNhanVien;
    SQLiteDatabase db;
    ArrayList<DonVi> list;
    ArrayList<NhanVien> listNhanVien;
    ArrayAdapterContactItem<DonVi> adapterDonVi;
    ArrayAdapterContactItem<NhanVien> adapterNhanVien;
    private String DB_PATH_SUFFIX = "/databases/";
    private String DATABASE_NAME = "giuaky.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        processCopy();
        db = openOrCreateDatabase("giuaky.db", MODE_PRIVATE, null);
        addControl();
        tab1_action();
    }

    private void processCopy() {
        File dbFile = new File(getDBPath(DATABASE_NAME));
        if (!dbFile.exists()) {
            try {
                CopyDataBaseFromAsset();
                Toast.makeText(this, "Copy database successful", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getDBPath(String DATABASE_NAME) {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + this.DATABASE_NAME;
    }

    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);
            String outFileName = getDBPath(DATABASE_NAME);
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();
            OutputStream myOutput = new FileOutputStream(outFileName);
            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addControl() {
        //Định nghĩa tabHost
        tabHost = findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec spec1, spec2;
        //Tab1
        spec1 = tabHost.newTabSpec("t1");
        spec1.setContent(R.id.tab_donvi);
        spec1.setIndicator("Đơn vị");
        tabHost.addTab(spec1);
        //Tab2
        spec2 = tabHost.newTabSpec("t2");
        spec2.setContent(R.id.tab_nhanvien);
        spec2.setIndicator("Nhân viên");
        tabHost.addTab(spec2);

        //Khi chuyển tab
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("t2")) {
                    tab2_action();
                }
                else {
                    tab1_action();
                }
            }
        });

        //Ánh xạ ID
        btnThemDonVi = findViewById(R.id.btnThemDonVi);
        btnThemNhanVien = findViewById(R.id.btnThemNhanVien);
        searchView = findViewById(R.id.svDonVi);
        searchViewNhanVien = findViewById(R.id.svNhanVien);
        lvDonVi = findViewById(R.id.lvDonVi);
        lvNhanVien = findViewById(R.id.lvNhanVien);
        list = new ArrayList<DonVi>();
        listNhanVien = new ArrayList<NhanVien>();
        adapterDonVi = new ArrayAdapterContactItem<DonVi>(this, R.layout.layout_contact_item, list);
        adapterNhanVien = new ArrayAdapterContactItem<NhanVien>(this, R.layout.layout_contact_item, listNhanVien);
        lvNhanVien.setAdapter(adapterNhanVien);
        lvDonVi.setAdapter(adapterDonVi);

        btnThemDonVi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThemDonVi.class);
                startActivity(intent);
            }
        });

        btnThemNhanVien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ThemNhanVien.class);
                startActivity(intent);
            }
        });

        lvDonVi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DonVi donVi = list.get(position);
                Intent intent = new Intent(MainActivity.this, ChiTietDonVi.class);
                intent.putExtra("IDDonVi", donVi.getID());
                startActivity(intent);
            }
        });

        lvNhanVien.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NhanVien nhanVien = listNhanVien.get(position);
                Intent intent = new Intent(MainActivity.this, ChiTietNhanVien.class);
                intent.putExtra("IDNhanVien", nhanVien.getID());
                startActivity(intent);
            }
        });

        lvNhanVien.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Chọn hành động");
                builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                NhanVien nhanVien = listNhanVien.get(position);
                                Intent intent = new Intent(MainActivity.this, SuaNhanVien.class);
                                intent.putExtra("IDNhanVien", nhanVien.getID());
                                startActivity(intent);
                                break;
                            case 1:
                                int id = listNhanVien.get(position).getID();
                                showDeleteConfirmationDialogNhanVien(id);
                                break;
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        lvDonVi.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Chọn hành động");
                builder.setItems(new CharSequence[]{"Sửa", "Xóa"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                DonVi donVi = list.get(position);
                                Intent intent = new Intent(MainActivity.this, SuaDonVi.class);
                                intent.putExtra("IDDonVi", donVi.getID());
                                startActivity(intent);
                                break;
                            case 1:
                                int id = list.get(position).getID();
                                showDeleteConfirmationDialog(id);
                                break;
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tab1_action();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = "SELECT * FROM DonVi WHERE TenDonVi LIKE '%" + newText + "%'";
                Cursor cursor = db.rawQuery(query, null);
                list.clear();
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String TenDonVi = cursor.getString(1);
                        String Email = cursor.getString(2);
                        String website = cursor.getString(3);
                        byte[] logo = cursor.getBlob(4);
                        if(logo == null) {
                            logo = new byte[0];
                        }
                        String DiaChi = cursor.getString(5);
                        String DienThoai = cursor.getString(6);
                        int DonViCha = cursor.getInt(7);

                        list.add(new DonVi(id, TenDonVi, Email, website, logo, DiaChi, DienThoai, DonViCha));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                adapterDonVi.notifyDataSetChanged();
                return false;
            }
        });

        searchViewNhanVien.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tab2_action();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                String query = "SELECT * FROM NhanVien WHERE Hoten LIKE '%" + newText + "%'";
                Cursor cursor = db.rawQuery(query, null);
                listNhanVien.clear();
                if (cursor.moveToFirst()) {
                    do {
                        int id = cursor.getInt(0);
                        String Hoten = cursor.getString(1);
                        String ChucVu = cursor.getString(2);
                        String Email = cursor.getString(3);
                        String DienThoai = cursor.getString(4);
                        byte[] AnhDaiDien = cursor.getBlob(5);
                        int DonViID = cursor.getInt(6);

                        listNhanVien.add(new NhanVien(id, Hoten, ChucVu, Email, DienThoai, AnhDaiDien, DonViID));
                    } while (cursor.moveToNext());
                }
                cursor.close();
                adapterNhanVien.notifyDataSetChanged();
                return false;
            }
        });

    }

    private void showDeleteConfirmationDialogNhanVien(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteNhanVien(id);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteNhanVien(int id) {
        String query = "DELETE FROM NhanVien WHERE ID = " + id;
        db.execSQL(query);
        tab2_action();
    }

    public void tab1_action(){
        list.clear();
        String query = "SELECT * FROM DonVi";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String TenDonVi = cursor.getString(1);
                String Email = cursor.getString(2);
                String website = cursor.getString(3);
                byte[] logo = cursor.getBlob(4);
                if(logo == null) {
                    logo = new byte[0];
                }
                String DiaChi = cursor.getString(5);
                String DienThoai = cursor.getString(6);
                int DonViCha = cursor.getInt(7);

                list.add(new DonVi(id, TenDonVi, Email, website, logo, DiaChi, DienThoai, DonViCha));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapterDonVi.notifyDataSetChanged();
    }


    public void tab2_action() {
        listNhanVien.clear();
        String query = "SELECT * FROM NhanVien";
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String Hoten = cursor.getString(1);
                String ChucVu = cursor.getString(2);
                String Email = cursor.getString(3);
                String DienThoai = cursor.getString(4);
                byte[] AnhDaiDien = cursor.getBlob(5);
                int DonViID = cursor.getInt(6);

                listNhanVien.add(new NhanVien(id, Hoten, ChucVu, Email, DienThoai, AnhDaiDien, DonViID));
            } while (cursor.moveToNext());
        }
        cursor.close();
        adapterNhanVien.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        tab1_action();
        tab2_action();
    }

    private void showDeleteConfirmationDialog(int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa?");
        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteDonVi(id);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    private void deleteDonVi(int id) {
        String query = "DELETE FROM DonVi WHERE ID = " + id;
        db.execSQL(query);
        tab1_action();
    }
}