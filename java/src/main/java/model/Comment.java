package model;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


/**
 * Model representing a comment on a blog post.
 * 
 * @author bploetz
 */
@Entity
@Table(name="comments")
public class Comment extends Model {

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
  @JoinColumn(name="post_id")
  private Post post;


  private Comment() { }

  public Comment(Post post, String title, String content, Date postDate, User author) {
    this.post = post;
    this.title = title;
    this.content = content;
    this.postDate = postDate;
    this.author = author;
  }

  public long getId() {
    return id;
  }

  public Post getPost() {
    return post;
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

  public boolean equals(Object other) {
    if (this == other) return true;
    if (!(other instanceof Comment)) return false;

    final Comment comment = (Comment) other;

    if (comment.getId() != getId()) return false;
    if (!comment.getTitle().equals(getTitle())) return false;
    if (!comment.getContent().equals(getContent())) return false;
    if (!comment.getPostDate().equals(getPostDate())) return false;
    if (!comment.getAuthor().equals(getAuthor())) return false;

    return true;
  }

  public int hashCode() {
    return 37 * (int)getId();
  }
}
