package org.anima.model;

import org.anima.entities.Author;
import org.anima.views.UpdatableView;
/**
 * Model for loading an author
 * @author Marie
 *
 */
public class AuthorModel extends Model {
	
	private Author author;

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


}
