package com.example.musicplayer;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends Activity
{
    private ImageButton imgBtn_Previous;
    private ImageButton imgBtn_PlayOrPause;
    private ImageButton imgBtn_Stop;
    private ImageButton imgBtn_Next;
    private ListView list;
    private int number;

    private MediaPlayer player;

    private void load(int number)
    {
        if (player != null)
        {
            player.release();
        }
        Uri musicUri = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "" + number);
        player = MediaPlayer.create(this, musicUri);
    }

    private void play(int number)
    {
        if (player != null && player.isPlaying())
        {
            player.stop();
        }
        load(number);
        player.start();
    }

    private void pause()
    {
        if (player.isPlaying())
        {
            player.pause();
        }
    }

    private void stop()
    {
        if (player != null)
        {
            player.stop();
        }
    }

    private void resume()
    {
        player.start();
    }

    private void replay()
    {
        player.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findView();
        registerListeners();
        number = 1;
    }

    private void moveNumberToNext()
    {
        if ((number + 1) > list.getCount())
        {
            number = 1;
            Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.tip_reach_bottom), Toast.LENGTH_SHORT).show();
        } else
        {
            ++number;
        }
    }

    private void moveNumberToPrevious()
    {
        if (number == 1)
        {
            number = list.getCount();
            Toast.makeText(MainActivity.this, MainActivity.this.getString(R.string.tip_reach_top), Toast.LENGTH_SHORT).show();
        } else
        {
            --number;
        }
    }

    private void registerListeners()
    {
        imgBtn_Previous.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                moveNumberToPrevious();
                play(number);
                imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_pause);
            }
        });
        imgBtn_PlayOrPause.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (player != null && player.isPlaying())
                {
                    pause();
                    imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_play);
                } else
                {
                    play(number);
                    imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_pause);
                }
            }
        });
        imgBtn_Stop.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stop();
                imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_play);
            }
        });
        imgBtn_Next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                moveNumberToNext();
                play(number);
                imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_pause);
            }
        });
        list.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                number = position +1;
                play(number);
                imgBtn_PlayOrPause.setBackgroundResource(R.drawable.button_pause);
            }
        });
    }

    private void findView()
    {
        imgBtn_Previous = (ImageButton) findViewById(R.id.imageButton1);
        imgBtn_PlayOrPause = (ImageButton) findViewById(R.id.imageButton2);
        imgBtn_Stop = (ImageButton) findViewById(R.id.imageButton3);
        imgBtn_Next = (ImageButton) findViewById(R.id.imageButton4);
        list = (ListView) findViewById(R.id.listview1);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        initMusicList();
        if (list.getCount() == 0)
        {
            imgBtn_Previous.setEnabled(false);
            imgBtn_PlayOrPause.setEnabled(false);
            imgBtn_Stop.setEnabled(false);
            imgBtn_Next.setEnabled(false);
            Toast.makeText(this, this.getString(R.string.tip_no_music_file), Toast.LENGTH_SHORT).show();
        } else
        {
            imgBtn_Previous.setEnabled(true);
            imgBtn_PlayOrPause.setEnabled(true);
            imgBtn_Stop.setEnabled(true);
            imgBtn_Next.setEnabled(true);
        }
    }


    private void initMusicList()
    {
//        Cursor cursor = getMusicCursor();
//        setListContent(cursor);
    }

    private void setListContent(Cursor musicCursor)
    {
        CursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2, musicCursor, new String[]{MediaStore.Audio.AudioColumns.TITLE, MediaStore.Audio.AudioColumns.ARTIST}, new int[]{android.R.id.text1, android.R.id.text2});
        list.setAdapter(adapter);
    }

    private Cursor getMusicCursor()
    {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        return cursor;
    }
}
