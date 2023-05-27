package nextstep.users.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nextstep.courses.DuplicatedException;
import nextstep.courses.domain.Registration;
import nextstep.courses.domain.Session;
import nextstep.qna.UnAuthorizedException;

import java.time.LocalDateTime;
import java.util.Objects;

public class NsUser {

  public static final GuestNsUser GUEST_USER = new GuestNsUser();

  private Long id;

  private String userId;

  private String password;

  private String name;

  private String email;

  private List<Registration> registrations = new ArrayList<>();

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  public NsUser() {
  }

  public NsUser(Long id, String userId, String password, String name, String email) {
    this(id, userId, password, name, email, LocalDateTime.now(), null);
  }

  public NsUser(Long id, String userId, String password, String name, String email,
      LocalDateTime createdAt, LocalDateTime updatedAt) {
    this.id = id;
    this.userId = userId;
    this.password = password;
    this.name = name;
    this.email = email;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public Long getId() {
    return id;
  }

  public String getUserId() {
    return userId;
  }

  public NsUser setUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public NsUser setPassword(String password) {
    this.password = password;
    return this;
  }

  public String getName() {
    return name;
  }

  public NsUser setName(String name) {
    this.name = name;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public NsUser setEmail(String email) {
    this.email = email;
    return this;
  }

  public void update(NsUser loginUser, NsUser target) {
    if (!matchUserId(loginUser.getUserId())) {
      throw new UnAuthorizedException();
    }

    if (!matchPassword(target.getPassword())) {
      throw new UnAuthorizedException();
    }

    this.name = target.name;
    this.email = target.email;
  }

  public boolean matchUser(NsUser target) {
    return matchUserId(target.getUserId());
  }

  private boolean matchUserId(String userId) {
    return this.userId.equals(userId);
  }

  public boolean matchPassword(String targetPassword) {
    return password.equals(targetPassword);
  }

  public boolean equalsNameAndEmail(NsUser target) {
    if (Objects.isNull(target)) {
      return false;
    }

    return name.equals(target.name) &&
        email.equals(target.email);
  }

  public boolean isGuestUser() {
    return false;
  }

  private static class GuestNsUser extends NsUser {

    @Override
    public boolean isGuestUser() {
      return true;
    }
  }

  public void register(Session session, Registration registration) {
    validateRegister(session);
    registrations.add(registration);
  }

  private void validateRegister(Session session) {
    boolean hasSession = registrations.stream()
        .anyMatch(registration -> registration.hasSession(session));

    if (hasSession) {
      throw new DuplicatedException("강의를 중복으로 신청할 수 없습니다.");
    }
  }

  @Override
  public String toString() {
    return "NsUser{" +
        "id=" + id +
        ", userId='" + userId + '\'' +
        ", name='" + name + '\'' +
        ", email='" + email + '\'' +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
  }
}
