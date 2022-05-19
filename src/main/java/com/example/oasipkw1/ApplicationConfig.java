package com.example.oasipkw1;

import com.example.oasipkw1.services.ListMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.TimeZone;


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
