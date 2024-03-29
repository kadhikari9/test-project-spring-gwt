package com.me.testproject.client.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.Receiver;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.TabData;
import com.gwtplatform.mvp.client.TabDataBasic;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TabInfo;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.TabContentProxyPlace;
import com.me.testproject.client.dataproviders.UsersTableDataProvider;
import com.me.testproject.client.mvp.NameTokens;
import com.me.testproject.client.mvp.TestProjectGinInjector;
import com.me.testproject.client.presenter.ProfilePresenter.ProfileProxy;
import com.me.testproject.client.view.ProfileView;
import com.me.testproject.shared.proxy.UserProxy;
import com.me.testproject.shared.request.UserRequestContext;

public class ProfilePresenter extends Presenter<ProfileView, ProfileProxy> implements ProfileView.ProfileViewUiHandler {

	@Inject
	private UserRequestContext userRequestContext;

	@Inject
	public ProfilePresenter(EventBus eventBus, ProfileView view, ProfileProxy proxy, UsersTableDataProvider dataProvider) {
		super(eventBus, view, proxy);
		getView().setUiHandlers(this);
		dataProvider.addDataDisplay(view.getDisplay());
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.HOME)
	public interface ProfileProxy extends TabContentProxyPlace<ProfilePresenter> {

	}

	@TabInfo(container = HomePagePresenter.class)
	static TabData getTabLabel(TestProjectGinInjector ginjector) {
		return new TabDataBasic(ginjector.getTestProjectConstants().profile(), 0);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, HomePagePresenter.TYPE_SetTabContent, this);
	}

	@Override
	protected void onReveal() {
		super.onReveal();
	}

	@TitleFunction
	public String getTranslatedTitle(TestProjectGinInjector gInjector) {
		return gInjector.getTestProjectConstants().profile();
	}

	private void refreshTable() {
		getView().getDisplay().setVisibleRangeAndClearData(getView().getDisplay().getVisibleRange(), true);
	}

	@Override
	public void updateProfile(UserProxy user) {
		userRequestContext.updateUser(user).fire(new Receiver<Void>() {

			@Override
			public void onSuccess(Void response) {
				refreshTable();
			}
		});
	}

	@Override
	public UserRequestContext getUserRequestContext() {
		return this.userRequestContext;
	}
}
