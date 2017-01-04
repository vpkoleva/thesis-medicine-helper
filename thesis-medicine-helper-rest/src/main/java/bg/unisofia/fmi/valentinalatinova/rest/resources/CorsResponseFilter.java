package bg.unisofia.fmi.valentinalatinova.rest.resources;

import java.io.IOException;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

public class CorsResponseFilter implements ContainerResponseFilter {

    private String allowCorsForDomain;

    public CorsResponseFilter(String allowCorsForDomain) {
        this.allowCorsForDomain = allowCorsForDomain;
    }

    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.add("Access-Control-Allow-Origin", allowCorsForDomain);
        headers.add("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT");
        headers.add("Access-Control-Allow-Headers", "X-Requested-With, Content-Type, Authorization");
    }
}