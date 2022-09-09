package com.charlesbabbage.fashionblogapi.serviceImpl;

import com.charlesbabbage.fashionblogapi.dto.PostDTO;
import com.charlesbabbage.fashionblogapi.model.Post;
import com.charlesbabbage.fashionblogapi.model.User;
import com.charlesbabbage.fashionblogapi.pojos.APIResponse;
import com.charlesbabbage.fashionblogapi.repository.PostRepository;
import com.charlesbabbage.fashionblogapi.repository.UserRepository;
import com.charlesbabbage.fashionblogapi.service.PostService;
import com.charlesbabbage.fashionblogapi.utils.ResponseUtil;
import com.charlesbabbage.fashionblogapi.utils.UUID;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

    UserRepository userRepo;
    PostRepository postRepo;

    ResponseUtil responseUtil;

    @Override
    public ResponseEntity<APIResponse> uploadPost(PostDTO postDTO) {
        Post post = new Post();
        User user = userRepo.findById(postDTO.getUser_id()).get();
        String id = UUID.getUniqueId();
        post.setId(id);
        post.setTitle(postDTO.getTitle());
        post.setImage(postDTO.getImage());
        post.setDescription(postDTO.getDescription());
        post.setSlug(makeSlug(post.getTitle(),id));
        post.setUser(user);
        return responseUtil.Okay(postRepo.save(post));
    }

    @Override
    public String makeSlug(String title, String id) {
        String slug = title.replaceAll("\\s","-");
        return (slug + id.substring(0,8)).toLowerCase(Locale.ENGLISH);
    }

    @Override
    public ResponseEntity<APIResponse> getPost(String id){
        if (postRepo.findById(id).isEmpty()){
            return responseUtil.NotFound("Post does not exist");
        } else {
            return  responseUtil.Okay(postRepo.findById(id).get());
        }
    }

    @Override
    public ResponseEntity<APIResponse> deletePost(String id) {
        if (postRepo.findById(id).isEmpty()){
            return responseUtil.NotFound("Post does not exist");
        } else {
            postRepo.deleteById(id);
            return responseUtil.Deleted();
        }
    }
}