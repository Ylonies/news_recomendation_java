//package org.example.repository;
//
//import org.example.entity.Article;
//import org.example.utils.DataSourceConfig;
//
//import javax.sql.DataSource;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class ArticleRepositoryImpl implements ArticleRepository {
//  private final DataSource dataSource = DataSourceConfig.getDataSource();
//
//  @Override
//  public List<Article> getLastArticles(UUID userId) {
//    String sql = "SELECT * FROM articles WHERE user_id = ?";
//    List<Article> articles = new ArrayList<>();
//    try (Connection connection = dataSource.getConnection();
//         PreparedStatement statement = connection.prepareStatement(sql));
//  }
//}
