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
	public Float y1; //�v���g�e�B�u�����킸�Ƀ��b�p�[�N���X�ŃX�g�C�b�N��
	public Integer w;
	public Integer h;
	
	public boolean onTouch(View v, MotionEvent event){ //���̂����̂�abstract���g��
		//return Boolean.valueOf(true);//�Q�ƌ^�ɂ�������ăX�g�C�b�N�ɏ����Ƃ����Ȃ�BOXING �Ƃ��true
		
		return Boolean.valueOf(true);
		
		//return Boolean2, TURE;
	}
	/*
	public enum Boolean { //enum�ŏ����Ƃ����Ȃ�͂�
		TRUE(true),False(false);
		boolean value = true;
		Boolean(boolean bool) {
			this.value = bool;
		}
	}
	*/

}
