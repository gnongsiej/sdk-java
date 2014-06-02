package net.authorize.apicore.controller;

import net.authorize.apicore.contract.v1.ANetApiRequest;
import net.authorize.apicore.contract.v1.IsAliveResponse;
import net.authorize.apicore.controller.base.ApiOperationBase;

public class IsAliveController extends ApiOperationBase<ANetApiRequest, IsAliveResponse> {

	public IsAliveController(ANetApiRequest apiRequest) {
		super(apiRequest);
	}
	
	@Override
	protected void validateRequest() {
		//ANetApiRequest request = this.getApiRequest();
		
		//validate required fields		
		//if ( null == request.get()) throw new NullPointerException("XXX cannot be null");
		
		//validate not-required fields		
		//creditCardOne.setCardCode("");
	}

	@Override
	protected Class<IsAliveResponse> getResponseType() {
		return IsAliveResponse.class;
	}
}
