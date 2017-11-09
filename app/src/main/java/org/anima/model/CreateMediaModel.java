package org.anima.model;

import org.anima.entities.Media;
import org.anima.views.UpdatableView;
/**
 * Model for creating a new media in the database
 * @author Momo
 *
 */
public class CreateMediaModel extends Model {
	

	private Media media;
	private String localMediaUri;
	private String login;
	private String password;
	private Integer idAuthor;
	
	


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

	public String getLocalMediaUri() {
		return localMediaUri;
	}

	public void setLocalMediaUri(String localMediaUri) {
		this.localMediaUri = localMediaUri;
	}
	


	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getIdAuthor() {
		return idAuthor;
	}

	public void setIdAuthor(Integer idAuthor) {
		this.idAuthor = idAuthor;
	}



}
