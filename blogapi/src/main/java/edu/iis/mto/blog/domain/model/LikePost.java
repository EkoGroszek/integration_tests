package edu.iis.mto.blog.domain.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;


@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LikePost extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "postId", nullable = false)
    private BlogPost post;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BlogPost getPost() {
        return post;
    }

    public void setPost(BlogPost post) {
        this.post = post;
    }

}
