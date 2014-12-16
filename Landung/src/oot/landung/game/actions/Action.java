package oot.landung.game.actions;

import java.util.ArrayList;
import java.util.List;

import oot.landung.game.board.Board;
import oot.landung.game.board.Stone;
import oot.landung.game.player.Player;
import oot.landung.game.utils.Utils;
import oot.landung.game.utils.Vector;

/**
 * Eine Klasse zur Darstellung einer Aktion. Eine Aktion ist ein Zug. Sie
 * besteht grunds�tzlich aus 5 Eigenschaften. Sie hat einen Akteur, ein Feld von
 * dem gezogen wird, ein Feld zu dem gezogen wird, ein Feld auf das gesetzt wird
 * und die sudo Eingeschaft, die Regeln missachten kann. Die Aktion bildet das
 * Herzst�ck unseres Spiels, da die Aktion auch bestimmt was ein g�ltiger und
 * m�glicher Zug ist. Jede Spielregel ist in der Aktionsklasse oder ihren
 * Kindklassen definiert. Die Logik l�sst sich somit stark kapseln.
 * 
 * @author Landung
 *
 */
public abstract class Action {

	private final Player actor;
	private final Vector<Integer> moveFrom;
	private final Vector<Integer> moveTo;
	private final Vector<Integer> setTo;
	private final boolean sudo;

	/**
	 * Erstellt eine neue Instanz einer Aktion.
	 * 
	 * @param sudo
	 *            true, wenn Regeln missachtet werden sollen
	 * @param actor
	 *            der Spieler, der die Aktion ausf�hrt
	 * @param moveFrom
	 *            das Feld von dem gezogen werden soll
	 * @param moveTo
	 *            das Feld zu dem gezogen werden soll
	 * @param setTo
	 *            das Feld, auf das gesetzt wird
	 */
	public Action(boolean sudo, Player actor, Vector<Integer> moveFrom,
			Vector<Integer> moveTo, Vector<Integer> setTo) {
		this.actor = actor;
		this.moveFrom = moveFrom;
		this.moveTo = moveTo;
		this.setTo = setTo;
		this.sudo = sudo;
	}

	/**
	 * Gibt an, ob diese Aktion g�ltig ist. Beachtet Spielfeldgrenzen und wenn
	 * es keine sudo-Aktion ist auch die Spielregeln.
	 * 
	 * @param board
	 *            das Spielbrett
	 * @param turn
	 *            der aktuelle Spielzug
	 * @return true, wenn die Aktion g�ltig ist
	 */
	public boolean isActionValid(Board board, int turn, boolean print) {

		Player player = getActor();

		if (!getSudo()) {

			// Z�ge 1 bis 4
			if (turn == 0 || turn == 1) {
				if (!(this instanceof SetAction)) {
					if (print)
						player.notifyUnvalidMove("Im ersten Zug darf man nur setzen.");
					return false;
				}
			} else if (turn == 2 || turn > 3) {
				if (!(this instanceof MoveAndSetAction)) {
					if (player.getStones() > 0
							&& !(this instanceof RemoveAction)) {
						if (print)
							player.notifyUnvalidMove("In diesem Zug darf man nur bewegen und setzen.");
						return false;
					}
				}
			} else if (turn == 3) {
				if (!((this instanceof MoveAndSetAction) || (this instanceof SetAction))) {
					if (print)
						player.notifyUnvalidMove("In diesem Zug darf man nur setzen oder bewegen und setzen.");
					return false;
				}
			}

			// Spielfeldgrenzen
			List<Vector<Integer>> vectors = new ArrayList<Vector<Integer>>();
			vectors.add(getMoveFrom());
			vectors.add(getMoveTo());
			vectors.add(getSetTo());
			
			for (Vector<Integer> v : vectors) {
				if (v != null) {
					if (v.getX() < 0 || v.getX() >= Board.SIZE) {
						if (print)
							player.notifyUnvalidMove("Die X-Koordinaten m�ssen zwischen a und e liegen.");
						return false;
					}
					if (v.getY() < 0 || v.getY() >= Board.SIZE) {
						if (print)
							player.notifyUnvalidMove("Die Y-Koordinaten m�ssen zwischen 1 und 5 liegen.");
						return false;
					}
				}
			}
		}

		return true;
	}

	/**
	 * F�hrt diese Aktion auf einem Spielfeld aus.
	 * 
	 * @param board
	 *            das Spielfeld
	 */
	public abstract void execute(Board board);

	/**
	 * Gibt den Akteur zur�ck
	 * 
	 * @return der Aktuer
	 */
	public Player getActor() {
		return actor;
	}

	/**
	 * Gibt den Vektor zur�ck, der die Koordinaten des Feldes besitzt, von dem
	 * gezogen wird.
	 * 
	 * @return Vektor
	 */
	public Vector<Integer> getMoveFrom() {
		return moveFrom;
	}

	/**
	 * Gibt den Vektor zur�ck, der die Koordinaten des Feldes besitzt, auf das
	 * gezogen wird.
	 * 
	 * @return Vektor
	 */
	public Vector<Integer> getMoveTo() {
		return moveTo;
	}

	/**
	 * Gibt den Vektor zur�ck, der die Koordinaten des Feldes besitzt, auf das
	 * gesetzt wird.
	 * 
	 * @return Vektor
	 */
	public Vector<Integer> getSetTo() {
		return setTo;
	}

	/**
	 * Gibt an ob es eine sudo-Aktion ist
	 * 
	 * @return true, wenn sudo
	 */
	public boolean getSudo() {
		return sudo;
	}

	/**
	 * Stringrepr�sentation der Aktion
	 */
	public String toString() {
		String s = "";
		s += actor.getName() + ";";
		s += Utils.convertInternalVectorToExternalString(moveFrom) + "->";
		s += Utils.convertInternalVectorToExternalString(moveTo) + ";set:";
		s += Utils.convertInternalVectorToExternalString(setTo) + ";";
		return s;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actor == null) ? 0 : actor.hashCode());
		result = prime * result
				+ ((moveFrom == null) ? 0 : moveFrom.hashCode());
		result = prime * result + ((moveTo == null) ? 0 : moveTo.hashCode());
		result = prime * result + ((setTo == null) ? 0 : setTo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Action other = (Action) obj;
		if (actor == null) {
			if (other.actor != null)
				return false;
		} else if (!actor.equals(other.actor))
			return false;
		if (moveFrom == null) {
			if (other.moveFrom != null)
				return false;
		} else if (!moveFrom.equals(other.moveFrom))
			return false;
		if (moveTo == null) {
			if (other.moveTo != null)
				return false;
		} else if (!moveTo.equals(other.moveTo))
			return false;
		if (setTo == null) {
			if (other.setTo != null)
				return false;
		} else if (!setTo.equals(other.setTo))
			return false;
		return true;
	}

}
