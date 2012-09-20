package sample.application.fingerpaint;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Bundle;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;

public class FingerPaintActivity extends Activity implements OnTouchListener {
	public Canvas canvas;
	public Paint paint;
	public Path path;
	public Bitmap bitmap;
	public Float x1; //プリミティブをつかわずにラッパークラスでストイックに
	public Float y1; //プリミティブをつかわずにラッパークラスでストイックに
	public Integer w; //プリミティブをつかわずにラッパークラスでストイックに
	public Integer h; //プリミティブをつかわずにラッパークラスでストイックに
	
/*	public boolean onTouch(View v, MotionEvent event){ //実体を持つのでabstractをトル
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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); //親クラスの初期化
		this.setContentView(R.layout.fingerpaint); //レイアウトの指定
		
		ImageView iv = (ImageView) this.findViewById(R.id.imageView1); //findViewByIdはFingerPaintActivityクラスのインスタンスメソッド Activityクラスで定義されている
		Display disp = ((WindowManager)this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(); //Display型のdispというローカル変数 getSystemServiceはFingerPaintActivityクラスのインスタンスメソッド Contextクラスで定義されている WindowManagerでかえってくるのがわかってるから変数をキャストしてる getDefaultDisplayはクラスメソッド 
		/*↑をわかりやすくすると
		WindowManager wm = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
		Display disp = wm.getDefaultDisplay();

		もっとわかりやすくすると
		Object obj = this.getSystemService(Context.WINDOW_SERVICE);
		WindowManager wm = (WindowManager)obj;
		Display disp = wm.getDefaultDisplay();
		
		this.getSystemService(Context.WINDOW_SERVICE)で戻ってくるのはObject型
		WindowManagerという確証があるのでキャスト
		Object型では利用できないgetDefaultDisplay()メソッドをキャストしたインスタンスから実行 
		*/
		this.w = disp.getWidth(); //端末の解像度を取得
		this.h = disp.getHeight(); //端末の解像度を取得
		this.bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888); //BitMapオブジェクトを作成 Bitmapクラスはandroid.graphicsパッケージ Bitmapクラスのクラスメソッド
		this.paint = new Paint();
		this.path = new Path();
		this.canvas = new Canvas(bitmap);
		this.paint.setStrokeWidth(5); //線の太さを指定 単位はdp 1ピクセルの線の場合は0を指定
		this.paint.setStyle(Paint.Style.STROKE);
		this.paint.setStrokeJoin(Paint.Join.ROUND);
		this.paint.setStrokeCap(Paint.Cap.ROUND);
		this.canvas.drawColor(Color.WHITE);
		iv.setImageBitmap(bitmap); //BitMapオブジェクトをImageViewに表示
		iv.setOnTouchListener(this);
	}
	
	public boolean onTouch(View v, MotionEvent event) {
		float x = event.getX(); //タッチ位置の座標を取得
		float y = event.getY(); //タッチ位置の座標を取得
		switch (event.getAction()){ //アクションの種類を取得して分岐処理
		case MotionEvent.ACTION_DOWN:
			this.path.reset();
			this.path.moveTo(x, y);
			this.x1 = x;
			this.y1 = y;
			break;
		case MotionEvent.ACTION_MOVE:
			path.quadTo(x1, y1, x, y);
			this.x1 = x;
			this.y1 = y;
			this.canvas.drawPath(this.path,this.paint);
			this.path.reset();
			this.path.moveTo(x, y);
			break;
		case MotionEvent.ACTION_UP:
			if(x == x1 && y == y1)y1 = y1 + 1;
			this.path.quadTo(x1, y1, x, y);
			this.canvas.drawPath(this.path, this.paint);
			this.path.reset();
			break;
		}
		ImageView iv = (ImageView)this.findViewById(R.id.imageView1);
		iv.setImageBitmap(bitmap); //ImageViewに表示
		return true;
	}
	
	

}
