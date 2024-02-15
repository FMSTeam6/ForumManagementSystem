package com.example.forummanagementsystem.controllers.mvc;

import com.example.forummanagementsystem.models.Comment;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.User;
import com.example.forummanagementsystem.models.filters.SearchUser;
import com.example.forummanagementsystem.services.CommentServices;
import com.example.forummanagementsystem.services.PostService;
import com.example.forummanagementsystem.services.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class HomeMvcController {

    private final UserService userService;
    private final PostService postService;
    private final CommentServices commentServices;
    @Autowired
    public HomeMvcController(UserService userService, PostService postService, CommentServices commentServices) {
        this.userService = userService;
        this.postService = postService;
        this.commentServices = commentServices;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session){
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping
    public String homePageView(Model model){
        List<User> userList = userService.getAll(new SearchUser());
        model.addAttribute("userCount",userList.size());
        List<Post> postsList = postService.getAll();
        model.addAttribute("postCount",postsList.size());
        List<Comment> commentList = commentServices.getAll();
        model.addAttribute("commentsCount",commentList.size());
        return "index";
    }

    @GetMapping("/about")
    public String aboutPageView(){
        return "aboutView";
    }


}
