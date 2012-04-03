package model;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


/**
 * Model representing a user.
 * 
 * @author bploetz
 */
@Entity
@Table(name="users",
       uniqueConstraints=@UniqueConstraint(name="username", columnNames={"username"}))
public class User extends Model {

  @Id
  @GeneratedValue
  @Column(name="id")
  private long id;

  @Column(name="username")
  private String username;

  @OneToMany(mappedBy="author", cascade=CascadeType.ALL)
  private Set<Comment> comments;

  @OneToMany(mappedBy="author", cascade=CascadeType.ALL)
  private Set<Post> posts;


  private User() { }

  public User(String username) {
    this.username = username;
  }

  public long getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(Set<Post> posts) {
    this.posts = posts;
  }

  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof User)) return false;

    final User user = (User) other;

    if (user.getId() != getId()) return false;
    if (!user.getUsername().equals(getUsername())) return false;

    return true;
  }

  public int hashCode() {
    return 37 * (int)getId();
  }
}
