package org.anima.model;

import org.anima.entities.Media;
import org.anima.views.UpdatableView;
/**
 * Model for loading a media with its full-size document
 * @author Momo
 *
 */
public class MediaFullDocumentModel extends Model {

	private Media media;

	public MediaFullDocumentModel(Integer mediaId) {
		media = new Media();
		media.setId(mediaId);
	}

	public Media getMedia() {
		return media;
	}

	public void setMedia(Media media) {
		this.media = media;
	}


	@Override
	public void updateView(UpdatableView v) {

		v.update();

	}

}
