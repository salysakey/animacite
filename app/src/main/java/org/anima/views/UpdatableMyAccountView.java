package org.anima.views;

import org.anima.model.AuthorModel;

import android.widget.TextView;
/**
 * Displays the "Mon espace" screen
 * @author Momo
 *
 */
public class UpdatableMyAccountView implements UpdatableView {

	private AuthorModel authorModel;

	private TextView authorNameView;
	private UpdatableImageView avatarView;

	public UpdatableMyAccountView(TextView authorNameView,
			UpdatableImageView avatarView, AuthorModel authorModel) {
		this.authorNameView = authorNameView;
		this.avatarView = avatarView;
		this.authorModel = authorModel;
	}

	public AuthorModel getAuthorModel() {
		return authorModel;
	}

	public void setAuthorModel(AuthorModel authorModel) {
		this.authorModel = authorModel;
	}

	public TextView getAuthorView() {
		return authorNameView;
	}

	public void setAuthorView(TextView authorView) {
		this.authorNameView = authorView;
	}

	public UpdatableImageView getAvatarView() {
		return avatarView;
	}

	public void setAvatarView(UpdatableImageView avatarView) {
		this.avatarView = avatarView;
	}

	@Override
	public void update() {
		if (authorModel.getAuthor().getAvatarUrl() != null
				&& !authorModel.getAuthor().getAvatarUrl().trim().equals("")) {
			this.avatarView.loadImage(authorModel.getAuthor().getAvatarUrl());
		}

		authorNameView.setText(authorModel.getAuthor().getName());
	}

}
