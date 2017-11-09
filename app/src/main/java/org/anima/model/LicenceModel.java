package org.anima.model;

import java.util.List;

import org.anima.entities.Licence;
import org.anima.views.UpdatableView;
/**
 * Model for the license list
 * @author Momo
 *
 */
public class LicenceModel extends Model {
	
	private List<Licence> licences;
	

	@Override
	public void updateView(UpdatableView v) {
		v.update();
		
	}


	
	public List<Licence> getLicences(){
		return licences;
	}


	public void setLicences(List<Licence> licences) {
		this.licences = licences;
	}


}
