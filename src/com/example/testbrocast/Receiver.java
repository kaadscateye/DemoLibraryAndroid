package com.example.testbrocast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

public class Receiver extends BroadcastReceiver {

	private static Boolean EventPthread = false;
	public static final String Message = "message";
	private static Object lock = new Object();

	public static final int Key_menu_up = 255;//向上按键
	public static final int Key_menu_down = 256;//向下按键
	public static final int Key_menu_ok = 257;//确定按键
	public static final int Key_menu_exit = 258;//退出按键
	public static final int Key_menu_picture = 259;//抓拍摄像头按键
	public static final int Key_menu_ring = 260;//门铃事件
	public static final int Key_menu_pir = 261;//pir 事件

	@Override
	public void onReceive(Context arg0, Intent arg1) {
		// TODO Auto-generated method stub
		String message = arg1.getExtras().getString(Receiver.Message);
		String action = arg1.getAction();
		LogUtils.e("message :" + message + "action " + action);

		if (!Tools.isNumeric(message)) {
			LogUtils.e("message error ...");
			return;
		}
		int keyCode = Integer.parseInt(message);
		Handler_KeyEvent(keyCode, action);
	}
	//处理底层发送上来的广播按键事件
	private void Handler_KeyEvent(int keyCode, String action) {
		switch (keyCode) {
			case KeyEvent.KEYCODE_POWER:
				if (EventPthread) {
					return;
				}
				LogUtils.e("create pthread run ");
				EventPthread = true;
				MyThread Thread1 = new MyThread();
				Thread1.start();
				break;
			case KeyEvent.KEYCODE_VOLUME_DOWN:
				break;
			case KeyEvent.KEYCODE_VOLUME_UP:
				waitUplock();
				break;
			case Key_menu_up:		//向上按键
				break;
			case Key_menu_down:		//向下按键
				break;
			case Key_menu_ok:		//确定按键
				break;
			case Key_menu_exit:		//退出按键
				break;
			case Key_menu_picture:	//抓拍摄像头按键
				break;
			case Key_menu_ring:		//门铃事件
				break;
			case Key_menu_pir:		//pir 事件
				break;
			}
	}

	class MyThread extends Thread {
		@Override
		public void run() {
			super.run();
			while (EventPthread) {
				synchronized (lock) {
					try {
						lock.wait();
					} catch (Exception e) {
						e.printStackTrace();
						Log.e("Receiver",
								"printStackTrace .........." + e.toString());
					}
				}
				new PlayRing().playMusicFile(PlayRing.strMusic[0], 20);
				LogUtils.e("wait up" + currentThread().getName());
			}
		}
	}

	private void waitUplock() {
		if (lock == null)
			return;
		LogUtils.e("start wake up" + lock.toString());
		synchronized (lock) {
			lock.notify();
		}
	}
}
