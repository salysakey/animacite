package org.anima.views;

import org.anima.model.MediaModel;

import android.view.View;
import android.webkit.WebView;
/**
 * Displays the document in full size
 * @author Momo
 *
 */
public class UpdatableMediaFullScreenView implements UpdatableView {

	private MediaModel mediaModel;

	private WebView imageView;

	public UpdatableMediaFullScreenView(MediaModel mediaModel, View view) {


		// updatable image views
		this.imageView = (WebView) view;


		this.mediaModel = mediaModel;
	}

	@Override
	public void update() {
		this.imageView.loadUrl(mediaModel.getMedia().getImageUrl());
		
		//allow zoom
		this.imageView.getSettings().setBuiltInZoomControls(true);



	}
}
