package adrian.ecualizador12bandas;

import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.IOException;

public class Player extends AppCompatActivity {

    private MediaPlayer mMediaPlayer;
    private String[] mMusicList;
    private String[] rutas;
    public int sessionID;
    public Button pausebutton;
    public boolean isPaused = false;
    public int lenght;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        mMediaPlayer = new MediaPlayer();

        ListView mListView = (ListView) findViewById(R.id.listView);

        mMusicList = getMusic();
        rutas = getPaths();

        ArrayAdapter<String> mAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mMusicList);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                try {
                    playSong(mMusicList[arg2], rutas[arg2]);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        pausebutton = (Button) findViewById(R.id.button_pause);
        if(!mMediaPlayer.isPlaying()){
            pausebutton.setEnabled(false);
        }
    }

    private String[] getMusic() {
        String[] proj = { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA };

        final Cursor mCursor = Player.this.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count = mCursor.getCount();
        //int column_index = mCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

        String[] songs = new String[count];
        int i = 0;
        if (mCursor.moveToFirst()) {
            do {
                songs[i] = mCursor.getString(0);
                //rutas[i] = mCursor.getString(column_index);
                i++;
            } while (mCursor.moveToNext());
        }

        //mCursor.close();
        return songs;
    }

    private String[] getPaths() {
        String[] proj2 = { MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.DATA };

        final Cursor mCursor2 = Player.this.getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, proj2, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        int count2 = mCursor2.getCount();
        int column_index2 = mCursor2.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);

        String[] ruta = new String[count2];
        int i = 0;
        if (mCursor2.moveToFirst()) {
            do {
                ruta[i] = mCursor2.getString(column_index2);
                i++;
            } while (mCursor2.moveToNext());
        }

        //mCursor.close();
        return ruta;
    }


    private void playSong(String path, String ruta) throws IllegalArgumentException, IllegalStateException, IOException {

        //String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

        path = ruta; //extStorageDirectory + File.separator + "Music" + File.separator + path;


        mMediaPlayer.reset();
        mMediaPlayer.setDataSource(path);
        mMediaPlayer.prepare();
        mMediaPlayer.start();
        pausebutton.setEnabled(true);
        sessionID = mMediaPlayer.getAudioSessionId();
    }

    public void TOEQ(View view){
        Intent intent = new Intent(Player.this, MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("SessionID", sessionID);
        intent.putExtras(b);
        startActivity(intent);
    }

    public void pauseAction(View view){
        if(isPaused){
            pausebutton.setText("Pausar");
            mMediaPlayer.start();
            mMediaPlayer.seekTo(lenght);
        }else{
            if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                mMediaPlayer.pause();
                isPaused = false;
                lenght = mMediaPlayer.getCurrentPosition();
                pausebutton.setText("Continuar");
            }
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }
}
