/**
 * Copyright 2005-2009 Noelios Technologies.
 * 
 * The contents of this file are subject to the terms of one of the following
 * open source licenses: LGPL 3.0 or LGPL 2.1 or CDDL 1.0 or CDL 1.0 (the
 * "Licenses"). You can select the license that you prefer but you may not use
 * this file except in compliance with one of these Licenses.
 * 
 * You can obtain a copy of the LGPL 3.0 license at
 * http://www.opensource.org/licenses/lgpl-3.0.html
 * 
 * You can obtain a copy of the LGPL 2.1 license at
 * http://www.opensource.org/licenses/lgpl-2.1.php
 * 
 * You can obtain a copy of the CDDL 1.0 license at
 * http://www.opensource.org/licenses/cddl1.php
 * 
 * You can obtain a copy of the EPL 1.0 license at
 * http://www.opensource.org/licenses/eclipse-1.0.php
 * 
 * See the Licenses for the specific language governing permissions and
 * limitations under the Licenses.
 * 
 * Alternatively, you can obtain a royalty free commercial license with less
 * limitations, transferable or non-transferable, directly at
 * http://www.noelios.com/products/restlet-engine
 * 
 * Restlet is a registered trademark of Noelios Technologies.
 */

package org.restlet.engine.resource;

import java.util.ArrayList;
import java.util.List;

import org.restlet.data.MediaType;
import org.restlet.data.Method;
import org.restlet.representation.Variant;
import org.restlet.service.MetadataService;

/**
 * Descriptor for method annotations.
 * 
 * @author Jerome Louvel
 */
public class AnnotationInfo {
    /** The annotated Java method. */
    private final java.lang.reflect.Method javaMethod;

    /** The matching Restlet method. */
    private final Method restletMethod;

    /** The annotation value. */
    private final String value;

    /**
     * Constructor.
     * 
     * @param restletMethod
     *            The matching Restlet method.
     * @param javaMethod
     *            The annotated Java method.
     * @param value
     *            The annotation value.
     */
    public AnnotationInfo(Method restletMethod,
            java.lang.reflect.Method javaMethod, String value) {
        super();
        this.restletMethod = restletMethod;
        this.javaMethod = javaMethod;
        this.value = value;
    }

    /**
     * Returns the annotated Java method.
     * 
     * @return The annotated Java method.
     */
    public java.lang.reflect.Method getJavaMethod() {
        return javaMethod;
    }

    /**
     * Returns the parameter types of the Java method.
     * 
     * @return The parameter types of the Java method.
     */
    public Class<?>[] getJavaParameterTypes() {
        return getJavaMethod().getParameterTypes();
    }

    /**
     * Returns the return type of the Java method.
     * 
     * @return The return type of the Java method.
     */
    public Class<?> getJavaReturnType() {
        return getJavaMethod().getReturnType();
    }

    /**
     * Returns a list of request variants based on the annotation value.
     * 
     * @param metadataService
     *            The metadata service to use.
     * @return A list of response variants.
     */
    public List<Variant> getRequestVariants(MetadataService metadataService) {
        List<Variant> result = null;
        String value = getValue();

        if (value != null) {
            int colonIndex = value.indexOf(':');

            if (colonIndex == -1) {
                if ((getJavaParameterTypes() == null)
                        || (getJavaParameterTypes().length == 0)) {
                    value = null;
                }
            } else {
                value = getValue().substring(0, colonIndex);
            }

            if (value != null) {
                List<MediaType> mediaTypes = metadataService
                        .getAllMediaTypes(value);

                if (mediaTypes != null) {
                    if (result == null) {
                        result = new ArrayList<Variant>();
                    }

                    for (MediaType mediaType : mediaTypes) {
                        result.add(new Variant(mediaType));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Returns a list of response variants based on the annotation value.
     * 
     * @param metadataService
     *            The metadata service to use.
     * @return A list of response variants.
     */
    public List<Variant> getResponseVariants(MetadataService metadataService) {
        List<Variant> result = null;
        String value = getValue();

        if (value != null) {
            int colonIndex = value.indexOf(':');

            if (colonIndex == -1) {
                if ((getJavaReturnType() == null)
                        || ((getJavaParameterTypes() != null) && (getJavaParameterTypes().length > 0))) {
                    value = null;
                }
            } else {
                value = getValue().substring(colonIndex + 1);
            }

            if (value != null) {
                List<MediaType> mediaTypes = metadataService
                        .getAllMediaTypes(value);

                if (mediaTypes != null) {
                    if (result == null) {
                        result = new ArrayList<Variant>();
                    }

                    for (MediaType mediaType : mediaTypes) {
                        result.add(new Variant(mediaType));
                    }
                }
            }
        }

        return result;
    }

    /**
     * Returns the matching Restlet method.
     * 
     * @return The matching Restlet method.
     */
    public Method getRestletMethod() {
        return restletMethod;
    }

    /**
     * Returns the annotation value.
     * 
     * @return The annotation value.
     */
    public String getValue() {
        return value;
    }
}