package lv.celokopa.config;


import lv.celokopa.config.root.ProductionConfiguration;
import lv.celokopa.config.root.RootContextConfig;
import lv.celokopa.config.servlet.ServletContextConfig;
import lv.celokopa.config.root.AppSecurityConfig;
import lv.celokopa.config.root.DevelopmentConfiguration;
import lv.celokopa.config.root.TestConfiguration;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 *
 * Replacement for most of the content of web.xml, sets up the root and the servlet context config.
 *
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{RootContextConfig.class, DevelopmentConfiguration.class, TestConfiguration.class,
                              ProductionConfiguration.class, AppSecurityConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] {ServletContextConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }




}


