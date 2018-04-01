/*
 * The Apache Software License, Version 1.1
 *
 *
 * Copyright (c) 2003 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Xerces" and "Apache Software Foundation" must
 *    not be used to endorse or promote products derived from this
 *    software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    nor may "Apache" appear in their name, without prior written
 *    permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation and was
 * originally based on software copyright (c) 2003, International
 * Business Machines, Inc., http://www.apache.org.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

package com.sun.org.apache.xerces.internal.xs;

/**
 * This interface represents the Annotation schema component.
 */
public interface XSAnnotation extends XSObject {
    // TargetType
    /**
     * The object type is <code>org.w3c.dom.Element</code>.
     */
    public static final short W3C_DOM_ELEMENT           = 1;
    /**
     * The object type is <code>org.xml.sax.ContentHandler</code>.
     */
    public static final short SAX_CONTENTHANDLER        = 2;
    /**
     * The object type is <code>org.w3c.dom.Document</code>.
     */
    public static final short W3C_DOM_DOCUMENT          = 3;

    /**
     *  Write contents of the annotation to the specified object. If the 
     * specified <code>target</code> is a DOM object, in-scope namespace 
     * declarations for <code>annotation</code> element are added as 
     * attribute nodes of the serialized <code>annotation</code>, otherwise 
     * the corresponding events for all in-scope namespace declarations are 
     * sent via the specified document handler. 
     * @param target  A target pointer to the annotation target object, i.e. 
     *   <code>org.w3c.dom.Document</code>, <code>org.w3c.dom.Element</code>
     *   , <code>org.xml.sax.ContentHandler</code>. 
     * @param targetType  A target type. 
     * @return  True if the <code>target</code> is a recognized type and 
     *   supported by this implementation, otherwise false. 
     */
    public boolean writeAnnotation(Object target, 
                                   short targetType);

    /**
     * A text representation of the annotation.
     */
    public String getAnnotationString();

}
