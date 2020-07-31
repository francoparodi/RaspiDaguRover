package it.dagu.rover.model;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

@Named
@SessionScoped
public class UserLogged extends User implements Serializable
{
  private static final long serialVersionUID = 1L;
  private Boolean authorized;
  
  public UserLogged() {}
  
  public Boolean getAuthorized()
  {
    return authorized;
  }
  
  public void setAuthorized(Boolean authorized) { this.authorized = authorized; }
}
