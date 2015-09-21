package cn.lsz.diysms;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class MainActivity extends Activity implements OnClickListener{

	private EditText phoneNum;
	private EditText passwdNum;
	private EditText varNum;
	private Button   smsCode;
	private Button   okBtn;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		
//		SMSSDK.getSupportedCountries();                             // 获取短信目前支持的国家列表，在监听中返回
//		SMSSDK.getVerificationCode(String country, String phone);   // 请求获取短信验证码，在监听中返回
//		submitVerificationCode(String country, String phone, String code)                         // 校验短信验证码，在监听中返回
//		submitUserInfo(String uid, String nickname, String avatar, String country, String phone)  // 提交用户信息，在监听中返回
	}
	
	private void init() {
//		1. 初始化 SMSSDK
		SMSSDK.initSDK(MainActivity.this, "a662475f4b50", "4e8db5996fc5ea1336b48dc919bc511e");
//		2. 注册回调接口
		SMSSDK.registerEventHandler(eventHandler);
		
		phoneNum  = (EditText) findViewById(R.id.phone_et);
		passwdNum = (EditText) findViewById(R.id.passwd_et);
		varNum    = (EditText) findViewById(R.id.key_et);
		smsCode   = (Button) findViewById(R.id.sms_var_btn);
		okBtn     = (Button) findViewById(R.id.check_var_btn);
		smsCode.setOnClickListener(this);
		okBtn.setOnClickListener(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
//		3. 注销回调接口
		SMSSDK.unregisterEventHandler(eventHandler);
	}
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sms_var_btn:     // 获取验证码
			SMSSDK.getVerificationCode("86", phoneNum.getText().toString().trim());
			break;
		case R.id.check_var_btn:  // 校验
			SMSSDK.submitVerificationCode("86", phoneNum.getText().toString().trim(), varNum.getText().toString());
			break;
		}
	}
	
	
	/**
	 * 注册回调接口
	 */
	EventHandler eventHandler = new EventHandler() {
		
		/*
		 * 解析注册结果
		 */
		@Override
		@SuppressWarnings("unchecked")
		public void afterEvent(int event, final int result, Object data) {

//			 回调完成
			if (result == SMSSDK.RESULT_COMPLETE) {
				
				if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {           // 获取验证码成功
					MainActivity.this.runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							Toast.makeText(MainActivity.this, "获取验证码成功", 0).show();
						}
					});
					
				} else if(event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {  // 校验验证码成功
					MainActivity.this.runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MainActivity.this, "校验验证码成功", 0).show();
						}
					});

				} else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {  // 返回支持发送验证码的国家列表
					Log.v(MainActivity.this.getClass().getSimpleName(), "返回支持发送验证码的国家列表 == ");
				}
				
				
//				HashMap<String, Object> phoneMap = (HashMap<String, Object>) data;
//				String country = (String) phoneMap.get("country"); // 获取国家
//				String phone   = (String) phoneMap.get("phone"); // 获取手机号
//				Log.v(this.getClass().getSimpleName(), "国家 = " + country + "   手机 : " + phone);
				
			} else {
				((Throwable) data).printStackTrace();
			}
		 }
	};
}
