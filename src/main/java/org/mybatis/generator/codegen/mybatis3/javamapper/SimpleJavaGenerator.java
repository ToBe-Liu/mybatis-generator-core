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
package org.mybatis.generator.codegen.mybatis3.javamapper;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.dom.java.*;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.elements.*;
import org.mybatis.generator.codegen.mybatis3.xmlmapper.SimpleXMLMapperGenerator;
import org.mybatis.generator.config.PlainJavaGeneratorConfiguration;
import org.mybatis.generator.config.PropertyRegistry;

import java.util.ArrayList;
import java.util.List;

import static org.mybatis.generator.internal.util.StringUtility.stringHasValue;
import static org.mybatis.generator.internal.util.messages.Messages.getString;

/**
 * @author Jeff Butler
 * 
 */
public class SimpleJavaGenerator extends AbstractJavaClientGenerator {

    private PlainJavaGeneratorConfiguration plainJavaGeneratorConfiguration;

    public PlainJavaGeneratorConfiguration getPlainJavaGeneratorConfiguration() {
        return plainJavaGeneratorConfiguration;
    }

    public void setPlainJavaGeneratorConfiguration(PlainJavaGeneratorConfiguration plainJavaGeneratorConfiguration) {
        this.plainJavaGeneratorConfiguration = plainJavaGeneratorConfiguration;
    }

    public SimpleJavaGenerator() {
        super(true);
    }

    public SimpleJavaGenerator(boolean requiresMatchedXMLGenerator) {
        super(requiresMatchedXMLGenerator);
    }

    @Override
    public List<CompilationUnit> getCompilationUnits() {
        if ("DAO".equals(plainJavaGeneratorConfiguration.getConfigurationType())
                || "SERVICE".equals(plainJavaGeneratorConfiguration.getConfigurationType())){
            return generatorInterfaceCompilationUnits();
        }  else if ("DAOIMPL".equals(plainJavaGeneratorConfiguration.getConfigurationType())
                || "SERVICEIMPL".equals(plainJavaGeneratorConfiguration.getConfigurationType())){
            return generatorClassCompilationUnits();
        }
        return null;
    }
    public List<CompilationUnit> generatorInterfaceCompilationUnits() {
        progressCallback.startTask(getString("Progress.17",
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();
        FullyQualifiedJavaType type = null;
        if ("DAO".equals(plainJavaGeneratorConfiguration.getConfigurationType())){
            type = new FullyQualifiedJavaType(introspectedTable.getJavaDaoType());
        }  else if ("SERVICE".equals(plainJavaGeneratorConfiguration.getConfigurationType())){
            type = new FullyQualifiedJavaType(introspectedTable.getJavaServiceType());
        }
        Interface interfaze = new Interface(type);
        interfaze.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addInterfaceComment(interfaze,introspectedTable,false);

        String rootInterface = introspectedTable
                .getTableConfigurationProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        if (!stringHasValue(rootInterface)) {
            rootInterface = context.getJavaClientGeneratorConfiguration()
                    .getProperty(PropertyRegistry.ANY_ROOT_INTERFACE);
        }

        if (stringHasValue(rootInterface)) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(
                    rootInterface);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(fqjt);
        }

        List<CompilationUnit> answer = new ArrayList<>();
        if (context.getPlugins().clientGenerated(interfaze, null,
                introspectedTable)) {
            answer.add(interfaze);
        }

        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }

        return answer;
    }
    public List<CompilationUnit> generatorClassCompilationUnits() {
        progressCallback.startTask(getString("Progress.17",
                introspectedTable.getFullyQualifiedTable().toString()));
        CommentGenerator commentGenerator = context.getCommentGenerator();
        FullyQualifiedJavaType type = null;
        FullyQualifiedJavaType rootInterfaceType = null;
        String annotation = "";
        String annotationType = "";
        if ("DAOIMPL".equals(plainJavaGeneratorConfiguration.getConfigurationType())){
            type = new FullyQualifiedJavaType(introspectedTable.getJavaDaoImplType());
            rootInterfaceType = new FullyQualifiedJavaType(introspectedTable.getJavaDaoType());
            annotation = "@Repository";
            annotationType = "org.springframework.stereotype.Repository";
        }  else if ("SERVICEIMPL".equals(plainJavaGeneratorConfiguration.getConfigurationType())){
            type = new FullyQualifiedJavaType(introspectedTable.getJavaServiceImplType());
            rootInterfaceType = new FullyQualifiedJavaType(introspectedTable.getJavaServiceType());
            annotation = "@Service";
            annotationType = "org.springframework.stereotype.Service";
        }
        TopLevelClass topLevelClass = new TopLevelClass(type);
        topLevelClass.setVisibility(JavaVisibility.PUBLIC);
        commentGenerator.addClassComment(topLevelClass,introspectedTable,false);

        //superInterFace
        topLevelClass.addSuperInterface(rootInterfaceType);
        topLevelClass.addImportedType(rootInterfaceType.getFullyQualifiedName());

        //annotation
        topLevelClass.addImportedType(annotationType);
        topLevelClass.addAnnotation(annotation+"(\""+lowerFirst(rootInterfaceType.getShortName())+"\")");

        List<CompilationUnit> answer = new ArrayList<>();
        if (context.getPlugins().clientGenerated(null, topLevelClass,
                introspectedTable)) {
            answer.add(topLevelClass);
        }
        List<CompilationUnit> extraCompilationUnits = getExtraCompilationUnits();
        if (extraCompilationUnits != null) {
            answer.addAll(extraCompilationUnits);
        }

        return answer;
    }

    public List<CompilationUnit> getExtraCompilationUnits() {
        return null;
    }

    @Override
    public AbstractXmlGenerator getMatchedXMLGenerator() {
        return new SimpleXMLMapperGenerator();
    }

    private String lowerFirst(String oldStr){
        if (oldStr == null){
            return null;
        }
        char[]chars = oldStr.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}
