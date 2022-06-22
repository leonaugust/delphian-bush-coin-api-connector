package com.delphian.bush.util;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WebUtil {


    public static RestTemplate getRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();

        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.ALL));

        List<HttpMessageConverter<?>> converters =  new ArrayList<>();
        converters.add(converter);
        restTemplate.setMessageConverters(converters); // List.of(converter)
        return restTemplate;
    }
}
