package com.sistem.sistema.security;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class SimpleGrantedAuthorityJson  {
    

    @JsonCreator
    public SimpleGrantedAuthorityJson(@JsonProperty("authority") String role){

    }    
}
