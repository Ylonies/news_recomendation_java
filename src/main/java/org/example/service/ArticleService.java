package org.example.service;

import org.example.dto.response.ArticleResponse;
import org.example.entity.Article;
import org.example.entity.Catalog;
import org.example.entity.Response;
import org.example.entity.User;
import org.example.repository.ArticleRepository;
import org.example.repository.MockArticleRepository;
import spark.Request;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArticleService {
    ArticleRepository articleRepository = new MockArticleRepository();

    private User getCurrentUser (Request request) {
        return request.session().attribute("currentUser ");
    }

    public Response<List<String>> getLastArticles(Request request){
        User currentUser = getCurrentUser(request);
        try{
            List<Article> articles = articleRepository.getLastArticles(currentUser.getId());
            List<String> articleResponses = new ArrayList<>();
            for (Article article: articles){
                articleResponses.add(new ArticleResponse(article).json());
            }
            return new Response<>(200, articleResponses);

        } catch (Exception e) {
            return new Response<>(500, "Internal server error while getting last articles");
        }

    }

}
