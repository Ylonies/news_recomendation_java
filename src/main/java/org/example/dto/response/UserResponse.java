package org.example.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.entity.User;
import lombok.Data;
import lombok.ToString;




@Data
@ToString
public class UserResponse {
    @JsonProperty("user_name")
    private String name;

    @JsonProperty("password")
    private String password;

    public UserResponse(User user){
        this.name = user.getName();
        this.password = user.getPassword();
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