package sample.application.fingerpaint;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class FingerPaintActivity extends Activity implements OnTouchListener {
	public Canvas canvas;
	public Paint paint;
	public Path path;
	public Bitmap bitmap;
	public Float x1;
	public Float y1; //プリ身ティブをつかわずにラッパークラスでストイックに
	public Integer w;
	public Integer h;
	
	public boolean onTouch(View v, MotionEvent event){ //実体を持つのでabstractをトル
		//return Boolean.valueOf(true);//参照型にこだわってストイックに書くとこうなるBOXING とりまtrue
		
		return Boolean.valueOf(true);
		
		//return Boolean2, TURE;
	}
	/*
	public enum Boolean { //enumで書くとこうなるはず
		TRUE(true),False(false);
		boolean value = true;
		Boolean(boolean bool) {
			this.value = bool;
		}
	}
	*/

}
