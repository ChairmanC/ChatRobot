package com.Chairman.robot;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.Chairman.robot.bean.ChatMessage;
import com.Chairman.robot.bean.ChatMessage.Type;
import com.Chairman.robot.utils.GetMessage;
import com.example.android_robot_01.R;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Created by Chairman on 2015/3/23.
 */
public class MainActivity extends Activity
{
	/**
	 * 展示消息的listview
	 */
	private ListView mChatView;
	/**
	 * 文本域
	 */
	private EditText mMsg;
	/**
	 * 存储聊天消息
	 */
	private List<ChatMessage> mDatas = new ArrayList<ChatMessage>();
	/**
	 * 适配器
	 */
	private ChatMessageAdapter mAdapter;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			ChatMessage from = (ChatMessage) msg.obj;
			mDatas.add(from);
			mAdapter.notifyDataSetChanged();
			mChatView.setSelection(mDatas.size() - 1);
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            // Translucent navigation bar
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }


		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_chatting);
		
		initView();

		mAdapter = new ChatMessageAdapter(this, mDatas);
		mChatView.setAdapter(mAdapter);

    }

	private void initView()
	{
		mChatView = (ListView) findViewById(R.id.id_chat_listView);
		mMsg = (EditText) findViewById(R.id.id_chat_msg);
		mDatas.add(new ChatMessage(Type.INPUT, "您好，我是聊天机器人大白，很高兴为您服务！\n\n（回复 菜单 查看功能）"));
	}

	public void sendMessage(View view)
	{
		final String msg = mMsg.getText().toString();
		if (TextUtils.isEmpty(msg))
		{
			alert( "您还没有写入要发送的内容哦 ^__^");
			return;
		}else if(msg.trim().startsWith("时间")){
            try {
                alert("当前的北京时间为 "+getTime());
            } catch (Exception e) {
                alert("获取北京时间超时，请重试！");
            }
            return;
        }else if(msg.trim().startsWith("新闻")){
            open("http://news.baidu.com/");
            alert("正在打开百度新闻...");
            return;
        }else if(msg.trim().startsWith("退出")){
            exit();
        }

		ChatMessage to = new ChatMessage(Type.OUTPUT, msg);
		to.setDate(new Date());
		mDatas.add(to);

		mAdapter.notifyDataSetChanged();
		mChatView.setSelection(mDatas.size() - 1);

		mMsg.setText("");

		// 关闭软键盘
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		// 得到InputMethodManager的实例
		if (imm.isActive())
		{
			// 如果开启
			imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
					InputMethodManager.HIDE_NOT_ALWAYS);
			// 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
		}

		new Thread()
		{
			public void run()
			{
				ChatMessage from = null;
				try
				{
					from = GetMessage.sendMsg(msg);
				} catch (Exception e)
				{
					from = new ChatMessage(Type.INPUT, "连接俺滴服务器超时了，请检查下你手机的网络是否连通哦 +__+");
				}
				Message message = Message.obtain();
				message.obj = from;
				mHandler.sendMessage(message);
			};
		}.start();

	}
    public String getTime()  throws Exception{
        URL url = new URL("http://www.bjtime.cn");
        URLConnection uc = url.openConnection();
        uc.connect();
        long time = uc.getDate();
        Date date = new Date(time);
        String d = new SimpleDateFormat("yyyy-MM-dd HH:mm:sss").format(date);
        return d;
    }
    public void exit(){
        this.finish();
    }
    public void open(String msg){
        startIntent(Intent.ACTION_VIEW,msg);
    }
    public void startIntent(String str,String msg){
        Intent intent = new Intent(str, Uri.parse(msg));
        startActivity(intent);
    }
    public void alert(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }

}
