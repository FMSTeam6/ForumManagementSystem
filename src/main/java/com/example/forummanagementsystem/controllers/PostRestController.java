package com.example.forummanagementsystem.controllers;
import com.example.forummanagementsystem.exceptions.EntityDuplicateException;
import com.example.forummanagementsystem.exceptions.EntityNotFoundException;
import com.example.forummanagementsystem.exceptions.UnauthorizedOperationException;
import com.example.forummanagementsystem.mappers.PostMapper;
import com.example.forummanagementsystem.models.Post;
import com.example.forummanagementsystem.models.dto.PostDto;
import com.example.forummanagementsystem.models.filters.FilterOptions;
import com.example.forummanagementsystem.services.PostService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.sql.Timestamp;
import java.util.List;


@RestController
@RequestMapping("/post")
public class PostRestController {
    private final PostService postService;
  //  private final PostMapper postMapper;

    @Autowired
    public PostRestController(PostService postService) {
        this.postService = postService;
       // this.postMapper = postMapper;
    }

    @GetMapping
    public List<Post> get(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) Timestamp timestampCreated,
            @RequestParam(required = false) Integer likes,
            @RequestParam(required = false) Integer dislikes,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String sortOrder) {
        FilterOptions filterOptions = new FilterOptions(title, author, timestampCreated, likes, dislikes, sortBy, sortOrder);
        return postService.get(filterOptions);
    }

    @GetMapping("/{id}")
    public Post get(@PathVariable int id) {
        try {
            return postService.getPostById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/search")
    public Post getByTitle(@RequestParam String title) {
        try {
            return postService.getPostByTitle(title);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/search/author")
    public List<Post> getByAuthor(@RequestParam String username) {
        try {
            return postService.getPostByAuthor(username);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
   // @PostMapping
   // public Post create(@RequestHeader HttpHeaders headers, @Valid @RequestBody PostDto postDto) {
      //  try {
          //  User user = authenticationHelper.tryGetUser(headers);
         //   Post post = postMapper.fromDto(postDto);
           // postService.create(post, user);
         //   return post;
      //  } catch (EntityNotFoundException e) {
         //   throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
      //  } catch (EntityDuplicateException e) {
        //    throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
      //  }catch (UnauthorizedOperationException e){
       //     throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
       // }
  //  }
}
