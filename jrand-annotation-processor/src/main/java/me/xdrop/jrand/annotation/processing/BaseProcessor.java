package me.xdrop.jrand.annotation.processing;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.PackageElement;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class BaseProcessor extends AbstractProcessor {
    public static String rootPackage = "me.xdrop.jrand";
    private Messager messager;
    private Filer filer;
    private Path outputPathGenerators;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.outputPathGenerators = Paths.get("jrand-core", "src", "generated",
                "java", "me", "xdrop", "jrand", "generators");
    }

    public Messager getMessager() {
        return messager;
    }

    public Filer getFiler() {
        return filer;
    }

    public Path getOutputPathGenerators() {
        return outputPathGenerators;
    }

    protected String getLastPackageName(PackageElement pkg) {
        String[] subpackageParts = pkg.getQualifiedName().toString()
                .split("\\.");
        return subpackageParts[subpackageParts.length - 1];
    }
}