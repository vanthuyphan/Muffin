package com.muffin.theme.material.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.AnchorElement;
import com.google.gwt.dom.client.ButtonElement;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.LIElement;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.mvu.core.client.JsUtil;
import com.mvu.core.client.NavBarAppearance;
import com.mvu.core.client.PlaceController;
import com.mvu.core.client.style.IconType;
import com.mvu.core.client.style.Styles;
import com.mvu.core.client.widget.DropdownMenu;
import com.mvu.core.client.widget.Navs;
import com.mvu.core.shared.Action;
import com.mvu.core.shared.Attempt;
import com.mvu.core.shared.HasFields;
import com.mvu.core.shared.Place;
import com.mvu.core.shared.SharedConstants;

import static com.mvu.core.client.PlaceController.placeController;
import static com.mvu.core.shared.typekey.CoreSection.core;


public class MNavBarAP extends Navs implements NavBarAppearance{

  protected final HTMLPanel root;
  private MSideNavAP sideNav;

  public void fixedTop() {
    nav.addClassName("navbar-fixed-top");
    RootPanel.get(SharedConstants.UI_CONTENT).getElement().getStyle().setMarginTop(100, Style.Unit.PX);
  }

  public void staticTop(){
    nav.addClassName("navbar-static-top");
    nav.getStyle().setPaddingRight(50, Style.Unit.PX);
  }

  @Override
  public LIElement createItem(String name, String label, final Attempt<String> command) {
    final AnchorElement anchor = createAnchor(name, label);
    JsUtil.addClickHandler(anchor, new Action<String>() {
      @Override
      public void execute(String name) {
        if (command.attempt(name)) {
          selectItem(name);
          //          hide();
        }
      }
    });
    return createItem(name, anchor);
  }

  private AnchorElement createAnchor(String name, String label) {
    final AnchorElement anchor = Document.get().createAnchorElement();
    anchor.setHref("javascript:;");
    if(name != null) {
      anchor.setAttribute(SharedConstants.DATA_NAME, name);
    }
    anchor.addClassName("white-text");
    anchor.setInnerHTML(label);
    return anchor;
  }

  public Element addItem(String label, Place place, boolean right) {
    final LIElement liElement = createItem(label, place);
    liElement.getStyle().setFontSize(17, Style.Unit.PX);
    liElement.addClassName("waves-effect waves-light white-text");
    add(right, liElement);
    return liElement.getFirstChildElement();
  }

  protected LIElement createItem(String html, final Place place) {
    final AnchorElement anchorElement = createAnchor(null, html);
    JsUtil.addClickHandler(anchorElement, new Action<String>() {
      @Override
      public void execute(String values) {
        placeController().goTo(place);
      }
    });
    return createItem(place.toString(), anchorElement);
  }

  public Element addButton(String label, final Place place) {
    final ButtonElement button = createButton(label, place);
    final LIElement liElement = createItem(label, button);
    liElement.addClassName("waves-effect waves-light");
    add(true, liElement);
    return button;
  }

  private ButtonElement createButton(String label, final Place place) {
    final ButtonElement button = Document.get().createPushButtonElement();
    button.setClassName("btn btn-primary navbar-btn waves-effect waves-light");
    button.setInnerText(label);
    JsUtil.addClickHandler(button, new Action<String>() {
      @Override
      public void execute(String type) {
        PlaceController.placeController().goTo(place);
      }
    });
    return button;
  }

  protected void add(boolean right, Element liElement) {
    if (right) {
      rightNav.appendChild(liElement);
    } else {
      leftNav.appendChild(liElement);
    }
  }

  public void inverse() {
    nav.removeClassName("navbar-default");
    nav.addClassName("navbar-inverse");
  }

  @Override
  public void clearRight() {
    rightNav.removeAllChildren();
  }

  @Override
  public BrandBuilder buildBrand() {
    return new BrandBuilderImpl(brand);
  }

  @Override
  public boolean hasSideNav() {
    return sideNav != null;
  }

  @Override
  public SideNavAP sideNav() {
    sideNav = new MSideNavAP();
    RootPanel.get(SharedConstants.UI_SIDE_BAR).add(sideNav.asWidget());
    return sideNav;
  }

  @Override
  public void setBadge(String name, String text) {
    Element child = rightNav.getFirstChildElement();
    while (child != null) {
      if (child.getAttribute(SharedConstants.DATA_NAME).equals(name)) {
        final Element link = child.getFirstChildElement();
        Element badge;
        if (link.getChildCount() < 2) {
          badge = Document.get().createSpanElement();
          badge.setClassName(Styles.BADGE);
          link.appendChild(badge);
        }else{
          badge = link.getFirstChildElement().getNextSiblingElement();
        }
        badge.setInnerText(text);
        return;
      }
      child = child.getNextSiblingElement();
    }
  }

  @Override
  public Element addItem(Enum section, HasFields data) {
    return null;
  }

  public Element addItem(Enum section, boolean right) {
    Place place = new Place(section);
    return addItem(place.getTitle(), place, right);
  }

  public Element addItem(Enum section) {
    return addItem(section, true);
  }

  @Override
  public Element addItem(String[] section) {
    return addItem(section[1], new Place(section), true);
  }

  public LIElement addItem(String name, final Action<String> command, boolean right) {
    final LIElement liElement = createItem(name, command);
    liElement.addClassName("white-text");
    add(right, liElement);
    return liElement;
  }

  public void clearItems() {
    leftNav.removeAllChildren();
    rightNav.removeAllChildren();
  }

  @Override
  public void selectItem(String name) {
    select(name, leftNav);
    select(name, rightNav);
  }

  public DropdownMenu addDropdown(IconType iconType, boolean right) {
    return addDropdown("<i class=\"fa " + iconType.getCssName() + "\"></i>", right);
  }

  public DropdownMenu addDropdown(String label, boolean right) {
    final DropdownMenu dropdown = DropdownMenu.navigationMenu(label);
    add(right, dropdown.getElement());
    return dropdown;
  }

  public LIElement addItem(Element element, boolean right) {
    final LIElement liElement = Document.get().createLIElement();
    if (right) {
      rightNav.appendChild(liElement);
    } else {
      leftNav.appendChild(liElement);
    }
    liElement.appendChild(element);
    return liElement;
  }

  public DropdownMenu addSystemMenu() {
    DropdownMenu systemMenu = addDropdown(IconType.GEAR, true);
    for (final String[] section : new String[][]{core.templates(),
            core.messages(), core.events(), core.eventListeners(), core.libraries(),
            core.root(), core.emailHistory(), core.settings(), core.releaseNotes()}) {
      systemMenu.addItem(section);
    }
    return systemMenu;
  }

  interface NavBarUiBinder extends UiBinder<HTMLPanel, MNavBarAP> {
  }

  private static NavBarUiBinder binder = GWT.create(NavBarUiBinder.class);

  @UiField
  UListElement leftNav;
  @UiField
  protected
  UListElement rightNav;
  @UiField
  protected
  AnchorElement brand;
  @UiField
  DivElement nav;

  public MNavBarAP(RootPanel panel) {
    root = binder.createAndBindUi(this);
    panel.add(root);
  }

  @Override
  public void showSideNav(boolean isShowed) {
    final Style sideBarStyle = RootPanel.get(SharedConstants.UI_SIDE_BAR).getElement().getStyle();
    final Style uiContentStyle = RootPanel.get(SharedConstants.UI_CONTENT).getElement().getStyle();
    sideBarStyle.setDisplay(isShowed ? Style.Display.BLOCK : Style.Display.NONE);
    uiContentStyle.setMarginLeft(isShowed ? 210 : 0, Style.Unit.PX);
    uiContentStyle.setOverflow(isShowed ? Style.Overflow.AUTO : Style.Overflow.VISIBLE);
  }

}