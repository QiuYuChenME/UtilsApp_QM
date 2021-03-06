package com.qm.sanforvpnconnlib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.sangfor.ssl.IVpnDelegate;
import com.sangfor.ssl.SFException;
import com.sangfor.ssl.SangforAuth;
import com.sangfor.ssl.common.VpnCommon;
import com.sangfor.ssl.service.setting.SystemConfiguration;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;


public class SanforConnActivity extends Activity implements IVpnDelegate {
    private static final String TAG = "SanforConnLoginActivity";
    private ArrayList<TypeBean> mList = new ArrayList<TypeBean>();
    // 认证所需信息
    private static String VPN_IP = "202.97.140.66"; // VPN设备地址　（也可以使用域名访问）
    private static int VPN_PORT = 443; // vpn设备端口号，一般为443
    // 用户名密码认证；用户名和密码
    private static String USER_NAME = "shwb";
    private static String USER_PASSWD = "q1w2e3r4.0317";
    // 证书认证；导入证书路径和证书密码（如果服务端没有设置证书认证此处可以不设置）
    private static String CERT_PATH = "";
    private static String CERT_PASSWD = "";
    // 测试内网服务器地址。（在vpn服务器上，配置的内网资源）
    private static String TEST_URL = "http://10.14.10.24:8080/SXproject";

    private static String SMS_CODE = "";
    private static String RADIUS_CODE = "";//radius认证的
    private int nowpositon = -1;
    private final int TEST_URL_TIMEOUT_MILLIS = 8 * 1000;// 测试vpn资源的超时时间
    private int AUTH_MODULE = SangforAuth.AUTH_MODULE_L3VPN;
    private TextView tv_mode_sanforlib, tv_quickset_sanforlib;
    private ImageButton cancel;
    private Button btn_loginvpn_sanforlib, btn_save_sanforlib;
    private Context mContext;
    private EditText et_vpnip_sanforlib, et_port_sanforlib, et_user_sanforlib, et_pwd_sanforlib;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    private SanforinfoDAO sanforinfoDAO;
    private ArrayList<SanforInfoBean> sanforlist;
    private ProgressDialog progressDialog;
    private Button btn_delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sanfor_conn);
        mContext = this;
        /**
         * 组件初始化
         */
        tv_mode_sanforlib = (TextView) findViewById(R.id.tv_mode_sanforlib);
        cancel = (ImageButton) findViewById(R.id.cancel);
        et_vpnip_sanforlib = (EditText) findViewById(R.id.et_vpnip_sanforlib);
        et_port_sanforlib = (EditText) findViewById(R.id.et_port_sanforlib);
        et_user_sanforlib = (EditText) findViewById(R.id.et_user_sanforlib);
        et_pwd_sanforlib = (EditText) findViewById(R.id.et_pwd_sanforlib);
        btn_save_sanforlib = (Button) findViewById(R.id.btn_save_sanforlib);
        btn_loginvpn_sanforlib = (Button) findViewById(R.id.btn_loginvpn_sanforlib);
        tv_quickset_sanforlib = (TextView) findViewById(R.id.tv_quickset_sanforlib);
        btn_delete = (Button) findViewById(R.id.btn_delete_sanfor);
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowpositon != -1) {
                    int deletedata = sanforinfoDAO.deletedata(sanforlist.get(nowpositon).getName());

                    if (deletedata==1){
                        tv_quickset_sanforlib.setText("请选择");
                        tv_mode_sanforlib.setText("请选择VPN模式");
                        et_vpnip_sanforlib.setText("");
                        et_port_sanforlib.setText("");
                        et_user_sanforlib.setText("");
                        et_pwd_sanforlib.setText("");
                    }
                }
            }
        });
        tv_quickset_sanforlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sanforlist = (ArrayList<SanforInfoBean>) sanforinfoDAO.loadData();

                Util.alertBottomWheelOption(SanforConnActivity.this, sanforlist, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int i) {
                        nowpositon = i;
                        tv_quickset_sanforlib.setText(sanforlist.get(i).getName());
                        SanforInfoBean sanforInfoBean = sanforlist.get(i);
                        tv_mode_sanforlib.setText(sanforInfoBean.getMode());
                        et_vpnip_sanforlib.setText(sanforInfoBean.getIP());
                        et_port_sanforlib.setText(sanforInfoBean.getPort());
                        et_user_sanforlib.setText(sanforInfoBean.getUser());
                        et_pwd_sanforlib.setText(sanforInfoBean.getPwd());
                        tv_quickset_sanforlib.setText(sanforInfoBean.getName());
                    }
                });
            }
        });

        sanforinfoDAO = new SanforinfoDAO(this);


        /**
         * 将保存的信息显示
         */
        loadData(getApplicationContext());
        /**
         * 保存按钮逻辑
         */
        btn_save_sanforlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText et = new EditText(SanforConnActivity.this);
                new AlertDialog.Builder(SanforConnActivity.this)
                        .setTitle("保存名称")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String etname = et.getText().toString().trim();

                                if (et.getText().toString().trim().equals("")) {
                                    Toast.makeText(SanforConnActivity.this, "请输入名称", Toast.LENGTH_SHORT).show();
                                } else {
                                    String mode = tv_mode_sanforlib.getText().toString().trim();
                                    String ip = et_vpnip_sanforlib.getText().toString().trim();
                                    String port = et_port_sanforlib.getText().toString().trim();
                                    String user = et_user_sanforlib.getText().toString().trim();
                                    String pwd = et_pwd_sanforlib.getText().toString().trim();
                                    long test1 = sanforinfoDAO.addDate(etname, mode, ip, port, user, pwd);

                                }

                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();


            }
        });
        /**
         * 返回按钮
         */
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /**
         * vpn登录按钮
         */
        btn_loginvpn_sanforlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog = new ProgressDialog(mContext);
                progressDialog.setMessage("登录中。。。");
                progressDialog.show();
                initVpnModule();
                String ip = et_vpnip_sanforlib.getText().toString().trim();
                String port = et_port_sanforlib.getText().toString().trim();
                String user = et_user_sanforlib.getText().toString().trim();
                String pwd = et_pwd_sanforlib.getText().toString().trim();
                VPN_IP = !ip.equals("") ? ip : "202.97.140.66";
                try {
                    VPN_PORT = port.equals("") ? 443 : Integer.parseInt(port);
                } catch (Exception e) {
                    VPN_PORT = 443;
                }

                USER_NAME = user.equals("") ? "shwb" : user;
                USER_PASSWD = pwd.equals("") ? "q1w2e3r4.0317" : pwd;

                if (SangforAuth.getInstance().vpnQueryStatus() != IVpnDelegate.VPN_STATUS_AUTH_OK) {
                    initSslVpn();
                } else {
                    Toast.makeText(SanforConnActivity.this, "VPN正常运行中,注销后才能重新登录", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tv_mode_sanforlib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.alertBottomWheelOption(SanforConnActivity.this, mList, new Util.OnWheelViewClick() {
                    @Override
                    public void onClick(View view, int postion) {
                        if (postion == 0) {
                            tv_mode_sanforlib.setText("L3VPN模式");
                            AUTH_MODULE = SangforAuth.AUTH_MODULE_L3VPN;
                        } else {
                            tv_mode_sanforlib.setText("EASYAPP模式");
                            AUTH_MODULE = SangforAuth.AUTH_MODULE_EASYAPP;
                        }
                    }
                });
            }
        });
        // 单项选择

        mList.add(new TypeBean(0, "L3VPN模式(针对整个手机)"));
        mList.add(new TypeBean(1, "EASYAPP模式(只针对集成SDK的apk)"));

    }


    private void initVpnModule() {
        SangforAuth sfAuth = SangforAuth.getInstance();
        try {
            // SDK模式初始化，easyapp模式或者是l3vpn模式，两种模式区别请参考文档
//            Intent intent = getIntent();
//            AUTH_MODULE = intent.getIntExtra("vpn_mode", SangforAuth.AUTH_MODULE_EASYAPP);
            sfAuth.init(getApplication(), this, this, AUTH_MODULE);//SangforAuth.AUTH_MODULE_L3VPN、SangforAuth.AUTH_MODULE_EASYAPP
            sfAuth.setLoginParam(AUTH_CONNECT_TIME_OUT, String.valueOf(8));
        } catch (SFException e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始初始化VPN，该初始化为异步接口，后续动作通过回调函数vpncallback通知结果
     *
     * @return 成功返回true，失败返回false，一般情况下返回true
     */
    private boolean initSslVpn() {
        InitSslVpnTask initSslVpnTask = new InitSslVpnTask();
        initSslVpnTask.execute();
        return true;
    }




    class InitSslVpnTask extends AsyncTask<Void, Void, Boolean> {
        InetAddress m_iAddr = null;

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                m_iAddr = InetAddress.getByName(VPN_IP);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("11111111", "1111111111111111111");
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            SangforAuth sfAuth = SangforAuth.getInstance();
            String strHost = "";
            if (m_iAddr != null) {
                strHost = m_iAddr.getHostAddress();
//                Toast.makeText(getApplicationContext(), strHost, Toast.LENGTH_LONG).show();
            } else {
//                Toast.makeText(getApplicationContext(), strHost, Toast.LENGTH_LONG).show();

            }
            if (TextUtils.isEmpty(strHost)) {
                Log.i(TAG, "解析VPN服务器域名失败");
                strHost = "0.0.0.0";
            }
            Log.i(TAG, "vpn server ip is: " + strHost);
            long host = VpnCommon.ipToLong(strHost);

            if (sfAuth.vpnInit(host, VPN_PORT) == false) {
                Log.d(TAG, "vpn init fail, errno is " + sfAuth.vpnGeterr());
                return;
            }
        }
    }

    @Override
    public void vpnCallback(int vpnResult, int authType) {
        SangforAuth sfAuth = SangforAuth.getInstance();

        switch (vpnResult) {
            case IVpnDelegate.RESULT_VPN_INIT_FAIL:
                /**
                 * 初始化vpn失败
                 */
                Log.i(TAG, "RESULT_VPN_INIT_FAIL, error is " + sfAuth.vpnGeterr());
                displayToast("RESULT_VPN_INIT_FAIL, error is " + sfAuth.vpnGeterr());
                progressDialog.dismiss();
                break;

            case IVpnDelegate.RESULT_VPN_INIT_SUCCESS:
                /**
                 * 初始化vpn成功，接下来就需要开始认证工作了
                 */
                Log.i(TAG, "RESULT_VPN_INIT_SUCCESS, current vpn status is " + sfAuth.vpnQueryStatus());
                displayToast("RESULT_VPN_INIT_SUCCESS, current vpn status is " + sfAuth.vpnQueryStatus());
                Log.i(TAG, "vpnResult============" + vpnResult + "\nauthType ============" + authType);
                sfAuth.setLoginParam(IVpnDelegate.AUTH_DEVICE_LANGUAGE, "zh_CN");// zh_CN or en_US
                // 设置后台不自动登陆,true为off,即取消自动登陆.默认为false,后台自动登陆.
                // sfAuth.setLoginParam(AUTO_LOGIN_OFF_KEY, "true");
                // 初始化成功，进行认证操作　（此处采用“用户名密码”认证）
                doVpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
                break;

            case IVpnDelegate.RESULT_VPN_AUTH_FAIL:
                /**
                 * 认证失败，有可能是传入参数有误，具体信息可通过sfAuth.vpnGeterr()获取
                 */
                String errString = sfAuth.vpnGeterr();
                Log.i(TAG, errString);
                displayToast("RESULT_VPN_AUTH_FAIL, error is " + errString);

                if (errString.contains("psw_errorCode")) {
                    String pwdMsg = sfAuth.getPasswordSafePolicyPrompt(errString);
                    displayToast("RESULT_VPN_AUTH_FAIL, error is " + pwdMsg);
                }
                progressDialog.dismiss();
                break;

            case IVpnDelegate.RESULT_VPN_AUTH_SUCCESS:
                /**
                 * 认证成功，认证成功有两种情况，一种是认证通过，可以使用sslvpn功能了，
                 *
                 * 另一种是 前一个认证（如：用户名密码认证）通过，但需要继续认证（如：需要继续证书认证）
                 */
                if (authType == IVpnDelegate.AUTH_TYPE_NONE) {

				/*
                 * // session共享登陆--主APP保存：认证成功 保存TWFID（SessionId），供子APP使用 String twfid = sfAuth.getTwfid(); Log.i(TAG, "twfid = "+twfid);
				 */
                    Log.i(TAG, "welcome to sangfor sslvpn!");
                    displayToast("welcome to sangfor sslvpn!");

                    // 若为L3vpn流程，认证成功后会自动开启l3vpn服务，需等l3vpn服务开启完成后再访问资源
                    if (SangforAuth.getInstance().getModuleUsed() == SangforAuth.AUTH_MODULE_EASYAPP) {
                        // EasyApp流程，认证流程结束，可访问资源。
                        displayToast("welcome to sangfor AUTH_MODULE_EASYAPP!");
                        Toast.makeText(this, "welcome to sangfor AUTH_MODULE_EASYAPP!", Toast.LENGTH_SHORT).show();
                        finish();
                        doResourceRequest();
                        progressDialog.dismiss();
                    }
                } else if (authType == IVpnDelegate.VPN_TUNNEL_OK) {
                    // l3vpn流程，l3vpn服务通道建立成功，可访问资源
                    String mode = "";
                    if (AUTH_MODULE == 2) {
                        //lv
                        mode = "L3VPN模式";
                    } else if (AUTH_MODULE == 1) {
                        mode = "EASYAPP模式";
                    }
                    saveData(getApplicationContext(), VPN_IP, String.valueOf(VPN_PORT), USER_NAME, USER_PASSWD, mode);


                    Log.i(TAG, "L3VPN tunnel OK!");
                    Toast.makeText(this, "L3VPN tunnel OK!", Toast.LENGTH_SHORT).show();
//                    displayToast("L3VPN tunnel OK!");
                    doResourceRequest();
                    finish();
                } else {
                    Log.i(TAG, "auth success, and need next auth, next auth type is " + authType);
                    displayToast("auth success, and need next auth, next auth type is " + authType);

                    if (authType == IVpnDelegate.AUTH_TYPE_SMS) {
                        // 下一次认证为短信认证，获取相关的信息
                        String phoneNum = SangforAuth.getInstance().getSmsPhoneNum();
                        String countDown = SangforAuth.getInstance().getSmsCountDown();
                        String toastStrsg = "sms code send to [" + phoneNum + "]\n" + "reget code count down [" + countDown + "]\n";
                        Toast.makeText(this, toastStrsg, Toast.LENGTH_SHORT).show();
                    } else if (authType == IVpnDelegate.AUTH_TYPE_RADIUS) {
                        Toast.makeText(this, "start radius challenge auth", Toast.LENGTH_SHORT).show();
                    } else if (authType == IVpnDelegate.AUTH_TYPE_TOKEN) {
                        String tokenCode = RADIUS_CODE;
                        if (TextUtils.isEmpty(tokenCode)) {
                            displayToast("need input tokencode");
                            break;
                        }
                        sfAuth.setLoginParam(IVpnDelegate.TOKEN_AUTH_CODE, tokenCode);
                    } else {
                        doVpnLogin(authType);
                    }
                }
                break;
            case IVpnDelegate.RESULT_VPN_AUTH_CANCEL:
                Log.i(TAG, "RESULT_VPN_AUTH_CANCEL");
                displayToast("RESULT_VPN_AUTH_CANCEL");
                break;
            case IVpnDelegate.RESULT_VPN_AUTH_LOGOUT:
                /**
                 * 主动注销（自己主动调用logout接口）
                 */
                Log.i(TAG, "RESULT_VPN_AUTH_LOGOUT");
                displayToast("RESULT_VPN_AUTH_LOGOUT");
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_FAIL:
                /**
                 * L3vpn启动失败，有可能是没有l3vpn资源，具体信息可通过sfAuth.vpnGeterr()获取
                 */
                Log.i(TAG, "RESULT_VPN_L3VPN_FAIL, error is " + sfAuth.vpnGeterr());
                displayToast("RESULT_VPN_L3VPN_FAIL, error is " + sfAuth.vpnGeterr());
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_SUCCESS:
                /**
                 * L3vpn启动成功
                 */
                Log.i(TAG, "RESULT_VPN_L3VPN_SUCCESS ===== " + SystemConfiguration.getInstance().getSessionId());
                break;
            case IVpnDelegate.RESULT_VPN_L3VPN_RELOGIN:
                /**
                 * L3vpn服务端注销虚拟IP,一般是私有帐号在其他设备同时登录造成的
                 */
                Log.i(TAG, "relogin now");
                displayToast("relogin now");
                break;
            default:
                /**
                 * 其它情况，不会发生，如果到该分支说明代码逻辑有误
                 */
                Log.i(TAG, "default result, vpn result is " + vpnResult);
                displayToast("default result, vpn result is " + vpnResult);
                break;
        }
    }

    @Override
    public void reloginCallback(int i, int i1) {

    }

    @Override
    public void vpnRndCodeCallback(byte[] bytes) {

    }

    @Override
    /*
     * l3vpn模式（SangforAuth.AUTH_MODULE_L3VPN）必须重写这个函数： 注意：当前Activity的launchMode不能设置为 singleInstance，否则L3VPN服务启动会失败。 原因：
	 * L3VPN模式需要通过startActivityForResult向系统申请使用L3VPN权限，{@link VpnService#prepare} 但startActivityForResult有限制： You cannot use startActivityForResult()
	 * if the activity being started is not running in the same task as the activity that starts it. This means that neither of the activities can
	 * have launchMode="singleInstance" 也就是说当前Activity的launchMode不能设置为 singleInstance
	 *
	 * EASYAPP模式 (SangforAuth.AUTH_MODULE_EASYAPP）： 请忽略。
	 */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        SangforAuth.getInstance().onActivityResult(requestCode, resultCode);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void displayToast(String str) {
//        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    private void doResourceRequest() {
        // 认证结束，可访问资源。
    }

    /**
     * 处理认证，通过传入认证类型（需要的话可以改变该接口传入一个hashmap的参数用户传入认证参数）.
     * <p>
     * 也可以一次性把认证参数设入，这样就如果认证参数全满足的话就可以一次性认证通过，可见下面屏蔽代码
     *
     * @param authType 认证类型
     * @throws SFException
     */
    private void doVpnLogin(int authType) {
        Log.d(TAG, "doVpnLogin authType " + authType);
        boolean ret = false;
        SangforAuth sfAuth = SangforAuth.getInstance();
        /*
         * // session共享登陆：主APP封装时走原认证流程，子APP认证时使用TWFID（SessionId）认证方式 boolean isMainApp = true; //子APP,isMainApp = false; if(!isMainApp){ authType =
		 * IVpnDelegate.AUTH_TYPE_TWFID; }
		 */
        switch (authType) {
            case IVpnDelegate.AUTH_TYPE_CERTIFICATE:
                if (CERT_PATH.isEmpty()) {
                    Toast.makeText(this, "vpn证书路径不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String certPath = CERT_PATH;
                String certPasswd = CERT_PASSWD;
                sfAuth.setLoginParam(IVpnDelegate.CERT_PASSWORD, certPasswd);
                sfAuth.setLoginParam(IVpnDelegate.CERT_P12_FILE_NAME, certPath);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_CERTIFICATE);
                break;
            case IVpnDelegate.AUTH_TYPE_PASSWORD:
                if (USER_NAME.isEmpty()) {
                    Toast.makeText(this, "vpn用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String user = USER_NAME;
                String passwd = USER_PASSWD;
                sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_USERNAME, user);
                sfAuth.setLoginParam(IVpnDelegate.PASSWORD_AUTH_PASSWORD, passwd);

                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_PASSWORD);
                break;
            case IVpnDelegate.AUTH_TYPE_SMS:
                // 进行短信认证
                if (SMS_CODE.isEmpty()) {
                    Toast.makeText(this, "vpn短信验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String smsCode = SMS_CODE;
                sfAuth.setLoginParam(IVpnDelegate.SMS_AUTH_CODE, smsCode);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS);
                break;
            case IVpnDelegate.AUTH_TYPE_SMS1://重新获取短信
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_SMS1);
                break;
            case IVpnDelegate.AUTH_TYPE_TOKEN:
                if (RADIUS_CODE.isEmpty()) {
                    Toast.makeText(this, "vpn 动态口令不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                String token = RADIUS_CODE;
                Log.e(TAG, "TOKEN_AUTH_CODE = " + token);
                sfAuth.setLoginParam(IVpnDelegate.TOKEN_AUTH_CODE, token);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_TOKEN);
                break;
            case IVpnDelegate.AUTH_TYPE_RADIUS:
                if (RADIUS_CODE.isEmpty()) {
                    Toast.makeText(this, "vpn radius 挑战验证码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 进行挑战认证
                String challenge = RADIUS_CODE;
                sfAuth.setLoginParam(IVpnDelegate.CHALLENGE_AUTH_REPLY, challenge);
                ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_RADIUS);
                break;
            case IVpnDelegate.AUTH_TYPE_TWFID:
                // session共享登陆--子APP登陆：子APP使用TWFID登陆。这两个APP就共享VPN隧道，占用同一个授权。
                String twfid = "11438E3C617095C50D28BA337133872730CBAB0D64F98B53F5105221B9D937E8";
                if (twfid != null && !twfid.equals("")) {
                    Log.i(TAG, "do TWFID Auth, TwfId:" + twfid);
                    sfAuth.setLoginParam(IVpnDelegate.TWF_AUTH_TWFID, twfid);
                    ret = sfAuth.vpnLogin(IVpnDelegate.AUTH_TYPE_TWFID);
                } else {
                    Log.e(TAG, "You hasn't written TwfId");
                    Toast.makeText(this, "You hasn't written TwfId", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                Log.w(TAG, "default authType " + authType);
                break;
        }

        if (ret == true) {
            Log.i(TAG, "success to call login method");
        } else {
            Log.i(TAG, "fail to call login method");
        }

    }

    /**
     * 保存VPN配置信息
     *
     * @param context
     * @param ip
     * @param port
     * @param user
     * @param pwd
     * @param mode
     */
    private void saveData(Context context, String ip, String port, String user, String pwd, String mode) {
        SharedPreferences sp = context.getSharedPreferences("sanforlib", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("sanformode", mode);
        editor.putString("sanforip", ip);
        editor.putString("sanforport", port);
        editor.putString("sanforuser", user);
        editor.putString("sanforpwd", pwd);
        editor.commit();
    }

    /**
     * 加载保存的配置信息
     *
     * @param context
     */
    private void loadData(Context context) {
        SharedPreferences sp = context.getSharedPreferences("sanforlib", MODE_PRIVATE);
        String sanformode = sp.getString("sanformode", "");
        String sanforip = sp.getString("sanforip", "");
        String sanforport = sp.getString("sanforport", "");
        String sanforuser = sp.getString("sanforuser", "");
        String sanforpwd = sp.getString("sanforpwd", "");
        if (sanformode.equals("")) {
        } else {
            tv_mode_sanforlib.setText(sanformode);
        }
        if (sanforip.equals("")) {
        } else {
            et_vpnip_sanforlib.setText(sanforip);
        }
        if (sanforport.equals("")) {
        } else {
            et_port_sanforlib.setText(sanforport);
        }
        if (sanforuser.equals("")) {
        } else {
            et_user_sanforlib.setText(sanforuser);
        }
        if (sanforpwd.equals("")) {
        } else {
            et_pwd_sanforlib.setText(sanforpwd);
        }
    }
}

