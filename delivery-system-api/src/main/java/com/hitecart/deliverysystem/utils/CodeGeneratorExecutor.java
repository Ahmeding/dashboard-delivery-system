package com.hitecart.deliverysystem.utils;

public class CodeGeneratorExecutor {
    public static void executeCodeGenerator() {
        try {
            System.out.println("Starting code generation...");

            // Parse the JSON file and process class diagram
            CodeGenerator.main(new String[0]);

            System.out.println("Code generation finished successfully.");
        } catch (Exception e) {
            System.err.println("Error during code generation: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
