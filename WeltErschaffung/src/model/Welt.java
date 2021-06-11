package model;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import exe.WC_MainView;

public class Welt {

	private HashMap<Integer, HashMap<Integer, WeltFeld>> welt;

	private List<int[]> erzeugreihenfolge;

	private double weltMinHoehe = Double.MAX_VALUE;
	private double weltMaxHoehe = Double.MIN_VALUE;

	private int minIndexX = Integer.MAX_VALUE;
	private int minIndexY = Integer.MAX_VALUE;
	private int maxIndexX = Integer.MIN_VALUE;
	private int maxIndexY = Integer.MIN_VALUE;

	private WC_MainView v;

	public Welt() {

		this.welt = new HashMap<>();
		this.erzeugreihenfolge = new ArrayList<int[]>();

		createWelt(100000);
	}

	public void setView(WC_MainView v) {
		this.v = v;
		v.updateUI();
	}

	public void createWelt(int anzahlFelder) {

		long startCreateWelt = System.currentTimeMillis();

		// Init-Felder
		WeltFeld f = new WeltFeld();
		f.setHoehe(0);
		HashMap<Integer, WeltFeld> zero = new HashMap<>();
		zero.put(0, f);
		welt.put(0, zero);
		erzeugreihenfolge.add(new int[] { 0, 0 });

		int current = 0;

		while (erzeugreihenfolge.size() < anzahlFelder) {
			// Lege die Nachbarn zum aktuellen Weltfeld an.
			createNachbarn(erzeugreihenfolge.get(current)[0], erzeugreihenfolge.get(current)[1]);
			current++;
		}

		applyRandomSpotFilter();

		System.out.println("Dauer CreateWelt: " + (System.currentTimeMillis() - startCreateWelt));

		for (int[] is : erzeugreihenfolge) {
			double h = welt.get(is[0]).get(is[1]).getHoehe();
			if (h < weltMinHoehe) {
				weltMinHoehe = h;
			} else if (h > weltMaxHoehe) {
				weltMaxHoehe = h;
			}
		}

		applyHoehenNormierung();

		if (v != null) {
			v.updateUI();
		}

	}

	private void applyHoehenNormierung() {

		double hdif = weltMaxHoehe - weltMinHoehe;

		for (int[] is : erzeugreihenfolge) {
			double h = welt.get(is[0]).get(is[1]).getHoehe() - weltMinHoehe;
			welt.get(is[0]).get(is[1]).setHoehe(h / hdif);
		}

		weltMaxHoehe = 1.0;
		weltMinHoehe = 0;

	}

	/**
	 * Create Nachbarn zu x y, davon ausgehend, dass welt[x][y] existiert
	 * 
	 * @param x
	 * @param y
	 */
	private void createNachbarn(int x, int y) {

		if (!welt.containsKey(x - 1)) {
			createFeld(x - 1, y);
		}
		if (!welt.get(x - 1).containsKey(y - 1)) {
			createFeld(x - 1, y - 1);

		}
		if (!welt.get(x).containsKey(y - 1)) {
			createFeld(x, y - 1);

		}
		if (!welt.containsKey(x + 1)) {
			createFeld(x + 1, y - 1);

		}
		if (!welt.get(x + 1).containsKey(y)) {
			createFeld(x + 1, y);

		}
		if (!welt.get(x + 1).containsKey(y + 1)) {
			createFeld(x + 1, y + 1);

		}
		if (!welt.get(x).containsKey(y + 1)) {
			createFeld(x, y + 1);

		}
		if (!welt.get(x - 1).containsKey(y + 1)) {
			createFeld(x - 1, y + 1);

		}

	}

	private double calcAvgUmgebungsHoehe(int x, int y) {

		double anzahl = 0;
		double avgSum = 0;

		if (welt.containsKey(x - 1)) {
			if (welt.get(x - 1).containsKey(y - 1)) {
				avgSum += welt.get(x - 1).get(y - 1).getHoehe();
				anzahl++;
			}
			if (welt.get(x - 1).containsKey(y)) {
				avgSum += welt.get(x - 1).get(y).getHoehe();
				anzahl++;
			}
			if (welt.get(x - 1).containsKey(y + 1)) {
				avgSum += welt.get(x - 1).get(y + 1).getHoehe();
				anzahl++;
			}
		}

		if (welt.containsKey(x)) {
			if (welt.get(x).containsKey(y - 1)) {
				avgSum += welt.get(x).get(y - 1).getHoehe();
				anzahl++;
			}
			if (welt.get(x).containsKey(y + 1)) {
				avgSum += welt.get(x).get(y + 1).getHoehe();
				anzahl++;
			}
		}

		if (welt.containsKey(x + 1)) {
			if (welt.get(x + 1).containsKey(y - 1)) {
				avgSum += welt.get(x + 1).get(y - 1).getHoehe();
				anzahl++;
			}
			if (welt.get(x + 1).containsKey(y)) {
				avgSum += welt.get(x + 1).get(y).getHoehe();
				anzahl++;
			}
			if (welt.get(x + 1).containsKey(y + 1)) {
				avgSum += welt.get(x + 1).get(y + 1).getHoehe();
				anzahl++;
			}
		}

		if (anzahl > 0) {
			return avgSum / anzahl;
		} else {
			throw new IllegalArgumentException("Möööp");
		}

	}

	/**
	 * Legt neuen HashMap Eintrag an der Koordinate x y an. Legt Eintrag in
	 * erzeugtreihenfolge ab
	 * 
	 * @param x
	 * @param y
	 */
	public void createFeld(int x, int y) {

		Random r = new Random();
		WeltFeld f = new WeltFeld();
// 		double avgHoehe = calcAvgUmgebungsHoehe(x, y);
//		System.out.println(avgHoehe);
		f.setHoehe(r.nextDouble());

		if (welt.containsKey(x)) {
			if (!welt.get(x).containsKey(y)) {
				welt.get(x).put(y, f);
				erzeugreihenfolge.add(new int[] { x, y });
			}
		} else {
			welt.put(x, new HashMap<>());
			welt.get(x).put(y, f);
			erzeugreihenfolge.add(new int[] { x, y });
		}

		if (x < minIndexX) {
			minIndexX = x;
		} else if (x > maxIndexX) {
			maxIndexX = x;
		}

		if (y < minIndexY) {
			minIndexY = y;
		} else if (y > maxIndexY) {
			maxIndexY = y;
		}

	}

	public void applyRandomSpotFilter() {

		System.out.println(minIndexX);
		System.out.println(maxIndexX);
		System.out.println(minIndexY);
		System.out.println(maxIndexY);

		for (int k = 0; k < 10; k++) {
			for (int i = minIndexX; i < maxIndexX + 1; i++) {
				for (int j = minIndexY; j < maxIndexY + 1; j++) {
					if (welt.containsKey(i)) {
						if (welt.get(i).containsKey(j)) {
							welt.get(i).get(j).setHoehe(calcAvgUmgebungsHoehe(i, j));
						}
					}
				}
			}
		}
	}

	public void draw(Graphics2D g) {

		long startDraw = System.currentTimeMillis();

		int feldgroesse = 3;

		double hdif = weltMaxHoehe - weltMinHoehe;

		System.out.println("Höhendif:" + hdif);

		for (int[] is : erzeugreihenfolge) {
			float hrel = (float) ((welt.get(is[0]).get(is[1]).getHoehe() - weltMinHoehe) / hdif);
			if (hrel < 0.4) {
				g.setColor(Color.BLUE);
			} else {
				g.setColor(new Color(hrel, hrel, hrel));
			}
			g.fillRect(is[0] * feldgroesse, is[1] * feldgroesse, feldgroesse, feldgroesse);
		}

		System.out.println("Dauer PaintWelt: " + (System.currentTimeMillis() - startDraw));

	}

}
