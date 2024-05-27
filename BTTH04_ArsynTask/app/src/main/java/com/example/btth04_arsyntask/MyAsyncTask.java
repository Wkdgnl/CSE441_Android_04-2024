package com.example.btth04_arsyntask;

import android.app.Activity;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class MyAsyncTask extends AsyncTask<Void, Integer, Void> {
    //khai báo Activity đề lưu trữ context của MainActivity
    Activity contextCha;
//constructor này được truyền vào là MainActivity
    public MyAsyncTask (Activity ctx)
    {
        contextCha=ctx;
    }
    //hàm này sẽ được thực hiện đầu tiên @Override
    protected void onPreExecute() {
// TODO Auto-generated method stub
        super.onPreExecute(); Toast.makeText(contextCha, "onPreExecute!", Toast.LENGTH_LONG).show();
    }
//sau đó tới hàm doInBackground /
 @Override
    protected Void doInBackground (Void... arg0) {
        for(int i=0;i<=100;i++)
        {
//nghỉ 100 milisecond thì tiến hành update UI SystemClock.sleep(100);
//khi gọi hàm này thì onProgressUpdate sẽ thực thi publishProgress(i);
        }
        return null;
    }
    @Override
    protected void onProgressUpdate (Integer... values) {


        contextCha.findViewById(R.id.progressBar1);
//vì publishProgress chỉ truyền 1 đối số //nên mảng values chỉ có 1 phần tử
        int giatri=values[0];
//tăng giá trị của Progressbar lên
        Activity paCha = null;
        paCha.setProgress (giatri);
//đồng thời hiện thị giá trị là % lên TextView
        TextView
                txtmsg= (TextView)contextCha.findViewById(R.id.textView1);
        txtmsg.setText (giatri+"%");
    }
    /**
     * sau khi tiến trình thực hiện xong thì hàm này sảy ra
     */
    @Override
    protected void onPostExecute (Void result) {
// TODO Auto-generated method stub
        super.onPostExecute (result);
        Toast.makeText(contextCha, "Update xong roi do!", Toast.LENGTH_LONG).show();
    }
}