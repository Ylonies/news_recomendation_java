package org.example.controller;

import org.example.dto.response.ArticleResponse;
import org.example.entity.Article;
import org.example.entity.Response;
import org.example.service.ArticleService;

import java.util.List;

import static spark.Spark.*;

public class ArticleController extends Controller {
    ArticleService articleService = new ArticleService();
    @Override
    public void startController()  {
        path("/article", () -> {
            get("/last_articles", (request, response) -> {
                Response<List<String>> articlesJson =  articleService.getLastArticles(request);
                response.status(articlesJson.getStatusCode());
                if (articlesJson.isSuccess()){
                    return articlesJson.getData();
                }
                return articlesJson.getMessage();
            });

        });
    }
}
