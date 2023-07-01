package com.gdsc.jmt.global.logs;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.lang.reflect.*;
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
            try {
                logger.info(getRequestUrl(joinPoint));
                logger.info("request : " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(params(joinPoint)));
                logger.info("response : " + objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result));
            } catch (Exception e) {
                logger.error("error : " + e.getMessage());
            }
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
    private Map<String, Object> params(JoinPoint joinPoint) throws IllegalAccessException {
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        String[] parameterNames = codeSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            paramsProcess(parameterNames[i], args[i], params);
        }
        return params;
    }

    private void paramsProcess(String parameterName, Object args, Map<String, Object> params) {
        try {
            if(args == null) {
                params.put(parameterName, "null");
            } else if (args instanceof MultipartFile) {
                params.put(parameterName, ((MultipartFile) args).getOriginalFilename());
            } else if(!(args instanceof String || args instanceof  Integer || args instanceof Long)) {
                Field[] fields = args.getClass().getDeclaredFields();
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (!paramsForMultipartFile(field, args, params)) {
                        params.put(field.getName(), field.get(args));
                    }
                }
            } else {
                params.put(parameterName, args);
            }
        }catch (IllegalAccessException ex) {
            logger.error(ex.getMessage());
        }
    }

    private boolean paramsForMultipartFile(Field field, Object args, Map<String, Object> params) throws IllegalAccessException {
        Type type = field.getGenericType();
        // List<MultipartFile>
        if(field.get(args) instanceof List && type instanceof ParameterizedType) {
            for(Type gType : ((ParameterizedType) type).getActualTypeArguments()) {
                if(gType.getTypeName().equals(MultipartFile.class.getTypeName())) {
                    params.put(field.getName(), ((List<?>) field.get(args)).size() + "개의 파일이 들어옴.");
                    return true;
                }
            }
        }

        return false;
    }
}
