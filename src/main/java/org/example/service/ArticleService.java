package org.example.service;

import org.example.dto.response.ArticleResponse;
import org.example.entity.*;
import org.example.repository.Implementation.CatalogRepositoryImpl;
import org.example.repository.Implementation.WebsiteRepositoryImpl;
import org.example.repository.interfaces.ArticleRepository;
import org.example.repository.Implementation.ArticleRepositoryImpl;
import org.example.repository.interfaces.CatalogRepository;
import org.example.repository.interfaces.WebsiteRepository;
import spark.Request;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArticleService {
    ArticleRepository articleRepository = new ArticleRepositoryImpl();
    AuthenticationService authService = new AuthenticationService();
    CatalogRepository catalogRepository = new CatalogRepositoryImpl();
    WebsiteRepository websiteRepository = new WebsiteRepositoryImpl();

    public Response<List<String>> getLastArticles(Request request){
        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }

        User currentUser = authService.getUser(request);
        UUID userId = currentUser.getId();
        try{
            List<Catalog> catalogs = catalogRepository.getUserCatalogs(userId);
            List<Website> websites = websiteRepository.getUserWebsites(userId);
            List<Article> articles = articleRepository.getNewArticles(userId, catalogs, websites);
            articleRepository.updateUserLastRequestTime(currentUser.getId());

            List<String> articleResponses = new ArrayList<>();
            for (Article article: articles){
                articleResponses.add(new ArticleResponse(article).json());
            }
            return new Response<>(articleResponses);

        } catch (Exception e) {
            return new Response<>(500, "Internal server error while getting last articles");
        }
    }

    public Response<Article> saveArticle(Request request){

        if (!authService.isAuthenticated(request)) {
            return new Response<>(401, "Unauthorized");
        }

        UUID id = UUID.randomUUID();
        String name = request.queryParams("name");
        String description = request.queryParams("description");
        String link = request.queryParams("link");

        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formattedDate = today.format(formatter);

        Article article = new Article(id, name, description, link, formattedDate);
        articleRepository.saveArticle(article);
        try{
            return new Response<>(article);

        } catch (Exception e) {
            return new Response<>(500, "Internal server error while getting last articles");
        }
    }
}
