package fr.inra.urgi.gpds.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.reflect.ClassPath;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


/**
 * @author gcornut
 *
 *
 */
public class JacksonFactory {

	/**
	 * Creates a Jackson module with type mapping from a map of abstract/interface to concrete classes
	 */
	public static SimpleModule createTypeMappingModule(Map<Class, Class> typeMap) {
		SimpleModule module = new SimpleModule();
		for (Class type : typeMap.keySet()) {
			module.addAbstractTypeMapping(type, typeMap.get(type));
		}
		return module;
	}

	/**
	 * Create a Jackson ObjectMapper with very permissive configuration
	 */
	public static ObjectMapper createPermissiveMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
		mapper.disable(DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS);
		mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
		mapper.enable(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES);
		return mapper;
	}

	/**
	 * Generate a type mapping between interfaces and their concrete implementation given their respective packages
	 *
	 * @param interfacePackage
	 * @param concreteClassPackage
	 * @param ignoreNoClassesFound
	 */
	@Deprecated
	public static Map<Class, Class> mapInterfacesToClasses(
			String interfacePackage, String concreteClassPackage,
			boolean ignoreNoClassesFound
	) {
		Map<Class, Class> typeMapping = new HashMap<>();

		ClassLoader classLoader = JacksonFactory.class.getClassLoader();
		ClassPath classPath;
		try {
			classPath = ClassPath.from(classLoader);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		Set<ClassPath.ClassInfo> interfacesInfo = classPath.getTopLevelClasses(interfacePackage);
		Set<ClassPath.ClassInfo> classesInfo = classPath.getTopLevelClassesRecursive(concreteClassPackage);

		for (ClassPath.ClassInfo interfaceInfo : interfacesInfo) {
			Class<?> anInterface = interfaceInfo.load();
			if (anInterface.isInterface()) {
				// For each interfaces

				Class<?> concreteClass = null;
				for (ClassPath.ClassInfo classInfo : classesInfo) {
					Class<?> aClass = classInfo.load();

					if (anInterface.isAssignableFrom(aClass)) {
						// Find the concrete class
						concreteClass = aClass;
					}
				}
				if (concreteClass != null) {
					// Add to mapping
					typeMapping.put(anInterface, concreteClass);
				} else if (!ignoreNoClassesFound) {
					throw new RuntimeException(
							"Could not find implementation of interface '"+anInterface.getSimpleName()+"'"
					);
				}
			}
		}

		return typeMapping;
	}

}
