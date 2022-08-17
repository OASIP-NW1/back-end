package com.example.oasipnw1;

import com.example.oasipnw1.services.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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

}
