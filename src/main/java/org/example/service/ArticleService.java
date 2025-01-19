package org.example.service;

import org.example.dto.response.ArticleResponse;
import org.example.entity.Article;
import org.example.entity.Response;
import org.example.entity.User;
import org.example.repository.ArticleRepository;
import org.example.repository.ArticleRepositoryImpl;
import spark.Request;
import spark.Service;

import java.util.ArrayList;
import java.util.List;

public class ArticleService {
    ArticleRepository articleRepository = new ArticleRepositoryImpl();
    AuthenticationService authService = new AuthenticationService();

    public Response<List<String>> getLastArticles(Request request){
        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }

        User currentUser = authService.getUser(request);
        try{
            List<Article> articles = articleRepository.getNewArticles(currentUser.getId());
            List<String> articleResponses = new ArrayList<>();
            for (Article article: articles){
                articleResponses.add(new ArticleResponse(article).json());
            }
            return new Response<>(articleResponses);

        } catch (Exception e) {
            return new Response<>(500, "Internal server error while getting last articles");
        }

    }

}
