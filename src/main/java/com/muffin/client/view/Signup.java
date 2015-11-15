package com.muffin.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;
import com.muffin.shared.MuffinSection;
import com.mvu.core.client.BaseActivity;
import com.mvu.core.client.Core;
import com.mvu.core.client.PlaceController;
import com.mvu.core.shared.HasFields;
import com.mvu.core.shared.Place;
import com.mvu.core.shared.entity.Contact;
import com.mvu.core.shared.entity.Credential;
import gwt.material.design.client.ui.MaterialButton;
import gwt.material.design.client.ui.MaterialTextBox;
import gwt.material.design.client.ui.MaterialToast;

/**
 * Created by Van on 11/7/15.
 */
public class Signup implements IsWidget, BaseActivity {
  private Panel rootElement;

  @UiField
  MaterialTextBox email;

  @UiField
  MaterialTextBox password;

  @UiField
  MaterialTextBox name;

  @UiField
  MaterialButton submitBtn;

  @Override
  public Widget asWidget() {
    return rootElement;
  }

  @Override
  public void start(AcceptsOneWidget panel, Place place) {
    panel.setWidget(rootElement);
  }

  interface LoginUiBinder extends UiBinder<Panel, Signup> {
  }

  private static LoginUiBinder ourUiBinder = GWT.create(LoginUiBinder.class);

  public Signup() {
    rootElement = ourUiBinder.createAndBindUi(this);
    submitBtn.addClickHandler(new ClickHandler() {
      @Override
      public void onClick(ClickEvent event) {
        registerUser();
      }
    });
  }

  private void registerUser() {
    PlaceController.placeController().goTo(MuffinSection.search);
    MaterialToast.alert("Now check your mailbox and click confirm");
  }

  public HasFields getValues() {
    HasFields params = Core.CF.createBean();
    params.set(Contact.Email, email.getValue());
    params.set(Credential.Password, password.getValue());
    params.set(Contact.Name, name.getValue());
    return params;
  }
}