package org.anima.model;

import org.anima.entities.Media;
import org.anima.views.UpdatableView;
/**
 * Model for loading a media
 * @author Momo
 *
 */
public class MediaModel extends Model {
	

	private Media media;
	private AuthorModel authorModel;
	
	

	public MediaModel(Integer mediaId) {
		this.authorModel = new AuthorModel();
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

	public AuthorModel getAuthorModel() {
		return authorModel;
	}

	public void setAuthorModel(AuthorModel authorModel) {
		this.authorModel = authorModel;
	}


}
