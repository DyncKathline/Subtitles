package org.dync.subtitles;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.dync.subtitleconverter.SubtitleView;
import org.dync.subtitleconverter.subtitleFile.FatalParsingException;
import org.dync.subtitleconverter.subtitleFile.FormatASS;
import org.dync.subtitleconverter.subtitleFile.FormatSCC;
import org.dync.subtitleconverter.subtitleFile.FormatSRT;
import org.dync.subtitleconverter.subtitleFile.FormatSTL;
import org.dync.subtitleconverter.subtitleFile.FormatTTML;
import org.dync.subtitleconverter.subtitleFile.TimedTextFileFormat;
import org.dync.subtitleconverter.subtitleFile.TimedTextObject;

import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public String TAG = getClass().getSimpleName();
    @BindView(R.id.subtitleview)
    SubtitleView subtitleview;
    @BindView(R.id.btnASS)
    Button btnASS;
    @BindView(R.id.btnSCC)
    Button btnSCC;
    @BindView(R.id.btnSRT)
    Button btnSRT;
    @BindView(R.id.btnSTL)
    Button btnSTL;
    @BindView(R.id.btnXXML)
    Button btnXXML;
    @BindView(R.id.edittext)
    EditText edittext;
    @BindView(R.id.seekTo)
    Button seekTo;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private Context mContext;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;

    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS};
    private SubtitleAdapter subtitleAdapter;

    private final int msg_ass = 0;
    private final int msg_scc = 1;
    private final int msg_srt = 2;
    private final int msg_stl = 3;
    private final int msg_xml = 4;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            TimedTextObject tto = null;
            switch (msg.what) {
                case msg_ass:
                    tto = (TimedTextObject) msg.obj;
                    subtitleview.setData(tto);
                    subtitleAdapter.setNewData(tto);
                    Toast.makeText(mContext, "加载ASS成功", Toast.LENGTH_SHORT).show();
                    break;
                case msg_scc:
                    tto = (TimedTextObject) msg.obj;
                    subtitleview.setData(tto);
                    subtitleAdapter.setNewData(tto);
                    Toast.makeText(mContext, "加载SCC成功", Toast.LENGTH_SHORT).show();
                    break;
                case msg_srt:
                    tto = (TimedTextObject) msg.obj;
                    subtitleview.setData(tto);
                    subtitleAdapter.setNewData(tto);
                    Toast.makeText(mContext, "加载SRT成功", Toast.LENGTH_SHORT).show();
                    break;
                case msg_stl:
                    tto = (TimedTextObject) msg.obj;
                    subtitleview.setData(tto);
                    subtitleAdapter.setNewData(tto);
                    Toast.makeText(mContext, "加载STL成功", Toast.LENGTH_SHORT).show();
                    break;
                case msg_xml:
                    tto = (TimedTextObject) msg.obj;
                    subtitleview.setData(tto);
                    subtitleAdapter.setNewData(tto);
                    Toast.makeText(mContext, "加载XML成功", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout);
        ButterKnife.bind(this);
        mContext = this;
        checkAndApplyPermission();
        subtitleAdapter = new SubtitleAdapter(mContext);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setAdapter(subtitleAdapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void checkAndApplyPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    @OnClick({R.id.btnASS, R.id.btnSCC, R.id.btnSRT, R.id.btnSTL, R.id.btnXXML, R.id.seekTo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnASS:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TimedTextObject tto;
                        TimedTextFileFormat ttff;
                        try {
                            InputStream is = getAssets().open("standards/ASS/Oceans.Eight.2018.1080p.BluRay.x264-SPARKS.简体.ass");
                            ttff = new FormatASS();

                            tto = ttff.parseFile("test", is);
//                        IOClass.writeFileTxt("test.srt", tto.toSRT());

                            Message message = handler.obtainMessage(msg_ass);
                            message.obj = tto;
                            handler.sendMessage(message);
                            Log.d(TAG, "加载ASS成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (FatalParsingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.btnSCC:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TimedTextObject tto;
                        TimedTextFileFormat ttff;
                        try {
                            InputStream is = getAssets().open("standards/SCC/test3.scc");
                            ttff = new FormatSCC();

                            tto = ttff.parseFile("test", is);
//                        IOClass.writeFileTxt("test.srt", tto.toSRT());

                            Message message = handler.obtainMessage(msg_scc);
                            message.obj = tto;
                            handler.sendMessage(message);
                            Log.d(TAG, "加载SCC成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (FatalParsingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.btnSRT:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TimedTextObject tto;
                        TimedTextFileFormat ttff;
                        try {
                            InputStream is = getAssets().open("standards/SRT/Oceans.Eight.2018.1080p.BluRay.x264-SPARKS.简体.srt");
                            ttff = new FormatSRT();

                            tto = ttff.parseFile("test", is);
//                        IOClass.writeFileTxt("test.srt", tto.toSRT());

                            Message message = handler.obtainMessage(msg_srt);
                            message.obj = tto;
                            handler.sendMessage(message);
                            Log.d(TAG, "加载SRT成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (FatalParsingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.btnSTL:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TimedTextObject tto;
                        TimedTextFileFormat ttff;
                        try {
                            InputStream is = getAssets().open("standards/STL/Aquí no hay quien viva 1.STL");
                            ttff = new FormatSTL();

                            tto = ttff.parseFile("test", is);
//                        IOClass.writeFileTxt("test.srt", tto.toSRT());

                            Message message = handler.obtainMessage(msg_stl);
                            message.obj = tto;
                            handler.sendMessage(message);
                            Log.d(TAG, "加载STL成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (FatalParsingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.btnXXML:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        TimedTextObject tto;
                        TimedTextFileFormat ttff;
                        try {
                            InputStream is = getAssets().open("standards/XML/Debate0_03-03-08.dfxp.xml");
                            ttff = new FormatTTML();

                            tto = ttff.parseFile("test", is);
//                        IOClass.writeFileTxt("test.srt", tto.toSRT());

                            Message message = handler.obtainMessage(msg_xml);
                            message.obj = tto;
                            handler.sendMessage(message);
                            Log.d(TAG, "加载XML成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (FatalParsingException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
                break;
            case R.id.seekTo:
                String time = edittext.getText().toString().trim();
                try {
                    int parseInt = Integer.parseInt(time);
                    if (subtitleview != null) {
                        subtitleview.seekTo(parseInt);
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(mContext, "必须是数字", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
