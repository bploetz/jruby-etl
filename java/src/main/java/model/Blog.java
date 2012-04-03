package model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Model representing a blog.
 * 
 * @author bploetz
 */
@Entity
@Table(name="blogs")
public class Blog extends Model {

  @Id
  @GeneratedValue
  @Column(name="id")
  private long id;

  @Column(name="title")
  private String title;

  @OneToMany(mappedBy="blog", cascade=CascadeType.ALL)
  private Set<Post> posts = new HashSet<Post>();


  private Blog() { }

  public Blog(String title) {
    this.title = title;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Set<Post> getPosts() {
    return posts;
  }

  public void setPosts(Set<Post> posts) {
    this.posts = posts;
  }

  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof Blog)) return false;

    final Blog blog = (Blog) other;

    if (blog.getId() != getId()) return false;
    if (!blog.getTitle().equals(getTitle())) return false;

    return true;
  }

  public int hashCode() {
    return 37 * (int)getId();
  }
}
