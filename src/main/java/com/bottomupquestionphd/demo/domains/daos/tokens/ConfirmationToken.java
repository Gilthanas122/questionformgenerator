package com.bottomupquestionphd.demo.domains.daos.tokens;

import com.bottomupquestionphd.demo.domains.daos.appuser.AppUser;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
public class ConfirmationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "confirmation_token")
  private String confirmationToken;

  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;

  @OneToOne(targetEntity = AppUser.class, fetch = FetchType.EAGER)
  @JoinColumn(nullable = false, name = "user_id")
  private AppUser appUser;

  public static class Builder {
    private long id;
    private String confirmationToken;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @OneToOne(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    private AppUser appUser;

    public Builder id(long id) {
      this.id = id;
      return this;
    }

    public Builder confirmationToken() {
      this.confirmationToken = UUID.randomUUID().toString();
      return this;
    }

    public Builder createdDate(Date createdDate) {
      this.createdDate = createdDate;
      return this;
    }

    public Builder appUser(AppUser appUser) {
      this.appUser = appUser;
      return this;
    }

    public ConfirmationToken build() {
      ConfirmationToken confirmationToken = new ConfirmationToken();
      confirmationToken.setId(this.id);
      confirmationToken.setConfirmationToken(this.confirmationToken);
      confirmationToken.setCreatedDate(this.createdDate);
      confirmationToken.setAppUser(this.appUser);
      return confirmationToken;
    }
  }

  public ConfirmationToken() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getConfirmationToken() {
    return confirmationToken;
  }

  public void setConfirmationToken(String confirmationToken) {
    this.confirmationToken = confirmationToken;
  }

  public Date getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(Date createdDate) {
    this.createdDate = createdDate;
  }

  public AppUser getAppUser() {
    return appUser;
  }

  public void setAppUser(AppUser appUser) {
    this.appUser = appUser;
  }
}
