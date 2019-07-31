package cn.zz.user;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.SimpleMetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

/**
 * @author : zhourui
 * @version: 2019-07-05 15:53
 **/
public class PackageScanTest {

    private static final PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
    private static final SimpleMetadataReaderFactory register = new SimpleMetadataReaderFactory();
    private static final StandardEnvironment environment = new StandardEnvironment();


    /**
     * 根据包路径,获取Class的资源路径
     *
     * @param packagePath
     * @return
     */
    public static String getResourcePath(String packagePath) {
        if (StringUtils.isEmpty(packagePath)) return "";
        String resourcePath = ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX
                + ClassUtils.convertClassNameToResourcePath(environment.resolveRequiredPlaceholders(packagePath))
                + '/' + "**/*.class";
        return resourcePath;
    }

    /**
     * 获取指定路径下的类
     *
     * @param pkgPath
     * @param annoClazz
     * @return
     */
    public static Set<Class> getClazzFromAnnotation(String pkgPath, Class<? extends Annotation> annoClazz) {
        //获取spring的包路径
        String pathPackage = getResourcePath(pkgPath);
        Set<Class> paths = new HashSet<>();
        Resource[] resources;
        try {
            //加载路径
            resources = resolver.getResources(pathPackage);
        } catch (IOException e) {
            //异常处理
            e.printStackTrace();
            return new HashSet<>();
        }
        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];

            MetadataReader metadataReader;
            try {
                //读取资源
                metadataReader = register.getMetadataReader(resource);
            } catch (IOException e) {
                continue;
            }
            //读取资源的注解配置
            AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
            //判断是否包含注解
            if (!annotationMetadata.hasAnnotation(annoClazz.getName())) continue;
            //类信息
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            //类全名
            String className = classMetadata.getClassName();
            try {
                //加载类
                Class<?> clazz = Class.forName(className);
                paths.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return paths;
    }


    public static void main(String[] args) throws IOException {
        //测试
        Set<Class> clazz = getClazzFromAnnotation("cn.zz.user", Mapper.class);

        System.out.println(clazz);

    }
}
