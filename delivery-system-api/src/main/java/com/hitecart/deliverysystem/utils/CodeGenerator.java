package com.hitecart.deliverysystem.utils;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CodeGenerator {
    private static String projectRoot;
    private static final String BASE_PACKAGE = "com.hitecart.deliverysystem";
    private static final String MODEL_PATH = "src/main/java/com/hitecart/deliverysystem/model";//com.hitecart.deliverysystem.model
    private static final String REPOSITORY_PATH = "src/main/java/com/hitecart/deliverysystem/repository";
    private static final String DTO_PATH = "src/main/java/com/hitecart/deliverysystem/dto";
    private static final String SERVICE_PATH = "src/main/java/com/hitecart/deliverysystem/services";
    private static final String CONTROLLER_PATH = "src/main/java/com/hitecart/deliverysystem/controllers";

    public static void main(String[] args) {

        projectRoot = System.getProperty("user.dir")+"\\delivery-system-api\\";
        String jsonFilePath = "diagramm/uml_weekly_planning.json";//Paths.get(projectRoot, "src/main/resources/diagramm/uml_weekly_planning.json").toString();;//"diagramm/uml_weekly_planning.json";



    try {
        System.out.println("MODEL_PATH directory: " + Paths.get(projectRoot,MODEL_PATH));
        System.out.println("Current working directory: " + System.getProperty("user.dir"));
        System.out.println("Resolved file path: " + getFileFromResources("diagramm/uml_weekly_planning.json").getAbsolutePath());

            File jsonFile = getFileFromResources(jsonFilePath);
            // Parse the JSON file
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonFile);//(new File(jsonFilePath));
            JsonNode classes = rootNode.get("classes");

            if (classes != null && classes.isArray()) {
                // Create directories
               createDirectories();

                for (JsonNode clazz : classes) {
                    String className = clazz.get("name").asText();
                    JsonNode attributes = clazz.get("attributes");

                    // Generate code
                    generateModel(className, attributes);
                    generateRepository(className);
                    generateDTO(className, attributes);
                    generateServiceInterface(className);
                    generateServiceImpl(className);
                    generateController(className);
                }
                System.out.println("Code generation completed successfully!");
            } else {
                System.out.println("Invalid class diagram structure in JSON.");
            }
        } catch (IOException e) {
            System.err.println("Error reading or parsing JSON file: " + e.getMessage());
        }
    }
    // Helper method to load the file from resources folder using the class loader
    private static File getFileFromResources(String filePath) throws IOException {
        ClassLoader classLoader = CodeGeneratorExecutor.class.getClassLoader();
        URL resource = classLoader.getResource(filePath);

        if (resource == null) {
            throw new IOException("File not found in resources folder: " + filePath);
        }

        return new File(resource.getFile());
    }


    private static void createDirectories() throws IOException {
        Files.createDirectories(Paths.get(projectRoot,MODEL_PATH));
        Files.createDirectories(Paths.get(projectRoot,REPOSITORY_PATH));
        Files.createDirectories(Paths.get(projectRoot,DTO_PATH));
        Files.createDirectories(Paths.get(projectRoot,SERVICE_PATH));
        Files.createDirectories(Paths.get(projectRoot,CONTROLLER_PATH));
    }

    private static void generateModel(String className, JsonNode attributes) throws IOException {
        StringBuilder modelCode = new StringBuilder();
        modelCode.append("package ").append(BASE_PACKAGE).append(".model;\n\n");
        modelCode.append("import jakarta.persistence.*;\n");
        modelCode.append("import lombok.*;\n");
        modelCode.append("import lombok.experimental.SuperBuilder;\n");
        modelCode.append("import java.time.LocalDate;\n");
        modelCode.append("import java.util.*;\n\n");
        modelCode.append("@Setter\n");
        modelCode.append("@Getter\n");
        modelCode.append("@AllArgsConstructor\n");
        modelCode.append("@NoArgsConstructor\n");
        modelCode.append("@EqualsAndHashCode(callSuper = true)\n");
        modelCode.append("@SuperBuilder\n");
        modelCode.append("@Entity\n");
        modelCode.append("public class ").append(className).append(" extends AbstractEntity {\n");

        if (attributes != null) {
            modelCode.append("    @Id\n    @GeneratedValue(strategy = GenerationType.IDENTITY)\n    private Integer id;\n");
            for (JsonNode attribute : attributes) {
                String name = attribute.get("name").asText();
                String type = attribute.get("type").asText();
                modelCode.append("    private ").append(type).append(" ").append(name).append(";\n");
            }
        }

        modelCode.append("\n    // Getters and Setters\n");
        modelCode.append("}\n");

        writeFile(projectRoot+MODEL_PATH, className + ".java", modelCode.toString());
    }

    private static void generateRepository(String className) throws IOException {
        String repositoryCode = "package " + BASE_PACKAGE + ".repository;\n\n" +
                "import org.springframework.data.jpa.repository.JpaRepository;\n" +
                "import " + BASE_PACKAGE + ".models." + className + ";\n\n" +
                "public interface " + className + "Repository extends JpaRepository<" + className + ", Integer> {\n}\n";

        writeFile(projectRoot+REPOSITORY_PATH, className + "Repository.java", repositoryCode);
    }

    private static void generateDTO(String className, JsonNode attributes) throws IOException {
        StringBuilder dtoCode = new StringBuilder();
        dtoCode.append("package ").append(BASE_PACKAGE).append(".dto;\n\n");
        dtoCode.append("@Getter\n");
        dtoCode.append("@Setter\n");
        dtoCode.append("@Builder\n");
        dtoCode.append("public class ").append(className).append("DTO {\n");

        if (attributes != null) {
            for (JsonNode attribute : attributes) {
                String name = attribute.get("name").asText();
                String type = attribute.get("type").asText();
                dtoCode.append("    private ").append(type).append(" ").append(name).append(";\n");
            }
        }

        dtoCode.append("\n    // Getters and Setters\n");
        dtoCode.append("}\n");

        writeFile(projectRoot+DTO_PATH, className + "DTO.java", dtoCode.toString());
    }

    private static void generateServiceInterface(String className) throws IOException {
        String serviceCode = "package " + BASE_PACKAGE + ".services;\n\n" +
                "import " + BASE_PACKAGE + ".model." + className + ";\n\n" +
                "import java.util.List;\n\n" +
                "public interface " + className + "Service {\n" +
                "    List<" + className + "> findAll();\n" +
                "    " + className + " findById(Integer id);\n" +
                "    " + className + " save(" + className + " " + className.toLowerCase() + ");\n" +
                "    void deleteById(Integer id);\n" +
                "}\n";

        writeFile(projectRoot+SERVICE_PATH, className + "Service.java", serviceCode);
    }

    private static void generateServiceImpl(String className) throws IOException {
        String serviceImplCode = "package " + BASE_PACKAGE + ".services;\n\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.stereotype.Service;\n" +
                "import " + BASE_PACKAGE + ".model." + className + ";\n" +
                "import " + BASE_PACKAGE + ".repository." + className + "Repository;\n\n" +
                "import java.util.List;\n\n" +
                "@Service\n" +
                "@RequiredArgsConstructor\n" +
                "public class " + className + "ServiceImpl implements " + className + "Service {\n" +
                "    @Autowired\n" +
                "    private final " + className + "Repository repository;\n\n" +
                "    @Override\n" +
                "    public List<" + className + "> findAll() {\n" +
                "        return repository.findAll();\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public " + className + " findById(Integer id) {\n" +
                "        return repository.findById(id).orElseThrow(() -> new RuntimeException(\"Not found\"));\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public " + className + " save(" + className + " " + className.toLowerCase() + ") {\n" +
                "        return repository.save(" + className.toLowerCase() + ");\n" +
                "    }\n\n" +
                "    @Override\n" +
                "    public void deleteById(Integer id) {\n" +
                "        repository.deleteById(id);\n" +
                "    }\n" +
                "}\n";

        writeFile(projectRoot+SERVICE_PATH, className + "ServiceImpl.java", serviceImplCode);
    }

    private static void generateController(String className) throws IOException {
        String controllerCode = "package " + BASE_PACKAGE + ".controllers;\n\n" +
                "import org.springframework.beans.factory.annotation.Autowired;\n" +
                "import org.springframework.web.bind.annotation.*;\n" +
                "import " + BASE_PACKAGE + ".model." + className + ";\n" +
                "import " + BASE_PACKAGE + ".services." + className + "Service;\n\n" +
                "import java.util.List;\n\n" +
                "@RestController\n" +
                "@RequestMapping(\"/api/" + className.toLowerCase() + "s\")\n" +
                "public class " + className + "Controller {\n" +
                "    @Autowired\n" +
                "    private " + className + "Service service;\n\n" +
                "    @GetMapping\n" +
                "    public List<" + className + "> getAll() {\n" +
                "        return service.findAll();\n" +
                "    }\n\n" +
                "    @GetMapping(\"/{id}\")\n" +
                "    public " + className + " getById(@PathVariable Integer id) {\n" +
                "        return service.findById(id);\n" +
                "    }\n\n" +
                "    @PostMapping\n" +
                "    public " + className + " create(@RequestBody " + className + " " + className.toLowerCase() + ") {\n" +
                "        return service.save(" + className.toLowerCase() + ");\n" +
                "    }\n\n" +
                "    @DeleteMapping(\"/{id}\")\n" +
                "    public void delete(@PathVariable Integer id) {\n" +
                "        service.deleteById(id);\n" +
                "    }\n" +
                "}\n";

        writeFile(projectRoot+CONTROLLER_PATH, className + "Controller.java", controllerCode);
    }

    private static void writeFile(String folderPath, String fileName, String content) throws IOException {
        Path filePath = Paths.get(folderPath, fileName);
        Files.write(filePath, content.getBytes());
        System.out.println("Generated: " + filePath);
    }
}
