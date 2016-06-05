package adrian.ecualizador12bandas;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    TextView bass_boost_label = null;
    SeekBar bass_boost = null;
    CheckBox enabled = null;
    Button flat = null;

    Equalizer eq = null;
    BassBoost bb = null;

    int min_level = 0;
    int max_level = 100;

    static final int MAX_SLIDERS = 12; // Must match the XML layout
    SeekBar sliders[] = new SeekBar[MAX_SLIDERS];
    TextView slider_labels[] = new TextView[MAX_SLIDERS];
    int num_sliders = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bundle b = getIntent().getExtras();
        int SessionID = b.getInt("SessionID");

        enabled = (CheckBox)findViewById(R.id.enabled);
        if (enabled != null) {
            enabled.setOnCheckedChangeListener(this);
        }

        flat = (Button)findViewById(R.id.flat);
        if (flat != null) {
            flat.setOnClickListener(this);
        }

        bass_boost = (SeekBar)findViewById(R.id.bass_boost);
        if (bass_boost != null) {
            bass_boost.setOnSeekBarChangeListener(this);
        }
        bass_boost_label = (TextView) findViewById (R.id.bass_boost_label);

        sliders[0] = (SeekBar)findViewById(R.id.slider_1);
        slider_labels[0] = (TextView)findViewById(R.id.slider_label_1);
        sliders[1] = (SeekBar)findViewById(R.id.slider_2);
        slider_labels[1] = (TextView)findViewById(R.id.slider_label_2);
        sliders[2] = (SeekBar)findViewById(R.id.slider_3);
        slider_labels[2] = (TextView)findViewById(R.id.slider_label_3);
        sliders[3] = (SeekBar)findViewById(R.id.slider_4);
        slider_labels[3] = (TextView)findViewById(R.id.slider_label_4);
        sliders[4] = (SeekBar)findViewById(R.id.slider_5);
        slider_labels[4] = (TextView)findViewById(R.id.slider_label_5);
        sliders[5] = (SeekBar)findViewById(R.id.slider_6);
        slider_labels[5] = (TextView)findViewById(R.id.slider_label_6);
        sliders[6] = (SeekBar)findViewById(R.id.slider_7);
        slider_labels[6] = (TextView)findViewById(R.id.slider_label_7);
        sliders[7] = (SeekBar)findViewById(R.id.slider_8);
        slider_labels[7] = (TextView)findViewById(R.id.slider_label_8);
        sliders[8] = (SeekBar)findViewById(R.id.slider_9);
        slider_labels[8] = (TextView)findViewById(R.id.slider_label_9);
        sliders[9] = (SeekBar)findViewById(R.id.slider_10);
        slider_labels[9] = (TextView)findViewById(R.id.slider_label_10);
        sliders[10] = (SeekBar)findViewById(R.id.slider_11);
        slider_labels[10] = (TextView)findViewById(R.id.slider_label_11);
        sliders[11] = (SeekBar)findViewById(R.id.slider_12);
        slider_labels[11] = (TextView)findViewById(R.id.slider_label_12);

        eq = new Equalizer (0, SessionID);
        if (eq != null)
        {
            eq.setEnabled (true);
            int num_bands = eq.getNumberOfBands();
            Log.d("BandsNum", "" + num_bands);
            num_sliders = num_bands;
            short r[] = eq.getBandLevelRange();
            min_level = r[0];
            max_level = r[1];
            for (int i = 0; i < num_sliders && i < MAX_SLIDERS; i++)
            {
                int[] freq_range = eq.getBandFreqRange((short)i);
                sliders[i].setOnSeekBarChangeListener(this);
                slider_labels[i].setText (formatBandLabel (freq_range));
            }
        }
        for (int i = num_sliders ; i < MAX_SLIDERS; i++)
        {
            sliders[i].setVisibility(View.GONE);
            slider_labels[i].setVisibility(View.GONE);
        }

        bb = new BassBoost (0, SessionID);
        if (bb != null)
        {
        }
        else
        {
            bass_boost.setVisibility(View.GONE);
            bass_boost_label.setVisibility(View.GONE);
        }

        updateUI();
    }

    @Override
    public void onProgressChanged (SeekBar seekBar, int level,
                                   boolean fromTouch)
    {
        if (seekBar == bass_boost)
        {
            bb.setEnabled (level > 0 ? true : false);
            bb.setStrength ((short)level);
        }
        else if (eq != null)
        {
            int new_level = min_level + (max_level - min_level) * level / 100;

            for (int i = 0; i < num_sliders; i++)
            {
                if (sliders[i] == seekBar)
                {
                    eq.setBandLevel ((short)i, (short)new_level);
                    break;
                }
            }
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
    }

    public String formatBandLabel (int[] band)
    {
        return milliHzToString(band[0]) + "-" + milliHzToString(band[1]);
    }

    public String milliHzToString (int milliHz)
    {
        if (milliHz < 1000) return "";
        if (milliHz < 1000000)
            return "" + (milliHz / 1000) + "Hz";
        else
            return "" + (milliHz / 1000000) + "kHz";
    }

    public void updateSliders ()
    {
        for (int i = 0; i < num_sliders; i++)
        {
            int level;
            if (eq != null)
                level = eq.getBandLevel ((short)i);
            else
                level = 0;
            int pos = 100 * level / (max_level - min_level) + 50;
            sliders[i].setProgress (pos);
        }
    }

    public void updateBassBoost ()
    {
        if (bb != null)
            bass_boost.setProgress (bb.getRoundedStrength());
        else
            bass_boost.setProgress (0);
    }

    @Override
    public void onCheckedChanged (CompoundButton view, boolean isChecked)
    {
        if (view == enabled)
        {
            eq.setEnabled(isChecked);
        }
    }

    @Override
    public void onClick (View view)
    {
        if (view == flat)
        {
            setFlat();
        }
    }

    public void updateUI ()
    {
        updateSliders();
        updateBassBoost();
        enabled.setChecked(eq.getEnabled());
    }

    public void setFlat ()
    {
        if (eq != null)
        {
            for (int i = 0; i < num_sliders; i++)
            {
                eq.setBandLevel ((short)i, (short)0);
            }
        }

        if (bb != null)
        {
            bb.setEnabled (false);
            bb.setStrength ((short)0);
        }

        updateUI();
    }
}
