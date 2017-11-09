package org.anima.model;

import java.util.ArrayList;
import java.util.List;
import org.anima.views.UpdatableView;
/**
 * Commun parent of models
 * Each model should extends it
 * @author Momo
 *
 */
public abstract class Model {
	

	List<UpdatableView> views = new ArrayList<UpdatableView>();


	public void update() {
		for (UpdatableView v : views) {
			updateView(v);
		}
	}


	public abstract void updateView(UpdatableView v);

	public void addView(UpdatableView view) {
		views.add(view);
	}


}
