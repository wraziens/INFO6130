package com.example.drinkingapp;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.view.View;

public class WordleGraphics extends View {

	Context mContext;
	ArrayList<String[]> nestArrayList = new ArrayList<String[]>();
	String word;
	int count;
	ArrayList<Rect> wordRectList = new ArrayList<Rect>();

	public WordleGraphics(Context context, ArrayList<String[]> l) {
		super(context);
		// TODO Auto-generated constructor stu
		mContext = context;
		nestArrayList = l;

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		float leftX = canvas.getWidth() / 4;
		float rightX = 3 * canvas.getWidth() / 4;

		for (int n = 0; n < nestArrayList.size(); n++) {
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
					int randomSide = random.nextInt(5 - 1) + 1;;
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
										- (wordBound.width() / 2),
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
							if (wordBound.left > 0
									&& wordBound.right < canvas.getWidth()
									&& wordBound.top > 0
									&& wordBound.bottom < canvas.getHeight()) {
								canvas.drawText(word, parentRightSideX,
										parentRightSideY, textPainter);
								continueSearch = false;
							}


						}
						break;
					case 2:
						float parentBottomSideX = parentRect.exactCenterX();
						float parentBottomSideY = parentRect.exactCenterY()
								+ (parentRect.height() / 2);
						// checks if intersects anything
						wordBound
								.set((int) parentBottomSideX
										- (wordBound.width() / 2),
										(int) (parentBottomSideY),
										(int) (parentBottomSideX + (wordBound
												.width() / 2)),
										(int) (parentBottomSideY + wordBound
												.height()));
						intersection = false;
						for (int i = 0; i < wordRectList.size(); i++) {
							if (Rect.intersects(wordBound, wordRectList.get(i))) {
								intersection = true;
							}
						}
						if (!intersection) {
							if (wordBound.left > 0
									&& wordBound.right < canvas.getWidth()
									&& wordBound.top > 0
									&& wordBound.bottom < canvas.getHeight()) {
								canvas.drawText(word, parentBottomSideX,
										parentBottomSideY, textPainter);
								continueSearch = false;
							}


						}
						break;
					case 3:
						float parentLeftSideX = parentRect.exactCenterX()
								- (parentRect.width() / 2)
								- (wordBound.width() / 2);
						float parentLeftSideY = parentRect.exactCenterY();
						// checks if intersects anything
						wordBound
								.set((int) parentLeftSideX
										- (wordBound.width() / 2),
										(int) (parentLeftSideY - wordBound
												.height()),
										(int) (parentLeftSideX + (wordBound
												.width() / 2)),
										(int) (parentLeftSideY));
						intersection = false;
						for (int i = 0; i < wordRectList.size(); i++) {
							if (Rect.intersects(wordBound, wordRectList.get(i))) {
								intersection = true;
							}
						}
						if (!intersection) {
							if (wordBound.left > 0
									&& wordBound.right < canvas.getWidth()
									&& wordBound.top > 0
									&& wordBound.bottom < canvas.getHeight()) {
								canvas.drawText(word, parentLeftSideX,
										parentLeftSideY, textPainter);
								continueSearch = false;
							}


						}
						break;
					case 4:
						float parentTopSideX = parentRect.exactCenterX();
						float parentTopSideY = parentRect.exactCenterY()
								- (parentRect.height() / 2);
						// checks if intersects anything
						wordBound
								.set((int) parentTopSideX
										- (wordBound.width() / 2),
										(int) (parentTopSideY - wordBound
												.height()),
										(int) (parentTopSideX + (wordBound
												.width() / 2)),
										(int) (parentTopSideY ));
						intersection = false;
						for (int i = 0; i < wordRectList.size(); i++) {
							if (Rect.intersects(wordBound, wordRectList.get(i))) {
								intersection = true;
							}
						}
						if (!intersection) {
							if (wordBound.left > 0
									&& wordBound.right < canvas.getWidth()
									&& wordBound.top > 0
									&& wordBound.bottom < canvas.getHeight()) {
								canvas.drawText(word, parentTopSideX,
										parentTopSideY, textPainter);
								continueSearch = false;
							}


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