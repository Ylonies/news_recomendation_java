package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.User;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;


@Data
@ToString
public class UserResponse {
    @JsonProperty("user_id")
    private UUID id;

    @JsonProperty("user_name")
    private String name;


    public UserResponse(User user){
        this.id = user.getId();
        this.name = user.getName();
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