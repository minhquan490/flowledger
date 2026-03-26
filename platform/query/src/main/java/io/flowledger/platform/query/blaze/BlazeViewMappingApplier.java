package io.flowledger.platform.query.blaze;

import com.blazebit.persistence.view.Mapping;
import com.blazebit.persistence.view.spi.EntityViewConfiguration;
import io.flowledger.platform.query.QuerySystemException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.StringMemberValue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Applies FlowLedger mapping expressions by injecting Blaze {@link Mapping} annotations
 * before the Blaze metamodel is built.
 */
@Component
@RequiredArgsConstructor
public class BlazeViewMappingApplier {
  private final BlazeMappingResolver resolver;

  /**
   * Resolves FlowLedger mappings and registers a rewritten view with Blaze.
   *
   * @param viewClass the view class to process
   * @param config the Blaze configuration to mutate
   * @return the registered view class (rewritten if mappings were injected)
   * @throws QuerySystemException when the view cannot be rewritten
   */
  public Class<?> apply(Class<?> viewClass, EntityViewConfiguration config) {
    if (viewClass == null) {
      throw new QuerySystemException("viewClass must not be null");
    }
    if (config == null) {
      throw new QuerySystemException("EntityViewConfiguration must not be null");
    }
    Map<String, String> mappings = resolver.resolveMappings(viewClass);
    if (mappings.isEmpty()) {
      config.addEntityView(viewClass);
      return viewClass;
    }
    Class<?> rewritten = injectMappingAnnotations(viewClass, mappings);
    config.addEntityView(rewritten);
    return rewritten;
  }

  /**
   * Injects {@link Mapping} annotations for the resolved mappings.
   *
   * @param viewClass original view interface
   * @param mappings method name to expression
   * @return rewritten class with {@link Mapping} annotations
   * @throws QuerySystemException when bytecode injection fails
   */
  private Class<?> injectMappingAnnotations(Class<?> viewClass, Map<String, String> mappings) {
    try {
      ClassPool pool = ClassPool.getDefault();
      pool.insertClassPath(new javassist.ClassClassPath(viewClass));
      CtClass ctClass = pool.get(viewClass.getName());

      for (Map.Entry<String, String> entry : mappings.entrySet()) {
        CtMethod method = ctClass.getDeclaredMethod(entry.getKey());
        if (hasBlazeMapping(method)) {
          continue;
        }
        ConstPool constPool = ctClass.getClassFile().getConstPool();
        AnnotationsAttribute attr = new AnnotationsAttribute(constPool, AnnotationsAttribute.visibleTag);
        Annotation mapping = new Annotation(Mapping.class.getName(), constPool);
        mapping.addMemberValue("value", new StringMemberValue(entry.getValue(), constPool));
        attr.addAnnotation(mapping);
        method.getMethodInfo().addAttribute(attr);
      }

      byte[] bytecode = ctClass.toBytecode();
      ctClass.detach();
      return defineInChildLoader(viewClass, bytecode);
    } catch (Exception ex) {
      throw new QuerySystemException(
          "Failed to inject @Mapping on " + viewClass.getName() + ": " + ex.getMessage());
    }
  }

  /**
   * Checks whether a method already has a Blaze {@link Mapping}.
   *
   * @param method the Javassist method to inspect
   * @return true if {@link Mapping} is already present
   */
  private boolean hasBlazeMapping(CtMethod method) {
    AnnotationsAttribute attr = (AnnotationsAttribute)
        method.getMethodInfo().getAttribute(AnnotationsAttribute.visibleTag);
    if (attr == null) {
      return false;
    }
    return attr.getAnnotation(Mapping.class.getName()) != null;
  }

  /**
   * Defines the rewritten class in a child class loader to avoid duplicate definition.
   *
   * @param original the original view class
   * @param bytecode the rewritten bytecode
   * @return the defined class
   */
  private Class<?> defineInChildLoader(Class<?> original, byte[] bytecode) {
    ClassLoader loader = new ClassLoader(original.getClassLoader()) {
      Class<?> define() {
        return defineClass(
            original.getName(),
            bytecode,
            0,
            bytecode.length,
            original.getProtectionDomain());
      }
    };
    try {
      Object defined = loader.getClass().getDeclaredMethod("define").invoke(loader);
      if (defined instanceof Class<?> clazz) {
        return clazz;
      }
      throw new QuerySystemException(
          "Failed to define rewritten view class for " + original.getName() + ": unexpected type");
    } catch (Exception ex) {
      throw new QuerySystemException(
          "Failed to define rewritten view class for " + original.getName() + ": " + ex.getMessage());
    }
  }
}
