package org.anima.model;

import org.anima.entities.Author;
import org.anima.views.UpdatableView;
/**
 * Model for authentification
 * @author Momo
 *
 */
public class UserModel extends Model {
	
	private Author author;	// saisi dans l'interface
	private String login;// saisi dans l'interface
	private String password;

	@Override
	public void updateView(UpdatableView v) {
		v.update();
		
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}


	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

}
