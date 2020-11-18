package org.ak.project.swagger;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Data
@Component
@ConfigurationProperties("jwks-resources")
public class SecuredPaths {

    private List<SecuredPath> securedResources = new ArrayList<>();

    @Data
    public static class SecuredPath{
        private String paths;
    }






}
