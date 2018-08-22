/**
 *    Copyright 2006-2018 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.generator.internal;

import static org.mybatis.generator.internal.util.StringUtility.isTrue;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import java.util.Set;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.api.dom.xml.TextElement;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.config.MergeConstants;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.util.StringUtility;

/**
 * @author Jeff Butler
 */
public class DefaultCommentGenerator implements CommentGenerator {

    private Properties properties;

    private boolean suppressDate;

    private boolean suppressAllComments;

    /** If suppressAllComments is true, this option is ignored. */
    private boolean addRemarkComments;

    private SimpleDateFormat dateFormat;

    public DefaultCommentGenerator() {
        super();
        properties = new Properties();
        suppressDate = false;
        suppressAllComments = false;
        addRemarkComments = false;
    }

    @Override
    public void addJavaFileComment(CompilationUnit compilationUnit) {

    }

    /**
     * Adds a suitable comment to warn users that the element was generated, and when it was generated.
     *
     * @param xmlElement
     *            the xml element
     */
    @Override
    public void addComment(XmlElement xmlElement) {
        if (suppressAllComments) {
            return;
        }

    }

    @Override
    public void addRootComment(XmlElement rootElement) {

    }


    @Override
    public void addConfigurationProperties(Properties properties) {
        this.properties.putAll(properties);

        suppressDate = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_DATE));
        
        suppressAllComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_SUPPRESS_ALL_COMMENTS));

        addRemarkComments = isTrue(properties
                .getProperty(PropertyRegistry.COMMENT_GENERATOR_ADD_REMARK_COMMENTS));
        
        String dateFormatString = properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_DATE_FORMAT);
        if (StringUtility.stringHasValue(dateFormatString)) {
            dateFormat = new SimpleDateFormat(dateFormatString);
        }
    }

    /**
     * This method adds the custom javadoc tag for. You may do nothing if you do not wish to include the Javadoc tag -
     * however, if you do not include the Javadoc tag then the Java merge capability of the eclipse plugin will break.
     *
     * @param javaElement
     *            the java element
     * @param markAsDoNotDelete
     *            the mark as do not delete
     */
    protected void addJavadocTag(JavaElement javaElement,
            boolean markAsDoNotDelete) {
        javaElement.addJavaDocLine(" *"); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append(" * "); //$NON-NLS-1$
        sb.append(MergeConstants.NEW_ELEMENT_TAG);
        if (markAsDoNotDelete) {
            sb.append(" do_not_delete_during_merge"); //$NON-NLS-1$
        }
        String s = getDateString();
        if (s != null) {
            sb.append(' ');
            sb.append(s);
        }
        javaElement.addJavaDocLine(sb.toString());
    }

    /**
     * Returns a formated date string to include in the Javadoc tag
     * and XML comments. You may return null if you do not want the date in
     * these documentation elements.
     * 
     * @return a string representing the current timestamp, or null
     */
    protected String getDateString() {
        if (suppressDate) {
            return null;
        } else if (dateFormat != null) {
            return dateFormat.format(new Date());
        } else {
            return new Date().toString();
        }
    }

    @Override
    public void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }

    }

    @Override
    public void addClassComment(InnerClass innerClass,
            IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (suppressAllComments) {
            return;
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        innerClass.addJavaDocLine("/**");
        innerClass.addJavaDocLine(" * "+introspectedTable.getRemarks());
        innerClass.addJavaDocLine(" *@author "+ properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_AUTHOR));
        innerClass.addJavaDocLine(" *@date "+sdf.format(new Date())+"");
        innerClass.addJavaDocLine(" */");
    }

    @Override
    public void addInterfaceComment(InnerInterface innerInterface, IntrospectedTable introspectedTable, boolean markAsDoNotDelete) {
        if (suppressAllComments) {
            return;
        }

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd");
        innerInterface.addJavaDocLine("/**");
        innerInterface.addJavaDocLine(" * "+introspectedTable.getRemarks());
        innerInterface.addJavaDocLine(" *@author "+properties.getProperty(PropertyRegistry.COMMENT_GENERATOR_AUTHOR));
        innerInterface.addJavaDocLine(" *@date "+sdf.format(new Date())+"");
        innerInterface.addJavaDocLine(" */");
    }

    @Override
    public void addModelClassComment(TopLevelClass topLevelClass,
            IntrospectedTable introspectedTable) {
        if (suppressAllComments  || !addRemarkComments) {
            return;
        }

    }

    @Override
    public void addEnumComment(InnerEnum innerEnum,
            IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }


    }

    @Override
    public void addFieldComment(Field field,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("/** ").append(introspectedColumn.getRemarks()).append(" */");
        field.addJavaDocLine(sb.toString());
    }

    @Override
    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }


    }

    @Override
    public void addGeneralMethodComment(Method method,
            IntrospectedTable introspectedTable) {
        if (suppressAllComments) {
            return;
        }


    }

    @Override
    public void addGetterComment(Method method,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }


    }

    @Override
    public void addSetterComment(Method method,
            IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn) {
        if (suppressAllComments) {
            return;
        }

    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
            Set<FullyQualifiedJavaType> imports) {

    }

    @Override
    public void addGeneralMethodAnnotation(Method method, IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
            Set<FullyQualifiedJavaType> imports) {

    }

    @Override
    public void addFieldAnnotation(Field field, IntrospectedTable introspectedTable,
            IntrospectedColumn introspectedColumn, Set<FullyQualifiedJavaType> imports) {

    }

    @Override
    public void addClassAnnotation(InnerClass innerClass, IntrospectedTable introspectedTable,
            Set<FullyQualifiedJavaType> imports) {

    }


}
