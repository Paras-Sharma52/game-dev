package com.example.coinmane.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;


import java.util.ArrayList;
import java.util.Random;

public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	int manState;
	int pause;
	float gravity = 0.2f;
	float velocity = 0;
	int manY = 0;
	Rectangle manRectngle;
	Texture dizzy;
	BitmapFont font;

	int score = 0;
	int gamestate = 0;

	Random random;


	ArrayList<Integer> coinXs = new ArrayList<Integer>();
	ArrayList<Integer> coinYs = new ArrayList<Integer>();
	ArrayList<Rectangle> coinRectangles = new ArrayList<Rectangle>();
	Texture coin;
	int coincount;


	ArrayList<Integer> bombXs = new ArrayList<Integer>();
	ArrayList<Integer> bombYs = new ArrayList<Integer>();
	ArrayList<Rectangle> bombRectangles = new ArrayList<Rectangle>();

	Texture bomb;
	int bombcount;



	@Override
	public void create() {

		batch = new SpriteBatch();
		background = new Texture("bg.png");
		man = new Texture[4];
		man[0] = new Texture("frame-1.png");
		man[1] = new Texture("frame-2.png");
		man[2] = new Texture("frame-3.png");
		man[3] = new Texture("frame-4.png");

		manY = Gdx.graphics.getHeight() / 2;

		coin = new Texture("coin.png");
		bomb = new Texture("bomb.png");
		random = new Random();

		dizzy = new Texture("dizzy-1.png");

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);
	}

	public void makecoin() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		coinYs.add((int) height);
		coinXs.add(Gdx.graphics.getWidth());
	}

	public void makebomb() {
		float height = random.nextFloat() * Gdx.graphics.getHeight();
		bombYs.add((int) height);
		bombXs.add(Gdx.graphics.getWidth());
	}

	@Override
	public void render() {
		batch.begin();
		batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		if (gamestate == 1) {
			//bomb
			if (bombcount < 250) {
				bombcount++;
			} else {
				bombcount = 0;
				makebomb();
			}
			bombRectangles.clear();
			for (int i = 0; i < bombXs.size(); i++) {
				batch.draw(bomb, bombXs.get(i), bombYs.get(i));
				bombXs.set(i, bombXs.get(i) - 8);
				bombRectangles.add(new Rectangle(bombXs.get(i), bombYs.get(i), bomb.getWidth(), bomb.getHeight()));
			}


			//coin
			if (coincount < 100) {
				coincount++;
			} else {
				coincount = 0;
				makecoin();
			}
			coinRectangles.clear();
			for (int i = 0; i < coinXs.size(); i++) {
				batch.draw(coin, coinXs.get(i), coinYs.get(i));
				coinXs.set(i, coinXs.get(i) - 4);
				coinRectangles.add(new Rectangle(coinXs.get(i), coinYs.get(i), coin.getWidth(), coin.getHeight()));
			}
			if (Gdx.input.justTouched()) {
				velocity = -10;
			}

			if (pause < 8) {
				pause++;
			} else {
				pause = 0;
				if (manState < 3) {
					manState++;
				} else {
					manState = 0;
				}
			}

			velocity += gravity;
			manY -= velocity;

			if (manY <= 0) {
				manY = 0;
			}

		} else if (gamestate == 0) {
			// waiting to start
			if (Gdx.input.justTouched()) {
				gamestate = 1;
			}
		} else if (gamestate == 2) {
			//Gameover
			if (Gdx.input.justTouched()) {
				gamestate = 1;
				manY = Gdx.graphics.getHeight() / 2;
				score = 0;
				velocity = 0;
				coinXs.clear();
				coinYs.clear();
				coinRectangles.clear();
				coincount = 0;
				bombXs.clear();
				bombYs.clear();
				bombRectangles.clear();
				bombcount = 0;

			}
		}
			if (gamestate == 2) {
				batch.draw(dizzy, Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
			} else {
				batch.draw(man[manState], Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY);
			}

			manRectngle = new Rectangle(Gdx.graphics.getWidth() / 2 - man[manState].getWidth() / 2, manY, man[manState].getWidth(), man[manState].getHeight());

			for (int i = 0; i < coinRectangles.size(); i++) {
				if (Intersector.overlaps(manRectngle, coinRectangles.get(i))) {
					score++;

					coinRectangles.remove(i);
					coinXs.remove(i);
					coinYs.remove(i);
					break;
				}
			}
			for (int i = 0; i < bombRectangles.size(); i++) {
				if (Intersector.overlaps(manRectngle, bombRectangles.get(i))) {
					Gdx.app.log("bomb!", "collision!");
					gamestate = 2;
				}
			}
			font.draw(batch, String.valueOf(score), 100, 200);

			batch.end();
		}

		@Override
		public void dispose() {
			batch.dispose();

		}


	}




