package model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 * Model representing a post on a blog.
 * 
 * @author bploetz
 */
@Entity
@Table(name="posts")
public class Post extends Model {

  @Id
  @GeneratedValue
  @Column(name="id")
  private long id;

  @Column(name="title")
  private String title;

  @Column(name="content")
  private String content;

  @Column(name="post_date")
  private Date postDate;

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name="author_id")
  private User author;

  @ManyToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name="blog_id")
  private Blog blog;

  @OneToMany(mappedBy="post", cascade=CascadeType.ALL)
  private Set<Comment> comments = new HashSet<Comment>();


  private Post() { }

  public Post(Blog blog, String title, String content, Date postDate, User author) {
    this.blog = blog;
    this.title = title;
    this.content = content;
    this.postDate = postDate;
    this.author = author;
  }

  public long getId() {
    return id;
  }

  public Blog getBlog() {
    return blog;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Date getPostDate() {
    return postDate;
  }

  public void setPostDate(Date postDate) {
    this.postDate = postDate;
  }

  public User getAuthor() {
    return author;
  }

  public Set<Comment> getComments() {
    return comments;
  }

  public void setComments(Set<Comment> comments) {
    this.comments = comments;
  }

  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof Post)) return false;

    final Post post = (Post) other;

    if (post.getId() != getId()) return false;
    if (!post.getTitle().equals(getTitle())) return false;
    if (!post.getContent().equals(getContent())) return false;
    if (!post.getPostDate().equals(getPostDate())) return false;
    if (!post.getAuthor().equals(getAuthor())) return false;

    return true;
  }

  public int hashCode() {
    return 37 * (int)getId();
  }
}
