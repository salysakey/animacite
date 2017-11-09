package org.anima.model;

import java.util.List;
import java.util.Map;

import org.anima.entities.Media;
import org.anima.views.UpdatableView;

/**
 * Model for loading a list of medias
 * @author Momo
 *
 */
public class MediaListModel extends Model{
	
	private List<Media> mediaList;
	private Map<String,String> criteres;

	public MediaListModel(Map<String, String> criteres) {
		this.criteres=criteres;
	}

	@Override
	public void updateView(UpdatableView v) {
		v.update();
		
	}

	public List<Media> getMediaList() {
		return mediaList;
	}

	public void setMediaList(List<Media> mediaList) {
		this.mediaList = mediaList;
	}

	public Map<String,String> getCriteres() {
		return criteres;
	}

	public void setCriteres(Map<String,String> criteres) {
		this.criteres = criteres;
	}

	
	
	

}
