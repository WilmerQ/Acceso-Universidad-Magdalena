/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.webService;

import java.util.Set;
import javax.ws.rs.core.Application;

/**
 *
 * @author wilme
 */
@javax.ws.rs.ApplicationPath("webresources")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(co.leafcompany.accesounimagdalena.webService.SincronizarResource.class);
        resources.add(co.leafcompany.accesounimagdalena.webService.ValidacionesResource.class);
        resources.add(co.leafcompany.accesounimagdalena.webService.ValidacionesToImagenesResource.class);
        resources.add(org.jboss.resteasy.api.validation.ResteasyViolationExceptionMapper.class);
        resources.add(org.jboss.resteasy.client.exception.mapper.ApacheHttpClient4ExceptionMapper.class);
        resources.add(org.jboss.resteasy.core.AcceptHeaderByFileSuffixFilter.class);
        resources.add(org.jboss.resteasy.core.AsynchronousDispatcher.class);
        resources.add(org.jboss.resteasy.plugins.interceptors.encoding.AcceptEncodingGZIPFilter.class);
        resources.add(org.jboss.resteasy.plugins.interceptors.encoding.AcceptEncodingGZIPInterceptor.class);
        resources.add(org.jboss.resteasy.plugins.interceptors.encoding.GZIPDecodingInterceptor.class);
        resources.add(org.jboss.resteasy.plugins.interceptors.encoding.GZIPEncodingInterceptor.class);
        resources.add(org.jboss.resteasy.plugins.interceptors.encoding.MessageSanitizerContainerResponseFilter.class);
        resources.add(org.jboss.resteasy.plugins.providers.DataSourceProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.DefaultBooleanWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.DefaultNumberWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.DefaultTextPlain.class);
        resources.add(org.jboss.resteasy.plugins.providers.DocumentProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.FileProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.FormUrlEncodedProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.IIOImageProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.InputStreamProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.JaxrsFormProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.ReaderProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.SerializableProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.SourceProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.StringTextStar.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.CollectionProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.JAXBElementProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlRootElementProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlSeeAlsoProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.JAXBXmlTypeProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.MapProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.jaxb.XmlJAXBContextFinder.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.ListMultipartReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.ListMultipartWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MapMultipartFormDataReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MapMultipartFormDataWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MimeMultipartProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartFormAnnotationReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartFormAnnotationWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartRelatedReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartRelatedWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.MultipartWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.XopWithMultipartRelatedReader.class);
        resources.add(org.jboss.resteasy.plugins.providers.multipart.XopWithMultipartRelatedWriter.class);
        resources.add(org.jboss.resteasy.plugins.providers.sse.SseEventOutputProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.sse.SseEventProvider.class);
        resources.add(org.jboss.resteasy.plugins.providers.sse.SseEventSinkInterceptor.class);
        resources.add(org.jboss.resteasy.plugins.stats.RegistryStatsResource.class);
    }

}
