package com.hanling.xfvoicelibrary;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class XFLibVoiceActivity extends AppCompatActivity {
    private static final String TAG = "XFLibVoiceActivity";
    private static final int REQUEST_CODE = 1;
    private Context mContext;
    private Button btn_voice,btn_read_xflib;
    private TextView tv_voice_xflib;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xflib_voice);
        mContext = this;
        SpeechUtility.createUtility(XFLibVoiceActivity.this, SpeechConstant.APPID + "=590947e5");
        init();
////1.创建SpeechRecognizer对象，第二个参数：本地识别时传InitListener
//        SpeechRecognizer mIat= SpeechRecognizer.createRecognizer(XFLibVoiceActivity.this, null);
////2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
//        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
//        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
////3.开始听写
//        mIat.startListening(mRecoListener);


    }

    private void init() {
        btn_read_xflib = (Button) findViewById(R.id.btn_read_xflib);
        tv_voice_xflib = (TextView) findViewById(R.id.tv_voice_xflib);
        btn_voice = (Button) findViewById(R.id.btn_voice_xflib);
        btn_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.创建RecognizerDialog对象
                RecognizerDialog mDialog = new RecognizerDialog(mContext, mInitListener);
                //2.设置accent、language等参数
                mDialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
                mDialog.setParameter(SpeechConstant.ACCENT, "mandarin");
                //若要将UI控件用于语义理解，必须添加以下参数设置，设置之后onResult回调返回将是语义理解//结果
                // mDialog.setParameter("asr_sch", "1");
                // mDialog.setParameter("nlp_version", "2.0");

                //3.设置回调接口
                mDialog.setListener(mRecognizerDialogListener);
                //4.显示dialog，接收语音输入
                mDialog.show();
            }
        });
    }

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            Toast.makeText(mContext, results.getResultString().toString(), Toast.LENGTH_SHORT).show();
            String text = JsonParser.parseIatResult(results.getResultString());

            JSONObject resultJson = null;
            try {

                resultJson = new JSONObject(results.getResultString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            String sn = resultJson.optString("sn");
            if (!text.equals("？")&&!text.equals("。")&&!text.equals("?")){
                tv_voice_xflib.setText(text);

            }
            Log.e(TAG, "onResult: " + sn+text);
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Toast.makeText(mContext, error.toString(), Toast.LENGTH_SHORT).show();
        }

    };
    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG, "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(mContext, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };
}
