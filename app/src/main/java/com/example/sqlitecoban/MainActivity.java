package com.example.sqlitecoban;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Database database;
    ListView lvCongViec;
    ArrayList<CongViec> arrayCongViec;
    CongViecAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvCongViec=(ListView) findViewById(R.id.listviewCongViec);
        arrayCongViec=new ArrayList<>();
        adapter=new CongViecAdapter(this,R.layout.dong_cong_viec,arrayCongViec);
        lvCongViec.setAdapter(adapter);

        //Tạo database GhiChu
        database=new Database(this,"ghichu.sqlite",null,1);

        //Tạo bảng CongViec
        database.QueryData("CREATE TABLE IF NOT EXISTS CongViec(Id INTEGER PRIMARY KEY AUTOINCREMENT, TenCV VARCHAR(200))");

        //Thêm dữ liệu
       // database.QueryData("INSERT INTO CongViec VALUES(null, 'Viết ứng dụng')");

        //select data
        Cursor dataCongViec=database.GetData("SELECT * FROM CongViec");
        while(dataCongViec.moveToNext()){
            String ten=dataCongViec.getString(1);
            int id=dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id,ten));
            Toast.makeText(this, ten, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    public void DialogXoaCongViec(final String tencv, final int id){
        AlertDialog.Builder dialogXoa= new AlertDialog.Builder(this);
        dialogXoa.setMessage("Bạn có muốn xóa công việc " + tencv + " không?");
        dialogXoa.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                database.QueryData("DELETE  FROM  CongViec WHERE Id= '"+ id +"'");
                Toast.makeText(MainActivity.this, "Đã xóa " + tencv, Toast.LENGTH_SHORT).show();
                GetDataCongViec();
            }
        });

        dialogXoa.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialogXoa.show();
    }

    public void DialogSuaCongViec(String ten, final int id) {
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_sua_cong_viec);

        final EditText edtTenCV= (EditText) dialog.findViewById(R.id.editTextTenCVEdit);
        Button btnXacNhan =(Button) dialog.findViewById(R.id.buttonXacNhan);
        final Button btnHuy= (Button) dialog.findViewById(R.id.buttonHuyEdit);

        edtTenCV.setText(ten);
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenMoi=edtTenCV.getText().toString().trim();
                database.QueryData("UPDATE CongViec  SET TenCV ='"+ tenMoi +"' WHERE Id = '"+ id +"'");
                Toast.makeText(MainActivity.this,"Đã cập nhật",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                GetDataCongViec();
            }
        });

        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });





        dialog.show();
    }

    private void GetDataCongViec(){
        Cursor dataCongViec=database.GetData("SELECT * FROM CongViec");
        arrayCongViec.clear();
        while(dataCongViec.moveToNext()){
            String ten=dataCongViec.getString(1);
            int id=dataCongViec.getInt(0);
            arrayCongViec.add(new CongViec(id,ten));
            Toast.makeText(this, ten, Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_congviec,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.menuAdd){
            DialogThem();
        }
        return super.onOptionsItemSelected(item);
    }
    private void DialogThem(){
        final Dialog dialog=new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_them_cong_viec);
        final EditText edtTen=(EditText) dialog.findViewById(R.id.editTextTenCV);
        Button btnThem=(Button) dialog.findViewById(R.id.buttonThem);
        Button btnHuy=(Button) dialog.findViewById(R.id.buttonHuy);

        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tencv=edtTen.getText().toString();
                if(tencv.equals("")){
                    Toast.makeText(MainActivity.this,"Vui lòng nhập tên công việc",Toast.LENGTH_SHORT).show();
                }else{
                    database.QueryData("INSERT INTO CongViec VALUES(null, '"+ tencv +"')");
                    Toast.makeText(MainActivity.this,"Đã thêm!",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    GetDataCongViec();
                }
            }
        });
        btnHuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}