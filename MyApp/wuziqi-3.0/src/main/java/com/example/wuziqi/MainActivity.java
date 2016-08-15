package com.example.wuziqi;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
{
    private wuziqipanel wuziqiPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        wuziqiPanel = (wuziqipanel) findViewById(R.id.id_wuziqi);
        wuziqiPanel.imageViewPiece = (ImageView) findViewById(R.id.imageView);
    }

    public void gameOverDialog()
    {
//        new AlertDialog.Builder(MainActivity.this)
//                .setMessage("确认退出吗？")
//                .setTitle("提示")
//                .setPositiveButton("确认", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener()
//                {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which)
//                    {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//        Toast.makeText(MainActivity.this,"提示的内容", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            wuziqiPanel.start();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

