{ pkgs }: {
    deps = [
        pkgs.gradle
        pkgs.graalvm17-ce
        pkgs.maven
        pkgs.gradle
        pkgs.replitPackages.jdt-language-server
        pkgs.replitPackages.java-debug
    ];
}