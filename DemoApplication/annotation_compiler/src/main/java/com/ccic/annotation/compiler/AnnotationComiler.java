package com.ccic.annotation.compiler;


import com.google.auto.service.AutoService;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.io.Writer;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.JavaFileObject;

@AutoService(Process.class)
public class AnnotationComiler extends AbstractProcessor {
    private Filer filer;
    Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filer = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotataions = new LinkedHashSet<>();
        annotataions.add(BindJs.class.getCanonicalName());
        return annotataions;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        TypeSpec.Builder autoCreateJsBuilder = TypeSpec.classBuilder("AutoBindJs")
                .addModifiers(Modifier.PUBLIC);

        ClassName invokeFunction = ClassName.get("com.example.demoapplication", "InvokeFunction");

        ParameterSpec invokeFunctionParam = ParameterSpec.builder(invokeFunction, "invokeFunction").build();

        MethodSpec.Builder addJsBuilder = MethodSpec.methodBuilder("bindJs")
                .addModifiers(Modifier.PUBLIC)
                .returns(void.class)
                .addParameter(invokeFunctionParam);

        for (Element element : roundEnv.getElementsAnnotatedWith(BindJs.class)) {
            //包名
            String packageName = elementUtils.getPackageOf(element).getQualifiedName().toString();
            //类名
            String className = element.getSimpleName().toString();
            ClassName jsFun = ClassName.get(packageName, className);
            addJsBuilder.addStatement("invokeFunction.addJSApi(new $T())", jsFun);
        }
        MethodSpec methodJs = addJsBuilder.build();

        TypeSpec classBuilder = autoCreateJsBuilder.addMethod(methodJs).build();

        JavaFile javaFile = JavaFile.builder("com.ccic.bros.autojs", classBuilder).build();

        try {
            javaFile.writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        creatInterface(roundEnv);

        creatHelper(roundEnv);

        return true;
    }

    private void creatInterface(RoundEnvironment roundEnv) {

        StringBuilder builder = new StringBuilder()
                .append("package com.ccic.bros.autojs.helper;\n\n\r")
                .append("import android.content.Intent;\n\n\r")
                .append("public interface ActivityCallback {\n\r") // open class
                .append("void onSuccess(Intent intent);\n\r")
                .append("void onFailed(Intent intent);\n\r")
                .append("}\n"); // close class

        try { // write the file
            JavaFileObject source = processingEnv.getFiler().createSourceFile("com.ccic.bros.autojs.helper.ActivityCallback");
            Writer writer = source.openWriter();
            writer.write(builder.toString());
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void creatHelper(RoundEnvironment roundEnv) {

        for (Element element : roundEnv.getElementsAnnotatedWith(BindJs.class)) {
            boolean needCallback = element.getAnnotation(BindJs.class).callback();
            if (needCallback) {

                //类名
                String className = element.getSimpleName().toString();
                String newClassName = className + "Helper";
                StringBuilder builder = new StringBuilder()
                        .append("package com.ccic.bros.autojs.helper;\n\n\r")
                        .append("import android.app.Activity;\r")
                        .append("import android.content.Intent;\r")
                        .append("public class " + newClassName + " implements ActivityCallback {\n\r") // open class
                        .append("private ActivityCallback callback;\r")
                        .append("\tprivate static volatile " + newClassName + " instance = new " + newClassName + "();\n\r")
                        .append("\tpublic static " + newClassName + " getInstance() {\n\r") // open method
                        .append("\t\treturn instance;\n\r")
                        .append("\t}\n") // close method

                        .append("\tpublic void startActivity(Activity activity, Class claz, ActivityCallback activityCallback){\r" +
                                "\t\tthis.callback = activityCallback;\r" +
                                "\t\tIntent intent = new Intent(activity,claz);\r" +
                                "\t\tactivity.startActivity(intent);\r" +
                                "\t}\n\r")
                        .append("\t@Override\r")
                        .append("\tpublic void onSuccess(Intent intent) {\r" +
                                "\t\tcallback.onSuccess(intent);\r" +
                                "\t}\r")
                        .append("\t@Override\r")
                        .append("\tpublic void onFailed(Intent intent) {\r" +
                                "\t\tcallback.onFailed(intent);\r" +
                                "\t}\r")


                        .append("}\n"); // close class


                try {
                    JavaFileObject source = processingEnv.getFiler().createSourceFile("com.ccic.bros.autojs.helper." + newClassName);
                    Writer writer = source.openWriter();
                    writer.write(builder.toString());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
