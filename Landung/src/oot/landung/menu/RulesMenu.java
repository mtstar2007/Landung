package oot.landung.menu;

import java.util.ArrayList;
import java.util.List;

import oot.landung.Landung;
import oot.landung.game.Game;

public class RulesMenu extends Menu {

	public RulesMenu(Landung l, Menu parent) {
		super(l, parent, "Regeln");
	}

	@Override
	public void define(Game current) {
		this.addPoint(MenuPoints.backPoint(getLandung(), this, current));
	}

	@Override
	public List<String> getCustomText() {
		List<String> list = new ArrayList<String>();
		list.add("Jeder Spieler hat 9 Spielsteine. In seinem ersten Zug ");
		list.add("setzt jeder Spieler einen Spielstein auf ein freies ");
		list.add("Feld auf dem Spielfeld. In jedem weiteren Zug zieht ");
		list.add("der Spieler einen seiner Steine mindestens zwei Felder ");
		list.add("weit. Man darf in gerader Linie (Diagonal, Vertikal, ");
		list.add("Horizontal) ziehen. Es wdürfen keine Steine übersprungen ");
		list.add("werden und das letzte übersprungene Feld muss leer sein.");
		list.add("Im zweiten Zug gilt für den zweiten Spieler eine ");
		list.add("Sonderregelung. Er darf sich aussuchen, ob er zieht ");
		list.add("oder nur setzt. Ziel des Spiels ist es eine 4er Reihe zu ");
		list.add("bilden oder dem Gegner keine Züge mehr offen lassen.");
		return list;
	}

}