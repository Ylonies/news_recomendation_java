package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.ToString;
import org.example.entity.Article;
import org.example.entity.User;

import java.util.List;

@Data
@ToString
public class ArticleResponse {
    @JsonProperty("title")
    private String name;

    @JsonProperty("description")
    private String description;

    @JsonProperty("date")
    private String date;

    @JsonProperty("link")
    private String link;

    public ArticleResponse(Article article){
        this.name = article.getName();
        this.description = article.getDescription();
        this.date = article.getDate();
        this.link = article.getLink();
    }

    public String json(){
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            return objectMapper.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
