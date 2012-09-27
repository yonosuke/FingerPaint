package sample.application.fingerpaint;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuInflater;
import android.net.Uri;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.app.AlertDialog;
import android.content.DialogInterface;

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
	
	//@Override
	public boolean onTouch(View v, MotionEvent event){
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
			if(x == this.x1 && y == this.y1)this.y1 = this.y1 + 1;
			this.path.quadTo(x1, y1, x, y);
			this.canvas.drawPath(this.path, this.paint);
			this.path.reset();
			break;
		}
		ImageView iv = (ImageView) this.findViewById(R.id.imageView1);
		iv.setImageBitmap(this.bitmap); //ImageViewに表示
		return true;
	}
	
	void save() {
		SharedPreferences prefs = this.getSharedPreferences("FingerPaintPreferences", MODE_PRIVATE);
		int imageNumber = prefs.getInt("imageNumber", 1);
		File file = null;
		if (externalMedisChecker()) {
			DecimalFormat form = new DecimalFormat("0000");
			String path = Environment.getExternalStorageDirectory() + "/mypaint/";
			File outDir = new File(path);
			if(!outDir.exists());
			do {
				file = new File(path + "img" +form.format(imageNumber) + ".png");
				imageNumber++;
			} while(file.exists());
			if(writeImage(file)) {
				this.scanMedia(file.getPath());
				SharedPreferences.Editor editor = prefs.edit();
				editor.putInt("imageNumber", imageNumber);
				editor.commit();
			}
		}
	}

	private boolean writeImage(File file) { //書き換える
		try {
			FileOutputStream fo = new FileOutputStream(file);
			this.bitmap.compress(CompressFormat.PNG, 100, fo);
			fo.flush();//これに引っかかったらしたのcatch節
			fo.close();//←ひどすぎる Exceptionの意味がない catchしてreturnしてるからfinally通らない可能性あり
		} catch(Exception e) {
			System.out.println(e.getLocalizedMessage());
			return false;
		}
		return true;
	}

	private boolean externalMedisChecker() {
		boolean result = false;
		String status = Environment.getExternalStorageState();
		if(status.equals(Environment.MEDIA_MOUNTED))result = true;
		return result;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater mi = getMenuInflater();
		mi.inflate(R.menu.menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch(item.getItemId()){
		case R.id.menu_save:
			this.save();
			break;
		case R.id.menu_open:
			Intent intent = new Intent(this, FilePicker.class);
			this.startActivityForResult(intent, 0);
			break;
		case R.id.menu_color_change:
			final String[] items = this.getResources().getStringArray(R.array.ColorName);
			final int[] colors = this.getResources().getIntArray(R.array.Color);
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle(R.string.menu_color_change);
			ab.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					paint.setColor(colors[item]);
				}
			}
						);
			ab.show();
			break;
		case R.id.menu_new:
			ab = new AlertDialog.Builder(this);
			ab.setTitle(R.string.menu_new);
			ab.setMessage(R.string.confirm_new);
			ab.setPositiveButton(R.string.button_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
								canvas.drawColor(Color.WHITE);
									((ImageView)findViewById(R.id.imageView1)).setImageBitmap(bitmap);
						}
					}
			);
			ab.setNegativeButton(R.string.button_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
						}
					}
			); 
			ab.show();
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	MediaScannerConnection mc;
	void scanMedia(final String fp){ //finalついてます　インナークラスでかえられたくないから
		this.mc = new MediaScannerConnection(this, new MediaScannerConnection.MediaScannerConnectionClient(){
			public void onScanCompleted(String path, Uri uri){
				disconnect();
			}
			public void onMediaScannerConnected() {
				scanFile(fp);
			}
		});
		this.mc.connect();
	}

	void scanFile(String fp){
		this.mc.scanFile(fp, "image/png");
	}
	
	void disconnect(){
		this.mc.disconnect();
	}
	
	Bitmap loadImage(String path){
		boolean landscape = false;
		Bitmap bm;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int oh = options.outHeight;
		int ow = options.outWidth;
		if(ow > oh){
			landscape=true;
			oh = options.outWidth;
			ow = options.outHeight;
		}
		options.inJustDecodeBounds=false;
		options.inSampleSize = Math.max(ow/w, oh/h);
		bm = BitmapFactory.decodeFile(path, options);
		if(landscape){
			Matrix matrix = new Matrix();
			matrix.setRotate(90.0f);
			bm = Bitmap.createBitmap(bm, 0, 0,
					bm.getWidth(), bm.getHeight(), matrix, false);
		}
		bm = Bitmap.createScaledBitmap(bm,
				(int)(w), (int)(w*(((double)oh)/((double)ow))), false);
		Bitmap offBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888); 
		Canvas offCanvas = new Canvas(offBitmap);
		offCanvas.drawBitmap(bm, 0, (h-bm.getHeight())/2, null);
		bm = offBitmap;
		return bm;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == RESULT_OK){
			this.bitmap = loadImage(data.getStringExtra("fn"));
			this.canvas = new Canvas(this.bitmap);
			ImageView iv = (ImageView)this.findViewById(R.id.imageView1);
			iv.setImageBitmap(this.bitmap);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode == KeyEvent.KEYCODE_BACK){
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle(R.string.title_exit);
			ab.setMessage(R.string.confirm_new);
			ab.setPositiveButton(R.string.button_ok,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
				}
			}
			);
			ab.setNegativeButton(R.string.button_cancel,new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			}
			); 
			ab.show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}


