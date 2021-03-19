package tech.itpark.di.container;

public class Definition {
    private String name;
    private String[] dependencies;
    // Java Beans
    // no args constructor
    // get/set
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getDependencies() {
        return dependencies;
    }

    public void setDependencies(String[] dependencies) {
        this.dependencies = dependencies;
    }
}
