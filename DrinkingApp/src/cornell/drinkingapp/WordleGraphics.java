package cornell.drinkingapp;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.view.View;

public class WordleGraphics extends View {

	Context mContext;
	ArrayList<String[]> nestArrayList = new ArrayList<String[]>();
	String word,title;
	int count;
	ArrayList<Rect> wordRectList = new ArrayList<Rect>();

	public WordleGraphics(Context context, ArrayList<String[]> l,String t) {
		super(context);
		// TODO Auto-generated constructor stu
		mContext = context;
		nestArrayList = l;
		title = t;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		float leftX = canvas.getWidth() / 4;
		float rightX = 3 * canvas.getWidth() / 4;

		Paint titlePaint=new Paint();
		titlePaint.setTextSize(30);
		titlePaint.setTextAlign(Align.CENTER);
		titlePaint.setColor(Color.rgb(0, 153, 204));
		canvas.drawText("Cloud Visualization for "+title, canvas.getWidth()/2, 100, titlePaint);
		
		for (int n = 0; n < 4; n++) {
			word = nestArrayList.get(n)[0];
			count = Integer.valueOf(nestArrayList.get(n)[1]);

			Paint textPainter = new Paint();
			textPainter.setTextSize(10 * count);
			textPainter.setTextAlign(Align.CENTER);
			Rect wordBound = new Rect();
			textPainter.getTextBounds(word, 0, word.length(), wordBound);

			if (n == 0) {
				canvas.drawText(word, canvas.getWidth() / 2,
						canvas.getHeight() / 2, textPainter);
				wordBound.set(
						(canvas.getWidth() / 2) - (wordBound.width() / 2),
						canvas.getHeight() / 2 - (wordBound.height()),
						(canvas.getWidth() / 2) + (wordBound.width() / 2),
						canvas.getHeight() / 2 );
			} else {
				boolean continueSearch = true;
				boolean intersection = false;
				while (continueSearch) {
					intersection = false;
					Random random = new Random();
					int randomNode = random.nextInt(wordRectList.size() - 0) + 0;
					int randomSide = 1;
					// int randomSide = 1;
					Rect parentRect = wordRectList.get(randomNode);
					switch (randomSide) {
					case 1:
						float parentRightSideX = parentRect.centerX()
								+ (parentRect.width() / 2)
								+ (wordBound.width() / 2);
						float parentRightSideY = parentRect.centerY();
						// checks if intersects anything
						wordBound
								.set((int) parentRightSideX
										- (wordBound.width() / 2)+5,
										(int) (parentRightSideY - wordBound
												.height()),
										(int) (parentRightSideX + (wordBound
												.width() / 2)),
										(int) (parentRightSideY));
						intersection = false;
						for (int i = 0; i < wordRectList.size(); i++) {
							if (Rect.intersects(wordBound, wordRectList.get(i))) {
								intersection = true;
							}
						}
						if (!intersection) {

								canvas.drawText(word, parentRightSideX,
										parentRightSideY, textPainter);
								continueSearch = false;
							


						}
						break;

					}


				}
			}

			// saves so we can pull up the corner to slap in more words
			wordRectList.add(wordBound);

		}
		requestLayout();
	}
}
