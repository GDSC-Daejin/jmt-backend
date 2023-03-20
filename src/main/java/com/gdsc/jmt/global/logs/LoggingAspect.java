package com.gdsc.jmt.global.logs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.mutable.Mutable;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
@Aspect
@Component
public class LoggingAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.gdsc.jmt.domain..*Controller*.*(..))")
    public void onRequest() {}

    @Around("onRequest()")
    public Object printLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Object result = null;
        try {
            result = joinPoint.proceed(joinPoint.getArgs());
            return result;
        } finally {
            logger.info(getRequestUrl(joinPoint));
            logger.info("request : " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params(joinPoint)));
            logger.info("response : " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
        }
    }

    private String getRequestUrl(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        String url = Stream.of(GetMapping.class, PutMapping.class, PostMapping.class,
                        PatchMapping.class, DeleteMapping.class, RequestMapping.class)
                .filter(method::isAnnotationPresent)
                .map(mappingClass -> getUrl(method, mappingClass))
                .findFirst().orElse(null);
        return url;
    }

    /* httpMETHOD + requestURI 를 반환 */
    private String getUrl(Method method, Class<? extends Annotation> annotationClass){
        Annotation annotation = method.getAnnotation(annotationClass);
        String[] value;
        String httpMethod = null;
        try {
            value = (String[])annotationClass.getMethod("value").invoke(annotation);
            httpMethod = (annotationClass.getSimpleName().replace("Mapping", "")).toUpperCase();
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            return null;
        }
        return String.format("%s %s %s", httpMethod, method.getName(), value.length > 0 ? value[0] : "") ;
    }

    /* printing request parameter or request body */
    private Map<String, Object> params(JoinPoint joinPoint) {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            if(args[i] instanceof List) {
                List<?> files = (List<?>) args[i];
                if(files.size() > 0 && files.get(0) instanceof MultipartFile) {
                    paramMultiplePartFile(parameterNames[i], (List<MultipartFile>) files, params);
                }
            }
            else {
                params.put(parameterNames[i], args[i]);
            }
        }
        return params;
    }

    private void paramMultiplePartFile(String parameterName , List<MultipartFile> files, Map<String, Object> params) {
        for(int j = 0; j < files.size(); j++) {
            MultipartFile multipartFile = (MultipartFile) files.get(j);
            params.put(parameterName + j, multipartFile.getOriginalFilename());
        }
    }
}
