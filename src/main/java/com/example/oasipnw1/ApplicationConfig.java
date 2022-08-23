package com.example.oasipnw1;

import com.example.oasipnw1.services.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;


//class ที่ช่วย map entity ให้เป็น dto
@Configuration
//@EnableConfigurationProperties({
//        FileStorageProperties.class
//})
public class ApplicationConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
    @Bean
    public ListMapper listMapper() {
        return ListMapper.getInstance();
    }

//    Argon2PasswordEncoder
    @Bean
    public Argon2PasswordEncoder argon2PasswordEncoder(){
        return new Argon2PasswordEncoder(16,29,1,16,2);
    }


}
