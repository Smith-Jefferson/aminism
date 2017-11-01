<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd"
         version="3.1">
    <display-name>ctrip distribute manage</display-name>
        <context-param>
            <param-name>SOA.ServiceRegistry.Url</param-name>
            <param-value>{$RegistryService}</param-value>
        </context-param>
        <context-param>
            <param-name>fx-config-service-url</param-name>
            <param-value>{$FxConfigServiceUrl}</param-value>
        </context-param>
        <context-param>
            <param-name>app-id</param-name>
            <param-value>{$AppID}</param-value>
        </context-param>
        <context-param>
                    <param-name>CFX_DataSource_ServiceUrl</param-name>
                    <param-value>{$CFX_DataSource_ServiceUrl}</param-value>
        </context-param>
        <context-param>
        <param-name>offlineNewLoginDomain</param-name>
        <param-value>{$offlineNewLoginDomain}</param-value>
        </context-param>
        <context-param>
        <param-name>IsBasOfflineFastTestSupport</param-name>
        <param-value>{$IsBasOfflineFastTestSupport}</param-value>
        </context-param>
        <context-param>
            <param-name>sub-env</param-name>
            <param-value>{$RunEnvironment}</param-value>
        </context-param>
        <context-param>
            <param-name>LoggingServer.V2.IP</param-name>
            <param-value>{$LoggingServer.V2.IP}</param-value>
        </context-param>
        <context-param>
            <param-name>LoggingServer.V2.Port</param-name>
            <param-value>{$LoggingServer.V2.Port}</param-value>
        </context-param>
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/rootContext.xml</param-value>
    </context-param>
    <context-param>
        <param-name>com.ctrip.platform.dal.dao.DalWarmUp</param-name>
        <param-value>true</param-value>
    </context-param>

    <listener>
        <listener-class>com.ctrip.platform.dal.dao.helper.DalClientFactoryListener</listener-class>
    </listener>
    <servlet-mapping>
        <servlet-name>default</servlet-name>
        <url-pattern>/resource/*</url-pattern>
    </servlet-mapping>

    <filter>
        <filter-name>encodingFilter</filter-name>
        <filter-class>
            org.springframework.web.filter.CharacterEncodingFilter
        </filter-class>
        <init-param>
            <param-name>encoding</param-name>
            <param-value>UTF-8</param-value>
        </init-param>
        <init-param>
            <param-name>forceEncoding</param-name>
            <param-value>true</param-value>
        </init-param>
    </filter>
    <filter-mapping>
        <filter-name>encodingFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>

    <jsp-config>
        <jsp-property-group>
            <url-pattern>*.jsp</url-pattern>
            <url-pattern>*.jspf</url-pattern>
            <page-encoding>UTF-8</page-encoding>
            <scripting-invalid>true</scripting-invalid>
            <include-prelude>/WEB-INF/jsp/base.jspf</include-prelude>
            <trim-directive-whitespaces>true</trim-directive-whitespaces>
            <default-content-type>text/html</default-content-type>
        </jsp-property-group>
    </jsp-config>
    <session-config>
        <session-timeout>30</session-timeout>
        <cookie-config>
            <http-only>true</http-only>
        </cookie-config>
        <tracking-mode>COOKIE</tracking-mode>
    </session-config>
</web-app>